package com.bt.shopguide.dao.service.imp;

import com.bt.shopguide.dao.entity.GoodsDetail;
import com.bt.shopguide.dao.mapper.GoodsDetailMapper;
import com.bt.shopguide.dao.service.IGoodsDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by caiting on 2017/9/26.
 */
@Service
public class GoodsDetailService implements IGoodsDetailService {
    @Autowired
    private GoodsDetailMapper goodsDetailMapper;
    @Override
    public int save(GoodsDetail gdetail) {
        return goodsDetailMapper.insert(gdetail);
    }
}
