package com.example.flowable.service.impl;

import com.example.flowable.model.HolidayRequestProcess;
import com.example.flowable.model.TaskInfo;
import com.example.flowable.service.HolidayRequestService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HolidayRequestServiceImpl implements HolidayRequestService {
    private final static String REQUESTOR = "requestor";
    private final static String REQUEST_DAY = "requestDay";
    private final static String REASON = "reason";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Override
    public String hello(String name){
        return "Hi, " + name;
    }

    @Override
    public HolidayRequestProcess createHolidayRequestProcess(HolidayRequestProcess process){
        System.out.println("Process:" + process.toString());

        ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder()
                .processDefinitionKey(process.getProcessDefinitionKey())
                .variable(REQUESTOR, process.getEmployName())
                .variable(REQUEST_DAY, process.getRequestDays())
                .variable(REASON, process.getReason())
                .start();

        return convertProcessInstance(processInstance);
    }

    private HolidayRequestProcess convertProcessInstance(ProcessInstance processInstance){
        Map<String, Object> variables = runtimeService.getVariables(processInstance.getId());

        HolidayRequestProcess holidayRequestProcess = new HolidayRequestProcess();
        holidayRequestProcess.setExecutionId(processInstance.getId());
        holidayRequestProcess.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        holidayRequestProcess.setProcessDefinitionName(processInstance.getProcessDefinitionName());
        holidayRequestProcess.setName(processInstance.getName());
        holidayRequestProcess.setProcessInstanceId(processInstance.getProcessInstanceId());
        holidayRequestProcess.setDescription(processInstance.getDescription());
        holidayRequestProcess.setStartTime(processInstance.getStartTime());
        holidayRequestProcess.setStartUser(processInstance.getStartUserId());
        holidayRequestProcess.setEmployName(variables.get(REQUESTOR).toString());
        holidayRequestProcess.setRequestDays(Integer.valueOf(variables.get(REQUEST_DAY).toString()));
        holidayRequestProcess.setReason(variables.get(REASON).toString());
        holidayRequestProcess.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());

        return holidayRequestProcess;
    }

    private HolidayRequestProcess convertHistoricProcessInstance(HistoricProcessInstance historicProcessInstance){
        Map<String, Object> variables = historicProcessInstance.getProcessVariables();

        HolidayRequestProcess holidayRequestProcess = new HolidayRequestProcess();
        holidayRequestProcess.setExecutionId(historicProcessInstance.getId());
        holidayRequestProcess.setProcessDefinitionId(historicProcessInstance.getProcessDefinitionId());
        holidayRequestProcess.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
        holidayRequestProcess.setName(historicProcessInstance.getName());
        holidayRequestProcess.setProcessInstanceId(historicProcessInstance.getSuperProcessInstanceId());
        holidayRequestProcess.setDescription(historicProcessInstance.getDescription());
        holidayRequestProcess.setStartTime(historicProcessInstance.getStartTime());
        holidayRequestProcess.setStartUser(historicProcessInstance.getStartUserId());
        //holidayRequestProcess.setEmployName(variables.get(REQUESTOR).toString());
        //holidayRequestProcess.setRequestDays(Integer.valueOf(variables.get(REQUEST_DAY).toString()));
        //holidayRequestProcess.setReason(variables.get(REASON).toString());
        holidayRequestProcess.setProcessDefinitionKey(historicProcessInstance.getProcessDefinitionKey());

        return holidayRequestProcess;
    }

    @Override
    public List<HolidayRequestProcess> getAllProcesses(){
        return runtimeService.createProcessInstanceQuery().list().stream().map(p -> convertProcessInstance(p)).collect(Collectors.toList());
    }

    @Override
    public List<HolidayRequestProcess> getFinishedProcesses(){
        return historyService.createHistoricProcessInstanceQuery()
                .finished()
                .list()
                .stream()
                .map(p -> convertHistoricProcessInstance(p))
                .collect(Collectors.toList());
    }

    private TaskInfo convertTask(Task task){
        TaskInfo taskInfo = new TaskInfo();

        taskInfo.setId(task.getId());
        taskInfo.setName(task.getName());
        taskInfo.setDescription(task.getDescription());
        taskInfo.setAssignee(task.getAssignee());
        taskInfo.setExecutionId(task.getExecutionId());
        taskInfo.setProcessInstanceId(task.getProcessInstanceId());
        taskInfo.setProcessDefinitionId(task.getProcessDefinitionId());
        taskInfo.setTaskDefinitionId(task.getTaskDefinitionId());
        taskInfo.setTaskDefinitionKey(task.getTaskDefinitionKey());
        taskInfo.setPriority(task.getPriority());
        taskInfo.setCreateTime(task.getCreateTime());

        return taskInfo;
    }

    @Override
    public List<TaskInfo> getTasksInProcess(String pid){
        return taskService.createTaskQuery()
                .processInstanceId(pid)
                .orderByTaskCreateTime()
                .desc()
                .list()
                .stream()
                .map(t -> convertTask(t))
                .collect(Collectors.toList());
    }

    @Override
    public void handleTask(String taskId, Boolean approve){
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        if(task == null){
            throw new RuntimeException("There is no task.");
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("approved", approve);
        taskService.complete(taskId, map);
    }

    @Override
    public void deleteProcess(String pid){
        runtimeService.deleteProcessInstance(pid, "");
    }
}
