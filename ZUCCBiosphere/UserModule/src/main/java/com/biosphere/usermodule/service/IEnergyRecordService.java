package com.biosphere.usermodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biosphere.library.pojo.EnergyRecord;
import com.biosphere.library.pojo.User;
import com.biosphere.library.vo.RespBean;

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
    RespBean checkIn(User user);

    /**
     * 功能描述: 获取登录总天数
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/8/19 10:29
     */
    Integer getDaysSum(User user);

}
