package com.bt.shopguide.dao.service.imp;

import com.bt.shopguide.dao.entity.Category;
import com.bt.shopguide.dao.mapper.CategoryMapper;
import com.bt.shopguide.dao.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by caiting on 2017/10/10.
 */
@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getTop5() {
        return categoryMapper.getTopN(5);
    }

    @Override
    public List<Category> getTopN(Integer n) {
        return categoryMapper.getTopN(n);
    }
}
