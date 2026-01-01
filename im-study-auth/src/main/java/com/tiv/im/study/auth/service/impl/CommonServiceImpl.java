package com.tiv.im.study.auth.service.impl;

import com.tiv.im.study.gateway.common.CodeEnum;
import com.tiv.im.study.auth.constants.AuthConstants;
import com.tiv.im.study.auth.data.common.file.FileUploadUrlResponse;
import com.tiv.im.study.auth.data.common.sms.SmsRequest;
import com.tiv.im.study.gateway.exception.GlobalException;
import com.tiv.im.study.auth.service.CommonService;
import com.tiv.im.study.auth.utils.MinioUtil;
import com.tiv.im.study.auth.utils.SmsUtil;
import com.tiv.im.study.auth.utils.VerificationCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CommonServiceImpl implements CommonService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private MinioUtil minioUtil;

    @Override
    public Boolean sendSmsCode(SmsRequest request) {
        String phone = request.getPhone();
        String code = VerificationCodeGenerator.getRandomNum();
        stringRedisTemplate.opsForValue().set(AuthConstants.USER_PREFIX + phone, code, 5, TimeUnit.MINUTES);
        try {
            SmsUtil.mockSendSms(phone, code);
            log.info("手机号: {}, 验证码: {}", phone, code);
        } catch (Exception e) {
            log.error("发送短信失败", e);
            throw new GlobalException(CodeEnum.SEND_CODE_ERROR);
        }
        return null;
    }

    @Override
    public FileUploadUrlResponse getFileUploadUrl(String fileName) {
        String uploadUrl = minioUtil.uploadUrl(fileName, 60);
        String downUrl = minioUtil.downUrl(fileName);

        return FileUploadUrlResponse.builder()
                .uploadUrl(uploadUrl)
                .downloadUrl(downUrl)
                .build();
    }

}
