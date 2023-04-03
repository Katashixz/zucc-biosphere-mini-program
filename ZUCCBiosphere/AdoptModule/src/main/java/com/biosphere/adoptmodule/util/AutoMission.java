package com.biosphere.adoptmodule.util;

import com.biosphere.adoptmodule.service.IAnimalsWikiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author hyh
 * @description: TODO
 * @date 2023/3/29 13:49
 */
@Slf4j
@Component
public class AutoMission {

    @Autowired
    private IAnimalsWikiService animalsWikiService;


    /**
     * 功能描述: 每五分钟加载学名缓存到Redis
     * @param:
     * @return: void
     * @author hyh
     * @date: 2023/3/29 13:51
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void loadScientificName(){
        animalsWikiService.saveAdoptCache();
    }

}
