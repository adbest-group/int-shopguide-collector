package com.bt.shopguide.dao.service.imp;

import com.bt.shopguide.dao.entity.Malls;
import com.bt.shopguide.dao.mapper.MallsMapper;
import com.bt.shopguide.dao.service.IMallsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by caiting on 2017/9/27.
 */
@Service
public class MallService implements IMallsService {

    @Autowired
    private MallsMapper mallsMapper;

    @Override
    public List<Malls> getAllMalls() {
        return mallsMapper.selectAll();
    }

    @Override
    public List<Malls> getTopN(Integer n) {
        return mallsMapper.getTopN(n);
    }
}
