package com.example.HaiKangDemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.HaiKangDemo.module.HaiKangListenModule;
import com.example.HaiKangDemo.utils.AccessingThirdPartyInterface;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.HaiKangDemo.utils.ForDevTime.sendHeartBeat;

/**
 * @authon GMY
 * @create 2020-11-05 10:04
 */
@Service
public class HaiKang {
    private static List<HaiKangListenModule> haiKangListenModules = new ArrayList<>();

    public HaiKang() {
        String url = "https://tidukeji.cn/SchoolSecurityService1/schoolSafetySupervisor/getAllFaceRecognitionDevice";
        //使用JSject
        JSONObject json =  new JSONObject();
        json.put("department", 40);
        String dataSource = json.toString();
        String jsonString = AccessingThirdPartyInterface.httppost(url, dataSource);
        if(jsonString == "fail"){
            System.out.println("服务器接口获取设备信息错误");
        }
        JSONArray jsonArray = JSON.parseArray(jsonString);
        for (Object obj : jsonArray) {
            com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) obj;
            System.out.println(jsonObject.getString("name"));
            if(jsonObject.getString("name").equals("海康人脸识别机")){
                System.out.println(1);
                HaiKangListenModule gateListenModule = new HaiKangListenModule(jsonObject.getString("deviceIP"), jsonObject.getString("deviceUserName"), jsonObject.getString("devicePassword"));
                haiKangListenModules.add(gateListenModule);
            }
        }
//        HaiKangListenModule haiKangListenModule = new HaiKangListenModule("172.20.10.4", "admin", "12345678a");
//        haiKangListenModules.add(haiKangListenModule);
//        deviceIpIdMap.put("172.20.10.4", haiKangListenModule.getDeviceId());
//        threadForHeartBeat(haiKangListenModule);
        threadForHeartBeat(haiKangListenModules);

    }

    public static void threadForHeartBeat(List<HaiKangListenModule> haiKangListenModules) {
        new Thread() {
            @Override
            public void run() {

                String name2 = Thread.currentThread().getName();
                System.out.println(name2 + ":你好啊 我是一个线程!");
                while (true) {
                    try {
                        Thread.sleep(300000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (HaiKangListenModule haiKangListenModule : haiKangListenModules) {
                        String deviceId = haiKangListenModule.getDeviceId();
                        System.out.println(deviceId);
                        if(haiKangListenModule.getDevStatus()){
                            sendHeartBeat(deviceId, 1);
                        }else {
                            sendHeartBeat(deviceId, 0 );
                        }
                    }
                }
            }
        }.start();
    }

}
