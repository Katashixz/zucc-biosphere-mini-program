package com.biosphere.adminmodule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biosphere.adminmodule.mapper.AdministratorMapper;
import com.biosphere.adminmodule.service.IAdministratorService;
import com.biosphere.library.pojo.Administrator;
import com.biosphere.library.util.JwtUtil;
import com.biosphere.library.vo.CommunityPostVo;
import com.biosphere.library.vo.ExceptionNoLogVo;
import com.biosphere.library.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyh
 * @since 2023-04-18
 */
@Service
public class AdministratorServiceImpl extends ServiceImpl<AdministratorMapper, Administrator> implements IAdministratorService {

    @Autowired
    private AdministratorMapper administratorMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<CommunityPostVo> loadPost() {

        List<CommunityPostVo> communityPostVos = administratorMapper.loadPost();
        for (CommunityPostVo communityPostVo : communityPostVos) {
            //先处理图片
            if (!Objects.isNull(communityPostVo.getImageUrl()))
                communityPostVo.setImageUrlList(communityPostVo.getImageUrl().split("，"));
        }
        return communityPostVos;
    }

    @Override
    public String login(String account, String pwd) {
        QueryWrapper<Administrator> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id,account,password,nickName,access").eq("account",account);
        Administrator administrator = administratorMapper.selectOne(queryWrapper);
        if (administrator == null) {
            throw new ExceptionNoLogVo(RespBeanEnum.ACCOUNT_ERROR);
        }
        if (!administrator.getPassword().equals(pwd)) {
            throw new ExceptionNoLogVo(RespBeanEnum.PWD_ERROR);
        }
        // 账号密码组合，用,分割，生成token，解析的时候用split分割成数组
        String str = administrator.getId()
                + "," + administrator.getAccount()
                + "," + administrator.getPassword()
                + "," + administrator.getNickName();
        String jwt = JwtUtil.createJWT(str);
        redisTemplate.opsForValue().set("admin:" + administrator.getAccount(), jwt, 1, TimeUnit.DAYS);
        return jwt;
    }
}
