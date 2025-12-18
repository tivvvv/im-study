package com.tiv.im.study.auth.utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SmsUtil {

    private static final String ACCESS_KEY_ID = "";

    private static final String ACCESS_KEY_SECRET = "";

    private static final String SIGN_NAME = "";

    private static final String TEMPLATE_CODE = "";

    private static final String END_POINT = "";

    private static volatile Client client;

    public static Client getClient() throws Exception {
        if (client == null) {
            synchronized (SmsUtil.class) {
                if (client == null) {
                    Config config = new Config()
                            .setAccessKeyId(ACCESS_KEY_ID)
                            .setAccessKeySecret(ACCESS_KEY_SECRET)
                            .setEndpoint(END_POINT);
                    client = new Client(config);
                }
            }
        }
        return client;
    }

    public static void sendSms(String phone, String code) throws Exception {
        Client client = getClient();
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName(SIGN_NAME)
                .setTemplateCode(TEMPLATE_CODE)
                .setPhoneNumbers(phone)
                .setTemplateParam(code);
        RuntimeOptions runtime = new RuntimeOptions();
        SendSmsResponse response = client.sendSmsWithOptions(sendSmsRequest, runtime);
        log.info("短信发送成功,response: {}", response);
    }

    public static void mockSendSms(String phone, String code) throws Exception {

    }

}
