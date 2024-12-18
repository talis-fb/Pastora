package br.ufrn.imd.pastora.usecases;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;

import br.ufrn.imd.pastora.controllers.dto.MonitorResponseDTO;
import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.MonitorValidationModel;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import br.ufrn.imd.pastora.persistence.repository.MonitorValidationRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetMonitorUseCase {
  private final MonitorRepository monitorRepository;
  private final MonitorValidationRepository monitorValidationRepository;

  public Optional<MonitorResponseDTO> execute(String id) {
    final Optional<MonitorModel> findedMonitor = this.monitorRepository.findById(id);
    
    if(findedMonitor.isEmpty()) return Optional.empty();

    final MonitorResponseDTO response = new MonitorResponseDTO();
    BeanUtils.copyProperties(findedMonitor.get(), response, "validations");
    
    List<String> validationIds = findedMonitor.get().getValidations();
    final List<MonitorValidationModel> findedValidations = new ArrayList<>();
    this.monitorValidationRepository.findAllById(validationIds).forEach(findedValidations::add);

    response.setValidations(findedValidations);
    return Optional.of(response);
  }
}
