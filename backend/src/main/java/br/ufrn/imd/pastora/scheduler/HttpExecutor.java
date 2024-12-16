package br.ufrn.imd.pastora.scheduler;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.With;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class HttpExecutor {
    final private RestTemplate restTemplate;
    public HttpExecutor(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Response DoRequest(HttpMethod method, String url, Map<String, String> headers, Object payload) {
        // Create headers
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            httpHeaders.setAll(headers);
        }

        // Create the request entity
        HttpEntity<Object> requestEntity = new HttpEntity<>(payload, httpHeaders);

        // Execute the request
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(url, method, requestEntity, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Error during HTTP request: " + e.getMessage(), e);
        }

        // Return the response body
        return Response.builder()
            .statusCode(responseEntity.getStatusCode().value())
            .headers(responseEntity.getHeaders().toSingleValueMap())
            .body(responseEntity.getBody())
            .build();
    }

    @Value
    @With
    @Builder
    public static class Response {
        int statusCode;
        Map<String, String> headers;
        String body;
    }
}
