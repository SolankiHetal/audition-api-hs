package com.audition.configuration;

import io.opentelemetry.api.trace.Span;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ResponseHeaderInjector implements HandlerInterceptor {

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
        final Object handler, final Exception ex) {
        final Span currentSpan = Span.current();
        response.addHeader("X-Trace-Id", currentSpan.getSpanContext().getTraceId());
        response.addHeader("X-Span-Id", currentSpan.getSpanContext().getSpanId());
    }
}
