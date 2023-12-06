package org.crops.fitserver.global.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.jboss.logging.MDC;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * 가장 상위부터 UUID를 부여하기 위해 Filter 사용 (error 발생시에도 UUID가 부여되어야 하기 때문에)
 */
public class LogFilter extends OncePerRequestFilter {

  public static final String DEFAULT_MDC_UUID_KEY = "MDC_UUID";
  public static final int DEFAULT_MDC_UUID_SIZE = 10;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      MDC.put(DEFAULT_MDC_UUID_KEY,
          UUID.randomUUID().toString().toUpperCase().replace("-", "")
              .substring(0, DEFAULT_MDC_UUID_SIZE));
      filterChain.doFilter(request, response);
    } finally {
      MDC.remove(DEFAULT_MDC_UUID_KEY);
    }
  }
}
