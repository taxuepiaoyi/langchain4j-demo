package com.bruce.langchain4jdemo.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Data
@Builder
public class ImagePromptDTO {
    // 提示词
    private String prompt;

    // 上传的图片
    private MultipartFile image ;

    /**
     * 获取图片的base64编码
     * @return
     * @throws IOException
     */
    public String getBase64Image() throws IOException {
        return Base64.getEncoder().encodeToString(image.getBytes());
    }
}
