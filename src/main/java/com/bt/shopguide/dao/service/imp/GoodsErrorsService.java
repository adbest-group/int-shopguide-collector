package com.bt.shopguide.dao.service.imp;

import com.bt.shopguide.dao.entity.GoodsErrors;
import com.bt.shopguide.dao.mapper.GoodsErrorsMapper;
import com.bt.shopguide.dao.service.IGoodsErrorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by caiting on 2017/9/26.
 */
@Service
public class GoodsErrorsService implements IGoodsErrorsService {
    @Autowired
    private GoodsErrorsMapper goodsErrorsMapper;
    @Override
    public int save(GoodsErrors gerror) {
        return goodsErrorsMapper.insert(gerror);
    }
}
