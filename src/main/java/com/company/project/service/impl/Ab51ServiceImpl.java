package com.company.project.service.impl;

import com.company.project.dao.Ab51Mapper;
import com.company.project.model.Ab51;
import com.company.project.service.Ab51Service;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/09/17.
 */
@Service
@Transactional
public class Ab51ServiceImpl extends AbstractService<Ab51> implements Ab51Service {
    @Resource
    private Ab51Mapper ab51Mapper;

}
