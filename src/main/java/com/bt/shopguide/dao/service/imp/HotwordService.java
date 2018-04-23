package com.bt.shopguide.dao.service.imp;

import com.bt.shopguide.dao.entity.Hotword;
import com.bt.shopguide.dao.mapper.HotwordMapper;
import com.bt.shopguide.dao.service.IHotwordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by caiting on 2017/10/10.
 */
@Service
public class HotwordService implements IHotwordService {
    @Autowired
    HotwordMapper hotwordMapper;

    @Override
    public List<Hotword> getTop5() {
        return hotwordMapper.getTopN(5);
    }

    @Override
    public List<Hotword> gettTopN(Integer n) {
        return hotwordMapper.getTopN(n);
    }
}
