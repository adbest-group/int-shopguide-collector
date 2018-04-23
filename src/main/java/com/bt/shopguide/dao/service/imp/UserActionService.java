package com.bt.shopguide.dao.service.imp;

import com.bt.shopguide.dao.entity.UserAction;
import com.bt.shopguide.dao.mapper.UserActionMapper;
import com.bt.shopguide.dao.service.IUserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by caiting on 2017/10/17.
 */
@Service
public class UserActionService implements IUserActionService {
    @Autowired
    UserActionMapper userActionMapper;

    @Override
    public int save(UserAction userAction) {
        return userActionMapper.insert(userAction);
    }
}
