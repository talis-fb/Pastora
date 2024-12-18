package br.ufrn.imd.pastora.domain.http;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH;

    public static HttpMethod fromString(String method) {
        return HttpMethod.valueOf(method.toUpperCase());
    }

    public org.springframework.http.HttpMethod toSpringMethod() {
        return switch (this) {
            case GET -> org.springframework.http.HttpMethod.GET;
            case POST -> org.springframework.http.HttpMethod.POST;
            case PUT -> org.springframework.http.HttpMethod.PUT;
            case DELETE -> org.springframework.http.HttpMethod.DELETE;
            case PATCH -> org.springframework.http.HttpMethod.PATCH;
            default -> null;
        };
    }
}
