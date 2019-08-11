package com.bingsenh.seckill.service;

import com.bingsenh.seckill.dao.UserDao;
import com.bingsenh.seckill.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bingsenh on 2019/8/10.
 */
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getById(int id) {
        return userDao.getById(id);
    }

}
