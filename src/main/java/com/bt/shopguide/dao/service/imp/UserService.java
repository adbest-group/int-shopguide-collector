package com.bt.shopguide.dao.service.imp;

import com.bt.shopguide.dao.entity.User;
import com.bt.shopguide.dao.mapper.UserMapper;
import com.bt.shopguide.dao.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by caiting on 2017/10/17.
 */
@Service
public class UserService implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public int save(User user) {
        return userMapper.insert(user);
    }

    @Override
    public User selectByCookie(String cookie) {
        return userMapper.selectByCookie(cookie);
    }
}
