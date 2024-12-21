package br.ufrn.imd.pastora.domain;

import br.ufrn.imd.pastora.domain.http.HttpMethod;
import br.ufrn.imd.pastora.domain.http.HttpRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.HashMap;
import java.util.Map;

@Data
@With
@Builder
public class MonitorHttpDefinition  {
    @NotNull
    String url;

    String type = "HTTP";

    @NotNull
    HttpMethod method;

    Map<String, String> headers = new HashMap<>();
    String body;

    public HttpRequest toHttpRequest() {
        return HttpRequest.builder().url(url).method(method).headers(headers).payload(body).build();
    }
}
