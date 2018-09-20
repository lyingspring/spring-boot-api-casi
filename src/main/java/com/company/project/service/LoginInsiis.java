package com.company.project.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.project.configurer.InsiisActionConfig;
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
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

/**
 * 模拟登陆社保系统并发起请求
 * author maoxj
 */
public class LoginInsiis {
    private static String filePath = "d://springBoot_tmp/yzm.png";
    private static String cashcookie = "";
    private static String insiisUrl = "http://10.255.5.100:8090/insiis";//系统登陆地址
    private static String host = "10.255.5.100:8090";
    //private static String loginName = "xxk";//登陆用户名
   // private static String passwordMD5 = "e1dedb80775489a50158c94b36af96bb";//MD5加密后的密码
    private static String loginName = "admin";//登陆用户名
    private static String passwordMD5 = "21232f297a57a5a743894a0e4a801fc3";//MD5加密后的密码
    /**
     * @param args
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {
//        String cookis = getImage(filePath);
//        String code = getImgContent(filePath);
//        int bb = logonAction(code, cookis);
//        System.out.println("验证码 = " + code + " " + bb);
        System.out.println( doGet("http://10.255.5.11:8080/insiis/com/insigma/siis/local/module/medicalmgmt/patientrelocatedreg/GetHisPatientInfoAction.do?method=getHisPatientInfo&" +
                "_ODA_TRANSMIT_OBJECT=%7B%22chrDTO%22%3A%7B%22aac001%22%3A108046%2C%22aka083%22%3A%2231%22%2C%22flaglbf%22%3A%22%22%7D%7D","GBK"));
    }

    /**
     httpClient的get请求方式2
     * @return
     * @throws Exception
     */
    public static String doGet(String url, String charset)
            throws Exception {
        /*
         * 使用 GetMethod 来访问一个 URL 对应的网页,实现步骤: 1:生成一个 HttpClinet 对象并设置相应的参数。
         * 2:生成一个 GetMethod 对象并设置响应的参数。 3:用 HttpClinet 生成的对象来执行 GetMethod 生成的Get
         * 方法。 4:处理响应状态码。 5:若响应正常，处理 HTTP 响应内容。 6:释放连接。
         */
        /* 1 生成 HttpClinet 对象并设置参数 */
        HttpClient httpClient = new HttpClient();
        // 设置 Http 连接超时为5秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        /* 2 生成 GetMethod 对象并设置参数 */
        GetMethod getMethod = new GetMethod(url);
        // 设置 get 请求超时为 5 秒
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        // 设置请求重试处理，用的是默认的重试处理：请求三次
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,	new DefaultHttpMethodRetryHandler());
        getMethod.setRequestHeader("Cookie", cashcookie);

