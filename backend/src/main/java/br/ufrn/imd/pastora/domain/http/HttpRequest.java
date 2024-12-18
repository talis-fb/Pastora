package br.ufrn.imd.pastora.domain.http;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.Map;

@Value
@With
@Builder
public class HttpRequest {
    HttpMethod method;
    String url;
    Map<String, String> headers;
    Object payload;

    public org.springframework.http.HttpHeaders getSpringHeaders() {
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        if (this.headers != null) {
            headers.setAll(this.headers);
        }
        return headers;
    }

    public org.springframework.http.HttpMethod getSpringMethod() {
        return method.toSpringMethod();
    }
}
