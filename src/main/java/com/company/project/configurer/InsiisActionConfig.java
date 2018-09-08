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

    }
}
