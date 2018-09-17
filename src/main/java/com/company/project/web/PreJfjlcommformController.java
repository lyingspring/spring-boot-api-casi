package com.company.project.web;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.PreJfjlcommform;
import com.company.project.service.PreJfjlcommformService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/09/12.
*/
@RestController
@RequestMapping("/pre/jfjlcommform")
public class PreJfjlcommformController {
    @Resource
    private PreJfjlcommformService preJfjlcommformService;

    @PostMapping("/add")
    public Result add(PreJfjlcommform preJfjlcommform) {
        preJfjlcommformService.save(preJfjlcommform);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        preJfjlcommformService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(PreJfjlcommform preJfjlcommform) {
        preJfjlcommformService.update(preJfjlcommform);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        PreJfjlcommform preJfjlcommform = preJfjlcommformService.findById(id);
        return ResultGenerator.genSuccessResult(preJfjlcommform);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<PreJfjlcommform> list = preJfjlcommformService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
