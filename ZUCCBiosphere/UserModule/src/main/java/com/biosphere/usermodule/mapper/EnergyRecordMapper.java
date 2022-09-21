package com.biosphere.usermodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.EnergyRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyh
 * @since 2022-08-18
 */
public interface EnergyRecordMapper extends BaseMapper<EnergyRecord> {

    @Select("select COUNT(ID) from energy_record where DATE_FORMAT(getDate,'%Y-%m-%d')  = DATE_FORMAT(NOW(),'%Y-%m-%d') and userID = #{userID} and type = 0")
    Integer isCheckedInToday(@Param("userID")Integer userID);
}
