package com.biosphere.adoptmodule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biosphere.adoptmodule.mapper.ShopItemMapper;
import com.biosphere.adoptmodule.service.IShopItemService;
import com.biosphere.library.pojo.ShopItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyh
 * @since 2023-04-01
 */
@Service
public class ShopItemServiceImpl extends ServiceImpl<ShopItemMapper, ShopItem> implements IShopItemService {

    @Autowired
    private ShopItemMapper shopItemMapper;

    @Override
    public List<ShopItem> getShopList(Integer type) {
        QueryWrapper<ShopItem> shopItemQueryWrapper = new QueryWrapper<>();
        shopItemQueryWrapper.eq("`usage`", type);
        return shopItemMapper.selectList(shopItemQueryWrapper);
    }
}
