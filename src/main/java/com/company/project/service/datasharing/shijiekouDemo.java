package com.company.project.service.datasharing;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * 接口访问
 */
public class shijiekouDemo {
    private static String privateKey="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKLQgp07slkmHn/NrfvsaGrzI/TsiJ2KhXtQ9eNofWmX/NyJFROVCORfILRCL5e1qS+aFcuYg/jekQxBwOpDMxEStIy+RKySPms6VOS/abEZ1UKsxK8deLrNqoiwITL9uiKsOmTp2ULZ76ntwrKVxxmPQr6wlu3F7L8DyyE1SnoHAgMBAAECgYEAm2JH5WtdsLsyjGJtU2qx1LArdspvL3tOHPyNTvUgC7CkAI1Lch7gF6O6AI7SAQW8a9OwTVhHSzKOV5ZBWNG9X12kzWinHQbxiItFdWTvlCBziS/fGbzIyO+O6HmtTCFdIBz0PBy30q9UDaHdwB3kCd+VDLabWKsaM8A0KTUxmFECQQDXvj08NLG0x/kpGnH9GzhDu7H5UH5Ich2McO4h+EGq/s+dBX1aWfkLlRZgWV1ltOpe0Zz47ZLTKu8APygqQnNFAkEAwTHw690v8VX/m5I/kgvVdeQ3/fQIYKTI6tjk/Ozf8Q9KRojD9snCMUc+z760UYbHNE9jTgu8vokBr0fcF4lG2wJAVEN0bVhzdBWK5pfyn5YLEsFzkNn0iN0xV1IgYFozY9MkScMEI87ya6iuVbFxvjC8PY6HTd6Usy+Yq7L/QAo2NQJABk10yJ0MpVji39ZjkIYmTpRFZ1mAtHZrv42X2tB3dcvD5o0rp29pkGX8nJZiF47IDOLSIIetfqHFlkxH19S4pQJAINvZg5QNNSnjWYUr/S50CJXaEoG/zVfObpcP71R9qdAGvZYkuqFedzeG6kWHgUVyHgw46fhMNRITKMr4MBifkw==";

    //POST方式
    public static String post(String url,Part[] parts){
        org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
        PostMethod post = new PostMethod(url);
        post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        post.setRequestEntity(new MultipartRequestEntity(parts, post.getParams()));
        String response="";
        try {
            client.executeMethod(post);
            response = IOUtils.toString(post.getResponseBodyAsStream(),"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            post.releaseConnection();
        }
        return response;
    }
    public static void main(String[] args) throws Exception{

        String url="http://10.54.19.90:8085/ESBWeb/servlets/33.1111.zjhz.smzx.wjwhszcxx.SynReq@1.0";
       // String url="http://59.202.58.195:8000/ESBWeb/servlets/33.1111.zjhz.smzx.ycsl.transact.SynReq@1.0";
        String id_nbr = "330127197408153814";
        String name = "唐渊东";
        JSONObject json = new JSONObject();
        json.put("id_nbr",id_nbr);
        json.put("name",name);

        String request=RSAUtils.encryptByPrivateKey(json.toJSONString(), privateKey);
        String sign=RSAUtils.sign(request,privateKey);
        System.out.println("sign:"+sign);
        System.out.println("request:"+request);
        Part[] parts= new Part[2];
        parts[0] = new StringPart("request", request,"UTF-8");
        parts[1] = new StringPart("sign", sign,"UTF-8");
        //System.out.println(DcryptUtils.verify(request,sign));//验证
        System.out.println(DcryptUtils.decryptByPublicKey(request));
        System.out.println(post(url,parts));
        System.out.println(DcryptUtils.decryptByPublicKey(request));
    }
}




