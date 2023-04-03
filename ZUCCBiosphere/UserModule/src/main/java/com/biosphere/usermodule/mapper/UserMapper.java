package com.biosphere.usermodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.User;
import com.biosphere.library.vo.MyAdoptVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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

    @Select("SELECT userID, targetID, shopID, shopName, createdAt, nickName, image\n" +
            "FROM view_today_condition t\n" +
            "LEFT OUTER JOIN animals_wiki a ON t.targetID = a.ID\n" +
            "WHERE t.action = 2 AND shopID is not null AND t.userID = #{userID}\n" +
            "ORDER BY createdAt DESC")
    List<MyAdoptVo> getMyAdopt(@Param("userID") Integer userID);
}
