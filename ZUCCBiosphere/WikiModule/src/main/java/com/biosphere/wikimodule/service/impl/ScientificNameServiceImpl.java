package com.biosphere.wikimodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biosphere.wikimodule.mapper.ScientificNameMapper;
import com.biosphere.library.pojo.ScientificName;
import com.biosphere.wikimodule.service.IScientificNameService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyh
 * @since 2022-09-02
 */
@Service
public class ScientificNameServiceImpl extends ServiceImpl<ScientificNameMapper, ScientificName> implements IScientificNameService {

}
