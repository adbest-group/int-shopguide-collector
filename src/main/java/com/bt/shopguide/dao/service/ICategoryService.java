package com.bt.shopguide.dao.service;

import com.bt.shopguide.dao.entity.Category;

import java.util.List;

/**
 * Created by caiting on 2017/10/10.
 */
public interface ICategoryService {
    public List<Category> getTop5();
    public List<Category> getTopN(Integer n);
}
