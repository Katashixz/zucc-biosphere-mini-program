package com.biosphere.communitymodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.Comment;
import com.biosphere.library.vo.CommentVo;
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
            "\tc.postID AS postID,\n" +
            "\tu1.userName AS userName,\n" +
            "\tc.userID AS userID,\n" +
            "\tu1.avatarUrl AS userAvatarUrl,\n" +
            "\tc.commentToUser AS commentAccessID,\n" +
            "\tc.commentDate AS commentDate,\n" +
            "\tc.content AS content,\n" +
            "\tu2.userName AS commentAccessName \n" +
            "FROM\n" +
            "\t`comment` c\n" +
            "\tLEFT OUTER JOIN `user` u1 ON ( c.userID = u1.id )\n" +
            "\tLEFT OUTER JOIN `user` u2 ON ( c.commentToUser = u2.id )\n" +
            "WHERE\n" +
            "\tc.postID = #{postID} AND c.isDeleted = 0\n" +
            "\tORDER BY\n" +
            "\tc.commentDate ASC")
    List<CommentVo> loadCommentByPostID(@Param("postID") Long postID);

    @Select("SELECT postID\n" +
            "FROM `comment`\n" +
            "WHERE isDeleted = 0\n" +
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
}
