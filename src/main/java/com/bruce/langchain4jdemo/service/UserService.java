package com.bruce.langchain4jdemo.service;

import com.bruce.langchain4jdemo.dto.UserDTO;

public interface UserService {
    UserDTO extractPersonFrom(String userMassge);
}
