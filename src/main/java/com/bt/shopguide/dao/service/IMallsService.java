package com.bt.shopguide.dao.service;

import com.bt.shopguide.dao.entity.Malls;

import java.util.List;

/**
 * Created by caiting on 2017/9/27.
 */
public interface IMallsService {
    public List<Malls> getAllMalls();
    public List<Malls> getTopN(Integer n);
}
