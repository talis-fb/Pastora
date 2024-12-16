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
    String name;

    String description;

    String iconUrl;
}
