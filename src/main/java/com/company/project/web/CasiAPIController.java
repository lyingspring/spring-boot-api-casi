package com.company.project.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.project.core.DataShaingResult;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.dao.PublicMapper;
import com.company.project.model.Ac01;
import com.company.project.model.BasicinfoDTO;
import com.company.project.model.Datashaingtranslog;
import com.company.project.service.*;
import com.company.project.service.datasharing.DcryptUtils;
import com.company.project.service.datasharing.RSAUtils;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;
import org.apache.axis2.AxisFault;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author maoxj
 */
@RestController
@RequestMapping("/casiapi")
public class CasiAPIController {
    @Resource
    private Ac01Service ac01Service;
    @Resource
    private DatashaingtranslogService DLogService;
    @Resource
    private PublicMapper publicMapper;
    @Resource
    private DateSharing ds;//有spring 注入的工具类 不能new 只能Spring注入
    private static HashMap<String, String> phone = new HashMap<String, String>();

    @PostMapping("/sendSmsbyOnce")
    @CrossOrigin(origins = "*", maxAge = 3600)//浏览器跨域访问 origins 允许访问的地址如：http://domain2.com *为全部 maxAge过期时间 3600秒
    public Result sendSms(@RequestParam String phoneNumber, String msg) throws AxisFault {

        MasSMS msm = new MasSMS();
        String[] pnums = null;
        pnums = phoneNumber.split(",");
        msm.sendSms(pnums, msg);
        // phone.put(pnums[0]+String.valueOf(System.currentTimeMillis()),String.valueOf(System.currentTimeMillis()));
        // System.out.println(phone);
        return ResultGenerator.genSuccessResult("OK");
    }

    /**
     * 查询公安数据
     *
     * @param sfz
     * @return
     * @throws IOException
     */
    @PostMapping("/queryGA")
    public Result queryGA(@RequestParam String sfz) throws IOException {
        oracle_jdbc db = new oracle_jdbc();
        List<HashMap<String, String>> listmap = db.createSQLQuery("select * from AC01_FROMPOLICE where gmsfhm='" + sfz + "' and ryzt='0' and jlbz='1' and cxbz='0' and rownum=1 ");
        if (listmap.size() == 0) {
            throw new ServiceException("找不到该人员信息");
        }
        return ResultGenerator.genSuccessResult(listmap);
    }

    /**
     * 参保证明返回
     *
     * @param sfz 身份证
     * @return
     * @throws IOException
     */
    @PostMapping("/getInsuranceProofImg")
    public Result getInsuranceProof(@RequestParam String sfz) throws IOException {
        Condition condition = new Condition(Ac01.class);
        condition.createCriteria().andCondition("aae135 ='" + sfz + "'");
        List<Ac01> ac01 = ac01Service.findByCondition(condition);
        if (ac01.size() == 0) {
            throw new ServiceException("找不到该人员信息");
        }
        String urlstr = "http://10.255.5.100:8399/WebReport/ReportServer?reportlet=/casi/CA000019.cpt&v_aac001="
                + ac01.get(0).getAac001() + "&format=image&extype=JPG";
        String saveDir = "d://springBoot_tmp";
        String fileName = sfz + ".jpg";
        FileDownloadTool.download(urlstr, saveDir, fileName);//下载文件到本地
        File file = new File(saveDir + File.separator + fileName);
        String strBase64 = Base64_Coder.getFileBinary(file);
        // Base64_Coder.base64StringToFile(strBase64,"d://img/333.jpg");
        return ResultGenerator.genSuccessResult(strBase64);
    }

    /**
     * 参保证明返回
     *
     * @param sfz 身份证
     * @return
     * @throws IOException
     */
    @PostMapping("/getInsuranceProofPDF")
    public Result getInsuranceProofPDF(@RequestParam String sfz) throws IOException {
        Condition condition = new Condition(Ac01.class);
        condition.createCriteria().andCondition("aae135 ='" + sfz + "'");
        List<Ac01> ac01 = ac01Service.findByCondition(condition);
        if (ac01.size() == 0) {
            throw new ServiceException("找不到该人员信息");
        }
        String urlstr = "http://10.255.5.100:8399/WebReport/ReportServer?reportlet=/casi/CA000019.cpt&v_aac001="
                + ac01.get(0).getAac001() + "&&format=pdf";
        String saveDir = "d://springBoot_tmp";
        String fileName = sfz + ".pdf";
        System.out.printf(sfz + " " + fileName);
        FileDownloadTool.download(urlstr, saveDir, fileName);//下载文件到本地
        File file = new File(saveDir + File.separator + fileName);
        String strBase64 = Base64_Coder.getFileBinary(file);
        // Base64_Coder.base64StringToFile(strBase64,"d://img/333.pdf");
        return ResultGenerator.genSuccessResult(strBase64);
    }

