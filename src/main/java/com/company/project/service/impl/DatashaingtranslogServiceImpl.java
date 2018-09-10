package com.company.project.service.impl;

import com.company.project.dao.DatashaingtranslogMapper;
import com.company.project.model.Datashaingtranslog;
import com.company.project.service.DatashaingtranslogService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/09/10.
 */
@Service
@Transactional
public class DatashaingtranslogServiceImpl extends AbstractService<Datashaingtranslog> implements DatashaingtranslogService {
    @Resource
    private DatashaingtranslogMapper datashaingtranslogMapper;

}
