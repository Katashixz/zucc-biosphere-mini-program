package com.biosphere.usermodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biosphere.library.pojo.EnergyRecord;
import com.biosphere.library.pojo.User;
import com.biosphere.library.vo.ResponseResult;
import com.biosphere.library.vo.RewardVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hyh
 * @since 2022-08-18
 */
public interface IEnergyRecordService extends IService<EnergyRecord> {

    /**
     * 功能描述: 每日登录判定
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/8/18 14:17
     */
    ResponseResult checkIn(User user);

    /**
     * 功能描述: 获取登录总天数
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/8/19 10:29
     */
    Integer getDaysSum(User user);

    /**
     * 功能描述: 新增能量值记录
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/10/10 14:39
     */
    void insertEnergyRecord(RewardVo rewardVo);

    /**
     * 功能描述: 用户能量值变化，type为1代表增加，0代表减去
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/10/10 15:10
     */
    void updateUserEnergy(Integer userID, Integer point, Integer type);
}
