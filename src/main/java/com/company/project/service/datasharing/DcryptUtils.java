package com.company.project.service.datasharing;


import com.alibaba.fastjson.JSONObject;
import com.company.project.core.ServiceException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class DcryptUtils {

    private final static String publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCi0IKdO7JZJh5/za377Ghq8yP07IidioV7UPXjaH1pl/zciRUTlQjkXyC0Qi+XtakvmhXLmIP43pEMQcDqQzMRErSMvkSskj5rOlTkv2mxGdVCrMSvHXi6zaqIsCEy/boirDpk6dlC2e+p7cKylccZj0K+sJbtxey/A8shNUp6BwIDAQAB";

    /**
     * 验证签名
     * @return
     */
    public static boolean verify(String encodeData,String sign){
        try {
            if(RSAUtils.verify(encodeData,publicKey,sign)){
                return true;
            }
        }catch (Exception e){

        }
        return false;
    }

    /**
     * 使用公钥解密
     * @return
     */
    public static Map<String, Object> decryptByPublicKey(String encodeData){
        Map<String, Object> map=null;
        try {
            String decryptedData=RSAUtils.decryptByPublicKey(encodeData,publicKey);
            JSONObject  json=JSONObject.parseObject(decryptedData);
            map=json;
        }catch (Exception e){
            throw new ServiceException("参数解密失败！"+encodeData);
        }
        return map;
    }

}



