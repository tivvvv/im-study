package com.tiv.im.study.auth.service;

import com.tiv.im.study.auth.data.common.file.FileUploadUrlResponse;
import com.tiv.im.study.auth.data.common.sms.SmsRequest;

public interface CommonService {

    Boolean sendSmsCode(SmsRequest request);

    FileUploadUrlResponse getFileUploadUrl(String fileName);

}
