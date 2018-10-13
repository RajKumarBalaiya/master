package com.spring.profiles.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.spring.profiles.base.DatabaseService;

@Component()
@Profile("production")
public class ProdDBConnection implements DatabaseService {

    @Override
    public void getDBConnection() {
        
        System.out.println("Product DB connection establish");
    }
}