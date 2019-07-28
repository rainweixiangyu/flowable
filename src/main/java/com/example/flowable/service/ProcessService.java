package com.example.flowable.service;

import com.example.flowable.model.TaskInfo;
import com.example.flowable.model.Process;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;

import java.util.List;

public interface ProcessService {
    ProcessInstanceBuilder createProcess(Process process);
    void deleteProcessByProcessInstanceId(String processInstanceId);
    List<ProcessInstance> queryProcessesByProcessDefinitionKey(String processDefinitionKey);
    List<ProcessInstance> queryProcessesByProcessInstanceId(String processInstanceId);
    List<ProcessInstance> queryProcessesByStarter(String starter);
    List<ProcessInstance> queryRunningProcesses();
    List<HistoricProcessInstance> queryFinishedProcesses();
    List<TaskInfo> queryTasksInProcess(String pid);
    List<TaskInfo> queryTasksByAssignee(String assignee);
    List<TaskInfo> queryTasksByCandidateGroup(String candidateGroup);
    List<TaskInfo> queryTasksByCandidateGroups(List<String> candidateGroups);
    List<TaskInfo> queryTasksByTaskID(String taskId);
    void handleTask(String taskId, Boolean approve);
    String hello(String name);
}
