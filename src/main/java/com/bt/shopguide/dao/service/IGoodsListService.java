package com.bt.shopguide.dao.service;

import com.bt.shopguide.dao.entity.GoodsList;
import com.bt.shopguide.dao.entity.GoodsListWithHtml;
import com.bt.shopguide.dao.vo.PageDataVo;

import java.util.List;

/**
 * Created by caiting on 2017/9/26.
 */

public interface IGoodsListService {
    public int save(GoodsList glist);
    public void selectGoodsListPage(PageDataVo<GoodsList> vo);
    public GoodsListWithHtml selectGoodDetailByGoodsId(Long id);
    public void selectGoodsListPageWithHtml(PageDataVo<GoodsListWithHtml> vo);
    public int increaseThumbs(Long id, Integer count);
    public GoodsList getGoodsListById(Long id);
    public List<GoodsList> getRandGoodsByMall(String mallName, Integer count);
    public List<GoodsList> getRandGoodsByCategory(String category, Integer count);
    public List<GoodsList> getRandGoodsByMall(String mallName, Long id, Integer count);
    public List<GoodsList> getRandGoodsByCategory(String category, Long id, Integer count);
    public List<GoodsList> getRandGoods(Integer count, Integer topCount);
}
