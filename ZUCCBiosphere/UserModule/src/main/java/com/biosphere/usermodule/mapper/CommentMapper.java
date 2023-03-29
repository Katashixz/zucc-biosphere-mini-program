package com.biosphere.usermodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.Comment;
import com.biosphere.library.vo.CommentNotifyVo;
import com.biosphere.library.vo.CommentVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyh
 * @since 2022-09-08
 */
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT\n" +
            "c.postID AS postID,\n" +
            "u1.userName AS userName,\n" +
            "c.userID AS userID,\n" +
            "u1.avatarUrl AS userAvatarUrl,\n" +
            "c.commentToUser AS commentAccessID,\n" +
            "c.commentDate AS commentDate,\n" +
            "c.content AS content,\n" +
            "u2.userName AS commentAccessName \n" +
            "FROM\n" +
            "`comment` c\n" +
            "LEFT OUTER JOIN `user` u1 ON ( c.userID = u1.id )\n" +
            "LEFT OUTER JOIN `user` u2 ON ( c.commentToUser = u2.id )\n" +
            "WHERE\n" +
            "c.postID = #{postID} AND c.isDeleted = 0\n" +
            "ORDER BY\n" +
            "c.commentDate ASC")
    List<CommentVo> loadCommentByPostID(@Param("postID") Long postID);

    @Select("SELECT postID\n" +
            "FROM `comment`\n" +
            "GROUP BY postID")
    List<Long> loadPostWhichHasComments();

    @Select("SELECT\n" +
            "c.id AS id,\n" +
            "c.postID AS postID,\n" +
            "u1.userName AS userName,\n" +
            "c.userID AS userID,\n" +
            "u1.avatarUrl AS userAvatarUrl,\n" +
            "c.commentToUser AS commentAccessID,\n" +
            "c.commentDate AS commentDate,\n" +
            "c.content AS content,\n" +
            "u2.userName AS commentAccessName, \n" +
            "p.content AS postText, \n" +
            "p.imageUrl AS image \n" +
            "FROM\n" +
            "`comment` c\n" +
            "LEFT OUTER JOIN `user` u1 ON ( c.userID = u1.id )\n" +
            "LEFT OUTER JOIN `user` u2 ON ( c.commentToUser = u2.id )\n" +
            "LEFT OUTER JOIN `post` p ON ( c.postID = p.id )\n" +
            "WHERE\n" +
            "c.userID = #{userID} AND c.isDeleted = 0\n" +
            "ORDER BY\n" +
            "c.commentDate DESC")
    List<CommentVo> loadCommentByUserID(@Param("userID") Integer userID);

    @Delete("<script>" +
            "DELETE\n" +
            "FROM `comment`\n" +
            "WHERE postID in " +
            "<foreach item=\"item\"  collection=\"idList\"  index=\"index\" open=\"(\" close=\")\" separator=\",\">\n" +
            "            #{item}\n" +
            "</foreach>" +
            "</script>")
    Integer deleteComments(@Param("idList")List<Long> idList);

    @Delete("DELETE\n" +
            "FROM `comment`\n" +
            "WHERE isDeleted = 1")
    Integer deleteMarkedComments();


    @Select("SELECT c.id as commentID, c.userID as userID, u.userName as userName, u.avatarUrl as avatarUrl, c.commentDate as createdAt, c.content as content, c.postID as postID\n" +
            "FROM `comment` c\n" +
            "LEFT OUTER JOIN `user` u ON u.id = c.userID\n" +
            "WHERE c.commentToUser = #{userID} AND userID <> #{userID} AND isDeleted = 0\n" +
            "ORDER BY createdAt DESC")
    List<CommentNotifyVo> loadCommentNotify(@Param("userID") Integer userID);

}
