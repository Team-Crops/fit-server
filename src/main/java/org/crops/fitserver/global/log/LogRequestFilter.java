package org.crops.fitserver.global.log;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;


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
        if(request.getContentType() == null || !request.getContentType().contains("application/json")) {
            return "not json";
        }
        var requestBodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
        return new String(requestBodyBytes, StandardCharsets.UTF_8).replaceAll("\\s+",
                "");
    }
}