    /**
     * 调用社保一体化过程
     *
     * @return
     * @throws IOException
     */
    @PostMapping("/getInfo")
    public Result getInfo() throws IOException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String s=URLDecoder.decode("aaa029=1&aaa029_combo=%E5%85%AC%E6%B0%91%E8%BA%AB%E4%BB%BD%E8%AF%81&aae135=330283198602180016&aac003=%E6%AF%9B%E6%A0%A1%E5%86%9B&eac001=6000120378&aac004=1&aac004_combo=%E7%94%B7&aac006_str=1986-02-18&aac005=01&aac005_combo=%E6%B1%89%E6%97%8F&aac007_str=2018-09-04&aae147=1&aae147_combo=%E6%9C%AC%E7%BB%9F%E7%AD%B9%E5%9C%B0%E5%8C%BA&aaa084=1&aaa084_combo=%E6%96%B0%E5%A2%9E&aac009=20&aac009_combo=%E6%9C%AC%E5%9C%B0%E5%86%9C%E6%9D%91&aac010=%E6%B7%B3%E5%AE%89&aab301=330127&aab301_combo=%E6%B7%B3%E5%AE%89%E5%8E%BF&eab009=&eab009_combo=%E8%AF%B7%E6%82%A8%E9%80%89%E6%8B%A9...&eab030=&eab030_combo=%E8%AF%B7%E6%82%A8%E9%80%89%E6%8B%A9...&aac014=&aac014_combo=%E8%AF%B7%E6%82%A8%E9%80%89%E6%8B%A9...&aac015=&aac015_combo=%E8%AF%B7%E6%82%A8%E9%80%89%E6%8B%A9...&aac020=&aac020_combo=%E8%AF%B7%E6%82%A8%E9%80%89%E6%8B%A9...&aac017=&aac017_combo=%E8%AF%B7%E6%82%A8%E9%80%89%E6%8B%A9...&aae159=&aac011=&aac011_combo=%E8%AF%B7%E6%82%A8%E9%80%89%E6%8B%A9...&aae005_1=0571&aae005_2=&eac101=&aae007=&aab401=&aae006=&caa001=&caa001_combo=%E8%AF%B7%E6%82%A8%E9%80%89%E6%8B%A9...&caa002=&caa002_combo=%E8%AF%B7%E6%82%A8%E9%80%89%E6%8B%A9...&caa007=&caa007_combo=%E8%AF%B7%E6%82%A8%E9%80%89%E6%8B%A9...&caa005=&caa006=&caa003=&caa003_combo=%E8%AF%B7%E6%82%A8%E9%80%89%E6%8B%A9...&caa004=&caa004_combo=%E8%AF%B7%E6%82%A8%E9%80%89%E6%8B%A9...&aae005=0571&aac006=19860218&aac001=&aac001_=&aac007=20180904&aab001=&aaz001=&aab004=&aae013=&aae013_s=&userlog={\"functionid\":\"49FCCF5C7444559DCA2A976A242D4FD2\",\"aac001\":111,\"aab001\":111,\"digest\":\"姓名:毛校军,身份证:330283198602180016,社保编号:6000120381\",\"prcol1\":\"\",\"prcol2\":\"\",\"prcol3\":\"\",\"prcol4\":\"\",\"prcol5\":\"\",\"prcol6\":\"\",\"prcol7\":\"\",\"prcol8\":\"\",\"orisource\":\"此条记录由外网系统调用接口操作\"}", "UTF-8");

        //s=URLEncoder.encode(s,"GBK");
        map = ds.getUrlParams(s);

        System.out.println("s = " + s);
         map.put("personId", 106983);
//        JSONObject json= LoginInsiis.InsiisPost("http://10.255.5.11:8080/insiis/com/insigma/siis/local/module/common/search/GetPersonByIdAction.do?method=getPersonById",map);
        JSONObject json = LoginInsiis.InsiisPost("http://10.255.5.11:8080/insiis/com/insigma/siis/local/module/insuredmgmt/archivereg/SaveAction.do?method=save", map);

