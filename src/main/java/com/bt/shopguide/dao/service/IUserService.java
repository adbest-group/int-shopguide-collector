package com.bt.shopguide.dao.service;

import com.bt.shopguide.dao.entity.User;

/**
 * Created by caiting on 2017/10/17.
 */
public interface IUserService {
    public int save(User user);
    public User selectByCookie(String cookie);
}
