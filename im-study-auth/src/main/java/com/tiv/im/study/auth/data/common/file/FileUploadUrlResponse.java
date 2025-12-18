package com.tiv.im.study.auth.data.common.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadUrlResponse {

    /**
     * 文件上传地址
     */
    public String uploadUrl;

    /**
     * 文件下载地址
     */
    public String downloadUrl;

}
