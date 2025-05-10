package com.bruce.langchain4jdemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    // 用户名
    private String username;
    // 密码
    private String password;
    //身份证号
    private String idCard;
    //邮箱
    private String email;
    //年龄
    private Integer age;
}
