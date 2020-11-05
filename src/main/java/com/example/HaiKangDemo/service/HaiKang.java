package com.example.HaiKangDemo.service;

import com.example.HaiKangDemo.module.HaiKangListenModule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @authon GMY
 * @create 2020-11-05 10:04
 */
@Service
public class HaiKang {
    private static List<HaiKangListenModule> haiKangListenModules = new ArrayList<>();
    public HaiKang(){
        HaiKangListenModule haiKangListenModule = new HaiKangListenModule( "172.20.10.4", "admin", "12345678a");
        haiKangListenModules.add(haiKangListenModule);
    }

}
