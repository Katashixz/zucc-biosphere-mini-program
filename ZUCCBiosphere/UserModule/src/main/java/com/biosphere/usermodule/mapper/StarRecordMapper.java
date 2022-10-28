package com.biosphere.usermodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.StarRecord;
import com.biosphere.library.vo.StarPostVo;
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
 * @since 2022-10-13
 */
public interface StarRecordMapper extends BaseMapper<StarRecord> {

    @Select("SELECT\n" +
            "p.id AS postID,\n" +
            "p.content AS content,\n" +
            "p.theme AS theme,\n" +
            "p.imageUrl AS imageUrl,\n" +
            "u.avatarUrl AS avatarUrl,\n" +
            "u.userName AS userName,\n" +
            "DATE_FORMAT( p.postDate, \"%Y-%m-%d %H:%i\" ) AS dateFormat,\n" +
            "DATE_FORMAT( s.starDate, \"%Y-%m-%d %H:%i\" ) AS starDateformat\n" +
            "FROM\n" +
            "star_record s\n" +
            "LEFT JOIN post p ON s.postID = p.id\n" +
            "LEFT JOIN `user` u ON p.userID = u.id \n" +
            "WHERE\n" +
            "p.isDeleted = 0\n" +
            "AND s.userID = #{userID}\n" +
            "ORDER BY s.starDate DESC")
    List<StarPostVo> getStaredPost(@Param("userID")Integer userID);

    @Delete("<script>" +
            "DELETE\n" +
            "FROM `star_record`\n" +
            "WHERE postID in " +
            "<foreach item=\"item\"  collection=\"idList\"  index=\"index\" open=\"(\" close=\")\" separator=\",\">\n" +
            "            #{item}\n" +
            "</foreach>" +
            "</script>")
    Integer deleteStars(@Param("idList")List<Long> idList);

    @Delete("DELETE\n" +
            "FROM `star_record`\n" +
            "WHERE postID = #{postID} AND userID = #{userID}")
    Integer deleteStar(@Param("postID")Long postID, @Param("userID")Integer userID);


}
