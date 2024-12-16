package br.ufrn.imd.pastora.scheduler;

import br.ufrn.imd.pastora.domain.monitor.MonitorData;
import br.ufrn.imd.pastora.domain.monitor.definition.HttpMonitorDefinition;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class AsyncExecutor implements Callable<ExecutionResult> {
    final HttpExecutor httpExecutor;
    final MonitorData monitorData;

    @Override
    public ExecutionResult call() throws Exception {
        HttpMonitorDefinition httpData = (HttpMonitorDefinition) monitorData.getDefinition();
        var method = httpData.getSpringMethod();
        var url = httpData.getUrl();
        var headers = httpData.getHeaders();
        var body = httpData.getBody();
        var response = httpExecutor.DoRequest(method, url, headers, body);


        return ExecutionResult.builder().execution();
    }
}
