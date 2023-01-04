package com.biosphere.usermodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyh
 * @since 2022-08-10
 */
public interface UserMapper extends BaseMapper<User> {
    @Update("UPDATE `user`\n" +
            "SET energyPoint = #{point}\n" +
            "WHERE id = #{id}")
    Integer updateEnergyPoint(@Param("point") Integer point, @Param("id") Integer id);

    @Update("UPDATE `user`\n" +
            "SET userName = #{nickName}, avatarUrl = #{avatar}\n" +
            "WHERE id = #{id}")
    Integer updateInfo(@Param("id") Integer id, @Param("nickName") String nickName, @Param("avatar") String avatar);
}
