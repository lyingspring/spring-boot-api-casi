package com.company.project.service.impl;

import com.company.project.dao.Ka27Mapper;
import com.company.project.model.Ka27;
import com.company.project.service.Ka27Service;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/09/18.
 */
@Service
@Transactional
public class Ka27ServiceImpl extends AbstractService<Ka27> implements Ka27Service {
    @Resource
    private Ka27Mapper ka27Mapper;

}
