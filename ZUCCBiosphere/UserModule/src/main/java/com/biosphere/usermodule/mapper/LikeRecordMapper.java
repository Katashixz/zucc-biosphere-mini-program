package com.biosphere.usermodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.LikeRecord;
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
public interface LikeRecordMapper extends BaseMapper<LikeRecord> {

    @Select("SELECT postID\n" +
            "FROM like_record\n" +
            "WHERE userID = #{userID}")
    List<Long> loadLikeRecordsByUserID(@Param("userID") Integer userID);

    @Delete("<script>" +
            "DELETE\n" +
            "FROM `like_record`\n" +
            "WHERE postID in " +
            "<foreach item=\"item\"  collection=\"idList\"  index=\"index\" open=\"(\" close=\")\" separator=\",\">\n" +
            "            #{item}\n" +
            "</foreach>" +
            "</script>")
    Integer deleteLikes(@Param("idList")List<Long> idList);
}
