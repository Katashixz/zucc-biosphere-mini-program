package com.biosphere.communitymodule.filter;

import com.biosphere.library.pojo.User;
import com.biosphere.library.util.HttpMethodUtil;
import com.biosphere.library.util.JwtUtil;
import com.biosphere.library.vo.RespBean;
import com.biosphere.library.vo.RespBeanEnum;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * @description jwt权限过滤器
 */
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		log.info("-----------AuthenticationFilter Run-----------");

		// 取出Token
		String token = request.getHeader("token");
		if (Objects.isNull(token)) {
			filterChain.doFilter(request, response);
			return;
		}
		else  {
			User user = new User();
			try {
				//根据token解析出的用户openID去获取用户的登录状态
				String openID = new String();
				try{
					Claims claims = JwtUtil.parseJWT(token);
					openID = claims.getSubject();
					log.info("用户{}请求{}方法",openID,request.getPathInfo());
				}catch (Exception e1){
					HttpMethodUtil.responseJson(response,RespBean.error(RespBeanEnum.LOGIN_EXPIRED));
					return;
				}
				if (!redisTemplate.hasKey("login:" + openID)) {
					HttpMethodUtil.responseJson(response, RespBean.error(RespBeanEnum.TOKEN_ILLEGAL));
					return;
				}
				user = (User) redisTemplate.opsForValue().get("login:" + openID);

			}catch (Exception e){
				e.printStackTrace();
				log.info("校验token错误:{}",e.getMessage());
				HttpMethodUtil.responseJson(response,RespBean.error(RespBeanEnum.TOKEN_ILLEGAL));
				return;
				// throw new RuntimeException("token非法");
			}
			Collection<GrantedAuthority> authorities = new ArrayList<>();

			// GrantedAuthority authority = new
			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(user,null,authorities);
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			filterChain.doFilter(request, response);
		}

	}

}
