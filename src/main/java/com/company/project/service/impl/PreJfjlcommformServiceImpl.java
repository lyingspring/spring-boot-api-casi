package com.company.project.service.impl;

import com.company.project.dao.PreJfjlcommformMapper;
import com.company.project.model.PreJfjlcommform;
import com.company.project.service.PreJfjlcommformService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/09/12.
 */
@Service
@Transactional
public class PreJfjlcommformServiceImpl extends AbstractService<PreJfjlcommform> implements PreJfjlcommformService {
    @Resource
    private PreJfjlcommformMapper preJfjlcommformMapper;

}
