package com.company.project.configurer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义启动类
 */
@Component
@Order(1)
public class InsiisActionConfig implements ApplicationRunner {
  public static   Map configMap = new HashMap<String, Object>();
    @Override
    public void run(ApplicationArguments args) throws Exception {
        configInfo();
    }
    private void configInfo(){

        configMap.put("6031","/com/insigma/siis/local/module/insuredmgmt/archivereg/SaveAction.do?method=save");
        configMap.put("functionid6031","49FCCF5C7444559DCA2A976A242D4FD2");
        configMap.put("6032","/com/insigma/siis/local/module/common/search/GetPersonByIdAction.do?method=getPersonById");
        configMap.put("functionid6032","NULL");
        configMap.put("6006","/com/insigma/siis/local/module/landlessmgmt/rtheginseng/SaveAction.do?method=save");//方法路径
        configMap.put("functionid6006","7D4F7BCECCA972BA4DD3E14FCAD24CD1");//模块编码
        configMap.put("6006S","/com/insigma/siis/local/module/landlessmgmt/guaranteedpayment/SaveAction.do?method=save");//方法路径
        configMap.put("functionid6006S","5a3cb6d007b5920e26c4f439fa14037f");//模块编码
        configMap.put("6008","/com/insigma/siis/local/module/landlessmgmt/rtheginseng/SaveAction.do?method=save");//方法路径
        configMap.put("functionid6008","7D4F7BCECCA972BA4DD3E14FCAD24CD1");//模块编码
        configMap.put("6008S","/com/insigma/siis/local/module/landlessmgmt/guaranteedpayment/SaveAction.do?method=save");//方法路径
        configMap.put("functionid6008S","5a3cb6d007b5920e26c4f439fa14037f");//模块编码
        configMap.put("6010","/com/insigma/siis/local/module/insuredmgmt/personinsurereg/SaveAction.do?method=save");//方法路径
        configMap.put("functionid6010","6985a6b56291127c47ff4e3e65e58eb8");//模块编码

    }
}
