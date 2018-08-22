package com.company.project.web;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.model.Ac01;
import com.company.project.model.BasicinfoDTO;
import com.company.project.service.*;
import org.apache.axis2.AxisFault;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * author maoxj
 */
@RestController
@RequestMapping("/casiapi")
public class CasiAPIController {
    @Resource
    private Ac01Service ac01Service;
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
    @PostMapping("/getInsuranceProof")
    public Result getInsuranceProof(@RequestParam String sfz) throws IOException {
        Condition condition = new Condition(Ac01.class);
        condition.createCriteria().andCondition("aae135 ='" + sfz + "'");
        List<Ac01> ac01 = ac01Service.findByCondition(condition);
        if (ac01.size() == 0) {
            throw new ServiceException("找不到该人员信息");
        }
        String urlstr = "http://10.255.5.100:8399/WebReport/ReportServer?reportlet=/casi/CA000019.cpt&v_aac001="
                + ac01.get(0).getAac001() + "&format=image&extype=JPG";
        String saveDir = "d://img";
        String fileName = sfz + ".jpg";
        FileDownloadTool.download(urlstr, saveDir, fileName);//下载文件到本地
        File file = new File(saveDir + File.separator + fileName);
        String strBase64 = Base64_Coder.getFileBinary(file);
        // Base64_Coder.base64StringToFile(strBase64,"d://img/333.jpg");
        return ResultGenerator.genSuccessResult(strBase64);
    }
}
