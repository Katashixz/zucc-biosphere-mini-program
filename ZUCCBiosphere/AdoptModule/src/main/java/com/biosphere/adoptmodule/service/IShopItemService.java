package com.biosphere.adoptmodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biosphere.library.pojo.ShopItem;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hyh
 * @since 2023-04-01
 */
public interface IShopItemService extends IService<ShopItem> {

    /**
     * 功能描述: 获取商品列表
     * @param:
     * @return: java.util.List<com.biosphere.library.pojo.ShopItem>
     * @author hyh
     * @date: 2023/4/1 16:21
     */
    List<ShopItem> getShopList(Integer type);

}
