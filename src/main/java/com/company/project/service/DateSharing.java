package com.company.project.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.project.core.ServiceException;
import com.company.project.dao.DataSharingMapper;
import com.company.project.dao.HnsiAPIMapper;
import com.company.project.dao.PublicMapper;
import com.company.project.model.Ac01;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component//（把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>）

public class DateSharing {
    @Resource
    private Ac01Service ac01Service1;
    @Resource
    private DataSharingMapper dataSharingMapper;
    @Resource
    private PublicMapper publicMapper;

    /**
     * 转出凭证打印校验
     *
     * @param map AAE135 身份证
     *            AAC003  姓名
     * @return
     */
    public JSONObject trade6001(Map<String, Object> map) {
        try {
            map.get("AAE135").toString();
            map.get("AAC003").toString();
        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }

        Long aac001 = queryPerson(map.get("AAE135").toString(), map.get("AAC003").toString());
        List listsd = dataSharingMapper.checkinfosd(aac001);
        if (listsd.size() > 0) {
            throw new ServiceException("2009年之后有双低明细不能做转出");
        }
        List listzd = dataSharingMapper.checkinfozd(aac001);
        if (listzd.size() > 0) {
            throw new ServiceException("非中断状态不能打印转出凭证");
        }
        List listqf = dataSharingMapper.checkinfoqf(aac001);
        if (listqf.size() > 0) {
            throw new ServiceException("养老有欠费不能打印转出凭证");
        }
        List listybqf = dataSharingMapper.checkinfoybqf(aac001);
        if (listybqf.size() > 0) {
            throw new ServiceException("医疗有欠费不能打印转出凭证");
        }

        JSONObject json = new JSONObject();
        json.put("AAC001", aac001);

        return json;

    }

