package com.biosphere.wikimodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biosphere.library.pojo.CuringGuide;
import com.biosphere.library.vo.CuringGuideWithKeyContent;
import com.biosphere.wikimodule.mapper.CuringGuideMapper;
import com.biosphere.wikimodule.service.ICuringGuideService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyh
 * @since 2022-09-02
 */
@Service
@Slf4j
public class CuringGuideServiceImpl extends ServiceImpl<CuringGuideMapper, CuringGuide> implements ICuringGuideService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<CuringGuideWithKeyContent> getCuringGuideData(String familyID) {
        try {
            List<CuringGuide> curingGuideList = (List<CuringGuide>) redisTemplate.opsForValue().get("curingGuideList");
            List<CuringGuide> queryRes = new ArrayList<>();
            //获取本科分类下的生物养护指南
            for (CuringGuide curingGuide : curingGuideList) {
                if (curingGuide.getBelongToWhichType().equals(familyID)) {
                    queryRes.add(curingGuide);
                }
            }
            List<CuringGuideWithKeyContent> curingGuideWithKeyContents = new ArrayList<>();
            //进行关键词的处理
            List<String> keyFinal = new ArrayList<>();
            for (CuringGuide queryRe : queryRes) {
                int[] keyIndex = new int[queryRe.getContent().length()];
                Arrays.fill(keyIndex,-1);
                CuringGuideWithKeyContent temp = new CuringGuideWithKeyContent(queryRe);
                keyFinal = Arrays.asList(temp.getKeyContent().split("&&"));
                String str = temp.getContent();
                for (int i = 0; i < keyFinal.size(); i ++){
                    String s = keyFinal.get(i);
                    int left = str.indexOf(s);
                    if (left == -1) continue;
                    int right = left + s.length() - 1;
                    for (int j = left; j <= right; j ++){
                        keyIndex[j] = 1;
                    }
                }
                temp.setKeyIndex(keyIndex);
                temp.setContentFinal(str.toCharArray());
                curingGuideWithKeyContents.add(temp);
            }

            return curingGuideWithKeyContents;
        }catch (Exception e){
            log.error("加载养护指南页面信息失败：{}",e);
            return null;

        }

    }
}
