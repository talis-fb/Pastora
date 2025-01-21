package br.ufrn.imd.pastora.components;

import br.ufrn.imd.pastora.domain.http.HttpRequest;
import br.ufrn.imd.pastora.domain.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpExecutor {
    final static Logger logger = LoggerFactory.getLogger(HttpExecutor.class);
    final private RestTemplate restTemplate;
    public HttpExecutor(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    public HttpResponse submitRequest(HttpRequest request) {
        logger.info("EXECUTING HTTP Request -> URL {}, METHOD: {}", request.getUrl(), request.getMethod());

        // Create headers
        Object payload = request.getPayload();
        HttpHeaders httpHeaders = request.getSpringHeaders();

        // Create the request entity
        HttpEntity<Object> requestEntity = new HttpEntity<>(payload, httpHeaders);

        // Execute the request
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(
                request.getUrl(),
                request.getSpringMethod(),
                requestEntity,
                String.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Error during HTTP request: " + e.getMessage(), e);
        }

        // Return the response body
        logger.info("Finished request with status {}", responseEntity.getStatusCode());
        return HttpResponse
            .builder()
            .statusCode(responseEntity.getStatusCode().value())
            .body(responseEntity.getBody())
            .build();
    }
}
