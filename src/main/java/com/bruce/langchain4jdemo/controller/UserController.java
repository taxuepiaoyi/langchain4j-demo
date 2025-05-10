package com.bruce.langchain4jdemo.controller;

import com.bruce.langchain4jdemo.dto.UserDTO;
import com.bruce.langchain4jdemo.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService ;

    /**
     * 从文本中提取用户信息
     * @param userMassge
     * @return
     */
    @GetMapping("/extractPersonFrom")
    public UserDTO extractPersonFrom(@RequestParam("userMassge") String userMassge){
        return userService.extractPersonFrom(userMassge);
    }
}
