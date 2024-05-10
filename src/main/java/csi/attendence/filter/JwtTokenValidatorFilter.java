package csi.attendence.filter;

import java.io.IOException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
//import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import csi.attendence.entity.User;
import csi.attendence.exceptions.AccessTokenNotFound;
import csi.attendence.service.impl.JwtAuthenticationServiceImpl;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenValidatorFilter extends OncePerRequestFilter {

	public JwtTokenValidatorFilter(JwtAuthenticationServiceImpl authenticationService) {
		super();
		this.authenticationService = authenticationService;
	}

	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getServletPath().equals("/api/v1/login");
	}
	
	private final static Logger LOGGER = LogManager.getLogger(JwtTokenValidatorFilter.class);

	private final JwtAuthenticationServiceImpl authenticationService;

	@Autowired
	@Qualifier("handlerExceptionResolver")
	public  HandlerExceptionResolver resolver;


	public void setResolver(HandlerExceptionResolver resolver) {
		this.resolver = resolver;
	}


	@SuppressWarnings("deprecation")
//	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
//		System.out.println();
		String accessToken =request.getCookies()==null?null: Arrays.asList(request.getCookies()).stream().filter(x->x.getName().equals("accessToken")).map(x->x.getValue()).findAny().orElse(null);
		try {
			if(StringUtils.isBlank(accessToken)) {
				throw new AccessTokenNotFound();
			}
			User user = this.authenticationService.validateAccessToken(accessToken);
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
					user.getAuthorities());
			auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(auth);
		} catch (RuntimeException e) {
			LOGGER.error(e.getMessage());
			resolver.resolveException(request, response, null, e);
		}
//		String jwtHeader = request.getHeader("Authorization");
//		if (jwtHeader != null && !jwtHeader.trim().isEmpty() && jwtHeader.startsWith("Bearer ")) {
//			jwtHeader = jwtHeader.replace("Bearer ", "");
//			try {
//				User user = this.authenticationService.validateAccessToken(jwtHeader);
//				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
//						user.getAuthorities());
//				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				SecurityContextHolder.getContext().setAuthentication(auth);
//			} catch (RuntimeException e) {
//				LOGGER.error(e.getMessage());
//				resolver.resolveException(request, response, null, e);
//			}
//		}

		filterChain.doFilter(request, response);

	}


}
