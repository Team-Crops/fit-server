package org.crops.fitserver.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crops.fitserver.global.http.ErrorType;
import org.crops.fitserver.global.exception.FitException;
import org.crops.fitserver.global.http.FailResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			responseError(
					response,
					ErrorType.INTERNAL_SERVER_ERROR,
					ErrorType.INTERNAL_SERVER_ERROR.getMessage());
			log.error(e.getMessage());
		}
	}

	private void responseError(
			HttpServletResponse response,
			ErrorType errorType,
			String message)
			throws IOException {
		response.setStatus(errorType.getStatusCode());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter()
				.write(objectMapper.writeValueAsString(
						new FailResponse(
								errorType.getStatusCode(),
								message)));
	}
}
