package com.biosphere.adoptmodule.handler;

import com.biosphere.library.util.HttpMethodUtil;
import com.biosphere.library.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 * @description 用户登录成功后返回
 */
@Slf4j
@Component
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        // SysUserDetails sysUserDetails = (SysUserDetails) authentication.getPrincipal();
        // // 获得请求IP
        // String ip = AccessAddressUtils.getIpAddress(request);
        // sysUserDetails.setIp(ip);
        // String token = JWTTokenUtils.createAccessToken(sysUserDetails);
        //
        // // 保存Token信息到Redis中
        // JWTTokenUtils.setTokenInfo(token, sysUserDetails.getUsername(), ip);
        //
        // log.info("用户{}登录成功，Token信息已保存到Redis", sysUserDetails.getUsername());
        //
        // Map<String, String> tokenMap = new HashMap<>();
        // tokenMap.put("token", token);
        // tokenMap.put("openId", sysUserDetails.getUsername());
        // ResponseUtils.responseJson(response, ResponseUtils.response(200, "登录成功", tokenMap));
        // new ResponseResult<>(402, "访问", null);
        HttpMethodUtil.responseJson(response, RespBeanEnum.SUCCESS);

    }
}
