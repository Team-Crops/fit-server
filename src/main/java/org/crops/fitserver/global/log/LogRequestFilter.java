package org.crops.fitserver.global.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Slf4j
public class LogRequestFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    var requestBody = getRequestBody(request);

    var requestMethod = request.getMethod();
    var requestURN = request.getRequestURI() + (request.getQueryString() != null ? "?"
        + request.getQueryString() : "");
    log.info("{} {}, requestBody: {}", requestMethod, requestURN, requestBody);

    filterChain.doFilter(request, response);
  }

  private String getRequestBody(HttpServletRequest request) throws IOException {
    if (request.getContentType() == null || !request.getContentType()
        .contains("application/json")) {
      return "not json";
    }

    var cachedRequest = (ContentCachingRequestWrapper) request;
    var requestBodyBytes = cachedRequest.getContentAsByteArray();

    return new String(requestBodyBytes, StandardCharsets.UTF_8).replaceAll("\\s+",
        "");
  }
}
