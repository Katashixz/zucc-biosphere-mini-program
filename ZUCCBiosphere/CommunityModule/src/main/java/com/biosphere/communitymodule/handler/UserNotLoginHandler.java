package com.biosphere.communitymodule.handler;

import com.biosphere.library.util.HttpMethodUtil;
import com.biosphere.library.vo.RespBeanEnum;
import com.biosphere.library.vo.ResponseResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Administrator
 * @description 用户未登录处理类
 */
@Component
public class UserNotLoginHandler implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		// new ResponseResult<>(401, "未登录", authException.getMessage());
		HttpMethodUtil.responseJson(response, ResponseResult.error(RespBeanEnum.NO_LOGIN));


	}
}
