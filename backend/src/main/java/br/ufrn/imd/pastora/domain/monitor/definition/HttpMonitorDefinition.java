package br.ufrn.imd.pastora.domain.monitor.definition;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@With
@Builder
public class HttpMonitorDefinition extends AbstractMonitorDefinition {
    String id;

    // String KIND = "HTTP";

    @NotNull
    String url;

    @NotNull
    HttpMethod method;
    public enum HttpMethod {
        GET,
        POST,
        PUT,
        DELETE,
        PATCH,
    }

    Map<String, String> headers = new HashMap<>();
    String body;

    public org.springframework.http.HttpMethod getSpringMethod() {
        return switch (this.method) {
            case GET -> org.springframework.http.HttpMethod.GET;
            case POST -> org.springframework.http.HttpMethod.POST;
            case PUT -> org.springframework.http.HttpMethod.PUT;
            case DELETE -> org.springframework.http.HttpMethod.DELETE;
            case PATCH -> org.springframework.http.HttpMethod.PATCH;
            default -> null;
        };
    }
}
