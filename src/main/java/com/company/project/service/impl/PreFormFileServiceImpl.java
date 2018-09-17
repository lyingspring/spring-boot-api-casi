package com.company.project.service.impl;

import com.company.project.dao.PreFormFileMapper;
import com.company.project.model.PreFormFile;
import com.company.project.service.PreFormFileService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/09/11.
 */
@Service
@Transactional
public class PreFormFileServiceImpl extends AbstractService<PreFormFile> implements PreFormFileService {
    @Resource
    private PreFormFileMapper preFormFileMapper;

}
