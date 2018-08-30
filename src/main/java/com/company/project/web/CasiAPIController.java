package com.company.project.web;

import com.alibaba.fastjson.JSONObject;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.model.Ac01;
import com.company.project.model.BasicinfoDTO;
import com.company.project.service.*;
import com.company.project.service.datasharing.DcryptUtils;
import com.company.project.service.datasharing.RSAUtils;
import org.apache.axis2.AxisFault;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
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
        System.out.printf(sfz+" "+fileName);
        FileDownloadTool.download(urlstr, saveDir, fileName);//下载文件到本地
        File file = new File(saveDir + File.separator + fileName);
        String strBase64 = Base64_Coder.getFileBinary(file);
        // Base64_Coder.base64StringToFile(strBase64,"d://img/333.pdf");
        return ResultGenerator.genSuccessResult(strBase64);
    }

    /**
     * 调用社保一体化过程
     * @return
     * @throws IOException
     */
    @PostMapping("/getInfo")
    public Result getInfo() throws IOException {
        HashMap<String,Object> map=new HashMap<String,Object>();
        map.put("personId", 106983);
        JSONObject json= LoginInsiis.InsiisPost("http://10.255.5.11:8080/insiis/com/insigma/siis/local/module/common/search/GetPersonByIdAction.do?method=getPersonById",map);
        return ResultGenerator.genSuccessResult(json);
    }

    /**
            * 数据共享对外API
     * @return
             * @throws IOException
     */
    @PostMapping("/dataSharing")
    public Result dataSharing(@RequestParam String request,@RequestParam String sign)   {
        boolean b= DcryptUtils.verify(request,sign);
        Object obj=new Object();
        obj="未找到对应的接口";
        if(!b){
            throw new ServiceException("签名验证不通过");
        }
        Map<String,Object> map= DcryptUtils.decryptByPublicKey(request);

        if(map.get("TradeCode").toString().equals("6001")){//转出验证
            obj=ds.turnOutVerification(map);
        }
        if(map.get("TradeCode").toString().equals("6002")){//转出打印
            obj=ds.turnOutPrint(map);
        }

        return ResultGenerator.genSuccessResult(obj);
    }

    /**
     * 本地测试用
     * @param request
     * @return
     */
    @PostMapping("/test")
    public Result test(@RequestParam String request) throws Exception {
        String privateKey="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKLQgp07slkmHn/NrfvsaGrzI/TsiJ2KhXtQ9eNofWmX/NyJFROVCORfILRCL5e1qS+aFcuYg/jekQxBwOpDMxEStIy+RKySPms6VOS/abEZ1UKsxK8deLrNqoiwITL9uiKsOmTp2ULZ76ntwrKVxxmPQr6wlu3F7L8DyyE1SnoHAgMBAAECgYEAm2JH5WtdsLsyjGJtU2qx1LArdspvL3tOHPyNTvUgC7CkAI1Lch7gF6O6AI7SAQW8a9OwTVhHSzKOV5ZBWNG9X12kzWinHQbxiItFdWTvlCBziS/fGbzIyO+O6HmtTCFdIBz0PBy30q9UDaHdwB3kCd+VDLabWKsaM8A0KTUxmFECQQDXvj08NLG0x/kpGnH9GzhDu7H5UH5Ich2McO4h+EGq/s+dBX1aWfkLlRZgWV1ltOpe0Zz47ZLTKu8APygqQnNFAkEAwTHw690v8VX/m5I/kgvVdeQ3/fQIYKTI6tjk/Ozf8Q9KRojD9snCMUc+z760UYbHNE9jTgu8vokBr0fcF4lG2wJAVEN0bVhzdBWK5pfyn5YLEsFzkNn0iN0xV1IgYFozY9MkScMEI87ya6iuVbFxvjC8PY6HTd6Usy+Yq7L/QAo2NQJABk10yJ0MpVji39ZjkIYmTpRFZ1mAtHZrv42X2tB3dcvD5o0rp29pkGX8nJZiF47IDOLSIIetfqHFlkxH19S4pQJAINvZg5QNNSnjWYUr/S50CJXaEoG/zVfObpcP71R9qdAGvZYkuqFedzeG6kWHgUVyHgw46fhMNRITKMr4MBifkw==";
        JSONObject json = new JSONObject();
        json=JSONObject.parseObject(request);
        String request1=RSAUtils.encryptByPrivateKey(json.toJSONString(), privateKey);
        String sign=RSAUtils.sign(request1,privateKey);
        return dataSharing(request1,sign);
    }

}
