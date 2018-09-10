package com.company.project.web;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Datashaingtranslog;
import com.company.project.service.DatashaingtranslogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/09/10.
*/
@RestController
@RequestMapping("/datashaingtranslog")
public class DatashaingtranslogController {
    @Resource
    private DatashaingtranslogService datashaingtranslogService;

    @PostMapping("/add")
    public Result add(Datashaingtranslog datashaingtranslog) {
        datashaingtranslogService.save(datashaingtranslog);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        datashaingtranslogService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Datashaingtranslog datashaingtranslog) {
        datashaingtranslogService.update(datashaingtranslog);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Datashaingtranslog datashaingtranslog = datashaingtranslogService.findById(id);
        return ResultGenerator.genSuccessResult(datashaingtranslog);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Datashaingtranslog> list = datashaingtranslogService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
