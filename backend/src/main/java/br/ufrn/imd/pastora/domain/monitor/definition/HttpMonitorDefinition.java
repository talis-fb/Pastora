package br.ufrn.imd.pastora.domain.monitor.definition;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@With
@Builder
public class HttpMonitorDefinition {
    //String id;

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
}
