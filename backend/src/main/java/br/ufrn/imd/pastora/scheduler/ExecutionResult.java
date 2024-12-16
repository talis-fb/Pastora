package br.ufrn.imd.pastora.scheduler;

import br.ufrn.imd.pastora.domain.Execution;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
public class ExecutionResult {
    public final Execution execution;
    public final List<Execution> childExecutions;
}
