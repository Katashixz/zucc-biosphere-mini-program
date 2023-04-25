package com.biosphere.adminmodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biosphere.library.pojo.Administrator;
import com.biosphere.library.vo.CommunityPostVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hyh
 * @since 2023-04-18
 */
public interface IAdministratorService extends IService<Administrator> {

    /**
     * 功能描述: 加载帖子
     * @param:
     * @return: java.util.List<com.biosphere.library.vo.CommunityPostVo>
     * @author hyh
     * @date: 2023/4/18 15:34
     */
    List<CommunityPostVo> loadPost();

    /**
     * 功能描述: 登录
     * @param: account
    pwd
     * @return: java.lang.String
     * @author hyh
     * @date: 2023/4/18 20:08
     */
    String login(String account, String pwd);
}