        return ResultGenerator.genSuccessResult(json);
    }

    /**
     * 数据共享对外API
     *
     * @return
     * @throws IOException
     */
    @PostMapping("/dataSharing")
    public DataShaingResult dataSharing(@RequestParam String request, @RequestParam String sign) throws IOException {
        Datashaingtranslog dlog=new Datashaingtranslog();//日志表
        dlog.setSno(publicMapper.querySequenceByParam("SQ_SNO"));//取得序列号

        boolean b = DcryptUtils.verify(request, sign);
        Object obj = new Object();
        obj = "未找到对应的接口";
        if (!b) {
            dlog.setRequest(request);
            dlog.setVarout("签名验证不通过");
            dlog.setTransid(dlog.getSno()+"_0");
            DLogService.save(dlog);//插入日志
            throw new ServiceException("签名验证不通过");
        }
        Map<String, Object> map = DcryptUtils.decryptByPublicKey(request);
        System.out.println(map);

        dlog.setRequest(request);
        dlog.setTransid(dlog.getSno()+"_"+map.get("TradeCode").toString());
        dlog.setItradetype(map.get("TradeCode").toString());
        dlog.setVarin(JSON.toJSONString(map));
        DLogService.save(dlog);//插入日志
        try {
            if (map.get("TradeCode").toString().equals("6001")) {//转出验证
                obj = ds.trade6001(map);
            }
            if (map.get("TradeCode").toString().equals("6002")) {//转出打印
                obj = ds.trade6002(map);
            }
            if (map.get("TradeCode").toString().equals("6003")) {//参保信息变更验证
                obj = ds.trade6003(map);
            }
            if (map.get("TradeCode").toString().equals("6004")) {//人员手机及地址变更
                obj = ds.trade6004(map);
            }
            if (map.get("TradeCode").toString().equals("6032")) {//通过AAC001人员基本信息查询(人员搜索框)
                obj = ds.trade6032(map);
            }
        }catch (Exception e){
            dlog.setVarout(e.getMessage());
            DLogService.update(dlog);//更新日志
            throw new ServiceException(e.getMessage());//再次把异常抛出供拦截器拦截
        }


        dlog.setVarout(obj.toString());
        DLogService.update(dlog);//更新日志
        return ResultGenerator.genDSuccessResult(obj);
    }

    /**
     * 杭州数据共享接口本地测试用
     *
     * @param request
     * @return
     */
    @PostMapping("/test")
    public DataShaingResult test(@RequestParam String request) throws Exception {
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKLQgp07slkmHn/NrfvsaGrzI/TsiJ2KhXtQ9eNofWmX/NyJFROVCORfILRCL5e1qS+aFcuYg/jekQxBwOpDMxEStIy+RKySPms6VOS/abEZ1UKsxK8deLrNqoiwITL9uiKsOmTp2ULZ76ntwrKVxxmPQr6wlu3F7L8DyyE1SnoHAgMBAAECgYEAm2JH5WtdsLsyjGJtU2qx1LArdspvL3tOHPyNTvUgC7CkAI1Lch7gF6O6AI7SAQW8a9OwTVhHSzKOV5ZBWNG9X12kzWinHQbxiItFdWTvlCBziS/fGbzIyO+O6HmtTCFdIBz0PBy30q9UDaHdwB3kCd+VDLabWKsaM8A0KTUxmFECQQDXvj08NLG0x/kpGnH9GzhDu7H5UH5Ich2McO4h+EGq/s+dBX1aWfkLlRZgWV1ltOpe0Zz47ZLTKu8APygqQnNFAkEAwTHw690v8VX/m5I/kgvVdeQ3/fQIYKTI6tjk/Ozf8Q9KRojD9snCMUc+z760UYbHNE9jTgu8vokBr0fcF4lG2wJAVEN0bVhzdBWK5pfyn5YLEsFzkNn0iN0xV1IgYFozY9MkScMEI87ya6iuVbFxvjC8PY6HTd6Usy+Yq7L/QAo2NQJABk10yJ0MpVji39ZjkIYmTpRFZ1mAtHZrv42X2tB3dcvD5o0rp29pkGX8nJZiF47IDOLSIIetfqHFlkxH19S4pQJAINvZg5QNNSnjWYUr/S50CJXaEoG/zVfObpcP71R9qdAGvZYkuqFedzeG6kWHgUVyHgw46fhMNRITKMr4MBifkw==";
        JSONObject json = new JSONObject();
        json = JSONObject.parseObject(request);
        String request1 = RSAUtils.encryptByPrivateKey(json.toJSONString(), privateKey);
        String sign = RSAUtils.sign(request1, privateKey);
        System.out.println(request1);
        System.out.println(sign);
        return dataSharing(request1, sign);
    }

}
