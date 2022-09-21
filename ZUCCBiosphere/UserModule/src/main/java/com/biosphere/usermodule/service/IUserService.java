package com.biosphere.usermodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biosphere.library.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hyh
 * @since 2022-08-10
 */
public interface IUserService extends IService<User> {
    /** 
     * 功能描述: 从微信官方接口获取openID
     * @param:  
     * @return: 
     * @author hyh
     * @date: 2022/8/12 13:46
     */
    String getOpenID(String code);
    
    /**
     * 功能描述: 检查用户是否注册，若未注册自动注册，已注册则新增人品
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/8/12 13:46
     */
    User checkUserInfo(String openID,String url,String nickName);

    /**
     * 功能描述: 生成token并存入Redis
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/8/12 15:43
     */
    String tokenGenerate(User curUser);

    /**
     * 功能描述: 根据用户openID获取用户信息
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/8/18 14:02
     */
    User getUserInfoByOpenId(String openID);

    /**
     * 功能描述: 获取用户点赞数据存入缓存
     * @param:
     * @return:
     * @author hyh
     * @date: 2022/9/16 14:14
     */
    void saveUserLikeRecords(Integer userID);


}
