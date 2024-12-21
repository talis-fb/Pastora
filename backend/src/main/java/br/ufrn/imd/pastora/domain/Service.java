package br.ufrn.imd.pastora.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@With
@Builder
public class Service {
    @NotNull
    protected final String name;

    String userId;
    String description;

    String iconUrl;
}
