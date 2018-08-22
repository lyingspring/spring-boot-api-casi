package com.company.project.service;

import com.chinamobile.openmas.client.Sms;
import org.apache.axis2.AxisFault;

public class MasSMS {
    private static Sms sms = null;
    private static String extendCode = "8964"; //自定义扩展码（模块），可以为空，将显示在手机终端来信号码末位，注：来信号码总长度不能超过20位
    private static String ApplicationID = "8040003";//应用账号
    private static String Password = "0xxdAnWZYTFx";//应用密码

    public MasSMS() throws AxisFault {
        sms = new Sms("http://hz002.openmas.net:9080/OpenMasService");//短信接口地址
    }

    public void sendSms(String[] phoneNums, String message) {
        try {
            // 短信

            // String[] destinationAddresses = new String[]{"13588773989"};//发送对象：11数字手机号码
            // String message = "验证码:8123";//短信内容
            // 发送短信 方法三
            String GateWayid = "";
            GateWayid = sms.SendMessage(phoneNums, message,
                    extendCode, ApplicationID, Password);
            System.out.println(GateWayid);

//			// 发送短信 方法四 定时短信
//			// 设置定时时间
//			Calendar expectSendTime = Calendar.getInstance();
//			expectSendTime.add(Calendar.MINUTE, 5);
//			GateWayid = sms.SendMessage(destinationAddresses, message,
//					extendCode, ApplicationID, Password, expectSendTime);
//			System.out.println(GateWayid);
//
//			// 开始时间01-01 - 结束时间12-31
//			Calendar beginTime = Calendar.getInstance();
//			beginTime.set(Calendar.MONTH, 0);
//			beginTime.set(Calendar.DAY_OF_MONTH, 1);
//			Calendar endTime = Calendar.getInstance();
//			endTime.set(Calendar.MONTH, 11);
//			endTime.set(Calendar.DAY_OF_MONTH, 31);
//			// 定时 每月的7号 10:12:11
//			PeriodValue periodValue = new PeriodValue();
//			periodValue.setDay(7);
//			PeriodTime periodTime = new PeriodTime();
//			periodTime.setHour(10);
//			periodTime.setMinute(12);
//			periodTime.setSecond(11);
//
//			// 定时任务 方法二 含开始、截止时间
//			GateWayid = sms.AddTask(PeriodType.Month, periodValue, periodTime, destinationAddresses, message, extendCode, ApplicationID,Password, beginTime, endTime);
//			System.out.println(GateWayid);
//
//			// 定时任务 方法四
//			GateWayid = sms.AddTask(PeriodType.Month, periodValue, periodTime, destinationAddresses, message, extendCode, ApplicationID,Password);
//			System.out.println(GateWayid);
//
//			// 删除定时 方法
//			sms.RemoveTask(GateWayid);
//
//			// 获取上行短信
//			String MessageID = "02e2021b-e544-45df-87f0-192c96083826";
//			// 短信的MessageID获取方式：
//			// 1.客户开发工程师在项目中开放WebService接口
//			// 2.OpenMAS服务调用WebServcie接口，将MessageID通过参数的方式传递给应用系统
//			// 3.应用系统通过该MessageID去获取短信信息
//			// 注意：该MessageID不是发送短信是返回的ID
//			SmsMessage mo = sms.GetMessage(MessageID);
//			if (mo != null)
//				System.out.println(mo.toString());
        } catch (Exception ex) {
            System.err.println(ex);
        }
        //System.exit(0);// 正常结束程序

    }


}
