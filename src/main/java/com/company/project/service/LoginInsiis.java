package com.company.project.service;

import com.alibaba.fastjson.JSONObject;
import com.company.project.core.ServiceException;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import java.io.*;
import java.util.Date;
import java.util.HashMap;

public class LoginInsiis {
    private static String filePath="d://springBoot_tmp/yzm.png";
    private static String cashcookie="";
   /**
            * 测试类
     */

    public static void main(String[] args) throws Exception {
        String cookis=getImage(filePath);
        String code = getImgContent(filePath);
        int bb= logonAction(code,cookis);
        System.out.println("验证码 = " + code+" "+bb);

    }
    /**
     * 获取登陆成功后的Cookie
     * @return
     * @throws IOException
     */
    public static int  retrunLoginCookies() throws IOException {

        String cookis=getImage(filePath);
        String code = getImgContent(filePath);

        System.out.println("验证码 = " + code);
        int bb= logonAction(code,cookis);
        if(bb==200){
            cashcookie=cookis;
            return bb;
        }else{
            return bb;
        }

    }

    /**
     * 获取验证码图片保存本地并返回Cookie
     * @param filePath
     * @return
     */
    protected static String getImage(String filePath){
        //初始化httpclient
        HttpClient httpClient = new HttpClient();
        //首先把进入淳安一体化的首页得到cookie（里面会包括token和sessionid等）；
        String url1 = "http://10.255.5.11:8080/insiis";
        GetMethod getMethod1 = new GetMethod(url1);
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        try {
            //执行访问页面
            int statusCode=httpClient.executeMethod(getMethod1);
            System.out.println("首页 = " + statusCode);
        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 获得登陆后的 Cookie
        Cookie[] cookies = httpClient.getState().getCookies();
        StringBuffer tmpcookies = new StringBuffer();
        for (Cookie c : cookies) {
            tmpcookies.append(c.toString() + ";");
        }

        //给路径加后缀，避免相同路径被缓存不再请求
        String url2 = "http://10.255.5.11:8080/insiis/verifyCode?t="+new Date().getTime();
        GetMethod getMethod2 = new GetMethod(url2);
        getMethod2.setRequestHeader("Cookie",tmpcookies.toString());
        try {
            int statusCode2=httpClient.executeMethod(getMethod2);
        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //获取请求到的数据
        byte[] responseBody = null;
        try {
            responseBody = getMethod2.getResponseBody();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //将请求的验证码图片用输出流方式输出
        try {
            OutputStream out = new FileOutputStream(new File(filePath));
            out.write(responseBody);
            out.flush();
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return tmpcookies.toString();

    }

    /**
     * 验证码图片识别
     * @param imgUrl
     * @return
     */
    protected static String getImgContent(String imgUrl) {
        String content = "";
        File imageFile = new File(imgUrl);
//读取图片数字
        ITesseract instance = new Tesseract();


        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        instance.setLanguage("eng");//英文库识别数字比较准确
        instance.setDatapath(tessDataFolder.getAbsolutePath());


        try {
            content = instance.doOCR(imageFile).replace("\n", "");
            System.out.println(content);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        content=content.replace(":","3").replace("S","5")
                .replace("E","8").replace("s","6")
                .replace("o","0").replace("]","1");
        return content;
    }

    protected static int logonAction(String verifyCode,String cookies) throws IOException {
        HttpClient httpClient = new HttpClient();
        String posturl = "http://10.255.5.11:8080/insiis/logonAction.do";
        PostMethod postMethod = new PostMethod(posturl);
        //获取提取验证码时得到的cookie；
        postMethod.setRequestHeader("Cookie", cookies.replace(";",""));
        // referer指当前页面从哪里来的，页面为了限制机器操作的方法一般为cookie,referer和验证码；
        //设置一些header
        postMethod.setRequestHeader("Accept", "image/gif, image/jpeg, image/pjpeg, application/x-ms-application, application/xaml+xml, application/x-ms-xbap, */*");
        postMethod.setRequestHeader("Accept-Encoding", "gzip, deflate");
        postMethod.setRequestHeader("Accept-Language", "zh-Hans-CN, zh-Hans; q=0.8, zh-Hant-HK; q=0.5, zh-Hant; q=0.3");
        postMethod.setRequestHeader("Cache-Control", "no-cache");
        postMethod.setRequestHeader("Connection", "keep-alive");
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        postMethod.setRequestHeader("Host", "10.255.5.11:8080");
        postMethod.setRequestHeader("Referer", "http://10.255.5.11:8080/insiis/");
        postMethod.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)");
        //把官网需要提交的参数添加
        postMethod.addParameter("passwd", "e1dedb80775489a50158c94b36af96bb");
        postMethod.addParameter("submitbtn.x", "0");
        postMethod.addParameter("submitbtn.y", "0");
        postMethod.addParameter("username", "xxk");
        postMethod.addParameter("verifycode", verifyCode);

        int bb=httpClient.executeMethod(postMethod);

        //开始得到网站返回值
//            byte[] responseBody = null;
//            try {
//                responseBody = postMethod.getResponseBody();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            //转成字符串并以json格式返回页面
//            String result=new String(responseBody, "UTF-8");
//            System.out.println(result);
        return bb;
    }
    public static JSONObject InsiisPost(String url,HashMap<String,Object> params) throws IOException, ServiceException {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        //获取提取验证码时得到的cookie；
        postMethod.setRequestHeader("Cookie", cashcookie);
        // referer指当前页面从哪里来的，页面为了限制机器操作的方法一般为cookie,referer和验证码；
        //设置一些header
        postMethod.setRequestHeader("Accept", "*/*");
        postMethod.setRequestHeader("Accept-Encoding", "gzip, deflate");
        postMethod.setRequestHeader("Accept-Language", "zh-Hans-CN, zh-Hans; q=0.8, zh-Hant-HK; q=0.5, zh-Hant; q=0.3");
        postMethod.setRequestHeader("Cache-Control", "no-cache");
        postMethod.setRequestHeader("Connection", "keep-alive");
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        postMethod.setRequestHeader("Host", "10.255.5.11:8080");
        postMethod.setRequestHeader("Referer", "http://10.255.5.11:8080/insiis/");
        postMethod.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)");
        //把官网需要提交的参数添加

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                postMethod.addParameter(key, params.get(key).toString());
            }
        }

        int bb=httpClient.executeMethod(postMethod);

        //开始得到网站返回值
            byte[] responseBody = null;
            try {
                responseBody = postMethod.getResponseBody();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //转成字符串并以json格式返回页面
            String result=new String(responseBody, "GBK");
            System.out.println(bb+" "+result);
        JSONObject json=JSONObject.parseObject(result);
        if(json.get("mainMessage").toString().contains("登陆超时")){
            int cout=0;
           while(cout<10) {
              if(retrunLoginCookies()==200) {
                  break;
              }
               cout++;
              if(cout>5){
                  throw new ServiceException("登陆社保中心系统出错！");
              }
           }
            json= InsiisPost(url,params);//重新发起请求
        }
        return json;
    }

}
