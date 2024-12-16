package br.ufrn.imd.pastora.scheduler;

import br.ufrn.imd.pastora.domain.Execution;
import lombok.*;

import java.util.List;

@Value
@With
@Builder
public class ExecutionResult {
    public final Execution execution;
    public final List<Execution> childExecutions;
}
