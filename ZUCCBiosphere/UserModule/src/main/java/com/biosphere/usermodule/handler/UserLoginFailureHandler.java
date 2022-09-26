package com.biosphere.usermodule.handler;

import com.biosphere.library.util.HttpMethodUtil;
import com.biosphere.library.vo.RespBeanEnum;
import com.biosphere.library.vo.ResponseResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description 登陆失败处理类
 */
@Component
public class UserLoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) {
		HttpMethodUtil.responseJson(response, ResponseResult.error(RespBeanEnum.ERROR));

	}
}
