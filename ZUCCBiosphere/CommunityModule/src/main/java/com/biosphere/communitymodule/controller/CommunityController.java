package com.biosphere.communitymodule.controller;


import com.alibaba.fastjson.JSONObject;
import com.biosphere.communitymodule.service.IPostService;
import com.biosphere.communitymodule.util.CommunityDataAutoLoadUtil;
import com.biosphere.library.pojo.LikeRecord;
import com.biosphere.library.util.JwtUtil;
import com.biosphere.library.util.TencentCosUtil;
import com.biosphere.library.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
@RequestMapping("/community")
public class CommunityController implements InitializingBean {

    @Autowired
    private IPostService postService;

    @Autowired
    private CommunityDataAutoLoadUtil communityDataAutoLoadUtil;

    @ApiOperation(value = "社区主页帖子分页加载", notes = "需要传入当前页数和页大小")
    @RequestMapping(value = "/exposure/loadPostList",method = RequestMethod.GET)
    public ResponseResult loadPostList(Integer curPage, Integer pageSize, Integer userID){
        ResponseResult res = new ResponseResult();
        Map<String, Object> resData = postService.loadPost(curPage, pageSize);

        if (Objects.isNull(resData)) {
            res.setCode(RespBeanEnum.LOAD_POST_ERROR.getCode());
            res.setMsg(RespBeanEnum.LOAD_POST_ERROR.getMessage());
            return res;
        }
        //如果用户登录了，就要加载他点赞过哪些帖子
        if (!Objects.isNull(userID)) {
            resData = postService.updateLike(userID, resData);
        }
        res.setCode(RespBeanEnum.SUCCESS.getCode());
        res.setMsg(RespBeanEnum.SUCCESS.getMessage());
        res.setData(resData);

        return res;
    }

    @ApiOperation(value = "社区主页热帖加载", notes = "不需要传参")
    @RequestMapping(value = "/exposure/loadHotPost",method = RequestMethod.GET)
    public ResponseResult loadPostList(){
        ResponseResult res = new ResponseResult();
        JSONObject resData = new JSONObject();
        List<Map<String ,Object>> hotPostVoList = postService.loadHotPosts();
        if (Objects.isNull(hotPostVoList)) {
            res.setCode(RespBeanEnum.LOAD_HOT_POST_ERROR.getCode());
            res.setMsg(RespBeanEnum.LOAD_HOT_POST_ERROR.getMessage());
            return res;
        }
        if (hotPostVoList.size() == 0) {
            res.setCode(RespBeanEnum.EMPTY_HOT_POST.getCode());
            res.setMsg(RespBeanEnum.EMPTY_HOT_POST.getMessage());
            return res;
        }
        resData.put("tenHotPosts", hotPostVoList);
        res.setCode(RespBeanEnum.SUCCESS.getCode());
        res.setMsg(RespBeanEnum.SUCCESS.getMessage());
        res.setData(resData);
        return res;
    }

    @ApiOperation(value = "帖子详情加载", notes = "需要传入帖子ID")
    @RequestMapping(value = "/exposure/loadPostDetail",method = RequestMethod.GET)
    public ResponseResult loadPostDetail(Long postID, Integer userID){
        ResponseResult res = new ResponseResult();
        JSONObject resData = new JSONObject();
        boolean isLiked = false;
        CommunityPostVo communityPostVo = postService.loadPostDetail(postID);
        //判断用户是否对这个帖子点过赞
        if (!Objects.isNull(userID)){
            isLiked = postService.isLiked(postID, userID);
        }
        if (Objects.isNull(communityPostVo)) {
            res.setCode(RespBeanEnum.LOAD_POSTDETAIL_ERROR.getCode());
            res.setMsg(RespBeanEnum.LOAD_POSTDETAIL_ERROR.getMessage());
            return res;
        }
        resData.put("isLiked",isLiked);
        resData.put("postDetail",communityPostVo);
        res.setCode(RespBeanEnum.SUCCESS.getCode());
        res.setMsg(RespBeanEnum.SUCCESS.getMessage());
        res.setData(resData);
        return res;
    }

    @ApiOperation(value = "上传帖子", notes = "需要传入用户id、帖子主题、内容、图片BASE64")
    @RequestMapping(value = "/auth/uploadPost",method = RequestMethod.POST)
    public ResponseResult uploadPost(@RequestBody PostUploadVo postUploadVo){
        ResponseResult res = postService.uploadPost(postUploadVo);
        return res;
    }


