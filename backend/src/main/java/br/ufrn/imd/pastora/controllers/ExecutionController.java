package br.ufrn.imd.pastora.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
  private final Logger logger = LoggerFactory.getLogger(ExecutionController.class);
  private final AuthenticatedUserUtils authenticatedUserUtils;
  private final MonitorRepository monitorRepository;
  private final ExecutionRepository executionRepository;

  @GetMapping("/monitor/{id}")
  public ResponseEntity<Iterable<ExecutionModel>> getExecutionsByMonitor(
    @PathVariable(required = true) String id,
    @RequestParam(required = false, defaultValue = "100") Integer limit
  ) throws Exception {
    logger.info("Get executions by monitor id {} with limit {}", id, limit);
    final String userId = authenticatedUserUtils.getAuthenticatedUserId();

    final Iterable<ExecutionModel> executions = new GetExecutionsByMonitorUseCase(
      monitorRepository,
      executionRepository
    ).execute(id, userId, limit);

    return ResponseEntity.ok(executions);
  }
}
