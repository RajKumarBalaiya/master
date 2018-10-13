package com.spring.profiles.impl;

import org.springframework.stereotype.Component;

import com.spring.profiles.base.DatabaseService;

@Component()
public class DevDBConnection implements  DatabaseService {
    
    @Override
    public void getDBConnection() {
        System.out.println("DEV DB connection established");
    }
}