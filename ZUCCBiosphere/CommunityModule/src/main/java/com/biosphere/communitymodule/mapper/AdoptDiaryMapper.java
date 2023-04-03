package com.biosphere.communitymodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.AdoptDiary;
import com.biosphere.library.vo.DiaryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.parameters.P;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyh
 * @since 2023-04-03
 */
public interface AdoptDiaryMapper extends BaseMapper<AdoptDiary> {
    @Select("SELECT a.id as diaryID, u.id as userID, u.userName, u.avatarUrl as userAvatar, a.content, a.targetImage as animalUrl, a.createdAt, a.imageUrl\n" +
            "FROM adopt_diary a\n" +
            "LEFT OUTER JOIN `user` u ON a.userID = u.id\n" +
            "WHERE TO_DAYS(createdAt) = TO_DAYS(#{date})\n" +
            "ORDER BY createdAt DESC")
    List<DiaryVo> loadDiary(@Param("date") Date date);

}
