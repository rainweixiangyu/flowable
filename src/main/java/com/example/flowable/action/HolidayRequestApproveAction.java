package com.example.flowable.action;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class HolidayRequestApproveAction implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution){
        System.out.println("Holiday request is approved.");
    }
}
