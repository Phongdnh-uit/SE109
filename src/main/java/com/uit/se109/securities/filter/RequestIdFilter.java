package com.uit.se109.securities.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RequestIdFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      // Lấy ID từ Header hoặc tự tạo mới nếu không có
      String requestId = request.getHeader("X-Request-ID");
      if (requestId == null || requestId.isEmpty()) {
        requestId = UUID.randomUUID().toString();
      }

      // Đưa vào MDC - Key phải trùng với %X{X-Request-ID} trong config
      MDC.put("X-Request-ID", requestId);

      // Trả về header cho client để đối chiếu nếu cần
      response.setHeader("X-Request-ID", requestId);

      filterChain.doFilter(request, response);
    } finally {
      // Quan trọng: Phải clear sau khi kết thúc request để tránh rò rỉ dữ liệu sang thread khác
      MDC.clear();
    }
  }
}
