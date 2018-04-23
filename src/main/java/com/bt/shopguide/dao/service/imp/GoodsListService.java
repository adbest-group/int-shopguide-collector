package com.bt.shopguide.dao.service.imp;

import com.bt.shopguide.dao.entity.GoodsList;
import com.bt.shopguide.dao.entity.GoodsListWithHtml;
import com.bt.shopguide.dao.mapper.GoodsListMapper;
import com.bt.shopguide.dao.service.IGoodsListService;
import com.bt.shopguide.dao.vo.PageDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caiting on 2017/9/26.
 */
@Service
public class GoodsListService implements IGoodsListService {
    @Autowired
    private GoodsListMapper glMapper;

    @Override
    public int save(GoodsList glist) {
        return glMapper.insert(glist);
    }

    @Override
    public void selectGoodsListPage(PageDataVo<GoodsList> vo) {
        int totalCount = glMapper.getTotalCount(vo.getConditionMap());
        if(totalCount > 0){
            Map<String,Object> params = new HashMap<String,Object>();
            for(Map.Entry<String, Object> entry:vo.getConditionMap().entrySet()){
                params.put(entry.getKey(), entry.getValue());
            }
            params.put("startIndex", (vo.getPageIndex()-1)*vo.getPageSize());
            params.put("pageSize", vo.getPageSize());
            vo.setData(glMapper.selectPage(params));
            vo.setTotalCount(totalCount);
        }
    }

    @Override
    public GoodsListWithHtml selectGoodDetailByGoodsId(Long id) {
        return glMapper.selectGoodDetailByGoodsId(id);
    }

    @Override
    public void selectGoodsListPageWithHtml(PageDataVo<GoodsListWithHtml> vo) {
        int totalCount = glMapper.getTotalCountWithHtml(vo.getConditionMap());
        if(totalCount > 0){
            Map<String,Object> params = new HashMap<String,Object>();
            for(Map.Entry<String, Object> entry:vo.getConditionMap().entrySet()){
                params.put(entry.getKey(), entry.getValue());
            }
            params.put("startIndex", (vo.getPageIndex()-1)*vo.getPageSize());
            params.put("pageSize", vo.getPageSize());
            vo.setData(glMapper.selectPageWithHtml(params));
            vo.setTotalCount(totalCount);
        }
    }

    @Override
    public int increaseThumbs(Long id, Integer count) {
        return glMapper.increaseThumbs(id,count);
    }

    @Override
    public GoodsList getGoodsListById(Long id) {
        return glMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<GoodsList> getRandGoodsByMall(String mallName, Integer count) {
        Map paramMap = new HashMap();
        paramMap.put("mallName",mallName);
        paramMap.put("randCount",count);
        return glMapper.getRandGoodsList(paramMap);
    }

    @Override
    public List<GoodsList> getRandGoodsByCategory(String category, Integer count) {
        Map paramMap = new HashMap();
        paramMap.put("category",category);
        paramMap.put("randCount",count);
        return glMapper.getRandGoodsList(paramMap);
    }

    @Override
    public List<GoodsList> getRandGoodsByMall(String mallName, Long id, Integer count) {
        Map paramMap = new HashMap();
        paramMap.put("mallName",mallName);
        int totalCount = glMapper.getTotalCount(paramMap);
        if(totalCount > count){
            paramMap.put("randCount",count);
            paramMap.put("id",id);
        }else{
            paramMap.remove("mallName");
            paramMap.put("randCount",count);
            paramMap.put("id",id);
        }

        return glMapper.getRandGoodsList(paramMap);
    }

    @Override
    public List<GoodsList> getRandGoodsByCategory(String category, Long id, Integer count) {
        Map paramMap = new HashMap();
        paramMap.put("category",category);
        int totalCount = glMapper.getTotalCount(paramMap);
        if(totalCount > count){
            paramMap.put("randCount",count);
            paramMap.put("id",id);
        }else{
            paramMap.remove("category");
            paramMap.put("randCount",count);
            paramMap.put("id",id);
        }
        return glMapper.getRandGoodsList(paramMap);
    }

    @Override
    public List<GoodsList> getRandGoods(Integer count, Integer topCount) {
        Map paramMap = new HashMap();
        int totalCount = glMapper.getTotalCount(paramMap);
        paramMap.put("randCount",count);
        paramMap.put("randInTotalCount",topCount);
        return glMapper.getRandGoodsList(paramMap);
    }
}
