package com.bt.shopguide.dao.service.imp;

import com.bt.shopguide.dao.entity.Coupon;
import com.bt.shopguide.dao.mapper.CouponMapper;
import com.bt.shopguide.dao.service.ICouponService;
import com.bt.shopguide.dao.vo.PageDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caiting on 2017/11/23.
 */
@Service
public class CouponService implements ICouponService {
    @Autowired
    private CouponMapper couponMapper;

    @Override
    public int save(Coupon c) {
        return couponMapper.insert(c);
    }

    @Override
    public void selectCouponPage(PageDataVo<Coupon> vo) {
        int totalCount = couponMapper.getTotalCount(vo.getConditionMap());
        if(totalCount > 0){
            Map<String,Object> params = new HashMap<String,Object>();
            for(Map.Entry<String, Object> entry:vo.getConditionMap().entrySet()){
                params.put(entry.getKey(), entry.getValue());
            }
            params.put("startIndex", (vo.getPageIndex()-1)*vo.getPageSize());
            params.put("pageSize", vo.getPageSize());
            vo.setData(couponMapper.selectPage(params));
            vo.setTotalCount(totalCount);
        }
    }

    @Override
    public List<Coupon> selectTodayCoupon() {
        return couponMapper.selectTodayCoupon();
    }
}
