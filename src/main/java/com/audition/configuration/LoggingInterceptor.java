package com.audition.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

@RequiredArgsConstructor
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingInterceptor.class);

    private final ObjectMapper objectMapper;


    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
        final ClientHttpRequestExecution execution)
        throws IOException {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Request details");
            LOGGER.info("URI: {}", request.getURI());
            LOGGER.info("Headers: {}", request.getHeaders());
            LOGGER.info("Method: {}", request.getMethod());
        }

        final ClientHttpResponse response = execution.execute(request, body);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Response details");
            LOGGER.info("Headers: {}", response.getHeaders());
            LOGGER.info("status: {}", response.getStatusCode());
        }

        try (InputStream inputStream = response.getBody()) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Response body: {}", objectMapper.readValue(inputStream, Map.class));
            }

        } catch (IOException e) {
            LOGGER.error("Failed during response serialization");
        }

        return response;
    }
}
