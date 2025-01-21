package br.ufrn.imd.pastora.domain.http;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.Map;

@Value
@With
@Builder
public class HttpResponse {
    int statusCode;
    String body;
}
