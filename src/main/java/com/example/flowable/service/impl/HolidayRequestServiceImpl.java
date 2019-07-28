package com.example.flowable.service.impl;

import com.example.flowable.model.HolidayRequestProcess;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HolidayRequestServiceImpl extends ProcessServiceImpl {
    private final static String REQUESTOR = "requestor";
    private final static String REQUEST_DAY = "requestDay";
    private final static String REASON = "reason";

    protected HolidayRequestProcess convertProcessInstance(ProcessInstance processInstance){
        HolidayRequestProcess holidayRequestProcess = new HolidayRequestProcess();

        convertBasicProcessInstance(processInstance, holidayRequestProcess);

        Map<String, Object> variables = runtimeService.getVariables(processInstance.getId());
        holidayRequestProcess.setEmployName(variables.get(REQUESTOR).toString());
        holidayRequestProcess.setRequestDays(Integer.valueOf(variables.get(REQUEST_DAY).toString()));
        holidayRequestProcess.setReason(variables.get(REASON).toString());

        return holidayRequestProcess;
    }

    protected HolidayRequestProcess convertHistoricProcessInstance(HistoricProcessInstance historicProcessInstance){
        HolidayRequestProcess holidayRequestProcess = new HolidayRequestProcess();

        convertBasicHistoricProcessInstance(historicProcessInstance, holidayRequestProcess);

        Map<String, Object> variables = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(historicProcessInstance.getId())
                .list()
                .stream()
                .collect(Collectors.toMap(h -> h.getVariableName(), h -> h.getValue()));

        holidayRequestProcess.setEmployName(variables.get(REQUESTOR).toString());
        holidayRequestProcess.setRequestDays(Integer.valueOf(variables.get(REQUEST_DAY).toString()));
        holidayRequestProcess.setReason(variables.get(REASON).toString());

        return holidayRequestProcess;
    }

    public HolidayRequestProcess createHolidayRequestProcess(HolidayRequestProcess process){
        System.out.println("Process:" + process.toString());

        ProcessInstance processInstance = createProcess(process)
                .variable(REQUESTOR, process.getEmployName())
                .variable(REQUEST_DAY, process.getRequestDays())
                .variable(REASON, process.getReason())
                .start();

        return convertProcessInstance(processInstance);
    }

    private List<HolidayRequestProcess> getHolidayRequestProcess(List<ProcessInstance> processInstanceList){
        return processInstanceList.stream()
                .map(p -> convertProcessInstance(p))
                .collect(Collectors.toList());
    }

    public List<HolidayRequestProcess> getProcessesByProcessDefinitionKey(String processDefinitionKey){
        return getHolidayRequestProcess(queryProcessesByProcessDefinitionKey(processDefinitionKey));
    }

    public List<HolidayRequestProcess> getProcessesByProcessInstanceId(String processInstanceId){
        return getHolidayRequestProcess(queryProcessesByProcessInstanceId(processInstanceId));
    }

    public List<HolidayRequestProcess> getProcessesByStarter(String starter){
        return getHolidayRequestProcess(queryProcessesByStarter(starter));
    }

    public List<HolidayRequestProcess> getRunningProcesses(){
        return getHolidayRequestProcess(queryRunningProcesses());
    }

    private List<HolidayRequestProcess> getHolidayRequestProcessFromHistoric(List<HistoricProcessInstance> historicProcessInstanceList){
        return historicProcessInstanceList.stream()
                .map(p -> convertHistoricProcessInstance(p))
                .collect(Collectors.toList());
    }

    public List<HolidayRequestProcess> getFinishedProcesses(){
        return getHolidayRequestProcessFromHistoric(queryFinishedProcesses());
    }

    public List<HolidayRequestProcess> getAllProcesses(){
        List<HolidayRequestProcess> holidayRequestProcessList =  getRunningProcesses();

        holidayRequestProcessList.addAll(getFinishedProcesses());

        return holidayRequestProcessList;
    }

}