        String response = "";
        /* 3 执行 HTTP GET 请求 */
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            /* 4 判断访问的状态码 */
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("请求出错: "+ getMethod.getStatusLine());
            }
            /* 5 处理 HTTP 响应内容 */
            // HTTP响应头部信息，这里简单打印
            Header[] headers = getMethod.getResponseHeaders();
            for (Header h : headers)
                System.out.println(h.getName() + "------------ " + h.getValue());
            // 读取 HTTP 响应内容，这里简单打印网页内容
            byte[] responseBody = getMethod.getResponseBody();// 读取为字节数组
            response = new String(responseBody, charset);
            System.out.println("----------response:" + response);
            // 读取为 InputStream，在网页内容数据量大时候推荐使用
            // InputStream response = getMethod.getResponseBodyAsStream();
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("请检查输入的URL!");
            e.printStackTrace();
        } catch (IOException e) {
            // 发生网络异常
            System.out.println("发生网络异常!");
            e.printStackTrace();
        } finally {
            /* 6 .释放连接 */
            getMethod.releaseConnection();
        }
        return response;
    }

    /**
     * get调用统一入口
     *
     * @param TradeCode    接口代码
     * @param TradeCodeMap 交易参数
     * @return
     */
    public static JSONObject returnInsiisGet(String TradeCode,
                                              HashMap<String, Object> TradeCodeMap) throws Exception {
        String url = "";
        try {
            url = insiisUrl + InsiisActionConfig.configMap.get(TradeCode).toString();//方法URL
            if (url.equals(insiisUrl)||url.isEmpty()) {
                throw new ServiceException("读取InsiisActionConfig出错,url为空:" + TradeCode);
            }
        } catch (Exception e) {
            throw new ServiceException("读取InsiisActionConfig出错:" + TradeCode);
        }
        String input= JSON.toJSONString(TradeCodeMap) ;
        JSONObject json = new JSONObject();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map = TradeCodeMap;
        if(url.contains("?")){
            url=url+"&"+getUrlParamsByMap(map);
        }else{
            url=url+"?"+getUrlParamsByMap(map);
        }
        String resp= doGet(url,"GBK");
        json=JSONObject.parseObject(resp);
        return json;
    }

    /**
     * 将map转换成url
     *
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(HashMap<String, Object> map) throws UnsupportedEncodingException {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (String key : map.keySet()) {
            sb.append(key + "=" +  URLEncoder.encode(map.get(key).toString(),"GBK"));
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }

    /**
     * post调用统一入口
     *
     * @param TradeCode    接口代码
     * @param aac001       人员ID
     * @param aab001       单位编码 可空 记入操作日志用
     * @param digest       操作日志摘要
     * @param TradeCodeMap 交易参数
     * @return
     */
    public static JSONObject returnInsiisPost(String TradeCode, Long aac001, String aab001, String digest,
                                              HashMap<String, Object> TradeCodeMap) throws IOException {
        String url = "";
        String functionid = "";
        try {
            url = insiisUrl + InsiisActionConfig.configMap.get(TradeCode).toString();//方法URL
            functionid = InsiisActionConfig.configMap.get("functionid" + TradeCode).toString();//模块的functionid
            if (url.equals(insiisUrl)||url.isEmpty()) {
                throw new ServiceException("读取InsiisActionConfig出错,url为空:" + TradeCode);
            }
        } catch (Exception e) {
            throw new ServiceException("读取InsiisActionConfig出错:" + TradeCode);
        }
        String input= JSON.toJSONString(TradeCodeMap) ;
        JSONObject json = new JSONObject();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map = TradeCodeMap;
        map.put("userlog", "{\"functionid\":\"" + functionid + "\",\"aac001\":" + aac001 + ",\"aab001\":" + aab001 + ",\"digest\":" +
                "\"" + digest + "\",\"prcol1\":\"\",\"prcol2\":\"\",\"prcol3\":\"\"," +
                "\"prcol4\":\"\",\"prcol5\":\"\",\"prcol6\":\"\",\"prcol7\":\"\",\"prcol8\":\"\",\"orisource\":\"此条记录由外网系统调用接口操作\"}");

        json = InsiisPost(url, map);

        return json;
    }

    /**
     * 获取登陆成功后的Cookie
     *
     * @return
     * @throws IOException
     */
    public static int retrunLoginCookies() throws IOException {

        String cookis = getImage(filePath);
        String code = getImgContent(filePath);

        System.out.println("验证码 = " + code);
        int bb = logonAction(code, cookis);
        if (bb == 200) {
            cashcookie = cookis;
            return bb;
        } else {
            return bb;
        }

    }

    /**
     * 获取验证码图片保存本地并返回Cookie
     *
     * @param filePath
     * @return
     */
    protected static String getImage(String filePath) {
        //初始化httpclient
        HttpClient httpClient = new HttpClient();
        //首先把进入淳安一体化的首页得到cookie（里面会包括token和sessionid等）；
        String url1 = insiisUrl;
        GetMethod getMethod1 = new GetMethod(url1);
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        try {
            //执行访问页面
            int statusCode = httpClient.executeMethod(getMethod1);
            System.out.println("首页 = " + statusCode);
        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException("访问社保首页出错1！");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException("访问社保首页出错2！");
        }
        // 获得社保系统的 Cookie
        Cookie[] cookies = httpClient.getState().getCookies();
        StringBuffer tmpcookies = new StringBuffer();
        for (Cookie c : cookies) {
            tmpcookies.append(c.toString() + ";");
        }

        //给路径加后缀，避免相同路径被缓存不再请求
        String url2 = insiisUrl + "/verifyCode?t=" + new Date().getTime();
        GetMethod getMethod2 = new GetMethod(url2);
        getMethod2.setRequestHeader("Cookie", tmpcookies.toString());
        try {
            int statusCode2 = httpClient.executeMethod(getMethod2);
        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException("获取验证码图片出错1！");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException("获取验证码图片出错2！");
        }
        //获取请求到的数据
        byte[] responseBody = null;
        try {
            responseBody = getMethod2.getResponseBody();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException("获取验证码出错1！");
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
            throw new ServiceException("获取验证码写入本地出错2！");
        }

        return tmpcookies.toString();

    }

    /**
     * 验证码图片识别
     *
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
            throw new ServiceException("识别验证码出错2！");
        }
        content = content.replace(":", "3").replace("S", "5")
                .replace("E", "8").replace("s", "6")
                .replace("o", "0").replace("]", "1");
        return content;
    }

    /**
     * 进行模拟系统登陆操作
     *
     * @param verifyCode
     * @param cookies
     * @return
     * @throws IOException
     */
    protected static int logonAction(String verifyCode, String cookies) throws IOException {
        HttpClient httpClient = new HttpClient();
        String posturl = insiisUrl + "/logonAction.do";
        PostMethod postMethod = new PostMethod(posturl);
        //获取提取验证码时得到的cookie；
        postMethod.setRequestHeader("Cookie", cookies.replace(";", ""));
        // referer指当前页面从哪里来的，页面为了限制机器操作的方法一般为cookie,referer和验证码；
        //设置一些header
        postMethod.setRequestHeader("Accept", "image/gif, image/jpeg, image/pjpeg, application/x-ms-application, application/xaml+xml, application/x-ms-xbap, */*");
        postMethod.setRequestHeader("Accept-Encoding", "gzip, deflate");
        postMethod.setRequestHeader("Accept-Language", "zh-Hans-CN, zh-Hans; q=0.8, zh-Hant-HK; q=0.5, zh-Hant; q=0.3");
        postMethod.setRequestHeader("Cache-Control", "no-cache");
        postMethod.setRequestHeader("Connection", "keep-alive");
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        postMethod.setRequestHeader("Host", host);
        postMethod.setRequestHeader("Referer", insiisUrl);
        postMethod.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)");
        //把官网需要提交的参数添加
        postMethod.addParameter("passwd", passwordMD5);
        postMethod.addParameter("submitbtn.x", "0");
        postMethod.addParameter("submitbtn.y", "0");
        postMethod.addParameter("username", loginName);
        postMethod.addParameter("verifycode", verifyCode);

        int bb = httpClient.executeMethod(postMethod);

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

    /**
     * 向社保系统发起请求
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     * @throws ServiceException
     */
    public static JSONObject InsiisPost(String url, HashMap<String, Object> params) throws IOException, ServiceException {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        //获取提取验证码时得到的cookie；
        postMethod.setRequestHeader("Cookie", cashcookie);
        //postMethod.setRequestHeader("Cookie", "JSESSIONID=B7B3C2268352D3402FD79E8E531DC88B; __guid=96992031.765794392949568500.1529900658604.6194");
        // referer指当前页面从哪里来的，页面为了限制机器操作的方法一般为cookie,referer和验证码；
        //设置一些header
        postMethod.setRequestHeader("Accept", "*/*");
        postMethod.setRequestHeader("Accept-Encoding", "gzip, deflate");
        postMethod.setRequestHeader("Accept-Language", "zh-Hans-CN, zh-Hans; q=0.8, zh-Hant-HK; q=0.5, zh-Hant; q=0.3");
        postMethod.setRequestHeader("Cache-Control", "no-cache");
        postMethod.setRequestHeader("Connection", "keep-alive");
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        postMethod.setRequestHeader("Host", host);
        postMethod.setRequestHeader("Referer", insiisUrl);
        postMethod.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)");
        postMethod.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        //把官网需要提交的参数添加

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                postMethod.addParameter(key, params.get(key).toString());
            }
        }
       postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "GBK");//社保系统字符集是GBK
        int bb = httpClient.executeMethod(postMethod);

        //开始得到网站返回值
        byte[] responseBody = null;
        try {
            responseBody = postMethod.getResponseBody();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException("解析请求返回出错:" + e.getMessage());
        }
        //转成字符串并以json格式返回页面
        String result = new String(responseBody, "GBK");
