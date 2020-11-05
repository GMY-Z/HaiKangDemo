package com.example.HaiKangDemo.module;

import com.example.HaiKangDemo.utils.HCNetDeviceConUtil;
import com.example.HaiKangDemo.utils.HCNetSDK;
import com.example.HaiKangDemo.utils.Haikangimp;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

/**
 * @authon GMY
 * @create 2020-10-15 16:32
 */

public class HaiKangListenModule {
    String ip;
    String user;
    String password;

    //sdk
    public static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;
    //设备信息
    HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo;
    //已登录设备的IP地址
    String m_sDeviceIP;
    //用户句柄
    int lUserID;
    //报警布防句柄
    int lAlarmHandle;
    //报警回调函数实现
    Haikangimp haikangimp;

    public HaiKangListenModule() {
        this.initInformation();
    }

    public HaiKangListenModule(String ip, String user, String password) {
        this.ip = ip;
        this.user = user;
        this.password = password;
        this.initInformation();
    }
    /**
     * 初始化信息
     */
    public void initInformation() {
        //初始化的参数
        lUserID = -1;
        lAlarmHandle = -1;
        haikangimp = null;
        //注册
        Boolean login = this.Login();
        if (login) {
            //注册成功就进行布防
            int status = this.SetupAlarmChan();
        }else{
            //
        }
    }


    /**
     * 用户注册
     *
     * @return
     */
    public Boolean Login() {
        //初始化
        hCNetSDK.NET_DVR_Init();

        //注册之前先注销已注册的用户,预览情况下不可注销
        if (lUserID > -1) {
            //先注销
            hCNetSDK.NET_DVR_Logout(lUserID);
            lUserID = -1;
        }

        boolean initSuc = hCNetSDK.NET_DVR_Init();
        if (initSuc != true) {
            System.out.println("初始化失败" + "  失败原因是：" + hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("接口初始化成功");
        }
        m_sDeviceIP = ip;
        m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        int iPort = HCNetDeviceConUtil.PORT;
        lUserID = hCNetSDK.NET_DVR_Login_V30(m_sDeviceIP,
                (short) iPort, user, password, m_strDeviceInfo);
        int userID = lUserID;
        if (userID == -1) {
            System.out.println("注册失败" + "  失败原因是：" + hCNetSDK.NET_DVR_GetLastError());
            return false;
        } else {
            System.out.println("注册成功");
            return true;
        }
    }

    /**
     * 报警布防
     */
    public int SetupAlarmChan() {
        if (haikangimp == null) {
            haikangimp = new Haikangimp();
            Pointer pUser = null;
            String s = new String(m_strDeviceInfo.sSerialNumber).trim();
            haikangimp.setsSerialNumber(s);
            if (!hCNetSDK.NET_DVR_SetDVRMessageCallBack_V30(haikangimp, pUser)) {
                System.out.println("设置回调函数失败!" + "  失败原因是：" + hCNetSDK.NET_DVR_GetLastError());
            }
        }
        HCNetSDK.NET_DVR_SETUPALARM_PARAM m_strAlarmInfo = new HCNetSDK.NET_DVR_SETUPALARM_PARAM();
        m_strAlarmInfo.dwSize = m_strAlarmInfo.size();
        m_strAlarmInfo.byLevel = 1;
        m_strAlarmInfo.byAlarmInfoType = 1;
        m_strAlarmInfo.write();
        lAlarmHandle = hCNetSDK.NET_DVR_SetupAlarmChan_V41(lUserID, m_strAlarmInfo);
        if (lAlarmHandle == -1) {
            System.out.println("布放失败" + "  失败原因是：" + hCNetSDK.NET_DVR_GetLastError());
            return 0;
        } else {
            System.out.println("布防成功");
            return 1;
        }
    }
}
