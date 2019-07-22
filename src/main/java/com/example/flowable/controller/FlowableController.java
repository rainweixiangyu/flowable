package com.example.flowable.controller;

import com.example.flowable.model.HolidayRequestProcess;
import com.example.flowable.model.TaskInfo;
import com.example.flowable.service.HolidayRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flowable")
public class FlowableController {

    @Autowired
    private HolidayRequestService holidayRequestService;

    @GetMapping(value = {"/hello"})
    public String hello(@RequestParam(value = "name") String name){
        return holidayRequestService.hello(name);
    }

    @PostMapping(value = {"/processes"})
    public HolidayRequestProcess createProcess(@RequestBody HolidayRequestProcess process){
        HolidayRequestProcess holidayRequestProcess = holidayRequestService.createHolidayRequestProcess(process);

        return holidayRequestProcess;
    }

    @DeleteMapping(value = {"/processes/{pid}"})
    public void deleteProcess(@PathVariable String pid){
        holidayRequestService.deleteProcess(pid);
    }

    @GetMapping(value = {"/processes"})
    public  List<HolidayRequestProcess> getAllProcesses(){
        List<HolidayRequestProcess> holidayRequestProcesses = holidayRequestService.getAllProcesses();

        return holidayRequestProcesses;
    }

    @GetMapping(value = {"/processes/finished"})
    public List<HolidayRequestProcess> getFinishedProcesses(){
        List<HolidayRequestProcess> holidayRequestProcesses = holidayRequestService.getFinishedProcesses();

        return holidayRequestProcesses;
    }

    @GetMapping(value = {"/{pid}/tasks"})
    public List<TaskInfo> getTasksInProcess(@PathVariable String pid){
        List<TaskInfo> tasks = holidayRequestService.getTasksInProcess(pid);

        return tasks;
    }

    @PostMapping(value = {"/tasks/{taskId}"})
    public void handleTask(@PathVariable String taskId, @RequestParam Boolean approve){
        holidayRequestService.handleTask(taskId, approve);
    }

}
