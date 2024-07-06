package com.project.demo.service;

import com.project.demo.dto.UserDto;
import com.project.demo.model.User;

import java.util.List;

public interface UserService {

    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();




}
