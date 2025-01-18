package br.ufrn.imd.pastora.components;

import br.ufrn.imd.pastora.domain.http.HttpResponse;
import br.ufrn.imd.pastora.domain.monitor.MonitorValidation;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Optional;

public class ValidatorHttpResponse {
    HttpResponse response;
    Field statusCodeField;
    Field bodyField;

    @SneakyThrows
    public ValidatorHttpResponse(HttpResponse httpResponse) {
        this.statusCodeField = HttpResponse.class.getDeclaredField("statusCode");
        this.bodyField = HttpResponse.class.getDeclaredField("body");
        this.statusCodeField.setAccessible(true);
        this.bodyField.setAccessible(true);
        response = httpResponse;
    }

    @SneakyThrows
    public Optional<String> validateForError(MonitorValidation validation) {
        String responseFieldValue = switch (validation.getField()) {
            case status  -> statusCodeField.get(response).toString();
            case body -> bodyField.get(response).toString();
        };

        var validationFieldValue = validation.getValue();

        boolean isValid = switch (validation.getOperation()) {
            case equals -> responseFieldValue.equals(validationFieldValue);
            case not_equals -> !responseFieldValue.equals(validationFieldValue);
            case contains -> responseFieldValue.contains(validationFieldValue);
        };

        if (isValid) {
            return Optional.empty();
        }

        String errorMessage =  String.format("%s %s %s is invalid for %s", validation.getField(), validation.getOperation(), validation.getValue(), responseFieldValue);
        return Optional.of(errorMessage);
    }

}
