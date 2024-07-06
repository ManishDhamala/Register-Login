package com.project.demo.service;

import com.project.demo.dto.UserDto;
import com.project.demo.model.Role;
import com.project.demo.model.User;
import com.project.demo.repository.RoleRepository;
import com.project.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository  userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //This method takes a UserDto object and saves it as a User
    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName()+" "+userDto.getLastName());
        user.setEmail(userDto.getEmail());
        //Encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        //Fetching or creating role
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role==null){
            role = checkRoleExist(); //create and save the role if it is null
        }

        // "ROLE_ADMIN" is assigned to the user.
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // fetches all users and converts them to a list of UserDto objects.
    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user -> mapToUserDto(user)))
                .collect(Collectors.toList());
    }


    //Converts a User to a UserDto by splitting the full name
    // and setting the first name, last name, and email.
    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }


    // Check if the "ROLE_ADMIN" exist, if it doesn't create and save it
    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }



}
