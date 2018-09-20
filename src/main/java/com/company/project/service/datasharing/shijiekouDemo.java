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

        String url="https://sql.hz.gov.cn/ESBWeb/servlets/33.1111.zjhz.smzx.carsjdataSharingyz.SynReq@1.0";//淳安本地接口
       // String url="http://59.202.58.195:8000/ESBWeb/servlets/33.1111.zjhz.smzx.ycsl.transact.SynReq@1.0";//鸿程测试

        String id_nbr = "330127197408153814";
        String name = "唐渊东";
        JSONObject json = new JSONObject();
        json.put("AAC001","10907226201");
        json.put("FILETYPE","pdf");
        json.put("PRINTTYPE","1");
        json.put("TradeCode","6016");
        json.put("AAE135","513029196809066817");

        String request=RSAUtils.encryptByPrivateKey(json.toJSONString(), privateKey);
        String sign=RSAUtils.sign(request,privateKey);
        System.out.println("sign:"+sign);
        System.out.println("request:"+request);
        Part[] parts= new Part[2];
        parts[0] = new StringPart("request", request,"UTF-8");
        parts[1] = new StringPart("sign", sign,"UTF-8");



        System.out.println(DcryptUtils.verify(request,sign));//验证
        System.out.println(DcryptUtils.decryptByPublicKey(request));
        System.out.println(post(url,parts));
        //sendxunfei();//讯飞
       // System.out.println(DcryptUtils.decryptByPublicKey(request));
    }

    public static void sendxunfei(){
        String url="http://59.202.58.195:8000/ESBWeb/servlets/33.1111.zjhz.smzx.ycsl.register.SynReq@1.0";//讯飞 分发
        url="http://59.202.58.195:8000/ESBWeb/servlets/33.1111.zjhz.smzx.ycsl.accept.SynReq@1.0";//受理
        JSONObject json = new JSONObject();

        json.put("baseInfoXml","<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<RECORD>  \n" +
                "    <CALLINFO>    \n" +
                "        <CALLER>杭州市一窗受理平台</CALLER>    \n" +
                "        <CALLTIME>2017-09-11 10:11:25</CALLTIME>    \n" +
                "        <CALLBACK_URL>http://10.54.19.68:8800/yzt</CALLBACK_URL>    \n" +
                "        <ISSUE>33010000000001</ISSUE>  \n" +
                "    </CALLINFO>  \n" +
                "    <PROJID>330111050170909000009</PROJID>  \n" +
                "    <PROJPWD>415183</PROJPWD>  \n" +
                "    <IS_MANUBRIUM>2</IS_MANUBRIUM>  \n" +
                "    <SERVICECODE>020005</SERVICECODE>  \n" +
                "    <SERVICE_DEPTID>89559628-38a7-491c-8b16-27185f3e2176</SERVICE_DEPTID>  \n" +
                "    <BUS_MODE>00</BUS_MODE>  \n" +
                "    <BUS_MODE_DESC></BUS_MODE_DESC>  \n" +
                "    <SERVICEVERSION>1</SERVICEVERSION>  \n" +
                "    <SERVICENAME>公共场所卫生许可</SERVICENAME>  \n" +
                "    <PROJECTNAME>公共场所卫生许可</PROJECTNAME>  \n" +
                "    <INFOTYPE>承诺件</INFOTYPE>  \n" +
                "    <BUS_TYPE>0</BUS_TYPE>  \n" +
                "    <REL_BUS_ID></REL_BUS_ID>  \n" +
                "    <APPLYNAME>杭州归档电子商务有限公司</APPLYNAME>  \n" +
                "    <APPLY_CARDTYPE>1</APPLY_CARDTYPE>  \n" +
                "    <APPLY_CARDNUMBER>91330102MA28NBU477</APPLY_CARDNUMBER>  \n" +
                "    <CONTACTMAN>刘志强</CONTACTMAN>  \n" +
                "    <CONTACTMAN_CARDTYPE></CONTACTMAN_CARDTYPE>  \n" +
                "    <CONTACTMAN_CARDNUMBER>34082519900124151X</CONTACTMAN_CARDNUMBER>  \n" +
                "    <TELPHONE>18010873024</TELPHONE>  \n" +
                "    <POSTCODE></POSTCODE>  \n" +
                "    <ADDRESS></ADDRESS>  \n" +
                "    <LEGALMAN>朱鹏飞</LEGALMAN>  \n" +
                "    <DEPTID>89559628-38a7-491c-8b16-27185f3e2176</DEPTID>  \n" +
                "    <DEPTNAME>桐庐县卫生和计划生育局</DEPTNAME>  \n" +
                "    <APPLYFROM>1</APPLYFROM>  \n" +
                "    <APPROVE_TYPE>01</APPROVE_TYPE>  \n" +
                "    <APPLY_PROPERTIY>99</APPLY_PROPERTIY>  \n" +
                "    <RECEIVETIME>2017-09-09 16:54:52</RECEIVETIME>  \n" +
                "    <BELONGTO></BELONGTO>  \n" +
                "    <AREACODE>330122</AREACODE>  \n" +
                "    <DATASTATE>1</DATASTATE>  \n" +
                "    <BELONGSYSTEM>33000099004</BELONGSYSTEM>  \n" +
                "    <EXTEND></EXTEND>  \n" +
                "    <DATAVERSION>1</DATAVERSION>  \n" +
                "    <SYNC_STATUS>I</SYNC_STATUS>  \n" +
                "    <RECEIVE_USEID>LZQ16868</RECEIVE_USEID>  \n" +
                "    <RECEIVE_NAME>刘志强</RECEIVE_NAME>  \n" +
                "    <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>  \n" +
                "    <SS_ORGCODE>89559628-38a7-491c-8b16-27185f3e2176</SS_ORGCODE>  \n" +
                "    <MEMO></MEMO>  \n" +
                "    <FZBS>15049465467211345</FZBS>\n" +
                "</RECORD>");
        json.put("attrXml","<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<RECORDS>  \n" +
                "    <RECORD>    \n" +
                "        <UNID>8ab693e65e574aad015e65d94a5a0849</UNID>    \n" +
                "        <PROJID>330111050170909000009</PROJID>    \n" +
                "        <ATTRNAME>法定代表人或负责人身份证明</ATTRNAME>    \n" +
                "        <SORTID>2</SORTID>    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>    \n" +
                "        <ISTAKE>1</ISTAKE>    \n" +
                "        <AMOUNT>1</AMOUNT>    \n" +
                "        <TAKETIME>2017-09-09 16:54:52</TAKETIME>    \n" +
                "        <MEMO></MEMO>    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>    \n" +
                "        <AREACODE>330122</AREACODE>    \n" +
                "        <EXTEND></EXTEND>    \n" +
                "        <DATAVERSION>1</DATAVERSION>    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>    \n" +
                "        <ATTRID>c4fee352-d507-40a1-8d54-8b3a6f34a6ab</ATTRID>    \n" +
                "        <FILES>      \n" +
                "            <FILEINFO>        \n" +
                "                <FILENAME>1119493639_1472709631096_title0h.jpg</FILENAME>        \n" +
                "                <FILEURL>330111050170909000009_ChBeBLdRVOy</FILEURL>        \n" +
                "                <FILEPWD></FILEPWD>      \n" +
                "            </FILEINFO>    \n" +
                "        </FILES>  \n" +
                "    </RECORD>  \n" +
                "    <RECORD>    \n" +
                "        <UNID>8ab693e65e574aad015e65d94ab7084c</UNID>    \n" +
                "        <PROJID>330111050170909000009</PROJID>    \n" +
                "        <ATTRNAME>公共场所地址方位示意图、平面图和卫生设施平面布局图</ATTRNAME>    \n" +
                "        <SORTID>3</SORTID>    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>    \n" +
                "        <ISTAKE>1</ISTAKE>    \n" +
                "        <AMOUNT>1</AMOUNT>    \n" +
                "        <TAKETIME>2017-09-09 16:54:52</TAKETIME>    \n" +
                "        <MEMO></MEMO>    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>    \n" +
                "        <AREACODE>330122</AREACODE>    \n" +
                "        <EXTEND></EXTEND>    \n" +
                "        <DATAVERSION>1</DATAVERSION>    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>    \n" +
                "        <ATTRID>f43b0984-5a78-40f2-ab17-b3f267d97709</ATTRID>    \n" +
                "        <FILES>      \n" +
                "            <FILEINFO>        \n" +
                "                <FILENAME>a0d0e70c5e61384ca0d64464c0035b1964cbf6f8.jpg</FILENAME>        \n" +
                "                <FILEURL>330111050170909000009_sfdjqQ4Ws0H</FILEURL>        \n" +
                "                <FILEPWD></FILEPWD>      \n" +
                "            </FILEINFO>    \n" +
                "        </FILES>  \n" +
                "    </RECORD>  \n" +
                "    <RECORD>    \n" +
                "        <UNID>8ab693e65e574aad015e65d94b2e084f</UNID>    \n" +
                "        <PROJID>330111050170909000009</PROJID>    \n" +
                "        <ATTRNAME>具有资质的检测机构出具的公共场所卫生检测或者评价报告</ATTRNAME>    \n" +
                "        <SORTID>4</SORTID>    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>    \n" +
                "        <ISTAKE>1</ISTAKE>    \n" +
                "        <AMOUNT>1</AMOUNT>    \n" +
                "        <TAKETIME>2017-09-09 16:54:52</TAKETIME>    \n" +
                "        <MEMO></MEMO>    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>    \n" +
                "        <AREACODE>330122</AREACODE>    \n" +
                "        <EXTEND></EXTEND>    \n" +
                "        <DATAVERSION>1</DATAVERSION>    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>    \n" +
                "        <ATTRID>03f7ab5e-eaa4-476f-a3bb-937afce87fba</ATTRID>    \n" +
                "        <FILES>      \n" +
                "            <FILEINFO>        \n" +
                "                <FILENAME>20160102114555_514.JPG</FILENAME>        \n" +
                "                <FILEURL>330111050170909000009_dAXPNyw44sX</FILEURL>        \n" +
                "                <FILEPWD></FILEPWD>      \n" +
                "            </FILEINFO>    \n" +
                "        </FILES>  \n" +
                "    </RECORD>  \n" +
                "    <RECORD>    \n" +
                "        <UNID>8ab693e65e574aad015e65d94b520852</UNID>    \n" +
                "        <PROJID>330111050170909000009</PROJID>    \n" +
                "        <ATTRNAME>公共场所卫生管理制度</ATTRNAME>    \n" +
                "        <SORTID>5</SORTID>    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>    \n" +
                "        <ISTAKE>1</ISTAKE>    \n" +
                "        <AMOUNT>1</AMOUNT>    \n" +
                "        <TAKETIME>2017-09-09 16:54:53</TAKETIME>    \n" +
                "        <MEMO></MEMO>    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>    \n" +
                "        <AREACODE>330122</AREACODE>    \n" +
                "        <EXTEND></EXTEND>    \n" +
                "        <DATAVERSION>1</DATAVERSION>    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>    \n" +
                "        <ATTRID>4106c3bf-9df3-4ce0-9d7c-942dabe4b8ea</ATTRID>    \n" +
                "        <FILES>      \n" +
                "            <FILEINFO>        \n" +
                "                <FILENAME>a0d0e70c5e61384ca0d64464c0035b1964cbf6f8.jpg</FILENAME>        \n" +
                "                <FILEURL>330111050170909000009_4NIaA1kOKtj</FILEURL>        \n" +
                "                <FILEPWD></FILEPWD>      \n" +
                "            </FILEINFO>    \n" +
                "        </FILES>  \n" +
                "    </RECORD>  \n" +
                "    <RECORD>    \n" +
                "        <UNID>8ab693e65e574aad015e65d94b7e0855</UNID>    \n" +
                "        <PROJID>330111050170909000009</PROJID>    \n" +
                "        <ATTRNAME>从业人员名单及健康合格证明</ATTRNAME>    \n" +
                "        <SORTID>6</SORTID>    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>    \n" +
                "        <ISTAKE>1</ISTAKE>    \n" +
                "        <AMOUNT>1</AMOUNT>    \n" +
                "        <TAKETIME>2017-09-09 16:54:53</TAKETIME>    \n" +
                "        <MEMO></MEMO>    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>    \n" +
                "        <AREACODE>330122</AREACODE>    \n" +
                "        <EXTEND></EXTEND>    \n" +
                "        <DATAVERSION>1</DATAVERSION>    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>    \n" +
                "        <ATTRID>8b847544-be49-4e2f-b1f4-dbabe233b1f3</ATTRID>    \n" +
                "        <FILES>      \n" +
                "            <FILEINFO>        \n" +
                "                <FILENAME>a0d0e70c5e61384ca0d64464c0035b1964cbf6f8.jpg</FILENAME>        \n" +
                "                <FILEURL>330111050170909000009_mHDuTCUl6xO</FILEURL>        \n" +
                "                <FILEPWD></FILEPWD>      \n" +
                "            </FILEINFO>    \n" +
                "        </FILES>  \n" +
                "    </RECORD>  \n" +
                "    <RECORD>    \n" +
                "        <UNID>8ab693e65e574aad015e65d94bb40858</UNID>    \n" +
                "        <PROJID>330111050170909000009</PROJID>    \n" +
                "        <ATTRNAME>使用集中空有资质的检测机构出具的集中空调通风系统卫生检测或者评价报告</ATTRNAME>    \n" +
                "        <SORTID>7</SORTID>    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>    \n" +
                "        <ISTAKE>1</ISTAKE>    \n" +
                "        <AMOUNT>1</AMOUNT>    \n" +
                "        <TAKETIME>2017-09-09 16:54:53</TAKETIME>    \n" +
                "        <MEMO></MEMO>    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>    \n" +
                "        <AREACODE>330122</AREACODE>    \n" +
                "        <EXTEND></EXTEND>    \n" +
                "        <DATAVERSION>1</DATAVERSION>    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>    \n" +
                "        <ATTRID>14e77e3d-9594-4007-97b7-46e794346154</ATTRID>    \n" +
                "        <FILES>      \n" +
                "            <FILEINFO>        \n" +
                "                <FILENAME>a0d0e70c5e61384ca0d64464c0035b1964cbf6f8.jpg</FILENAME>        \n" +
                "                <FILEURL>330111050170909000009_my5CyOdduPA</FILEURL>        \n" +
                "                <FILEPWD></FILEPWD>      \n" +
                "            </FILEINFO>    \n" +
                "        </FILES>  \n" +
                "    </RECORD>  \n" +
                "    <RECORD>    \n" +
                "        <UNID>8ab693e65e574aad015e65d94bd9085b</UNID>    \n" +
                "        <PROJID>330111050170909000009</PROJID>    \n" +
                "        <ATTRNAME>委托办理的，提供授权委托书及受委托人身份证明；</ATTRNAME>    \n" +
                "        <SORTID>8</SORTID>    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>    \n" +
                "        <ISTAKE>1</ISTAKE>    \n" +
                "        <AMOUNT>1</AMOUNT>    \n" +
                "        <TAKETIME>2017-09-09 16:54:53</TAKETIME>    \n" +
                "        <MEMO></MEMO>    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>    \n" +
                "        <AREACODE>330122</AREACODE>    \n" +
                "        <EXTEND></EXTEND>    \n" +
                "        <DATAVERSION>1</DATAVERSION>    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>    \n" +
                "        <ATTRID>329e0019-9500-4eff-a4c6-bcdfb5bc59ff</ATTRID>    \n" +
                "        <FILES>      \n" +
                "            <FILEINFO>        \n" +
                "                <FILENAME>a0d0e70c5e61384ca0d64464c0035b1964cbf6f8.jpg</FILENAME>        \n" +
                "                <FILEURL>330111050170909000009_dG1AmPhuHtu</FILEURL>        \n" +
                "                <FILEPWD></FILEPWD>      \n" +
                "            </FILEINFO>    \n" +
                "        </FILES>  \n" +
                "    </RECORD>  \n" +
                "    <RECORD>    \n" +
                "        <UNID>8ab693e65e574aad015e65d94c15085e</UNID>    \n" +
                "        <PROJID>330111050170909000009</PROJID>    \n" +
                "        <ATTRNAME>省级卫生计生行政部门要求提供的其它资料。</ATTRNAME>    \n" +
                "        <SORTID>9</SORTID>    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>    \n" +
                "        <ISTAKE>1</ISTAKE>    \n" +
                "        <AMOUNT>1</AMOUNT>    \n" +
                "        <TAKETIME>2017-09-09 16:54:53</TAKETIME>    \n" +
                "        <MEMO></MEMO>    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>    \n" +
                "        <AREACODE>330122</AREACODE>    \n" +
                "        <EXTEND></EXTEND>    \n" +
                "        <DATAVERSION>1</DATAVERSION>    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>    \n" +
                "        <ATTRID>cd38b647-e778-4401-954f-5ef073507457</ATTRID>    \n" +
                "        <FILES>      \n" +
                "            <FILEINFO>        \n" +
                "                <FILENAME>a0d0e70c5e61384ca0d64464c0035b1964cbf6f8.jpg</FILENAME>        \n" +
                "                <FILEURL>330111050170909000009_pQXXeKweeN7</FILEURL>        \n" +
                "                <FILEPWD></FILEPWD>      \n" +
                "            </FILEINFO>    \n" +
                "        </FILES>  \n" +
                "    </RECORD>  \n" +
                "    <RECORD>    \n" +
                "        <UNID>8ab693e65e574aad015e65d94c430861</UNID>    \n" +
                "        <PROJID>330111050170909000009</PROJID>    \n" +
                "        <ATTRNAME>工商营业执照</ATTRNAME>    \n" +
                "        <SORTID>1</SORTID>    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>    \n" +
                "        <ISTAKE>0</ISTAKE>    \n" +
                "        <AMOUNT>1</AMOUNT>    \n" +
                "        <TAKETIME>2017-09-09 16:54:53</TAKETIME>    \n" +
                "        <MEMO></MEMO>    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>    \n" +
                "        <AREACODE>330122</AREACODE>    \n" +
                "        <EXTEND></EXTEND>    \n" +
                "        <DATAVERSION>1</DATAVERSION>    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>    \n" +
                "        <ATTRID>a49d49d2-648a-439d-9203-e24766a5b042</ATTRID>    \n" +
                "        <FILES>      \n" +
                "            <FILEINFO>        \n" +
                "                <FILENAME>3.jpg.jpg</FILENAME>        \n" +
                "                <FILEURL>330111050170909000009_QidcwMJ69lP</FILEURL>        \n" +
                "                <FILEPWD></FILEPWD>      \n" +
                "            </FILEINFO>    \n" +
                "        </FILES>  \n" +
                "    </RECORD>\n" +
                "</RECORDS>");
        json.put("formXml","<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<RECORDS>  \n" +
                "    <FormInfo name=\"本许可个性化信息\">    \n" +
                "        <Item name=\"freplictype\" name_cn=\"法人证件类型\">01</Item>    \n" +
                "        <Item name=\"hzqh\" name_cn=\"杭州市统一区划\">330122</Item>    \n" +
                "        <Item name=\"forgnm\" name_cn=\"单位名称\">杭州归档电子商务有限公司</Item>    \n" +
                "        <Item name=\"faddr\" name_cn=\"经营地址\">浙江省杭州市上城区清波街道清波街道111号</Item>    \n" +
                "        <Item name=\"frepicno\" name_cn=\"法人证件号码\">330104198809184718</Item>    \n" +
                "        <Item name=\"frepnm\" name_cn=\"法定代表人\">朱鹏飞</Item>    \n" +
                "        <Item name=\"yyslx\" name_cn=\"饮用水\">3</Item>    \n" +
                "        <Item name=\"frespicno\" name_cn=\"负责人证件号码\">34082519900124151X</Item>    \n" +
                "        <Item name=\"swtrlxdh\" name_cn=\"受委托人联系电话\">0556-4588221</Item>    \n" +
                "        <Item name=\"forgtel\" name_cn=\"单位电话\">0556-4588221</Item>    \n" +
                "        <Item name=\"yqbmcl\" name_cn=\"要求保密材料\">无</Item>    \n" +
                "        <Item name=\"fregtype\" name_cn=\"经济类型\"/>    \n" +
                "        <Item name=\"fwillcheckcnt\" name_cn=\"应体检人数\">12</Item>    \n" +
                "        <Item name=\"swtr\" name_cn=\"受委托人\">刘志强</Item>    \n" +
                "        <Item name=\"freptel\" name_cn=\"法人联系电话\"/>    \n" +
                "        <Item name=\"cntyyf\" name_cn=\"承诺书\">1</Item>    \n" +
                "        <Item name=\"jzkttfxt\" name_cn=\"集中空调通风系统\">2</Item>    \n" +
                "        <Item name=\"fempcnt\" name_cn=\"职工人数\">11</Item>    \n" +
                "        <Item name=\"zyxm\" name_cn=\"主营项目\">020002</Item>    \n" +
                "        <Item name=\"fsquare\" name_cn=\"经营面积\">11.00</Item>    \n" +
                "        <Item name=\"freplictypenm\" name_cn=\"其他\"></Item>    \n" +
                "        <Item name=\"gsfs\" name_cn=\"集中式供水方式\"></Item>    \n" +
                "        <Item name=\"jyxm\" name_cn=\"经营项目\">020017</Item>    \n" +
                "        <Item name=\"fresplictype\" name_cn=\"负责人证号类型\">01</Item>    \n" +
                "        <Item name=\"fresptel\" name_cn=\"负责人联系电话\">0556-4588221</Item>    \n" +
                "        <Item name=\"frespnm\" name_cn=\"负责人\">刘志强测试1</Item>    \n" +
                "        <Item name=\"wsgly\" name_cn=\"卫生管理员\">刘志强</Item>  \n" +
                "    </FormInfo>\n" +
                "</RECORDS>");
        json.put("apasPostXml","");
        json.clear();
        json.put("acceptInfoXml","<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<RECORD>\n" +
                "<PROJID>330901161710248000216</PROJID>\n" +
                "    <ACCEPT_MAN>叶洁</ACCEPT_MAN>\n" +
                "<HANDER_DEPTNAME>综合参保服务中心</HANDER_DEPTNAME>\n" +
                "<HANDER_DEPTID></HANDER_DEPTID>\n" +
                "<AREACODE>330901</AREACODE>\n" +
                "    <ACCEPT_TIME>2017-08-03 11:04:29</ACCEPT_TIME>\n" +
                "<PROMISEVALUE>5</PROMISEVALUE>\n" +
                "<PROMISETYPE>工作日</PROMISETYPE>\n" +
                "<PROMISE_ETIME>2017-08-08 11:04:29</PROMISE_ETIME>\n" +
                "    <BELONGSYSTEM>33090116003</BELONGSYSTEM>\n" +
                "<EXTEND></EXTEND>\n" +
                "<DATAVERSION>1</DATAVERSION>\n" +
                "<SYNC_STATUS>I</SYNC_STATUS>\n" +
                "    <CREATE_TIME>2017-08-03 11:04:29</CREATE_TIME>\n" +
                "</RECORD>");


//        Part[] parts= new Part[4];//讯飞
//        parts[0] = new StringPart("formXml", "","UTF-8");
//        parts[1] = new StringPart("apasPostXml", "","UTF-8");
//        parts[2] = new StringPart("baseInfoXml", json.getString("baseInfoXml"),"UTF-8");
//        parts[3] = new StringPart("attrXml", json.getString("attrXml"),"UTF-8");
        Part[] parts= new Part[2];//讯飞
        parts[0] = new StringPart("acceptInfoXml", json.getString("acceptInfoXml"),"UTF-8");
        parts[1] = new StringPart("attrXml", "","UTF-8");

        System.out.println(post(url,parts));
    }
}




