package com.example.HaiKangDemo.utils;

import com.example.HaiKangDemo.domain.EventAccessInfo;
import com.example.HaiKangDemo.mq.RabbitMQProducer;
import com.sun.jna.Pointer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @authon GMY
 * @create 2020-10-15 16:41
 */

public class Haikangimp implements HCNetSDK.FMSGCallBack {
    private BufferedImage gateBufferedImage = null;
    private String sSerialNumber;

    public void setsSerialNumber(String sSerialNumber) {
        this.sSerialNumber = sSerialNumber;
    }

    @Override
    public void invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
        String sAlarmType = new String();
        String[] newRow = new String[3];
        Date today = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String[] sIP = new String[2];
        System.out.println(lCommand);
        switch (lCommand) {
            case HCNetSDK.COMM_ALARM_ACS: //门禁主机报警信息
                HCNetSDK.NET_DVR_ACS_ALARM_INFO strACSInfo = new HCNetSDK.NET_DVR_ACS_ALARM_INFO();
                strACSInfo.write();
                Pointer pACSInfo = strACSInfo.getPointer();
                pACSInfo.write(0, pAlarmInfo.getByteArray(0, strACSInfo.size()), 0, strACSInfo.size());
                strACSInfo.read();

                sAlarmType = sAlarmType + "：门禁主机报警信息，卡号：" + new String(strACSInfo.struAcsEventInfo.byCardNo).trim() + "，卡类型：" +
                        strACSInfo.struAcsEventInfo.byCardType + "，报警主类型：" + strACSInfo.dwMajor + "，报警次类型：" + strACSInfo.dwMinor;

                newRow[0] = dateFormat.format(today);
                //报警类型
                System.out.println(sAlarmType);
                //报警设备IP地址
                sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);


                if (strACSInfo.dwPicDataLen > 0) {
                    //将字节写入文件
                    long offset = 0;
                    byte[] buffers = strACSInfo.pPicData.getByteArray(offset, strACSInfo.dwPicDataLen);

                    EventAccessInfo eventAccessInfo = new EventAccessInfo();
                    eventAccessInfo.setStudentId((String.valueOf(strACSInfo.struAcsEventInfo.dwEmployeeNo)).trim());
//                eventAccessInfo.setStudentName();
                    eventAccessInfo.setTime("" + System.currentTimeMillis());
                    eventAccessInfo.setImageBuffer(buffers);
                    eventAccessInfo.setDeviceId(sSerialNumber);
                    eventAccessInfo.setCardId( new String(strACSInfo.struAcsEventInfo.byCardNo).trim());
                    System.out.println(eventAccessInfo);
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newName = sf.format(new Date());

                    File path = new File("./GateSnapPicture/");
                    if (!path.exists()) {
                        path.mkdir();
                    }
                    String snapPicPath = path + "\\" + System.currentTimeMillis() + "GateSnapPicture.jpg";

                    //发送到mq
                    RabbitMQProducer rabbitMQProducer = (RabbitMQProducer) SpringUtil.getBean("RabbitMQProducer");
                    rabbitMQProducer.send(eventAccessInfo);

                    //保存图片在本地
                    ByteArrayInputStream byteArrInputGlobal = new ByteArrayInputStream(buffers);
                    try {
                        gateBufferedImage = ImageIO.read(byteArrInputGlobal);
                        if (gateBufferedImage != null) {
                            ImageIO.write(gateBufferedImage, "jpg", new File(snapPicPath));
                        }
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
//                    FileOutputStream fout;
                    try {
//                        String filename = ".\\pic\\" + new String(pAlarmer.sDeviceIP).trim() +
//                                "_byCardNo[" + new String(strACSInfo.struAcsEventInfo.byCardNo).trim() +
//                                "_" + newName + "_Acs.jpg";
//                        fout = new FileOutputStream(filename);
//                        byte[] bytes = new byte[strACSInfo.dwPicDataLen];
//                        buffers.rewind();
//                        buffers.get(bytes);
//                        fout.write(bytes);
//                        fout.close();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
        }

    }
}

