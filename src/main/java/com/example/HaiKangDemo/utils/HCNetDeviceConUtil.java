package com.example.HaiKangDemo.utils;

import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * @authon GMY
 * @create 2020-10-15 16:24
 */

public class HCNetDeviceConUtil {

//    // 登录IP
//    public static final String m_sDeviceIP = "登录IP";	//（登录IP 例如 192.168.0.1，它可以用来组网，可以在海康后台组建由这个ip控制的某几个海康摄像头）
//    // 登录名
//    public static final String USERNAME = "账号"; //（例如 admin）
//    // 密码
//    public static final String PASSWORD = "密码"; //（例如 123456）
    //设备端口号
    public static final Integer PORT = 8000;
    //加载海康HCNetSDK.dll文件的路径
    public static final String loadLibrary=HCNetSDKPath.DLL_PATH;

    public static class HCNetSDKPath {
        public static String DLL_PATH;
        /*下面这个是加载dll文件的 ，也就是上面的第3步（做了第3步可以不要这个static里面的内容，但是用这个把第3步换成工具类加载更加的方便后续的维护，所以我们把第3步的加载路径换成：
          HCNetSDK INSTANCE = (HCNetSDK) Native.loadLibrary(HCNetDeviceConUtil.loadLibrary,
                HCNetSDK.class);
    */
        static {
//            本地
//            String path = (HCNetSDKPath.class.getResource("/HCNetSDK/HCNetSDK.dll").getPath()).replaceAll("%20", " ").substring(1).replace("/",
//                    "\\");
//
//            try {
//                DLL_PATH = java.net.URLDecoder.decode(path, "utf-8");
//                System.out.println(DLL_PATH);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            DLL_PATH = "./src/main/resources/HCNetSDK/HCNetSDK.dll";
//            线上

            ApplicationHome h = new ApplicationHome(HCNetSDKPath.class);
            File jarF = h.getSource();
            String p = jarF.getParentFile().toString();
            System.out.println(p);
            DLL_PATH = p + "\\HCNetSDK\\HCNetSDK.dll";
        }
    }
}