    /**
     * 转出证明打印
     *
     * @param map AAC001
     *            FILETYPE 返回的文件类型 pdf image
     *            PRINTTYPE 打印类型 1 养老 2 医保
     * @return
     */
    public JSONObject trade6002(Map<String, Object> map) {
        try {
            map.get("AAC001").toString();
            map.get("FILETYPE").toString();
            map.get("PRINTTYPE").toString();
        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        Long aac001 = Long.valueOf(map.get("AAC001").toString());
        String fileType = map.get("FILETYPE").toString();//返回的文件类型 pdf image
        String printType = map.get("PRINTTYPE").toString();//打印类型 1 养老 2 医保
        if (!fileType.equals("pdf") && !fileType.equals("image")) {
            throw new ServiceException("FILETYPE 传入有误 请传入 pdf或image");
        }
        if (!printType.equals("1") && !printType.equals("2")) {
            throw new ServiceException("PRINTTYPE 传入有误 请传入 1或2，1 养老 2 医保。");
        }
        String urlstr = "http://10.255.5.100:8399/WebReport/ReportServer?reportlet=/casi/CA00002" + (printType.equals("1") ? "4" : "5") + ".cpt&v_aac001="
                + aac001 + "&format=" + fileType;
        String saveDir = "d://springBoot_tmp";
        String fileName = date + "_" + aac001 + "_turnOutPrint" + "." + (fileType.equals("image") ? "png" : "pdf");
        System.out.printf(aac001 + " " + fileName);
        FileDownloadTool.download(urlstr, saveDir, fileName);//下载文件到本地
        DeleteFileDate(saveDir);//删除非今天的文件
        File file = new File(saveDir + File.separator + fileName);
        String strBase64 = Base64_Coder.getFileBinary(file);
        JSONObject json = new JSONObject();
        json.put("FILEBASE64", strBase64);
        json.put("FILETYPE", fileType);
        return json;
    }


    /**
     * 参保人员信息校验
     *
     * @param map AAE135 身份证
     *            AAC003  姓名
     *            AAE140 险种
     * @return
     */
    public JSONObject trade6003(Map<String, Object> map) {
        try {
            map.get("AAE135").toString();
            map.get("AAC003").toString();
            map.get("AAE140").toString();
        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }
        Long aac001 = queryPerson(map.get("AAE135").toString(), map.get("AAC003").toString());
        String aae140 = map.get("AAE140").toString();
//        if(!aae140.equals("10")&&!aae140.equals("12")){
//            throw new ServiceException("AAE140 传入有误 请传入 10或12，10 职工养老 12城乡居民养老。");
//        }
        Map<String, String> mapSql = new HashMap<>();
        mapSql.put("SQL", "select 1 from ac02 a,ac20 b where a.aaz159=b.aaz159 and a.aae140 in('" + (aae140.equals("29") ? "28','29" : aae140) + "') and aac008='1' and aac031='1' and a.aac001=" + aac001);
        List listaae140 = dataSharingMapper.commQuery(mapSql);
        if (listaae140.size() == 0) {
            throw new ServiceException("该人员未正常参保 险种:" + publicMapper.getCodeValue("AAE140", aae140));
        }
        mapSql.clear();
        mapSql.put("SQL", "select aac001,nvl(AAA029,' ') zjlx,nvl(AAE135,' ') zjh,nvl(aac003,' ') xm,nvl(aac004,' ') xb," +
                "nvl(AAC005,' ') mz,nvl(AAC006,0) csrq ,nvl(aac009,' ') hkxz ,nvl(AAC010,' ') hjdz ," +
                " nvl(AAE147,' ') djd ,nvl(AAB301,'330127') xzqh ,nvl(eab009,' ') xz,nvl(eab030,' ') sq,nvl(EAC101,' ') " +
                "phone,nvl(AAE006,' ') addr from ac01 where aac001=" + aac001);
        List<HashMap<String, Object>> listac01 = dataSharingMapper.commQuery(mapSql);
        HashMap<String, Object> ac01map = new HashMap<String, Object>();
        ac01map = listac01.get(0);
        for (String key : ac01map.keySet()) {
            if (ac01map.get(key).equals(" ")) {
                ac01map.put(key, "");
            }
        }

        JSONObject json = new JSONObject();
        json = JSONObject.parseObject(JSON.toJSONString(ac01map));

        return json;

    }

    /**
     * 更新人员手机及地址
     *
     * @param map aac001
     *            phone 手机号
     *            addr 地址
     * @return
     */
    public JSONObject trade6004(Map<String, Object> map) {
        try {
            map.get("AAC001").toString();
            map.get("PHONE").toString();
            map.get("ADDR").toString();
        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }
        Long aac001 = Long.valueOf(map.get("AAC001").toString());
        Ac01 ac01 = ac01Service1.findBy("aac001", aac001);
        ac01.setEac101(map.get("PHONE").toString());
        ac01.setAae006(map.get("ADDR").toString());
        ac01Service1.update(ac01);
        Map<String, String> mapSql = new HashMap<>();
        mapSql.clear();
        mapSql.put("SQL", "select aac001,nvl(AAA029,' ') zjlx,nvl(AAE135,' ') zjh,nvl(aac003,' ') xm,nvl(aac004,' ') xb," +
                "nvl(AAC005,' ') mz,nvl(AAC006,0) csrq ,nvl(aac009,' ') hkxz ,nvl(AAC010,' ') hjdz ," +
                " nvl(AAE147,' ') djd ,nvl(AAB301,'330127') xzqh ,nvl(eab009,' ') xz,nvl(eab030,' ') sq,nvl(EAC101,' ') " +
                "phone,nvl(AAE006,' ') addr from ac01 where aac001=" + aac001);
        List listac01 = dataSharingMapper.commQuery(mapSql);


        JSONObject json = new JSONObject();
        json = JSONObject.parseObject(JSON.toJSONString(listac01.get(0)));

        return json;

    }

    /**
     * 人员基本信息查询(人员搜索框)
     * @param map
     * personId 人员ID aac001
     * @return
     * @throws IOException
     */
    public JSONObject trade6032(Map<String, Object> map) throws IOException {
        try {
            map.get("personId").toString();

        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }
        HashMap<String, Object> map1=new HashMap<String, Object>();
        map1.putAll(map);
        JSONObject json = LoginInsiis.returnInsiisPost("6032", 0L, "", "操作日志",
                map1);
        return json;
    }

    /**
     * 基本信息校验
     *
     * @param aae135
     * @param aac003
     * @return
     */
    public Long queryPerson(String aae135, String aac003) {
        Condition condition = new Condition(Ac01.class);
        condition.createCriteria().andCondition("aae135 ='" + aae135.trim() + "' and aac003='" + aac003.trim() + "'");
        List<Ac01> ac01 = ac01Service1.findByCondition(condition);
        if (ac01.size() > 1) {
            throw new ServiceException("该人员有多条基本信息，请到社保中心做人员合并业务！");
        } else if (ac01.size() == 0) {
            throw new ServiceException("社保系统没有该人员信息!");
        }
        return ac01.get(0).getAac001();

    }

    /**
     * 删除非今天的文件
     *
     * @param savePlace
     * @return
     */
    public int DeleteFileDate(String savePlace) {
        int number = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        File file = new File(savePlace);
        String[] tempList = file.list();
        File temp = null;
        if (!savePlace.endsWith("/")) {
            savePlace = savePlace + "/";
        }
        for (int i = 0; i < tempList.length; i++) {
            String path = savePlace + tempList[i];
            temp = new File(path);
            if (temp.exists() && !temp.getName().startsWith(date)) {//
                System.out.println(temp.getName());
                temp.delete();
                number++;

            }

        }
        return number;
    }

    /**
     * 将url参数转换成map
     *
     * @param param aa=11&bb=22&cc=33
     * @return
     */
    public HashMap<String, Object> getUrlParams(String param) {
        HashMap<String, Object> map = new HashMap<String, Object>(0);
        if (param.length() == 0) {
            return map;
        }
        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

}
