package com.bt.shopguide.dao.service.imp;

import com.bt.shopguide.dao.entity.ContactInfo;
import com.bt.shopguide.dao.mapper.ContactInfoMapper;
import com.bt.shopguide.dao.service.IContactInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by caiting on 2017/10/26.
 */
@Service
public class ContactInfoService implements IContactInfoService {
    @Autowired
    private ContactInfoMapper contactInfoMapper;

    @Override
    public int save(ContactInfo contactInfo) {
        return contactInfoMapper.insert(contactInfo);
    }
}