//        String result2=new String(responseBody, "iso8859-1");
//        String result3=new String(responseBody, "UTF-8");



        System.out.println(bb + " " + result);
        JSONObject json = JSONObject.parseObject(result);

        if (json.get("mainMessage").toString().contains("数据库处理异常！")) {
            json.clear();
            json = InsiisPostTestLogin();//查看数据库处理异常！是否是因为没登陆
        }
        if (json.get("mainMessage").toString().contains("登陆超时")) {
            int cout = 0;
            while (cout < 10) {
                if (retrunLoginCookies() == 200) {
                    break;
                }
                cout++;
                if (cout > 5) {
                    throw new ServiceException("登陆社保中心系统出错！");
                }
            }
            json = InsiisPost(url, params);//重新发起请求
        }
        return json;
    }

    /**
     * 发起一个验证登陆的交易 验证当前是否登陆
     *
     * @return
     * @throws IOException
     * @throws ServiceException
     */
    public static JSONObject InsiisPostTestLogin() throws IOException, ServiceException {
        String url = insiisUrl + "/com/insigma/siis/local/module/common/search/GetPersonByIdAction.do?method=getPersonById";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("personId", 106983);//随便找一个AAC001
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        //获取提取验证码时得到的cookie；
        postMethod.setRequestHeader("Cookie", cashcookie);
        //postMethod.setRequestHeader("Cookie", "JSESSIONID=B7B3C2268352D3402FD79E8E531DC88B; __guid=96992031.765794392949568500.1529900658604.6194");
        // referer指当前页面从哪里来的，页面为了限制机器操作的方法一般为cookie,referer和验证码；
        //设置一些header
        postMethod.setRequestHeader("Accept", "*/*");
        postMethod.setRequestHeader("Accept-Encoding", "gzip, deflate");
        postMethod.setRequestHeader("Accept-Language", "zh-Hans-CN, zh-Hans; q=0.8, zh-Hant-HK; q=0.5, zh-Hant; q=0.3");
        postMethod.setRequestHeader("Cache-Control", "no-cache");
        postMethod.setRequestHeader("Connection", "keep-alive");
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        postMethod.setRequestHeader("Host", host);
        postMethod.setRequestHeader("Referer", insiisUrl);
        postMethod.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)");
        postMethod.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        //把官网需要提交的参数添加

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                postMethod.addParameter(key, params.get(key).toString());
            }
        }
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "GBK");//社保系统字符集是GBK
        int bb = httpClient.executeMethod(postMethod);

        //开始得到网站返回值
        byte[] responseBody = null;
        try {
            responseBody = postMethod.getResponseBody();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException("解析请求返回出错:" + e.getMessage());
        }
        //转成字符串并以json格式返回页面
        String result = new String(responseBody, "GBK");
        // String result2=new String(responseBody, "iso8859-1");
        System.out.println(bb + " " + result);
        JSONObject json = JSONObject.parseObject(result);

        return json;
    }


}
