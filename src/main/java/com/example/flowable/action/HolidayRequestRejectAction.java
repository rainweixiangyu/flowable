package com.example.flowable.action;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class HolidayRequestRejectAction implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution){
        System.out.println("Holiday request is rejected.");
    }
}
