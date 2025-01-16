package br.ufrn.imd.pastora.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.pastora.persistence.ExecutionModel;
import br.ufrn.imd.pastora.persistence.repository.ExecutionRepository;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import br.ufrn.imd.pastora.usecases.execution.GetExecutionsByMonitorUseCase;
import br.ufrn.imd.pastora.utils.AuthenticatedUserUtils;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("executions")
@AllArgsConstructor
public class ExecutionController {
  private AuthenticatedUserUtils authenticatedUserUtils;
  private MonitorRepository monitorRepository;
  private ExecutionRepository executionRepository;

  @GetMapping("/monitor/{id}")
  public ResponseEntity<Iterable<ExecutionModel>> getExecutionsByMonitor(
    @PathVariable(required = true) String id
  ) throws Exception {
    final String userId = authenticatedUserUtils.getAuthenticatedUserId();

    final Iterable<ExecutionModel> executions = new GetExecutionsByMonitorUseCase(
      monitorRepository,
      executionRepository
    ).execute(id, userId);

    return ResponseEntity.ok(executions);
  }
}
