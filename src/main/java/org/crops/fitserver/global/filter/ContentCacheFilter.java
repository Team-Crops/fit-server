package org.crops.fitserver.global.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.web.util.ContentCachingRequestWrapper;

public class ContentCacheFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws ServletException, IOException {
    var cachedBodyHttpServletRequest =
        new ContentCachingRequestWrapper((HttpServletRequest) request);

    chain.doFilter(cachedBodyHttpServletRequest, response);
  }
}
