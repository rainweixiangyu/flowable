package com.example.flowable.service;

import com.example.flowable.model.HolidayRequestProcess;
import com.example.flowable.model.TaskInfo;

import java.util.List;

public interface HolidayRequestService {
    HolidayRequestProcess createHolidayRequestProcess(HolidayRequestProcess process);
    void deleteProcess(String pid);
    List<HolidayRequestProcess> getAllProcesses();
    List<HolidayRequestProcess> getFinishedProcesses();
    List<TaskInfo> getTasksInProcess(String pid);
    void handleTask(String taskId, Boolean approve);
    String hello(String name);
}