    @ApiOperation(value = "上传图片", notes = "需要传入用户id、帖子主题、内容、图片BASE64")
    @RequestMapping(value = "/auth/uploadImg",method = RequestMethod.POST)
    public ResponseResult uploadImg(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        ResponseResult res = new ResponseResult();
        JSONObject resData = new JSONObject();
        // 取出Token
        String token = request.getHeader("token");
        String openID = new String();
        try{
            openID = JwtUtil.parseJWT(token).getSubject();
        }catch (Exception e){
            res.setCode(RespBeanEnum.UPLOAD_IMG_ERROR.getCode());
            res.setMsg(RespBeanEnum.UPLOAD_IMG_ERROR.getMessage());
            return res;
        }
        String url = postService.uploadImage(file, openID);
        if (Objects.isNull(url)) {
            res.setCode(RespBeanEnum.UPLOAD_IMG_ERROR.getCode());
            res.setMsg(RespBeanEnum.UPLOAD_IMG_ERROR.getMessage());
            return res;
        }
            System.out.println(url);
        resData.put("imgUrl", url);
        res.setCode(RespBeanEnum.SUCCESS.getCode());
        res.setMsg(RespBeanEnum.SUCCESS.getMessage());
        res.setData(resData);
        return res;
    }

    @ApiOperation(value = "评论详情加载", notes = "需要传入帖子ID")
    @RequestMapping(value = "/exposure/loadComment",method = RequestMethod.GET)
    public ResponseResult loadComment(Long postID){
        ResponseResult res = new ResponseResult();
        JSONObject resData = new JSONObject();
        List<CommentVo> commentVoList = postService.loadComments(postID);
        // if (Objects.isNull(commentVoList)) {
        //     res.setCode(RespBeanEnum.LOAD_POSTDETAIL_ERROR.getCode());
        //     res.setMsg(RespBeanEnum.LOAD_POSTDETAIL_ERROR.getMessage());
        //     return res;
        // }
        resData.put("commentList",commentVoList);
        res.setCode(RespBeanEnum.SUCCESS.getCode());
        res.setMsg(RespBeanEnum.SUCCESS.getMessage());
        res.setData(resData);
        return res;

    }

    @ApiOperation(value = "增加点赞记录，更改点赞状态", notes = "需要传入用户id、帖子id、点赞状态")
    @RequestMapping(value = "/auth/changeLikeStatus",method = RequestMethod.POST)
    public ResponseResult changeLikeStatus(@RequestBody LikeStatusVo req){
        //取消点赞就删除，点赞就新增，利用消息队列更新到数据库去，再更新缓存，这样数据比较安全
        ResponseResult res = postService.changeLike(req);
        // System.out.println(req.toString());

        return res;
    }

    @ApiOperation(value = "增加评论记录", notes = "需要传入评论用户id、被评论用户id、帖子id、评论内容")
    @RequestMapping(value = "/auth/uploadComment",method = RequestMethod.POST)
    public ResponseResult uploadComment(@RequestBody UploadCommentVo req){
        ResponseResult res = postService.uploadComment(req);
        return res;
    }

    @ApiOperation(value = "返回论坛搜索结果", notes = "需要传入搜索类型、搜索内容")
    @RequestMapping(value = "/exposure/search",method = RequestMethod.GET)
    public ResponseResult uploadComment(Integer type, String content){
        ResponseResult res = new ResponseResult();
        JSONObject resData = new JSONObject();
        List<Map<String, Object>> postSearch = postService.postSearch(content);
        resData.put("searchResult",postSearch);
        res.setData(resData);
        res.setCode(RespBeanEnum.SUCCESS.getCode());
        res.setMsg(RespBeanEnum.SUCCESS.getMessage());

        return res;
    }



    @ApiOperation(value = "收藏帖子", notes = "需要传入用户id,帖子id")
    @RequestMapping(value = "/auth/starPost",method = RequestMethod.POST)
    public ResponseResult starPost(@RequestBody JSONObject req){
        Integer userID = (Integer) req.get("userID");
        Long postID = Long.valueOf(req.get("postID").toString());

        return postService.changeStar(userID,postID);
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        communityDataAutoLoadUtil.communityMainPageData();
        communityDataAutoLoadUtil.hotPostDataAutoUpdate();
    }


}
