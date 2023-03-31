package com.chaudhary.chaudharycattle.service.impl;

import com.chaudhary.chaudharycattle.entities.UserInfo;
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
        UserInfo userInfo = userRepository.findByUsername(userName);
        return userInfo != null && CommanUtils.checkPassword(password, userInfo.getPassword());
    }

    @Override
    public void init() {
        UserInfo userInfo = userRepository.findByUsername("Admin");
        if(userInfo == null) {
            userRepository.save(new UserInfo("Admin",CommanUtils.hashPassword("Admin")));
        }
    }
}
