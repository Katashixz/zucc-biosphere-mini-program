package com.biosphere.usermodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.Comment;
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
            "\tc.postID = #{postID}\n" +
            "\tORDER BY\n" +
            "\tc.commentDate ASC")
    List<CommentVo> loadCommentByPostID(@Param("postID") Long postID);

    @Select("SELECT postID\n" +
            "FROM `comment`\n" +
            "GROUP BY postID")
    List<Long> loadPostWhichHasComments();

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
            "\tc.userID = #{userID}\n" +
            "\tORDER BY\n" +
            "\tc.commentDate ASC")
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

}
