package com.bt.shopguide.dao.service;

import com.bt.shopguide.dao.entity.Hotword;

import java.util.List;

/**
 * Created by caiting on 2017/10/10.
 */
public interface IHotwordService {
    public List<Hotword> getTop5();
    public List<Hotword> gettTopN(Integer n);
}
