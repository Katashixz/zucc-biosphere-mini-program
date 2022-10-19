package com.biosphere.usermodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.Post;
import com.biosphere.library.vo.CommunityPostVo;
import com.biosphere.library.vo.HotPostVo;
import com.biosphere.library.vo.SimplePostVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyh
 * @since 2022-09-08
 */
public interface PostMapper extends BaseMapper<Post> {

    @Select("SELECT p.id as postID, p.userID as userID, u.avatarUrl as avatarUrl, u.userName as userName,p.theme as theme, p.content as content, p.postDate as postDate, p.imageUrl, DATE_FORMAT(p.postDate,\"%Y-%m-%d %H:%m\") as dateFormat\n" +
            "FROM post p\n" +
            "LEFT JOIN `user` u on p.userID = u.id\n" +
            "WHERE p.userID = #{userID}\n" +
            "ORDER BY p.postDate DESC")
    List<SimplePostVo> loadPostByUserID(@Param("userID")Integer userID);


}
