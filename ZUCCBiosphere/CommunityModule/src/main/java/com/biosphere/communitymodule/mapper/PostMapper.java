package com.biosphere.communitymodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.Post;
import com.biosphere.library.vo.CommunityPostVo;
import com.biosphere.library.vo.HotPostVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.parameters.P;

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

    @Select("SELECT p.id as postID, p.userID as userID, u.avatarUrl as avatarUrl, u.userName as userName,p.theme as theme, p.content as content, p.postDate as postDate, p.isTop as isTop, p.isEssential as isEssential, p.imageUrl, DATE_FORMAT(p.postDate,\"%Y-%m-%d %H:%i\") as dateformat, COUNT(c.isDeleted = 0 OR NULL) as commentNum, COUNT(DISTINCT l.id) as likeNum\n" +
            "from post p \n" +
            "LEFT JOIN `comment` c on p.id = c.postID \n" +
            "LEFT JOIN like_record l on p.id = l.postID\n" +
            "LEFT JOIN `user` u on p.userID = u.id\n" +
            "WHERE p.isDeleted = 0\n" +
            "GROUP BY p.id\n" +
            "ORDER BY p.postDate DESC\n" +
            "LIMIT #{start},#{end}")
    List<CommunityPostVo> loadPostWithPage(@Param("start")Integer start, @Param("end")Integer end);

    @Select("SELECT\n" +
            "\tp.id AS postID,\n" +
            "\tCOUNT( DISTINCT l.id ) AS likeNum,\n" +
            "\tCOUNT( DISTINCT c.id ) AS commentNum \n" +
            "FROM\n" +
            "\t( SELECT id, postDate FROM post WHERE isDeleted = 0) p\n" +
            "\tLEFT OUTER JOIN like_record l ON p.id = l.postID\n" +
            "\tLEFT OUTER JOIN `comment` c ON p.id = c.postID\n" +
            "WHERE p.postDate >= #{date}\n" +
            "GROUP BY p.id\n")
    List<HotPostVo> loadHotPost(@Param("date") Date date);

    @Select("SELECT p.id as postID, p.userID as userID, u.avatarUrl as avatarUrl, u.userName as userName,p.theme as theme, p.content as content, p.postDate as postDate, p.isTop as isTop, p.isEssential as isEssential, p.imageUrl, DATE_FORMAT(p.postDate,\"%Y-%m-%d %H:%i\") as dateformat, COUNT(DISTINCT c.id) as commentNum, COUNT(DISTINCT l.id) as likeNum\n" +
            "from post p \n" +
            "LEFT JOIN `comment` c on p.id = c.postID \n" +
            "LEFT JOIN like_record l on p.id = l.postID\n" +
            "LEFT JOIN `user` u on p.userID = u.id\n" +
            "WHERE p.id = #{id} AND p.isDeleted = 0")
    CommunityPostVo findOne(@Param("id")Long id);

    @Select("SELECT p.id as postID, p.userID as userID, u.avatarUrl as avatarUrl, u.userName as userName,p.theme as theme, p.content as content, p.postDate as postDate, p.isTop as isTop, p.isEssential as isEssential, p.imageUrl, DATE_FORMAT(p.postDate,\"%Y-%m-%d %H:%i\") as dateformat\n" +
            "FROM post p\n" +
            "LEFT JOIN `user` u on p.userID = u.id\n" +
            "WHERE p.content like CONCAT('%',#{content},'%') AND p.isDeleted = 0\n" +
            "ORDER BY p.postDate DESC")
    List<Map<String, Object>> postSearch(@Param("content") String content);

    @Select("SELECT id\n" +
            "FROM post\n" +
            "WHERE isDeleted = 0\n" +
            "GROUP BY id")
    List<Long> loadAllPostID();

}
