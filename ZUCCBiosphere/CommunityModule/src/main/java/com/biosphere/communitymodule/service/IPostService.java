package com.biosphere.communitymodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biosphere.library.pojo.Post;
import com.biosphere.library.pojo.ShopItem;
import com.biosphere.library.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hyh
 * @since 2022-09-08
 */
public interface IPostService extends IService<Post> {

    /**
     * 功能描述: 分页加载帖子
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/8 17:11
     */
    Map<String ,Object> loadPost(Integer curPage, Integer pageSize);

    /**
     * 功能描述: 跳转帖子详情
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/15 9:36
     */
    CommunityPostVo loadPostDetail(Long postID);

    /**
     * 功能描述: 取出此用户点赞过的帖子列表
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/15 10:10
     */
    Map<String, Object> updateLike(Integer userID, Map<String, Object> resData);

    /**
     * 功能描述: 判断用户是否点赞过此帖子(为了保证数据同步)
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/15 16:13
     */
    boolean isLiked(Long postID, Integer userID);

    /**
     * 功能描述: 加载帖子评论
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/15 9:37
     */
    List<CommentVo> loadComments(Long postID);

    /**
     * 功能描述: 热帖加载
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/19 14:32
     */
    List<Map<String ,Object>> loadHotPosts();

    /**
     * 功能描述: 上传图片
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/22 9:44
     */
    String uploadImage(MultipartFile file, String openID);

    /**
     * 功能描述: 上传帖子信息到Redis，再通过消息队列异步更新到数据库
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/22 14:36
     */
    void uploadPost(PostUploadVo postUploadVo);

    /**
     * 功能描述: 点赞状态更改
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/23 17:52
     */
    void changeLike(LikeStatusVo likeStatusVo);

    /**
     * 功能描述: 评论检测以及上传
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/28 9:01
     */
    void uploadComment(UploadCommentVo uploadCommentVo);

    /**
     * 功能描述: 获取搜索结果
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/10/10 11:40
     */
    List<Map<String, Object>> postSearch(String content);

    /**
     * 功能描述: 收藏帖子
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/10/13 9:31
     */
    void changeStar(Integer userID, Long postID);

    /**
     * 功能描述: 加载商城的商品列表
     * @param:
     * @return: java.util.List<com.biosphere.library.pojo.ShopItem>
     * @author hyh
     * @date: 2023/4/3 13:18
     */
    List<ShopItem> loadShopList();

    /**
     * 功能描述: 加载领养日记
     * @param: date
     * @return: java.util.List<com.biosphere.library.vo.DiaryVo>
     * @author hyh
     * @date: 2023/4/3 16:42
     */
    List<DiaryVo> loadDiary(String date);

    /**
     * 功能描述: 保存领养日记
     * @param: diaryUploadVo
     * @return: void
     * @author hyh
     * @date: 2023/4/3 21:30
     */
    void saveDiary(DiaryUploadVo diaryUploadVo);




}
