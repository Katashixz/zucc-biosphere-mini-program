package com.biosphere.communitymodule.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biosphere.communitymodule.mapper.*;
import com.biosphere.communitymodule.rabbitmq.MQSender;
import com.biosphere.library.pojo.*;
import com.biosphere.communitymodule.service.IPostService;
import com.biosphere.library.util.CommonUtil;
import com.biosphere.library.util.TencentCosUtil;
import com.biosphere.library.vo.*;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyh
 * @since 2022-09-08
 */
@Service
@Slf4j
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService, InitializingBean {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private MQSender mqSender;

    @Autowired
    private StarRecordMapper starRecordMapper;

    @Autowired
    private ShopItemMapper shopItemMapper;

    @Autowired
    private AdoptDiaryMapper adoptDiaryMapper;

    @Autowired
    private ReportRecordMapper reportRecordMapper;

    // 预计要插入多少数据
    private static int size = 1000000;

    // 期望的误判率
    private static double fpp = 0.01;

    // 创建评论的布隆过滤器
    private static BloomFilter<Long> commentBloomFilter = BloomFilter.create(Funnels.longFunnel(), size, fpp);

    // 创建帖子的布隆过滤器
    private static BloomFilter<Long> postBloomFilter = BloomFilter.create(Funnels.longFunnel(), size, fpp);

    @Override
    public Map<String ,Object> loadPost(Integer curPage, Integer pageSize) {
        // 根据页号加载帖子, 从left开始拿right条数据
        Integer left = (curPage - 1) * pageSize;
        Integer right = curPage * pageSize - 1;
        log.info("加载数据 {}-{}",left,curPage * pageSize - 1);
        Map<String ,Object> result = new HashMap<>();
        // 帖子数据是否加载完毕
        Boolean isEnd = false;
        try{
            Set<Integer> postSet = redisTemplate.opsForZSet().reverseRange("postSet", left, right);
            Map<String ,Object> postMap = redisTemplate.opsForHash().entries("postMap");
            List<CommunityPostVo> postVos = new ArrayList<>();

            // 如果Redis里的数据加载到底了，就返回已全部加载(目前只允许浏览前1000条) 【老版本】
            // 如果Redis里的数据加载到底了，去数据库查，再存入Redis，如果数据库也没了，就返回已全部加载【新版本】
            if (postSet.size() < (right - left)) {
                // limit是从left开始拿pageSize条数据
                List<CommunityPostVo> postVoListFromDB = postMapper.loadPostWithPage(left, pageSize);
                // 处理数据库
                if (postVoListFromDB.size() < (right - left)){
                    isEnd = true;
                }
                if (postVoListFromDB.size() != 0) {
                    //有数据就存入缓存
                    Set<ZSetOperations.TypedTuple<Long>> tempSet = new HashSet<>();

                    for (CommunityPostVo communityPostVo : postVoListFromDB) {
                        //先处理图片
                        if (!Objects.isNull(communityPostVo.getImageUrl()))
                            communityPostVo.setImageUrlList(communityPostVo.getImageUrl().split("，"));
                        ZSetOperations.TypedTuple<Long> temp = new DefaultTypedTuple<>(communityPostVo.getPostID(),Double.valueOf(communityPostVo.getPostDate().getTime()));
                        tempSet.add(temp);
                        postMap.put(communityPostVo.getPostID().toString(),communityPostVo);

                    }
                    postVos.addAll(postVoListFromDB);
                    redisTemplate.opsForZSet().add("postSet",tempSet);
                    redisTemplate.opsForHash().putAll("postMap", postMap);

                }

            }else{
                // 从Redis里取出的数量必须等于right-left才能直接返回
                for (Integer id : postSet) {
                    postVos.add((CommunityPostVo) postMap.get(id.toString()));
                }
            }

            result.put("postList", postVos);
            result.put("isEnd", isEnd);
        }catch (Exception e){
            log.error("帖子数据分页加载出现错误：");
            e.printStackTrace();
            return null;
        }
        return result;

    }

    @Override
    public List<Map<String ,Object>> loadHotPosts() {
        List<Map<String ,Object>> res = new ArrayList<>();
        try{
            Set<Integer> hotPostSet = redisTemplate.opsForZSet().reverseRange("hotPostSet", 0, 9);
            Map<String ,Object> hotPostMap = redisTemplate.opsForHash().entries("hotPostMap");
            for (Integer id : hotPostSet) {
                res.add((Map<String, Object>) hotPostMap.get(id.toString()));
            }

        }catch (Exception e){
            log.error("加载热帖错误：");
            e.printStackTrace();
            return null;
        }
        return res;
    }

    @Override
    public String uploadImage(MultipartFile file, String openID) {
        return TencentCosUtil.uploadfile(file, openID);
    }

    @Override
    public void uploadPost(PostUploadVo postUploadVo) {
        if (Objects.isNull(postUploadVo.getContent()) || Objects.isNull(postUploadVo.getUserID()) || Objects.isNull(postUploadVo.getTheme())) {
            throw new ExceptionLogVo(RespBeanEnum.INFO_ERROR);
        }
        if (SensitiveWordHelper.contains(postUploadVo.getContent())) {
            throw new ExceptionNoLogVo(RespBeanEnum.SENSITIVE_WORDS,SensitiveWordHelper.findAll(postUploadVo.getContent()));
        }
        try{
            Post post = new Post();
            String url = new String();
            //对象转换
            post.setUserID(postUploadVo.getUserID());
            post.setTheme(postUploadVo.getTheme());
            post.setContent(postUploadVo.getContent());
            post.setPostDate(new Date(System.currentTimeMillis()));
            //处理没图片的情况和有图片的情况
            if (!Objects.isNull(postUploadVo.getImages())) {
                for (int i = 0; i < postUploadVo.getImages().length; i ++){
                    if (i == 0) {
                        url = postUploadVo.getImages()[i];
                    }else {
                        url += "，" + postUploadVo.getImages()[i];
                    }
                }
                post.setImageUrl(url);
            }
            post.setIsDeleted(0);
            post.setIsTop(0);
            post.setIsEssential(0);
            //生产者发送消息到消息队列
            mqSender.sendPostMsg(JSON.toJSONString(post));
        }catch (Exception e){
            throw new ExceptionLogVo(RespBeanEnum.UPLOAD_POST_ERROR);
        }
    }

    @Override
    public void changeLike(LikeStatusVo likeStatusVo) {
        mqSender.sendLikeMsg(JSON.toJSONString(likeStatusVo));
    }

    @Override
    public void uploadComment(UploadCommentVo uploadCommentVo) {
        //敏感语言检测->数据库更新->缓存更新
        if (SensitiveWordHelper.contains(uploadCommentVo.getContent())) {
            throw new ExceptionNoLogVo(RespBeanEnum.SENSITIVE_WORDS,SensitiveWordHelper.findAll(uploadCommentVo.getContent()));
        }
        mqSender.sendCommentMsg(JSON.toJSONString(uploadCommentVo));
    }

    @Override
    public List<Map<String, Object>> postSearch(String content) {
        List<Map<String, Object>> maps = postMapper.postSearch(content);
        //图片数据需要转化成数组
        for (Map<String, Object> map : maps) {
            if (map.containsKey("imageUrl")){
                map.put("imageUrlList",((String) map.get("imageUrl")).split("，"));
            }else {
                map.put("imageUrlList",null);

            }

        }
        return maps;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStar(Integer userID, Long postID) {
        // 先检测缓存中有没有此帖的收藏，有就报提示，没有就收藏成功
        StarRecord starRecord = new StarRecord();
        List<StarPostVo> starRecords = (List<StarPostVo>) redisTemplate.opsForHash().get("userID:" + userID,"starRecords");
        for (StarPostVo record : starRecords) {
            if (record.getPostID().equals(postID)) {
                throw new ExceptionNoLogVo(RespBeanEnum.REPEAT_STAR);
            }
        }
        starRecord.setPostID(postID);
        starRecord.setUserID(userID);
        starRecord.setStarDate(new Date(System.currentTimeMillis()));
        starRecordMapper.insert(starRecord);
        //更新缓存
        StarPostVo starPostVo = starRecordMapper.getOneStaredPost(starRecord.getId());
        if (!Objects.isNull(starPostVo.getImageUrl()))
            starPostVo.setImageUrlList(starPostVo.getImageUrl().split("，"));
        starRecords.add(0,starPostVo);
        redisTemplate.opsForHash().put("userID:" + userID,"starRecords", starRecords);
        if (starRecord.getPostID() <= 0) {
            throw new ExceptionLogVo(RespBeanEnum.STAR_INSERT_ERROR);
        }
    }

    @Override
    public List<ShopItem> loadShopList() {

        QueryWrapper<ShopItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id,`name`,price,createdAt,imageUrl,`usage`,`stock`").orderByDesc("createdAt");
        return shopItemMapper.selectList(queryWrapper);



    }

    @Override
    public List<DiaryVo> loadDiary(String date) {
        Date date1 = new Date();
        try {
            date1 = CommonUtil.toDate(date);
        }catch (ParseException e){
            throw new ExceptionLogVo(RespBeanEnum.FORMAT_ERROR);
        }
        List<DiaryVo> diaryVos = adoptDiaryMapper.loadDiary(date1);
        for (DiaryVo diaryVo : diaryVos) {
            if (diaryVo.getImageUrl() != null)
                diaryVo.setImageUrlList(diaryVo.getImageUrl().split("，"));
            diaryVo.setCreatedAtFormat(CommonUtil.TimeFormat(diaryVo.getCreatedAt()));
        }
        return diaryVos;
    }

    @Override
    public void saveDiary(DiaryUploadVo diaryUploadVo) {
        AdoptDiary adoptDiary = new AdoptDiary();
        String timeFormat = CommonUtil.TimeFormat(new Date());
        String targetTime = diaryUploadVo.getCreatedAt() + " " + timeFormat;
        String images = new String();
        if (diaryUploadVo.getImages() != null){
            for(int i = 0; i < diaryUploadVo.getImages().length; i ++){
                if (i == 0)
                    images = images + diaryUploadVo.getImages()[i];
                else
                    images = images + "，" + diaryUploadVo.getImages()[i];

            }
        }

        try{
            adoptDiary.setCreatedAt(CommonUtil.toCompleteDate(targetTime));
        }catch (ParseException e){
            throw new ExceptionLogVo(RespBeanEnum.ERROR);
        }
        adoptDiary.setContent(diaryUploadVo.getContent());
        adoptDiary.setTargetImage(diaryUploadVo.getTargetImage());
        adoptDiary.setImageUrl(images);
        adoptDiary.setUserID(diaryUploadVo.getUserID());
        int insert = adoptDiaryMapper.insert(adoptDiary);
        if (insert <= 0) {
            throw new ExceptionLogVo(RespBeanEnum.ERROR_INFO);
        }

    }

    @Override
    public void saveReport(ReportRecord reportRecord) {
        reportRecord.setIsHandled(0);
        reportRecord.setCreatedAt(new Date());
        int insert = reportRecordMapper.insert(reportRecord);
        if (insert <= 0) {
            throw new ExceptionLogVo(RespBeanEnum.ERROR_INFO);
        }
    }


    @Override
    public CommunityPostVo loadPostDetail(Long postID) {
        CommunityPostVo res = new CommunityPostVo();
        try{
            res = (CommunityPostVo) redisTemplate.opsForHash().get("postMap", postID.toString());
            // 如果缓存没有就去数据库取，先过滤
            if (Objects.isNull(res)) {
                if (!postBloomFilter.mightContain(postID)) {
                    return null;
                }
                res = postMapper.findOne(postID);
                if (Objects.isNull(res.getPostID())) {
                    return null;
                }
                if (!Objects.isNull(res.getImageUrl()))
                    res.setImageUrlList(res.getImageUrl().split("，"));
                redisTemplate.opsForHash().put("postMap", postID.toString(), res);
            }
        }catch (Exception e){
            log.error("加载帖子详情失败：");
            e.printStackTrace();
            return null;
        }
        return res;
    }

    @Override
    public boolean isLiked(Long postID, Integer userID) {
        if (Objects.isNull(userID)) return false;
        try{
            // List<Long> likeRecords = (List<Long>) redisTemplate.opsForHash().get("likeRecords", userID.toString());
            // List<Long> likeRecords = (List<Long>) redisTemplate.opsForValue().get("userID:" + userID + ":likeRecords");
            List<Long> likeRecords = (List<Long>) redisTemplate.opsForHash().get("userID:" + userID, "likeRecords");

            if (!Objects.isNull(likeRecords) && likeRecords.contains(postID))
                return true;

        }catch (Exception e){
            log.error("点赞数据加载失败：");
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public List<CommentVo> loadComments(Long postID) {
        List<CommentVo> commentVos = new ArrayList<>();
        try{
            //如果缓存有就到缓存里取出来
            commentVos = (List<CommentVo>) redisTemplate.opsForHash().get("commentRecords", postID.toString());
            //此时容易有缓存穿透问题，对空值缓存防不住大流量撑爆Redis，所以采用布隆过滤器
            //如果缓存没有，先判断布隆过滤器里有没有，有再从数据库里取，再存入缓存，没有直接返回null
            if (Objects.isNull(commentVos)) {
                if (!commentBloomFilter.mightContain(postID)) {
                    return null;
                }
                commentVos = commentMapper.loadCommentByPostID(postID);
                Map<String, Object> map = new HashMap<>();
                map.put(postID.toString(), commentVos);
                redisTemplate.opsForHash().putAll("commentRecords", map);
                redisTemplate.expire("commentRecords",1503, TimeUnit.MINUTES);
            }


        }catch (Exception e){
            log.error("加载评论错误：");
            e.printStackTrace();
            return null;
        }
        return commentVos;
    }


    @Override
    public Map<String, Object> updateLike(Integer userID, Map<String, Object> post) {
        try{
            // List<Long> likeRecords = (List<Long>) redisTemplate.opsForHash().get("likeRecords", userID.toString());
            List<Long> likeRecords = (List<Long>) redisTemplate.opsForHash().get("userID:" + userID,"likeRecords");

            if (Objects.isNull(likeRecords)) {
                return post;
            }else {
                List<CommunityPostVo> postVos = (List<CommunityPostVo>) post.get("postList");
                for (CommunityPostVo postVo : postVos) {
                    if (likeRecords.contains(postVo.getPostID())) {
                        postVo.setPostIsLiked(true);
                    }
                }
                post.put("postList", postVos);
            }


        }catch (Exception e){
            log.error("点赞数据加载失败：");
            e.printStackTrace();
            return null;
        }
        return post;
    }
    @Override
    @Scheduled(cron = "0 0 3 1/1 * ? ")
    public void afterPropertiesSet() throws Exception {
        log.info("布隆过滤器初始化");
        List<Long> postHasComment = commentMapper.loadPostWhichHasComments();
        List<Long> postID = postMapper.loadAllPostID();
        for (Long aLong : postHasComment) {
            commentBloomFilter.put(aLong);
        }
        for (Long aLong : postID) {
            postBloomFilter.put(aLong);
        }
        log.info("布隆过滤器初始化完成");

    }
    @Scheduled(cron = "0 0/10 * * * ?")
    public void commentBloomFilterUpdate(){
        log.info("布隆过滤器更新");
        List<Long> postHasComment = commentMapper.loadPostWhichHasComments();
        List<Long> postID = postMapper.loadAllPostID();
        for (Long aLong : postHasComment) {
            commentBloomFilter.put(aLong);
        }
        for (Long aLong : postID) {
            postBloomFilter.put(aLong);
        }
        log.info("布隆过滤器更新完成");

    }


    }
