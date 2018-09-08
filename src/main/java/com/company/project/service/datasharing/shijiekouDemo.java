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
        json.put("TradeCode","6002");


        String request=RSAUtils.encryptByPrivateKey(json.toJSONString(), privateKey);
        String sign=RSAUtils.sign(request,privateKey);
        System.out.println("sign:"+sign);
        System.out.println("request:"+request);
        Part[] parts= new Part[2];
        parts[0] = new StringPart("request", request,"UTF-8");
        parts[1] = new StringPart("sign", sign,"UTF-8");



        //System.out.println(DcryptUtils.verify(request,sign));//验证
       // System.out.println(DcryptUtils.decryptByPublicKey(request));
        System.out.println(post(url,parts));
        sendxunfei();//讯飞
       // System.out.println(DcryptUtils.decryptByPublicKey(request));
    }

    public static void sendxunfei(){
        String url="http://59.202.58.195:8000/ESBWeb/servlets/33.1111.zjhz.smzx.ycsl.register.SynReq@1.0";//讯飞 分发

        JSONObject json = new JSONObject();

        json.put("baseInfoXml","<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\"?>\n" +
                "<RECORD>\\n  \n" +
                "    <CALLINFO>\\n    \n" +
                "        <CALLER>杭州市一窗受理平台</CALLER>\\n    \n" +
                "        <CALLTIME>2017-09-11 10:11:25</CALLTIME>\\n    \n" +
                "        <CALLBACK_URL>http://10.54.19.68:8800/yzt</CALLBACK_URL>\\n    \n" +
                "        <ISSUE>33010000000001</ISSUE>\\n  \n" +
                "    </CALLINFO>\\n  \n" +
                "    <PROJID>330111050170909000009</PROJID>\\n  \n" +
                "    <PROJPWD>415183</PROJPWD>\\n  \n" +
                "    <IS_MANUBRIUM>2</IS_MANUBRIUM>\\n  \n" +
                "    <SERVICECODE>020005</SERVICECODE>\\n  \n" +
                "    <SERVICE_DEPTID>89559628-38a7-491c-8b16-27185f3e2176</SERVICE_DEPTID>\\n  \n" +
                "    <BUS_MODE>00</BUS_MODE>\\n  \n" +
                "    <BUS_MODE_DESC></BUS_MODE_DESC>\\n  \n" +
                "    <SERVICEVERSION>1</SERVICEVERSION>\\n  \n" +
                "    <SERVICENAME>公共场所卫生许可</SERVICENAME>\\n  \n" +
                "    <PROJECTNAME>公共场所卫生许可</PROJECTNAME>\\n  \n" +
                "    <INFOTYPE>承诺件</INFOTYPE>\\n  \n" +
                "    <BUS_TYPE>0</BUS_TYPE>\\n  \n" +
                "    <REL_BUS_ID></REL_BUS_ID>\\n  \n" +
                "    <APPLYNAME>杭州归档电子商务有限公司</APPLYNAME>\\n  \n" +
                "    <APPLY_CARDTYPE>1</APPLY_CARDTYPE>\\n  \n" +
                "    <APPLY_CARDNUMBER>91330102MA28NBU477</APPLY_CARDNUMBER>\\n  \n" +
                "    <CONTACTMAN>刘志强</CONTACTMAN>\\n  \n" +
                "    <CONTACTMAN_CARDTYPE></CONTACTMAN_CARDTYPE>\\n  \n" +
                "    <CONTACTMAN_CARDNUMBER>34082519900124151X</CONTACTMAN_CARDNUMBER>\\n  \n" +
                "    <TELPHONE>18010873024</TELPHONE>\\n  \n" +
                "    <POSTCODE></POSTCODE>\\n  \n" +
                "    <ADDRESS></ADDRESS>\\n  \n" +
                "    <LEGALMAN>朱鹏飞</LEGALMAN>\\n  \n" +
                "    <DEPTID>89559628-38a7-491c-8b16-27185f3e2176</DEPTID>\\n  \n" +
                "    <DEPTNAME>桐庐县卫生和计划生育局</DEPTNAME>\\n  \n" +
                "    <APPLYFROM>1</APPLYFROM>\\n  \n" +
                "    <APPROVE_TYPE>01</APPROVE_TYPE>\\n  \n" +
                "    <APPLY_PROPERTIY>99</APPLY_PROPERTIY>\\n  \n" +
                "    <RECEIVETIME>2017-09-09 16:54:52</RECEIVETIME>\\n  \n" +
                "    <BELONGTO></BELONGTO>\\n  \n" +
                "    <AREACODE>330122</AREACODE>\\n  \n" +
                "    <DATASTATE>1</DATASTATE>\\n  \n" +
                "    <BELONGSYSTEM>33000099004</BELONGSYSTEM>\\n  \n" +
                "    <EXTEND></EXTEND>\\n  \n" +
                "    <DATAVERSION>1</DATAVERSION>\\n  \n" +
                "    <SYNC_STATUS>I</SYNC_STATUS>\\n  \n" +
                "    <RECEIVE_USEID>LZQ16868</RECEIVE_USEID>\\n  \n" +
                "    <RECEIVE_NAME>刘志强</RECEIVE_NAME>\\n  \n" +
                "    <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>\\n  \n" +
                "    <SS_ORGCODE>89559628-38a7-491c-8b16-27185f3e2176</SS_ORGCODE>\\n  \n" +
                "    <MEMO></MEMO>\\n  \n" +
                "    <FZBS>15049465467211345</FZBS>\\n\n" +
                "</RECORD>");
        json.put("attrXml","<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\"?>\n" +
                "<RECORDS>\\n  \n" +
                "    <RECORD>\\n    \n" +
                "        <UNID>8ab693e65e574aad015e65d94a5a0849</UNID>\\n    \n" +
                "        <PROJID>330111050170909000009</PROJID>\\n    \n" +
                "        <ATTRNAME>法定代表人或负责人身份证明</ATTRNAME>\\n    \n" +
                "        <SORTID>2</SORTID>\\n    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>\\n    \n" +
                "        <ISTAKE>1</ISTAKE>\\n    \n" +
                "        <AMOUNT>1</AMOUNT>\\n    \n" +
                "        <TAKETIME>2017-09-09 16:54:52</TAKETIME>\\n    \n" +
                "        <MEMO></MEMO>\\n    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>\\n    \n" +
                "        <AREACODE>330122</AREACODE>\\n    \n" +
                "        <EXTEND></EXTEND>\\n    \n" +
                "        <DATAVERSION>1</DATAVERSION>\\n    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>\\n    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>\\n    \n" +
                "        <ATTRID>c4fee352-d507-40a1-8d54-8b3a6f34a6ab</ATTRID>\\n    \n" +
                "        <FILES>\\n      \n" +
                "            <FILEINFO>\\n        \n" +
                "                <FILENAME>1119493639_1472709631096_title0h.jpg</FILENAME>\\n        \n" +
                "                <FILEURL>330111050170909000009_ChBeBLdRVOy</FILEURL>\\n        \n" +
                "                <FILEPWD></FILEPWD>\\n      \n" +
                "            </FILEINFO>\\n    \n" +
                "        </FILES>\\n  \n" +
                "    </RECORD>\\n  \n" +
                "    <RECORD>\\n    \n" +
                "        <UNID>8ab693e65e574aad015e65d94ab7084c</UNID>\\n    \n" +
                "        <PROJID>330111050170909000009</PROJID>\\n    \n" +
                "        <ATTRNAME>公共场所地址方位示意图、平面图和卫生设施平面布局图</ATTRNAME>\\n    \n" +
                "        <SORTID>3</SORTID>\\n    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>\\n    \n" +
                "        <ISTAKE>1</ISTAKE>\\n    \n" +
                "        <AMOUNT>1</AMOUNT>\\n    \n" +
                "        <TAKETIME>2017-09-09 16:54:52</TAKETIME>\\n    \n" +
                "        <MEMO></MEMO>\\n    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>\\n    \n" +
                "        <AREACODE>330122</AREACODE>\\n    \n" +
                "        <EXTEND></EXTEND>\\n    \n" +
                "        <DATAVERSION>1</DATAVERSION>\\n    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>\\n    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>\\n    \n" +
                "        <ATTRID>f43b0984-5a78-40f2-ab17-b3f267d97709</ATTRID>\\n    \n" +
                "        <FILES>\\n      \n" +
                "            <FILEINFO>\\n        \n" +
                "                <FILENAME>a0d0e70c5e61384ca0d64464c0035b1964cbf6f8.jpg</FILENAME>\\n        \n" +
                "                <FILEURL>330111050170909000009_sfdjqQ4Ws0H</FILEURL>\\n        \n" +
                "                <FILEPWD></FILEPWD>\\n      \n" +
                "            </FILEINFO>\\n    \n" +
                "        </FILES>\\n  \n" +
                "    </RECORD>\\n  \n" +
                "    <RECORD>\\n    \n" +
                "        <UNID>8ab693e65e574aad015e65d94b2e084f</UNID>\\n    \n" +
                "        <PROJID>330111050170909000009</PROJID>\\n    \n" +
                "        <ATTRNAME>具有资质的检测机构出具的公共场所卫生检测或者评价报告</ATTRNAME>\\n    \n" +
                "        <SORTID>4</SORTID>\\n    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>\\n    \n" +
                "        <ISTAKE>1</ISTAKE>\\n    \n" +
                "        <AMOUNT>1</AMOUNT>\\n    \n" +
                "        <TAKETIME>2017-09-09 16:54:52</TAKETIME>\\n    \n" +
                "        <MEMO></MEMO>\\n    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>\\n    \n" +
                "        <AREACODE>330122</AREACODE>\\n    \n" +
                "        <EXTEND></EXTEND>\\n    \n" +
                "        <DATAVERSION>1</DATAVERSION>\\n    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>\\n    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>\\n    \n" +
                "        <ATTRID>03f7ab5e-eaa4-476f-a3bb-937afce87fba</ATTRID>\\n    \n" +
                "        <FILES>\\n      \n" +
                "            <FILEINFO>\\n        \n" +
                "                <FILENAME>20160102114555_514.JPG</FILENAME>\\n        \n" +
                "                <FILEURL>330111050170909000009_dAXPNyw44sX</FILEURL>\\n        \n" +
                "                <FILEPWD></FILEPWD>\\n      \n" +
                "            </FILEINFO>\\n    \n" +
                "        </FILES>\\n  \n" +
                "    </RECORD>\\n  \n" +
                "    <RECORD>\\n    \n" +
                "        <UNID>8ab693e65e574aad015e65d94b520852</UNID>\\n    \n" +
                "        <PROJID>330111050170909000009</PROJID>\\n    \n" +
                "        <ATTRNAME>公共场所卫生管理制度</ATTRNAME>\\n    \n" +
                "        <SORTID>5</SORTID>\\n    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>\\n    \n" +
                "        <ISTAKE>1</ISTAKE>\\n    \n" +
                "        <AMOUNT>1</AMOUNT>\\n    \n" +
                "        <TAKETIME>2017-09-09 16:54:53</TAKETIME>\\n    \n" +
                "        <MEMO></MEMO>\\n    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>\\n    \n" +
                "        <AREACODE>330122</AREACODE>\\n    \n" +
                "        <EXTEND></EXTEND>\\n    \n" +
                "        <DATAVERSION>1</DATAVERSION>\\n    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>\\n    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>\\n    \n" +
                "        <ATTRID>4106c3bf-9df3-4ce0-9d7c-942dabe4b8ea</ATTRID>\\n    \n" +
                "        <FILES>\\n      \n" +
                "            <FILEINFO>\\n        \n" +
                "                <FILENAME>a0d0e70c5e61384ca0d64464c0035b1964cbf6f8.jpg</FILENAME>\\n        \n" +
                "                <FILEURL>330111050170909000009_4NIaA1kOKtj</FILEURL>\\n        \n" +
                "                <FILEPWD></FILEPWD>\\n      \n" +
                "            </FILEINFO>\\n    \n" +
                "        </FILES>\\n  \n" +
                "    </RECORD>\\n  \n" +
                "    <RECORD>\\n    \n" +
                "        <UNID>8ab693e65e574aad015e65d94b7e0855</UNID>\\n    \n" +
                "        <PROJID>330111050170909000009</PROJID>\\n    \n" +
                "        <ATTRNAME>从业人员名单及健康合格证明</ATTRNAME>\\n    \n" +
                "        <SORTID>6</SORTID>\\n    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>\\n    \n" +
                "        <ISTAKE>1</ISTAKE>\\n    \n" +
                "        <AMOUNT>1</AMOUNT>\\n    \n" +
                "        <TAKETIME>2017-09-09 16:54:53</TAKETIME>\\n    \n" +
                "        <MEMO></MEMO>\\n    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>\\n    \n" +
                "        <AREACODE>330122</AREACODE>\\n    \n" +
                "        <EXTEND></EXTEND>\\n    \n" +
                "        <DATAVERSION>1</DATAVERSION>\\n    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>\\n    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>\\n    \n" +
                "        <ATTRID>8b847544-be49-4e2f-b1f4-dbabe233b1f3</ATTRID>\\n    \n" +
                "        <FILES>\\n      \n" +
                "            <FILEINFO>\\n        \n" +
                "                <FILENAME>a0d0e70c5e61384ca0d64464c0035b1964cbf6f8.jpg</FILENAME>\\n        \n" +
                "                <FILEURL>330111050170909000009_mHDuTCUl6xO</FILEURL>\\n        \n" +
                "                <FILEPWD></FILEPWD>\\n      \n" +
                "            </FILEINFO>\\n    \n" +
                "        </FILES>\\n  \n" +
                "    </RECORD>\\n  \n" +
                "    <RECORD>\\n    \n" +
                "        <UNID>8ab693e65e574aad015e65d94bb40858</UNID>\\n    \n" +
                "        <PROJID>330111050170909000009</PROJID>\\n    \n" +
                "        <ATTRNAME>使用集中空有资质的检测机构出具的集中空调通风系统卫生检测或者评价报告</ATTRNAME>\\n    \n" +
                "        <SORTID>7</SORTID>\\n    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>\\n    \n" +
                "        <ISTAKE>1</ISTAKE>\\n    \n" +
                "        <AMOUNT>1</AMOUNT>\\n    \n" +
                "        <TAKETIME>2017-09-09 16:54:53</TAKETIME>\\n    \n" +
                "        <MEMO></MEMO>\\n    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>\\n    \n" +
                "        <AREACODE>330122</AREACODE>\\n    \n" +
                "        <EXTEND></EXTEND>\\n    \n" +
                "        <DATAVERSION>1</DATAVERSION>\\n    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>\\n    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>\\n    \n" +
                "        <ATTRID>14e77e3d-9594-4007-97b7-46e794346154</ATTRID>\\n    \n" +
                "        <FILES>\\n      \n" +
                "            <FILEINFO>\\n        \n" +
                "                <FILENAME>a0d0e70c5e61384ca0d64464c0035b1964cbf6f8.jpg</FILENAME>\\n        \n" +
                "                <FILEURL>330111050170909000009_my5CyOdduPA</FILEURL>\\n        \n" +
                "                <FILEPWD></FILEPWD>\\n      \n" +
                "            </FILEINFO>\\n    \n" +
                "        </FILES>\\n  \n" +
                "    </RECORD>\\n  \n" +
                "    <RECORD>\\n    \n" +
                "        <UNID>8ab693e65e574aad015e65d94bd9085b</UNID>\\n    \n" +
                "        <PROJID>330111050170909000009</PROJID>\\n    \n" +
                "        <ATTRNAME>委托办理的，提供授权委托书及受委托人身份证明；</ATTRNAME>\\n    \n" +
                "        <SORTID>8</SORTID>\\n    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>\\n    \n" +
                "        <ISTAKE>1</ISTAKE>\\n    \n" +
                "        <AMOUNT>1</AMOUNT>\\n    \n" +
                "        <TAKETIME>2017-09-09 16:54:53</TAKETIME>\\n    \n" +
                "        <MEMO></MEMO>\\n    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>\\n    \n" +
                "        <AREACODE>330122</AREACODE>\\n    \n" +
                "        <EXTEND></EXTEND>\\n    \n" +
                "        <DATAVERSION>1</DATAVERSION>\\n    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>\\n    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>\\n    \n" +
                "        <ATTRID>329e0019-9500-4eff-a4c6-bcdfb5bc59ff</ATTRID>\\n    \n" +
                "        <FILES>\\n      \n" +
                "            <FILEINFO>\\n        \n" +
                "                <FILENAME>a0d0e70c5e61384ca0d64464c0035b1964cbf6f8.jpg</FILENAME>\\n        \n" +
                "                <FILEURL>330111050170909000009_dG1AmPhuHtu</FILEURL>\\n        \n" +
                "                <FILEPWD></FILEPWD>\\n      \n" +
                "            </FILEINFO>\\n    \n" +
                "        </FILES>\\n  \n" +
                "    </RECORD>\\n  \n" +
                "    <RECORD>\\n    \n" +
                "        <UNID>8ab693e65e574aad015e65d94c15085e</UNID>\\n    \n" +
                "        <PROJID>330111050170909000009</PROJID>\\n    \n" +
                "        <ATTRNAME>省级卫生计生行政部门要求提供的其它资料。</ATTRNAME>\\n    \n" +
                "        <SORTID>9</SORTID>\\n    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>\\n    \n" +
                "        <ISTAKE>1</ISTAKE>\\n    \n" +
                "        <AMOUNT>1</AMOUNT>\\n    \n" +
                "        <TAKETIME>2017-09-09 16:54:53</TAKETIME>\\n    \n" +
                "        <MEMO></MEMO>\\n    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>\\n    \n" +
                "        <AREACODE>330122</AREACODE>\\n    \n" +
                "        <EXTEND></EXTEND>\\n    \n" +
                "        <DATAVERSION>1</DATAVERSION>\\n    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>\\n    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>\\n    \n" +
                "        <ATTRID>cd38b647-e778-4401-954f-5ef073507457</ATTRID>\\n    \n" +
                "        <FILES>\\n      \n" +
                "            <FILEINFO>\\n        \n" +
                "                <FILENAME>a0d0e70c5e61384ca0d64464c0035b1964cbf6f8.jpg</FILENAME>\\n        \n" +
                "                <FILEURL>330111050170909000009_pQXXeKweeN7</FILEURL>\\n        \n" +
                "                <FILEPWD></FILEPWD>\\n      \n" +
                "            </FILEINFO>\\n    \n" +
                "        </FILES>\\n  \n" +
                "    </RECORD>\\n  \n" +
                "    <RECORD>\\n    \n" +
                "        <UNID>8ab693e65e574aad015e65d94c430861</UNID>\\n    \n" +
                "        <PROJID>330111050170909000009</PROJID>\\n    \n" +
                "        <ATTRNAME>工商营业执照</ATTRNAME>\\n    \n" +
                "        <SORTID>1</SORTID>\\n    \n" +
                "        <TAKETYPE>附件上传</TAKETYPE>\\n    \n" +
                "        <ISTAKE>0</ISTAKE>\\n    \n" +
                "        <AMOUNT>1</AMOUNT>\\n    \n" +
                "        <TAKETIME>2017-09-09 16:54:53</TAKETIME>\\n    \n" +
                "        <MEMO></MEMO>\\n    \n" +
                "        <BELONGSYSTEM>33000099004</BELONGSYSTEM>\\n    \n" +
                "        <AREACODE>330122</AREACODE>\\n    \n" +
                "        <EXTEND></EXTEND>\\n    \n" +
                "        <DATAVERSION>1</DATAVERSION>\\n    \n" +
                "        <SYNC_STATUS>I</SYNC_STATUS>\\n    \n" +
                "        <CREATE_TIME>2017-09-11 10:11:25</CREATE_TIME>\\n    \n" +
                "        <ATTRID>a49d49d2-648a-439d-9203-e24766a5b042</ATTRID>\\n    \n" +
                "        <FILES>\\n      \n" +
                "            <FILEINFO>\\n        \n" +
                "                <FILENAME>3.jpg.jpg</FILENAME>\\n        \n" +
                "                <FILEURL>330111050170909000009_QidcwMJ69lP</FILEURL>\\n        \n" +
                "                <FILEPWD></FILEPWD>\\n      \n" +
                "            </FILEINFO>\\n    \n" +
                "        </FILES>\\n  \n" +
                "    </RECORD>\\n\n" +
                "</RECORDS>");
        json.put("formXml","");
        json.put("apasPostXml","");
        json.put("sign","123");
        json.put("appKey","123");
        json.put("requestTime","333");


        Part[] parts= new Part[7];//讯飞
        parts[0] = new StringPart("formXml", "","UTF-8");
        parts[1] = new StringPart("apasPostXml", "","UTF-8");
        parts[2] = new StringPart("baseInfoXml", json.getString("baseInfoXml"),"UTF-8");
        parts[3] = new StringPart("attrXml", json.getString("attrXml"),"UTF-8");
        parts[4] = new StringPart("sign", "123","UTF-8");
        parts[5] = new StringPart("appKey", "123","UTF-8");
        parts[6] = new StringPart("requestTime", "123","UTF-8");


        System.out.println(post(url,parts));
    }
}




