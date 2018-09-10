package com.company.project.service.impl;

import com.company.project.dao.Jc20Mapper;
import com.company.project.model.Jc20;
import com.company.project.service.Jc20Service;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/09/10.
 */
@Service
@Transactional
public class Jc20ServiceImpl extends AbstractService<Jc20> implements Jc20Service {
    @Resource
    private Jc20Mapper jc20Mapper;

}
