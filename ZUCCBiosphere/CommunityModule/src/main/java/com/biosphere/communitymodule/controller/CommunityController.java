package com.biosphere.communitymodule.controller;


import com.alibaba.fastjson.JSONObject;
import com.biosphere.communitymodule.service.IPostService;
import com.biosphere.communitymodule.util.CommunityDataAutoLoadUtil;
import com.biosphere.library.util.JwtUtil;
import com.biosphere.library.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hyh
 * @since 2022-09-08
 */
@Api(tags = "社区模块")
@Slf4j
@RestController
@CrossOrigin
@Validated
@RequestMapping("/community")
public class CommunityController implements InitializingBean {

    @Autowired
    private IPostService postService;

    @Autowired
    private CommunityDataAutoLoadUtil communityDataAutoLoadUtil;

    @ApiOperation(value = "社区主页帖子分页加载", notes = "需要传入当前页数和页大小")
    @RequestMapping(value = "/exposure/loadPostList",method = RequestMethod.GET)
    public Map<String, Object> loadPostList(Integer curPage, Integer pageSize, Integer userID){
        Map<String, Object> resData = postService.loadPost(curPage, pageSize);

        if (Objects.isNull(resData)) {
            throw new ExceptionLogVo(RespBeanEnum.LOAD_POST_ERROR);
        }
        //如果用户登录了，就要加载他点赞过哪些帖子
        if (!Objects.isNull(userID)) {
            resData = postService.updateLike(userID, resData);
        }
        return resData;
    }

    @ApiOperation(value = "社区主页热帖加载", notes = "不需要传参")
    @RequestMapping(value = "/exposure/loadHotPost",method = RequestMethod.GET)
    public JSONObject loadPostList(){
        JSONObject resData = new JSONObject();
        List<Map<String ,Object>> hotPostVoList = postService.loadHotPosts();
        if (Objects.isNull(hotPostVoList)) {
            throw new ExceptionLogVo(RespBeanEnum.LOAD_HOT_POST_ERROR);
        }
        if (hotPostVoList.size() == 0) {
            throw new ExceptionNoLogVo(RespBeanEnum.EMPTY_HOT_POST);
        }
        resData.put("tenHotPosts", hotPostVoList);
        return resData;
    }

    @ApiOperation(value = "帖子详情加载", notes = "需要传入帖子ID")
    @RequestMapping(value = "/exposure/loadPostDetail",method = RequestMethod.GET)
    public JSONObject loadPostDetail(Long postID, Integer userID){
        JSONObject resData = new JSONObject();
        boolean isLiked = false;
        CommunityPostVo communityPostVo = postService.loadPostDetail(postID);
        //判断用户是否对这个帖子点过赞
        if (!Objects.isNull(userID)){
            isLiked = postService.isLiked(postID, userID);
        }
        if (Objects.isNull(communityPostVo)) {
            throw new ExceptionNoLogVo(RespBeanEnum.LOAD_POSTDETAIL_ERROR);
        }
        resData.put("isLiked",isLiked);
        resData.put("postDetail",communityPostVo);
        return resData;
    }

    @ApiOperation(value = "上传帖子", notes = "需要传入用户id、帖子主题、内容、[选]图片BASE64")
    @RequestMapping(value = "/auth/uploadPost",method = RequestMethod.POST)
    public void uploadPost(@Validated @RequestBody PostUploadVo postUploadVo){
        postService.uploadPost(postUploadVo);
    }


    @ApiOperation(value = "上传图片", notes = "需要传入图片文件")
    @RequestMapping(value = "/auth/uploadImg",method = RequestMethod.POST)
    public JSONObject uploadImg(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        JSONObject resData = new JSONObject();
        // 取出Token
        String token = request.getHeader("token");
        String openID = new String();
        try{
            openID = JwtUtil.parseJWT(token).getSubject();
        }catch (Exception e){
            throw new ExceptionLogVo(RespBeanEnum.UPLOAD_IMG_ERROR);
        }
        String url = postService.uploadImage(file, openID);
        if (Objects.isNull(url)) {
            throw new ExceptionLogVo(RespBeanEnum.UPLOAD_IMG_ERROR);
        }
        resData.put("imgUrl", url);
        return resData;
    }

    @ApiOperation(value = "评论详情加载", notes = "需要传入帖子ID")
    @RequestMapping(value = "/exposure/loadComment",method = RequestMethod.GET)
    public JSONObject loadComment(Long postID){
        JSONObject resData = new JSONObject();
        List<CommentVo> commentVoList = postService.loadComments(postID);
        resData.put("commentList",commentVoList);
        return resData;

    }

    @ApiOperation(value = "增加点赞记录，更改点赞状态", notes = "需要传入用户id、帖子id、点赞状态")
    @RequestMapping(value = "/auth/changeLikeStatus",method = RequestMethod.POST)
    public void changeLikeStatus(@Validated @RequestBody LikeStatusVo req){
        //取消点赞就删除，点赞就新增，利用消息队列更新到数据库去，再更新缓存，这样数据比较安全
       postService.changeLike(req);
    }

    @ApiOperation(value = "增加评论记录", notes = "需要传入评论用户id、被评论用户id、帖子id、评论内容")
    @RequestMapping(value = "/auth/uploadComment",method = RequestMethod.POST)
    public void uploadComment(@Validated @RequestBody UploadCommentVo req){
        postService.uploadComment(req);
    }

    @ApiOperation(value = "返回论坛搜索结果", notes = "需要传入搜索类型、搜索内容")
    @RequestMapping(value = "/exposure/search",method = RequestMethod.GET)
    public JSONObject search(Integer type, String content){
        JSONObject resData = new JSONObject();
        List<Map<String, Object>> postSearch = postService.postSearch(content);
        resData.put("searchResult",postSearch);
        return resData;
    }

    @ApiOperation(value = "收藏帖子", notes = "需要传入用户id,帖子id")
    @RequestMapping(value = "/auth/starPost",method = RequestMethod.POST)
    public void starPost(@RequestBody JSONObject req){
        Integer userID = (Integer) req.get("userID");
        Long postID = Long.valueOf(req.get("postID").toString());
        postService.changeStar(userID,postID);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        communityDataAutoLoadUtil.communityMainPageData();
        communityDataAutoLoadUtil.hotPostDataAutoUpdate();
    }


}
