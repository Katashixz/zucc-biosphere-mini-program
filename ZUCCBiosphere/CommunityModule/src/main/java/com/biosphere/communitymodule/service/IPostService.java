package com.biosphere.communitymodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biosphere.library.pojo.Post;
import com.biosphere.library.vo.CommentVo;
import com.biosphere.library.vo.CommunityPostVo;

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


}