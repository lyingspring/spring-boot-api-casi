package com.company.project.service.impl;

import com.company.project.dao.PreApasinfoMapper;
import com.company.project.model.PreApasinfo;
import com.company.project.service.PreApasinfoService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/09/11.
 */
@Service
@Transactional
public class PreApasinfoServiceImpl extends AbstractService<PreApasinfo> implements PreApasinfoService {
    @Resource
    private PreApasinfoMapper preApasinfoMapper;

}
