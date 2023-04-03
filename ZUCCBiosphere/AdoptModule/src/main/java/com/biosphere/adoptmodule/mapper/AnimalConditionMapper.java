package com.biosphere.adoptmodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.AnimalCondition;
import com.biosphere.library.pojo.ViewTodayCondition;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyh
 * @since 2023-04-01
 */
public interface AnimalConditionMapper extends BaseMapper<AnimalCondition> {

    @Select("SELECT conditionID, userID, userName, targetID, shopID , shopName , action, DATE_FORMAT(createdAt,'%H:%i') as createdAt, imageUrl\n" +
            "FROM view_today_condition\n" +
            "WHERE targetID = #{targetID} AND DATE_FORMAT(createdAt,'%Y-%m-%d') = #{date}\n" +
            "ORDER BY createdAt ASC")
    List<ViewTodayCondition> getTodayCondition(@Param("targetID") Integer targetID, @Param("date") String date);

}
