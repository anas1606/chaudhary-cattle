package com.chaudhary.chaudharycattle.service.impl;

import com.chaudhary.chaudharycattle.entities.User;
import com.chaudhary.chaudharycattle.repositories.UserRepository;
import com.chaudhary.chaudharycattle.service.LoginService;
import com.chaudhary.chaudharycattle.utils.CommanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean login(String userName, String password) {
        User user = userRepository.findByUsername(userName);
        return user != null && CommanUtils.checkPassword(password, user.getPassword());
    }

    @Override
    public void init() {
        User user = userRepository.findByUsername("Admin");
        if(user == null) {
            userRepository.save(new User("Admin",CommanUtils.hashPassword("Admin")));
        }
    }
}
