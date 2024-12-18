package br.ufrn.imd.pastora.scheduler;

import br.ufrn.imd.pastora.domain.ExecutionData;
import lombok.*;

import java.util.List;

@Value
@With
@Builder
public class ExecutionResult {
    ExecutionData executionData;
    List<ExecutionData> childExecutionData;
}
