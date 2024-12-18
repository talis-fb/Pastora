package br.ufrn.imd.pastora.domain.monitor.definition;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@With
@Builder
public class HttpMonitorDefinition extends AbstractMonitorDefinition {
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
