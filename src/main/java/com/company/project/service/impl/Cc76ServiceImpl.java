package com.company.project.service.impl;

import com.company.project.dao.Cc76Mapper;
import com.company.project.model.Cc76;
import com.company.project.service.Cc76Service;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/09/19.
 */
@Service
@Transactional
public class Cc76ServiceImpl extends AbstractService<Cc76> implements Cc76Service {
    @Resource
    private Cc76Mapper cc76Mapper;

}
