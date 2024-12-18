package br.ufrn.imd.pastora.domain;

import br.ufrn.imd.pastora.domain.http.HttpMethod;
import br.ufrn.imd.pastora.domain.http.HttpRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@With
@Builder
public class MonitorHttpDefinition extends MonitorAbstractDefinition {
    String id;

    @NotNull
    private final String type = "HTTP";

    @NotNull
    String url;

    @NotNull
    HttpMethod method;

    Map<String, String> headers = new HashMap<>();
    String body;

    public HttpRequest toHttpRequest() {
        return HttpRequest.builder().url(url).method(method).headers(headers).payload(body).build();
    }
}
