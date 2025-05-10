package com.bruce.langchain4jdemo.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 用户ID
    private Long id;
    //用户名
    private String username;
    //密码
    private String password;
    //身份证号
    private String idCard;
    //邮箱
    private String email;
    //年龄
    private Integer age;
}
