package com.company.project.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.dao.DataSharingMapper;
import com.company.project.dao.HnsiAPIMapper;
import com.company.project.dao.PublicMapper;
import com.company.project.model.Ab51;
import com.company.project.model.Ac01;
import com.company.project.model.Cc76;
import com.company.project.model.Ka27;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import javax.security.sasl.SaslException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component//（把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>）

public class DateSharing {
    @Resource
    private Ac01Service ac01Service1;
    @Resource
    private Ab51Service ab51Service;
    @Resource
    private Ka27Service ka27Service;
    @Resource
    private Cc76Service cc76Service;
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
        if (aac001 == 0l) {
            throw new ServiceException("系统中找不到该人员基本信息");
        }
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
        if (aac001 == 0l) {
            throw new ServiceException("系统中找不到该人员基本信息");
        }
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
     * 城乡居民养老人员参保校验
     *
     * @param map
     * @return
     */
    public JSONObject trade6005(Map<String, Object> map) throws IOException {
        JSONObject json = new JSONObject();
        Map<String, String> mapSql = new HashMap<>();
        try {
            map.get("AAE135").toString();
        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }
        List<HashMap<String, String>> GAinfo = queryGA(map.get("AAE135").toString());
        if (!GAinfo.get(0).get("SSXQ").toString().equals("330127")) {
            throw new ServiceException("非本地户籍不能办理！");
        }
        mapSql.put("SQL", "select aaz289 ZJID, aaa044 ZJMC, eaa007 JFJE\n" +
                "  from aa18\n" +
                " where aae140 = '12'\n" +
                "   and aae100 = '1'\n" +
                "   and to_char(sysdate, 'yyyymm') between aae041 and aae042\n" +
                "   and aaa044 like '%普通%'\n");
        List<HashMap<String, Object>> aa18list = dataSharingMapper.commQuery(mapSql);
        JSONArray aa18array = new JSONArray();
        for (int i = 0; i < aa18list.size(); i++) {
            JSONObject aa18json = new JSONObject();
            aa18json.put("ZJID", aa18list.get(i).get("ZJID").toString());
            aa18json.put("ZJMC", aa18list.get(i).get("ZJMC").toString());
            aa18json.put("JFJE", aa18list.get(i).get("JFJE").toString());
            aa18array.add(aa18json);
        }

        Long aac001 = queryPerson(map.get("AAE135").toString(), null);

        int aac006 = Integer.valueOf(map.get("AAE135").toString().substring(6, 12));//身份证中的出生年月
        Date now = new Date();
        int i_now = Integer.valueOf(DateFormatUtils.format(now, "yyyyMM"));//当前年月
        if (aac006 + 1600 > i_now) {
            throw new ServiceException("城乡居民养老必须年满16周岁");
        }
        if (aac001.equals(0l)) {//系统不存在则取公安数据
            json.put("AAC003", GAinfo.get(0).get("XM").toString());//姓名
            json.put("AAE135", GAinfo.get(0).get("GMSFHM").toString());//身份证
            json.put("XB", GAinfo.get(0).get("XB").toString());//性别
            json.put("MZ", GAinfo.get(0).get("MZ").toString());//民族
            json.put("CSRQ", GAinfo.get(0).get("CSRQ").toString());//出生日期
            json.put("PHONE", "");//手机
            json.put("ADDR", "");//地址
            json.put("AAC001", "0");//人员ID
            json.put("XZ", publicMapper.getCodeAaa105("GA_EAB009", GAinfo.get(0).get("XZJD").toString()));//乡镇
            json.put("SQ", publicMapper.getCodeAaa105("GA_EAB030", GAinfo.get(0).get("JCWH").toString()));//社区/村
            json.put("JFINFO", aa18array);
            json.put("HZSFZ", "");//户主身份证
            json.put("HZGX", "");//户主关系
            return json;
        }


        mapSql.put("SQL", "select 1 from ac02 a,ac20 b where a.aaz159=b.aaz159 and (b.aaz001<>'516168' or " +
                "(b.aaz001='516168' and eac070<>'0')) " +
                "and a.aac001=b.aac001 and aac008='1' and (case when eac070='0' and b.aac031='1' then 1 when eac070<>'0'" +
                " then 1 else 0 end)=1 and a.aae140 in ('10','11') and a.aac001= " + aac001);
        List listcb = dataSharingMapper.commQuery(mapSql);
        if (listcb.size() > 0) {
            throw new ServiceException("该人员办理过职工或机关事业养老！");
        }
        mapSql.clear();
        mapSql.put("SQL", "select 1 from ac02 a,ac20 b where a.aaz159=b.aaz159 and a.aae140='12' and aac008='1' " +
                "and aac031='1' and a.aac001=" + aac001);
        List listjb = dataSharingMapper.commQuery(mapSql);
        if (listjb.size() > 0) {
            throw new ServiceException("该人员已经参保城乡居民养老保险");
        }

        mapSql.clear();
        mapSql.put("SQL", "select aac001,nvl(AAE135,' ') AAE135,nvl(aac003,' ') AAC003,nvl(aac004,' ') xb," +
                "nvl(AAC005,' ') mz,nvl(AAC006,0) csrq ," +
                "nvl(eab009,' ') xz,nvl(eab030,' ') sq,nvl(EAC101,' ') " +
                "phone,nvl(AAE006,' ') addr from ac01 where aac001=" + aac001);
        List listac01 = dataSharingMapper.commQuery(mapSql);
        json = JSONObject.parseObject(JSON.toJSONString(listac01.get(0)));

        mapSql.clear();
        mapSql.put("SQL", "select a.aae135,AAC069 from ab54 a,ab51 b where a.aaz067=b.aaz067 and b.aac001=" + aac001 + " and rownum=1");
        List<HashMap<String, Object>> listab51 = dataSharingMapper.commQuery(mapSql);
        if (listab51.size() > 0) {
            json.put("HZSFZ", listab51.get(0).get("AAE135").toString());//户主身份证
            json.put("HZGX", listab51.get(0).get("AAC069").toString());//户主关系
        } else {
            json.put("HZSFZ", "");//户主身份证
            json.put("HZGX", "");//户主关系
        }

        json.put("JFINFO", aa18array);
        return json;
    }

    /**
     * 城乡居民养老人员参保
     *
     * @param map
     * @return
     * @throws IOException
     */

    public JSONObject trade6006(Map<String, Object> map) throws IOException {
        String PHONE_NUMBER_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";//手机号码正则
        try {
            map.get("AAC001").toString();
            map.get("AAE135").toString();
            map.get("AAC003").toString();
            map.get("ADDR").toString();
            map.get("PHONE").toString();
            map.get("XZ").toString();
            map.get("SQ").toString();
            map.get("BANK").toString();
            map.get("ZJID").toString();
            map.get("HZSFZ");//户主身份证
            map.get("HZGX");//户主关系
        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }
        Long aac001 = Long.valueOf(map.get("AAC001").toString());
        if (map.get("AAC001").toString().equals("0")) {//人员基本信息新增
            Condition condition = new Condition(Ac01.class);
            condition.createCriteria().andCondition("aae135 ='" + map.get("AAE135").toString() + "'");
            List<Ac01> ac01o = ac01Service1.findByCondition(condition);
            if (ac01o.size() > 0) {
                throw new ServiceException("新增人员失败，身份证重复");
            }
            IdCardManageBS bs = new IdCardManageBS();
            if (bs.checkIdCard(map.get("AAE135").toString()).equals("Error")) {//验证身份证
                throw new ServiceException("身份证有误！");
            }
            if (map.get("XZ").toString().equals("") || map.get("SQ").toString().equals("")) {
                throw new ServiceException("乡镇社区不能为空！");
            }
            aac001 = publicMapper.querySequenceByParam("SQ_AAC001");
            Ac01 ac01 = new Ac01();
            ac01.setAaa029("1");
            ac01.setAae147("1");
            ac01.setAae135(map.get("AAE135").toString());
            ac01.setAac004(map.get("XB").toString());
            ac01.setAac003(map.get("AAC003").toString());
            ac01.setAac006(Integer.valueOf(map.get("CSRQ").toString()));
            ac01.setEab009(map.get("XZ").toString());
            ac01.setEab030(map.get("SQ").toString());
            //ac01.setAac007(aac007);//参加工作时间
            String sq = publicMapper.getCodeValue("EAB030", map.get("SQ").toString());
            ac01.setAac009(sq.contains("村") ? "20" : "10");
            ac01.setAac005(map.get("MZ").toString());
            ac01.setAac028("1");
            ac01.setAab301("330127");
            ac01.setEac164((short) 0);
            ac01.setAaz308(0L);
            ac01.setEac001(publicMapper.querySequenceByParam("SQ_EAC001").toString());
            ac01.setAac001(aac001);
            if (map.get("PHONE").toString().matches(PHONE_NUMBER_REG)) {
                ac01.setEac101(map.get("PHONE").toString());
            }
            ac01.setAae006(map.get("ADDR").toString());
            ac01.setAae013("对外接口调用录入 " + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd HH:mm:ss"));
            ac01Service1.save(ac01);
            HashMap<String, Object> mapsync = new HashMap<String, Object>();
            mapsync.put("arg", "1");
            mapsync.put("aac001", aac001);
            mapsync.put("eab009", map.get("XZ").toString());
            mapsync.put("eab030", map.get("SQ").toString());
            mapsync.put("aab301", "330127");
            dataSharingMapper.sync(mapsync);//人员同步到就业系统
            if ((int) mapsync.get("ret") == -1) {
                throw new ServiceException(mapsync.get("msg").toString());
            }
        }

        Map<String, String> mapSql = new HashMap<>();
        ////家庭户关系校验没有的话新增
        mapSql.clear();
        mapSql.put("SQL", "select aaz067 from ab54 where aae135='" + map.get("HZSFZ").toString() + "'");
        List<HashMap<String, Object>> listab54 = dataSharingMapper.commQuery(mapSql);
        if (listab54.size() == 0) {
            throw new ServiceException("找不到户主身份证对应家庭户信息，请到乡镇社保服务中心或行政中心窗口新增家庭户信息!");
        }
        mapSql.clear();
        mapSql.put("SQL", "select aaz067 from ab51 where aac001=" + aac001);
        List<HashMap<String, Object>> listab51 = dataSharingMapper.commQuery(mapSql);
        if (listab51.size() == 0) {
            Ab51 ab51 = new Ab51();
            ab51.setAac001(aac001);
            ab51.setAaz067(Long.valueOf(listab54.get(0).get("AAZ067").toString()));
            ab51.setAac069(map.get("HZGX").toString());
            ab51Service.save(ab51);
        }


        mapSql.clear();
        mapSql.put("SQL", "select d.aaz001, d.aab004\n" +
                "  from ac01 a, aa10 b, ab01 c, ae10 d\n" +
                " where a.eab030 = b.aaa102\n" +
                "   and aaa100 = 'EAB030'\n" +
                "   and b.aaa104 = c.aab001\n" +
                "   and c.aaz001 = d.aaz001\n" +
                "   and b.AAE100 = '1'\n" +
                "   and exists (select 1\n" +
                "          from ab02\n" +
                "         where aaz001 = c.aaz001\n" +
                "           and aae140 = '12'\n" +
                "           and AAB051 = '1')\n" +
                "           and a.aac001 =" + aac001);
        List<HashMap<String, Object>> listaaz001 = dataSharingMapper.commQuery(mapSql);
        String aaz001 = "";
        if (listaaz001.size() == 1) {
            aaz001 = listaaz001.get(0).get("AAZ001").toString();
        } else {
            throw new ServiceException("该人员所在的村/社区不允许参保！");
        }
        Ac01 ac01 = ac01Service1.findBy("aac001", aac001);
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.putAll(map);
        map1.put("aac050", "01");
        map1.put("aae160", "15");
        map1.put("aac012", "91");
        map1.put("aae140", "12");
        map1.put("aac001", aac001);
        map1.put("aaz001", aaz001);
        map1.put("aac016", "00");//人员类别
        if (ac01.getAac009().equals("10") || ac01.getAac009().equals("11")) {
            map1.put("eac066", "70");//参保身份
        } else {
            map1.put("eac066", "71");//参保身份
        }
        String aae030 = DateFormatUtils.format(publicMapper.queryDBdate(), "yyyyMM");
        map1.put("list2Data", "[{\"aaa027\":\"330127\",\"aaa041\":0,\"aaa042\":0,\"aab001\":\"\",\"aab004\":\"\"," +
                "\"aab033\":\"3\",\"aac001\":0,\"aac008\":\"1\",\"aac009\":\"\",\"aac013\":\"\",\"aac016\":\"00\"," +
                "\"aac031\":\"1\",\"aac033\":0,\"aac050\":\"\",\"aae005\":\"\",\"aae030\":\"" + aae030 + "\",\"aae140\":\"12\"," +
                "\"aae180_lower\":0,\"aae180_upper\":0,\"aaz001\":0,\"aaz159\":0,\"aaz289\":" + map.get("ZJID").toString() + ",\"aic001\":0," +
                "\"aic020\":0,\"check\":true,\"eaa018\":\"\",\"eac066\":\"\",\"eac070\":\"0\",\"eac086\":\"1\"," +
                "\"eac101\":\"\",\"eaz132_ac02\":\"1\",\"eaz132_ac20\":\"1\",\"eaz138\":\"\",\"ezd001\":\"00\"," +
                "\"ezd134\":0,\"max_aae003\":0}]");

        map1.put("AAE135", ac01.getAae135());
        trade6005(map1);//保存前再验证一次

        JSONObject json = LoginInsiis.returnInsiisPost("6006", aac001, "0",
                "社保编码:" + ac01.getEac001() + " 姓名:" + ac01.getAac003() + " 身份证:" + ac01.getAae135() + " 村/社区:" + listaaz001.get(0).get("AAB004").toString(),
                map1);//向社保系统发起参保请求

        if (json.get("mainMessage").equals("ok")) {
            json.clear();
            json.put("AAC003", ac01.getAac003());
            json.put("AAE135", ac01.getAae135());
            json.put("mainMessage", "ok");
        } else {
            throw new ServiceException("保存出错！");
        }


        /////////////人员缴费开始/////////////
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("aae140", "12");
        map2.put("aac016", "00");
        map2.put("aaz289", map.get("ZJID").toString());
        map2.put("aac001", aac001);
        map2.put("begin", publicMapper.queryDBYear());
        map2.put("end", publicMapper.queryDBYear());
        map2.put("falg", "1");
        map2.put("ybdtcb", "");
        map2.put("ybtime", "");
        map2.put("aae008", map.get("BANK").toString());
        map2.put("yearData", "");
        json.clear();
        json = LoginInsiis.returnInsiisPost("6006S", aac001, "0",
                "社保编码:" + ac01.getEac001() + " 姓名:" + ac01.getAac003() + " 身份证:" + ac01.getAae135() + " 村/社区:" + listaaz001.get(0).get("AAB004").toString(),
                map2);
        if (json.get("mainMessage").equals("ok")) {
            json.clear();
            json.put("AAC003", ac01.getAac003());
            json.put("AAE135", ac01.getAae135());
            json.put("mainMessage", "ok");
        } else {
            throw new ServiceException("保存出错！");
        }

        //更新手机号码和地址
        if (((map.get("PHONE").toString().length() == 11 && map.get("PHONE").toString().matches(PHONE_NUMBER_REG)) ||
                map.get("ADDR").toString().length() > 5) && !map.get("AAC001").toString().equals("0")) {
            Ac01 ac01update = new Ac01();
            ac01update = ac01Service1.findBy("aac001", aac001);
            if (map.get("ADDR").toString().length() > 5) {
                ac01update.setAae006(map.get("ADDR").toString());
            }
            if ((map.get("PHONE").toString().length() == 11 && map.get("PHONE").toString().matches(PHONE_NUMBER_REG))) {
                ac01update.setEac101(map.get("PHONE").toString());
            }
            ac01Service1.update(ac01update);
        }

        return json;
    }

    /**
     * 城乡居民医疗人员参保校验
     *
     * @param map
     * @return
     */
    public JSONObject trade6007(Map<String, Object> map) throws IOException {
        try {
            map.get("AAE135").toString();
        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }
        List<HashMap<String, String>> GAinfo = queryGA(map.get("AAE135").toString());
        if (!GAinfo.get(0).get("SSXQ").toString().equals("330127")) {
            throw new ServiceException("非本地户籍不能办理！");
        }
        Map<String, String> mapSql = new HashMap<>();
        JSONObject json = new JSONObject();
        Long aac001 = queryPerson(map.get("AAE135").toString(), null);

        ///判断该人员可以选择什么缴费标准
        String sq = publicMapper.getCodeValue("EAB030", publicMapper.getCodeAaa105("GA_EAB030", GAinfo.get(0).get("JCWH").toString()));
        String aae140_s = (sq.contains("村") ? "29" : "28");

        int aac006 = Integer.valueOf(map.get("AAE135").toString().substring(6, 12));//身份证中的出生年月
        Date now = new Date();
        int i_now = Integer.valueOf(DateFormatUtils.format(now, "yyyyMM"));//当前年月
        if (aac006 + 1800 > i_now) {
            aae140_s = aae140_s + "24";//18周岁以前
        } else if (aac006 + 6000 < i_now) {
            aae140_s = aae140_s + "28";//60周岁
        } else {
            aae140_s = aae140_s + "27";//普通人员
        }

        mapSql.put("SQL", "select aaz289 ZJID, aaa044 ZJMC, eaa007 JFJE\n" +
                "  from aa18\n" +
                " where aaz289 = " + aae140_s + "\n" +
                "   and aae100 = '1'\n" +
                "   and to_char(sysdate, 'yyyymm') between aae041 and aae042\n"
        );
        List<HashMap<String, Object>> aa18list = dataSharingMapper.commQuery(mapSql);
        JSONArray aa18array = new JSONArray();
        for (int i = 0; i < aa18list.size(); i++) {
            JSONObject aa18json = new JSONObject();
            aa18json.put("ZJID", aa18list.get(i).get("ZJID").toString());
            aa18json.put("ZJMC", aa18list.get(i).get("ZJMC").toString());
            aa18json.put("JFJE", aa18list.get(i).get("JFJE").toString());
            aa18array.add(aa18json);
        }

        if (aac001.equals(0l)) {//系统不存在则取公安数据
            json.put("AAC003", GAinfo.get(0).get("XM").toString());//姓名
            json.put("AAE135", GAinfo.get(0).get("GMSFHM").toString());//身份证
            json.put("XB", GAinfo.get(0).get("XB").toString());//性别
            json.put("MZ", GAinfo.get(0).get("MZ").toString());//民族
            json.put("CSRQ", GAinfo.get(0).get("CSRQ").toString());//出生日期
            json.put("PHONE", "");//手机
            json.put("ADDR", "");//地址
            json.put("AAC001", "0");//人员ID
            json.put("XZ", publicMapper.getCodeAaa105("GA_EAB009", GAinfo.get(0).get("XZJD").toString()));//乡镇
            json.put("SQ", publicMapper.getCodeAaa105("GA_EAB030", GAinfo.get(0).get("JCWH").toString()));//社区/村
            json.put("JFINFO", aa18array);
            json.put("HZSFZ", "");//户主身份证
            json.put("HZGX", "");//户主关系
            return json;
        }


        mapSql.put("SQL", "select 1 from ac02 a,ac20 b where a.aaz159=b.aaz159  " +
                "and a.aac001=b.aac001 and aac008='1' and aac031='1'" +
                "  and a.aae140 in ('20') and a.aac001= " + aac001);
        List listcb = dataSharingMapper.commQuery(mapSql);
        if (listcb.size() > 0) {
            throw new ServiceException("该人员已办理职工医疗险种！");
        }
        mapSql.clear();
        mapSql.put("SQL", "select 1 from ac02 a,ac20 b where a.aaz159=b.aaz159 and a.aae140 in('28','29') and aac008='1' " +
                "and aac031='1' and a.aac001=" + aac001);
        List listjb = dataSharingMapper.commQuery(mapSql);
        if (listjb.size() > 0) {
            throw new ServiceException("该人员已经参保城乡居民医疗保险");
        }

        mapSql.clear();
        mapSql.put("SQL", "select aac001,nvl(AAE135,' ') AAE135,nvl(aac003,' ') AAC003,nvl(aac004,' ') xb," +
                "nvl(AAC005,' ') mz,nvl(AAC006,0) csrq ," +
                "nvl(eab009,' ') xz,nvl(eab030,' ') sq,nvl(EAC101,' ') " +
                "phone,nvl(AAE006,' ') addr from ac01 where aac001=" + aac001);
        List listac01 = dataSharingMapper.commQuery(mapSql);

        json = JSONObject.parseObject(JSON.toJSONString(listac01.get(0)));

        mapSql.clear();
        mapSql.put("SQL", "select a.aae135,AAC069 from ab54 a,ab51 b where a.aaz067=b.aaz067 and b.aac001=" + aac001 + " and rownum=1");
        List<HashMap<String, Object>> listab51 = dataSharingMapper.commQuery(mapSql);
        if (listab51.size() > 0) {
            json.put("HZSFZ", listab51.get(0).get("AAE135").toString());//户主身份证
            json.put("HZGX", listab51.get(0).get("AAC069").toString());//户主关系
        } else {
            json.put("HZSFZ", "");//户主身份证
            json.put("HZGX", "");//户主关系
        }
        json.put("JFINFO", aa18array);
        return json;
    }

    /**
     * 城乡居民医疗人员参保
     *
     * @param map
     * @return
     * @throws IOException
     */
    public JSONObject trade6008(Map<String, Object> map) throws IOException {

        String PHONE_NUMBER_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";//手机号码正则
        try {
            map.get("AAC001").toString();
            map.get("AAE135").toString();
            map.get("AAC003").toString();
            map.get("ADDR").toString();
            map.get("PHONE").toString();
            map.get("XZ").toString();
            map.get("SQ").toString();
            map.get("BANK").toString();
            map.get("ZJID").toString();

        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }
        Long aac001 = Long.valueOf(map.get("AAC001").toString());
        if (map.get("AAC001").toString().equals("0")) {//人员基本信息新增
            Condition condition = new Condition(Ac01.class);
            condition.createCriteria().andCondition("aae135 ='" + map.get("AAE135").toString() + "'");
            List<Ac01> ac01o = ac01Service1.findByCondition(condition);
            if (ac01o.size() > 0) {
                throw new ServiceException("新增人员失败，身份证重复");
            }
            IdCardManageBS bs = new IdCardManageBS();
            if (bs.checkIdCard(map.get("AAE135").toString()).equals("Error")) {//验证身份证
                throw new ServiceException("身份证有误！");
            }
            if (map.get("XZ").toString().equals("") || map.get("SQ").toString().equals("")) {
                throw new ServiceException("乡镇社区不能为空！");
            }
            aac001 = publicMapper.querySequenceByParam("SQ_AAC001");
            Ac01 ac01 = new Ac01();
            ac01.setAaa029("1");
            ac01.setAae147("1");
            ac01.setAae135(map.get("AAE135").toString());
            ac01.setAac004(map.get("XB").toString());
            ac01.setAac003(map.get("AAC003").toString());
            ac01.setAac006(Integer.valueOf(map.get("CSRQ").toString()));
            ac01.setEab009(map.get("XZ").toString());
            ac01.setEab030(map.get("SQ").toString());
            //ac01.setAac007(aac007);//参加工作时间
            String sq = publicMapper.getCodeValue("EAB030", map.get("SQ").toString());
            ac01.setAac009(sq.contains("村") ? "20" : "10");
            ac01.setAac005(map.get("MZ").toString());
            ac01.setAac028("1");
            ac01.setAab301("330127");
            ac01.setEac164((short) 0);
            ac01.setAaz308(0L);
            ac01.setEac001(publicMapper.querySequenceByParam("SQ_EAC001").toString());
            ac01.setAac001(aac001);
            if (map.get("PHONE").toString().matches(PHONE_NUMBER_REG)) {
                ac01.setEac101(map.get("PHONE").toString());
            }
            ac01.setAae006(map.get("ADDR").toString());
            ac01.setAae013("对外接口调用录入 " + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd HH:mm:ss"));
            ac01Service1.save(ac01);
            HashMap<String, Object> mapsync = new HashMap<String, Object>();
            mapsync.put("arg", "1");
            mapsync.put("aac001", aac001);
            mapsync.put("eab009", map.get("XZ").toString());
            mapsync.put("eab030", map.get("SQ").toString());
            mapsync.put("aab301", "330127");
            dataSharingMapper.sync(mapsync);//人员同步到就业系统
            if ((int) mapsync.get("ret") == -1) {
                throw new ServiceException(mapsync.get("msg").toString());
            }
        }


        Map<String, String> mapSql = new HashMap<>();
        ////家庭户关系校验没有的话新增
        mapSql.clear();
        mapSql.put("SQL", "select aaz067 from ab54 where aae135='" + map.get("HZSFZ").toString() + "'");
        List<HashMap<String, Object>> listab54 = dataSharingMapper.commQuery(mapSql);
        if (listab54.size() == 0) {
            throw new ServiceException("找不到户主身份证对应家庭户信息，请到乡镇社保服务中心或行政中心窗口新增家庭户信息!");
        }
        mapSql.clear();
        mapSql.put("SQL", "select aaz067 from ab51 where aac001=" + aac001);
        List<HashMap<String, Object>> listab51 = dataSharingMapper.commQuery(mapSql);
        if (listab51.size() == 0) {
            Ab51 ab51 = new Ab51();
            ab51.setAac001(aac001);
            ab51.setAaz067(Long.valueOf(listab54.get(0).get("AAZ067").toString()));
            ab51.setAac069(map.get("HZGX").toString());
            ab51Service.save(ab51);
        }

        mapSql.clear();
        mapSql.put("SQL", "select d.aaz001, d.aab004\n" +
                "  from ac01 a, aa10 b, ab01 c, ae10 d\n" +
                " where a.eab030 = b.aaa102\n" +
                "   and aaa100 = 'EAB030'\n" +
                "   and b.aaa104 = c.aab001\n" +
                "   and c.aaz001 = d.aaz001\n" +
                "   and b.AAE100 = '1'\n" +
                "   and exists (select 1\n" +
                "          from ab02\n" +
                "         where aaz001 = c.aaz001\n" +
                "           and aae140 in('28','29')\n" +
                "           and AAB051 = '1')\n" +
                "           and a.aac001 =" + aac001);
        List<HashMap<String, Object>> listaaz001 = dataSharingMapper.commQuery(mapSql);
        String aaz001 = "";
        if (listaaz001.size() == 1) {
            aaz001 = listaaz001.get(0).get("AAZ001").toString();
        } else {
            throw new ServiceException("该人员所在的村/社区不允许参保！");
        }
        Ac01 ac01 = ac01Service1.findBy("aac001", aac001);
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.putAll(map);
        map1.put("aac050", "01");
        map1.put("aae160", "15");
        map1.put("aac012", "91");
        map1.put("aae140", map.get("ZJID").toString().substring(0, 2));
        map1.put("aac001", aac001);
        map1.put("aaz001", aaz001);
        map1.put("aac016", "00");//人员类别
        if (ac01.getAac009().equals("10") || ac01.getAac009().equals("11")) {
            map1.put("eac066", "70");//参保身份
        } else {
            map1.put("eac066", "71");//参保身份
        }
        String aae030 = DateFormatUtils.format(publicMapper.queryDBdate(), "yyyyMM");
        map1.put("list2Data", "[{\"aaa027\":\"330127\",\"aaa041\":0,\"aaa042\":0,\"aab001\":\"\",\"aab004\":\"\"," +
                "\"aab033\":\"3\",\"aac001\":0,\"aac008\":\"1\",\"aac009\":\"\",\"aac013\":\"\",\"aac016\":\"00\"," +
                "\"aac031\":\"1\",\"aac033\":0,\"aac050\":\"\",\"aae005\":\"\",\"aae030\":\"" + aae030 + "\",\"aae140\":\"" + map.get("ZJID").toString().substring(0, 2) + "\"," +
                "\"aae180_lower\":0,\"aae180_upper\":0,\"aaz001\":0,\"aaz159\":0,\"aaz289\":" + map.get("ZJID").toString() + ",\"aic001\":0," +
                "\"aic020\":0,\"check\":true,\"eaa018\":\"\",\"eac066\":\"\",\"eac070\":\"0\",\"eac086\":\"1\"," +
                "\"eac101\":\"\",\"eaz132_ac02\":\"1\",\"eaz132_ac20\":\"1\",\"eaz138\":\"\",\"ezd001\":\"00\"," +
                "\"ezd134\":0,\"max_aae003\":0}]");

        map1.put("AAE135", ac01.getAae135());
        trade6007(map1);//保存前再验证一次

        JSONObject json = LoginInsiis.returnInsiisPost("6008", aac001, "0",
                "社保编码:" + ac01.getEac001() + " 姓名:" + ac01.getAac003() + " 身份证:" + ac01.getAae135() + " 村/社区:" + listaaz001.get(0).get("AAB004").toString(),
                map1);//向社保系统发起参保请求

        if (json.get("mainMessage").equals("ok")) {
            json.clear();
            json.put("AAC003", ac01.getAac003());
            json.put("AAE135", ac01.getAae135());
            json.put("mainMessage", "ok");
        } else {
            throw new ServiceException("保存出错！");
        }


        /////////////人员缴费开始/////////////
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("aae140", map.get("ZJID").toString().substring(0, 2));
        map2.put("aac016", "00");
        map2.put("aaz289", map.get("ZJID").toString().substring(0, 2) + "1" + map.get("ZJID").toString().substring(3, 4));
        map2.put("aac001", aac001);
        map2.put("begin", publicMapper.queryDBYear());
        map2.put("end", publicMapper.queryDBYear());
        map2.put("falg", "1");
        map2.put("ybdtcb", "0");
        map2.put("ybtime", DateFormatUtils.format(publicMapper.queryDBdate(), "yyyyMMdd"));
        map2.put("aae008", map.get("BANK").toString());
        map2.put("yearData", "");
        json.clear();
        json = LoginInsiis.returnInsiisPost("6008S", aac001, "0",
                "社保编码:" + ac01.getEac001() + " 姓名:" + ac01.getAac003() + " 身份证:" + ac01.getAae135() + " 村/社区:" + listaaz001.get(0).get("AAB004").toString(),
                map2);
        if (json.get("mainMessage").equals("ok")) {
            json.clear();
            json.put("AAC003", ac01.getAac003());
            json.put("AAE135", ac01.getAae135());
            json.put("mainMessage", "ok");
        } else {
            throw new ServiceException("保存出错！");
        }

        //更新手机号码和地址
        if (((map.get("PHONE").toString().length() == 11 && map.get("PHONE").toString().matches(PHONE_NUMBER_REG)) ||
                map.get("ADDR").toString().length() > 5) && !map.get("AAC001").toString().equals("0")) {
            Ac01 ac01update = new Ac01();
            ac01update = ac01Service1.findBy("aac001", aac001);
            if (map.get("ADDR").toString().length() > 5) {
                ac01update.setAae006(map.get("ADDR").toString());
            }
            if ((map.get("PHONE").toString().length() == 11 && map.get("PHONE").toString().matches(PHONE_NUMBER_REG))) {
                ac01update.setEac101(map.get("PHONE").toString());
            }
            ac01Service1.update(ac01update);
        }

        return json;
    }


    /**
     * 灵活就业参保校验
     *
     * @param map
     * @return
     */
    public JSONObject trade6009(Map<String, Object> map) throws IOException {
        try {
            map.get("AAE135").toString();
        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }
        List<HashMap<String, String>> GAinfo = queryGA(map.get("AAE135").toString());
        if (!GAinfo.get(0).get("SSXQ").toString().equals("330127")) {
            throw new ServiceException("非本地户籍不能办理！");
        }
        Map<String, String> mapSql = new HashMap<>();
        JSONObject json = new JSONObject();
        Long aac001 = queryPerson(map.get("AAE135").toString(), null);


        int aac006 = Integer.valueOf(map.get("AAE135").toString().substring(6, 12));//身份证中的出生年月
        Date now = new Date();
        int i_now = Integer.valueOf(DateFormatUtils.format(now, "yyyyMM"));//当前年月
        if (aac006 + 1600 > i_now) {
            throw new ServiceException("16周岁以下不能参保");//16周岁以前
        } else if (aac006 + 6000 < i_now) {
            throw new ServiceException("60周岁以上不能参保");//60周岁
        } else if (aac006 + 5000 < i_now && GAinfo.get(0).get("XB").toString().equals("2")) {
            throw new ServiceException("女性50周岁以上不能参保");//普通人员
        }
        mapSql.clear();
        mapSql.put("SQL", "SELECT round((select max(eaa023)\n" +
                "                from aa02\n" +
                "               where eaa022 = '941'\n" +
                "                 and to_char(sysdate, 'yyyymm') >= eaa021\n" +
                "                 and eaa021 =\n" +
                "                     (select max(eaa021)\n" +
                "                        from aa02\n" +
                "                       where eaa022 = '941'\n" +
                "                         and to_char(sysdate, 'yyyymm') >= eaa021)) * 0.8) yl80,\n" +
                "       round((select max(eaa023)\n" +
                "                from aa02\n" +
                "               where eaa022 = '941'\n" +
                "                 and to_char(sysdate, 'yyyymm') >= eaa021\n" +
                "                 and eaa021 =\n" +
                "                     (select max(eaa021)\n" +
                "                        from aa02\n" +
                "                       where eaa022 = '941'\n" +
                "                         and to_char(sysdate, 'yyyymm') >= eaa021)) * 1) yl100,\n" +
                "       round((select max(eaa023)\n" +
                "                from aa02\n" +
                "               where eaa022 = '941'\n" +
                "                 and to_char(sysdate, 'yyyymm') >= eaa021\n" +
                "                 and eaa021 =\n" +
                "                     (select max(eaa021)\n" +
                "                        from aa02\n" +
                "                       where eaa022 = '941'\n" +
                "                         and to_char(sysdate, 'yyyymm') >= eaa021)) * 2) yl200,\n" +
                "       round((select max(eaa023)\n" +
                "                from aa02\n" +
                "               where eaa022 = '941'\n" +
                "                 and to_char(sysdate, 'yyyymm') >= eaa021\n" +
                "                 and eaa021 =\n" +
                "                     (select max(eaa021)\n" +
                "                        from aa02\n" +
                "                       where eaa022 = '941'\n" +
                "                         and to_char(sysdate, 'yyyymm') >= eaa021)) * 3) yl300\n" +
                "  from dual\n");
        List<HashMap<String, Object>> listzjje = dataSharingMapper.commQuery(mapSql);

        JSONArray aa05array = new JSONArray();
        JSONObject aa05json1 = new JSONObject();
        aa05json1.put("ZJID", "1003");
        aa05json1.put("ZJMC", "个体养老80%");
        aa05json1.put("JFJS", listzjje.get(0).get("YL80").toString());
        aa05array.add(aa05json1);
        JSONObject aa05json2 = new JSONObject();
        aa05json2.put("ZJID", "1004");
        aa05json2.put("ZJMC", "个体养老100%");
        aa05json2.put("JFJS", listzjje.get(0).get("YL100").toString());
        aa05array.add(aa05json2);
        JSONObject aa05json3 = new JSONObject();
        aa05json3.put("ZJID", "1005");
        aa05json3.put("ZJMC", "个体养老200%");
        aa05json3.put("JFJS", listzjje.get(0).get("YL200").toString());
        aa05array.add(aa05json3);
        JSONObject aa05json4 = new JSONObject();
        aa05json4.put("ZJID", "1006");
        aa05json4.put("ZJMC", "个体养老300%");
        aa05json4.put("JFJS", listzjje.get(0).get("YL300").toString());
        aa05array.add(aa05json4);

        if (aac001.equals(0l)) {//系统不存在则取公安数据
            json.put("AAC003", GAinfo.get(0).get("XM").toString());//姓名
            json.put("AAE135", GAinfo.get(0).get("GMSFHM").toString());//身份证
            json.put("XB", GAinfo.get(0).get("XB").toString());//性别
            json.put("MZ", GAinfo.get(0).get("MZ").toString());//民族
            json.put("CSRQ", GAinfo.get(0).get("CSRQ").toString());//出生日期
            json.put("PHONE", "");//手机
            json.put("ADDR", "");//地址
            json.put("AAC001", "0");//人员ID
            json.put("JFINFO", aa05array);
            json.put("XZ", publicMapper.getCodeAaa105("GA_EAB009", GAinfo.get(0).get("XZJD").toString()));//乡镇
            json.put("SQ", publicMapper.getCodeAaa105("GA_EAB030", GAinfo.get(0).get("JCWH").toString()));//社区/村
            return json;
        }


        mapSql.put("SQL", "select 1 from ac02 a,ac20 b where a.aaz159=b.aaz159  " +
                "and a.aac001=b.aac001 and aac008='1' and aac031='1'" +
                "  and a.aae140 in ('20','10','11','21') and a.aac001= " + aac001);
        List listcb = dataSharingMapper.commQuery(mapSql);
        if (listcb.size() > 0) {
            throw new ServiceException("该人员已办理职工险种！");
        }


        mapSql.clear();
        mapSql.put("SQL", "select aac001,nvl(AAE135,' ') AAE135,nvl(aac003,' ') AAC003,nvl(aac004,' ') xb," +
                "nvl(AAC005,' ') mz,nvl(AAC006,0) csrq ," +
                "nvl(eab009,' ') xz,nvl(eab030,' ') sq,nvl(EAC101,' ') " +
                "phone,nvl(AAE006,' ') addr from ac01 where aac001=" + aac001);
        List listac01 = dataSharingMapper.commQuery(mapSql);

        json = JSONObject.parseObject(JSON.toJSONString(listac01.get(0)));

        json.put("JFINFO", aa05array);
        return json;
    }


    /**
     * 灵活就业人员参保
     *
     * @param map
     * @return
     * @throws IOException
     */
    public JSONObject trade6010(Map<String, Object> map) throws IOException {

        String PHONE_NUMBER_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";//手机号码正则
        try {
            map.get("AAC001").toString();
            map.get("AAE135").toString();
            map.get("AAC003").toString();
            map.get("ADDR").toString();
            map.get("PHONE").toString();
            map.get("XZ").toString();
            map.get("SQ").toString();
            map.get("JFJS").toString();//养老缴费基数
            map.get("ZJID").toString();//养老缴费ID
        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }
        Long aac001 = Long.valueOf(map.get("AAC001").toString());
        if (map.get("AAC001").toString().equals("0")) {//人员基本信息新增
            Condition condition = new Condition(Ac01.class);
            condition.createCriteria().andCondition("aae135 ='" + map.get("AAE135").toString() + "'");
            List<Ac01> ac01o = ac01Service1.findByCondition(condition);
            if (ac01o.size() > 0) {
                throw new ServiceException("新增人员失败，身份证重复");
            }
            IdCardManageBS bs = new IdCardManageBS();
            if (bs.checkIdCard(map.get("AAE135").toString()).equals("Error")) {//验证身份证
                throw new ServiceException("身份证有误！");
            }
            if (map.get("XZ").toString().equals("") || map.get("SQ").toString().equals("")) {
                throw new ServiceException("乡镇社区不能为空！");
            }
            aac001 = publicMapper.querySequenceByParam("SQ_AAC001");
            Ac01 ac01 = new Ac01();
            ac01.setAaa029("1");
            ac01.setAae147("1");
            ac01.setAae135(map.get("AAE135").toString());
            ac01.setAac004(map.get("XB").toString());
            ac01.setAac003(map.get("AAC003").toString());
            ac01.setAac006(Integer.valueOf(map.get("CSRQ").toString()));
            ac01.setEab009(map.get("XZ").toString());
            ac01.setEab030(map.get("SQ").toString());
            //ac01.setAac007(aac007);//参加工作时间
            String sq = publicMapper.getCodeValue("EAB030", map.get("SQ").toString());
            ac01.setAac009(sq.contains("村") ? "20" : "10");
            ac01.setAac005(map.get("MZ").toString());
            ac01.setAac028("1");
            ac01.setAab301("330127");
            ac01.setEac164((short) 0);
            ac01.setAaz308(0L);
            ac01.setEac001(publicMapper.querySequenceByParam("SQ_EAC001").toString());
            ac01.setAac001(aac001);
            if (map.get("PHONE").toString().matches(PHONE_NUMBER_REG)) {
                ac01.setEac101(map.get("PHONE").toString());
            }
            ac01.setAae006(map.get("ADDR").toString());
            ac01.setAae013("对外接口调用录入 " + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd HH:mm:ss"));
            ac01Service1.save(ac01);
            HashMap<String, Object> mapsync = new HashMap<String, Object>();
            mapsync.put("arg", "1");
            mapsync.put("aac001", aac001);
            mapsync.put("eab009", map.get("XZ").toString());
            mapsync.put("eab030", map.get("SQ").toString());
            mapsync.put("aab301", "330127");
            dataSharingMapper.sync(mapsync);//人员同步到就业系统
            if ((int) mapsync.get("ret") == -1) {
                throw new ServiceException(mapsync.get("msg").toString());
            }
        }


        Map<String, String> mapSql = new HashMap<>();
        String aae030 = DateFormatUtils.format(publicMapper.queryDBdate(), "yyyyMM");
        String aac007 = DateFormatUtils.format(publicMapper.queryDBdate(), "yyyyMMdd");
        Ac01 ac01 = ac01Service1.findBy("aac001", aac001);
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.putAll(map);
        map1.put("aac001", aac001);
        map1.put("aaz001", 516168);
        map1.put("aac050", "01");//?
        map1.put("aae160", "10");//?
        map1.put("aae035", aae030);
        map1.put("aac009", ac01.getAac009());
        map1.put("aic020xz", "2");
        map1.put("aac012", "51");
        map1.put("eac066", "50");
        map1.put("aac007", aac007);
        map1.put("eac101", map.get("PHONE").toString());
        map1.put("aae006", map.get("ADDR").toString());

        mapSql.clear();
        mapSql.put("SQL", "select aae140 from ac02  where aae140 in('10','20','21') and aac001=" + aac001);
        List listac02 = dataSharingMapper.commQuery(mapSql);
        if (listac02.size() > 0) {
            map1.put("aac050", "02");//续保
            map1.put("aae160", "21");//调入再参保
        }
        String qyaic020 = "";
        String ybaic020 = "";
        mapSql.clear();
        mapSql.put("SQL", "select round((select max(eaa023)\n" +
                "                      from aa02\n" +
                "                     where eaa022 = '941'\n" +
                "                       and to_char(sysdate, 'yyyymm') >= eaa021\n" +
                "                       and eaa021 =\n" +
                "                           (select max(eaa021)\n" +
                "                              from aa02\n" +
                "                             where eaa022 = '941'\n" +
                "                               and to_char(sysdate, 'yyyymm') >= eaa021)) * 0.8) qy,(select max(eaa023)\n" +
                "                      from aa02\n" +
                "                     where eaa022 = '941'\n" +
                "                       and to_char(sysdate, 'yyyymm') >= eaa021\n" +
                "                       and eaa021 =\n" +
                "                           (select max(eaa021)\n" +
                "                              from aa02\n" +
                "                             where eaa022 = '941'\n" +
                "                               and to_char(sysdate, 'yyyymm') >= eaa021)) yb from dual ");
        List<HashMap<String, Object>> listaic020 = dataSharingMapper.commQuery(mapSql);
        qyaic020 = map.get("JFJS").toString();
        ybaic020 = listaic020.get(0).get("YB").toString();

        map1.put("list2Data", "[{\"aaa027\":\"330127\",\"aaa041\":0.08,\"aaa042\":0.1,\"aab001\":\"\",\"aab004\":\"\"," +
                "\"aab033\":\"2\",\"aac001\":0,\"aac008\":\"1\",\"aac009\":\"\",\"aac013\":\"\",\"aac016\":\"\",\"aac031\":\"1\"," +
                "\"aac033\":0,\"aac050\":\"\",\"aae005\":\"\",\"aae030\":\"" + aae030 + "\",\"aae140\":\"10\",\"aae180_lower\":" + qyaic020 + "," +
                "\"aae180_upper\":" + qyaic020 + ",\"aaz001\":0,\"aaz159\":0,\"aaz289\":1003,\"aic001\":0,\"aic020\":" + qyaic020 + ",\"check\":true," +
                "\"eaa018\":\"0\",\"eac066\":\"\",\"eac070\":\"0\",\"eac086\":\"1\",\"eac101\":\"\",\"eaz132_ac02\":\"1\"," +
                "\"eaz132_ac20\":\"1\",\"eaz138\":\"\",\"ezd001\":\"00\",\"ezd134\":0,\"max_aae003\":0},{\"aaa027\":\"330127\"," +
                "\"aaa041\":0.05,\"aaa042\":0,\"aab001\":\"\",\"aab004\":\"\",\"aab033\":\"2\",\"aac001\":0,\"aac008\":\"1\"," +
                "\"aac009\":\"\",\"aac013\":\"\",\"aac016\":\"\",\"aac031\":\"1\",\"aac033\":0,\"aac050\":\"\",\"aae005\":\"\"," +
                "\"aae030\":\"" + aae030 + "\",\"aae140\":\"20\",\"aae180_lower\":" + ybaic020 + ",\"aae180_upper\":" + ybaic020 + ",\"aaz001\":0,\"aaz159\":0," +
                "\"aaz289\":2005,\"aic001\":0,\"aic020\":" + ybaic020 + ",\"check\":true,\"eaa018\":\"0\",\"eac066\":\"\",\"eac070\":\"0\"," +
                "\"eac086\":\"1\",\"eac101\":\"\",\"eaz132_ac02\":\"1\",\"eaz132_ac20\":\"1\",\"eaz138\":\"\",\"ezd001\":\"00\"," +
                "\"ezd134\":0,\"max_aae003\":0},{\"aaa027\":\"330127\",\"aaa041\":5,\"aaa042\":0,\"aab001\":\"\",\"aab004\":\"\"," +
                "\"aab033\":\"2\",\"aac001\":0,\"aac008\":\"1\",\"aac009\":\"\",\"aac013\":\"\",\"aac016\":\"\",\"aac031\":\"1\"," +
                "\"aac033\":0,\"aac050\":\"\",\"aae005\":\"\",\"aae030\":\"" + aae030 + "\",\"aae140\":\"21\",\"aae180_lower\":" + ybaic020 + "," +
                "\"aae180_upper\":" + ybaic020 + ",\"aaz001\":0,\"aaz159\":0,\"aaz289\":2101,\"aic001\":0,\"aic020\":" + ybaic020 + ",\"check\":true," +
                "\"eaa018\":\"0\",\"eac066\":\"\",\"eac070\":\"0\",\"eac086\":\"0\",\"eac101\":\"\",\"eaz132_ac02\":\"1\"," +
                "\"eaz132_ac20\":\"1\",\"eaz138\":\"\",\"ezd001\":\"00\",\"ezd134\":0,\"max_aae003\":0}]");

        map1.put("AAE135", ac01.getAae135());
        trade6009(map1);//保存前再验证一次

        JSONObject json = LoginInsiis.returnInsiisPost("6010", aac001, "0",
                "社保编码:" + ac01.getEac001() + " 姓名:" + ac01.getAac003() + " 身份证:" + ac01.getAae135() + " 单位:县托管中心",
                map1);//向社保系统发起参保请求

        if (json.get("mainMessage").equals("ok")) {
            json.clear();
            json.put("AAC003", ac01.getAac003());
            json.put("AAE135", ac01.getAae135());
            json.put("mainMessage", "ok");
        } else {
            throw new ServiceException("保存出错！");
        }


        //更新手机号码和地址
        if (((map.get("PHONE").toString().length() == 11 && map.get("PHONE").toString().matches(PHONE_NUMBER_REG)) ||
                map.get("ADDR").toString().length() > 5) && !map.get("AAC001").toString().equals("0")) {
            Ac01 ac01update = new Ac01();
            ac01update = ac01Service1.findBy("aac001", aac001);
            if (map.get("ADDR").toString().length() > 5) {
                ac01update.setAae006(map.get("ADDR").toString());
            }
            if ((map.get("PHONE").toString().length() == 11 && map.get("PHONE").toString().matches(PHONE_NUMBER_REG))) {
                ac01update.setEac101(map.get("PHONE").toString());
            }
            ac01Service1.update(ac01update);
        }

        return json;
    }


    /**
     * 医保家庭共济校验
     *
     * @param map
     * @return
     */
    public JSONObject trade6011(Map<String, Object> map) throws IOException {
        try {
            map.get("AAE135").toString();//授权人
            map.get("AAE135_S").toString();//被授权人
        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }

        Long aac001 = queryPerson(map.get("AAE135").toString(), null);
        Long aac001_s = queryPerson(map.get("AAE135_S").toString(), null);
        if (aac001.equals(0L) || aac001_s.equals(0L)) {
            throw new ServiceException("人员基本信息不存在!");
        }


        Map<String, String> mapSql = new HashMap<>();
        mapSql.clear();
        mapSql.put("SQL", "select 1 from kc51 where aac001=" + aac001 + " and aae140='20' and aae100='1' and " +
                "to_char(sysdate,'yyyymmdd') >=aae030 and (to_char(sysdate,'yyyymmdd')<=aae031 or aae031 is null)");
        List listkc51 = dataSharingMapper.commQuery(mapSql);
        if (listkc51.size() == 0) {
            throw new ServiceException("授权人职工医保未正常享受!");
        }
        mapSql.clear();
        mapSql.put("SQL", "select 1 from kc51 where aac001=" + aac001_s + "  and aae100='1' and " +
                "to_char(sysdate,'yyyymmdd') >=aae030 and (to_char(sysdate,'yyyymmdd')<=aae031 or aae031 is null)");
        List listkc51_s = dataSharingMapper.commQuery(mapSql);
        if (listkc51_s.size() == 0) {
            throw new ServiceException("被授权人医保未正常享受!");
        }
        mapSql.clear();
        mapSql.put("SQL", "select 1 from ka27 where aac001=" + aac001_s + "  and aae100='1' and " +
                "to_char(sysdate,'yyyymmdd') >=aae030 and (to_char(sysdate,'yyyymmdd')<=aae031 or aae031 is null)");
        List listka27 = dataSharingMapper.commQuery(mapSql);
        if (listka27.size() > 0) {
            throw new ServiceException("被授权人已经有绑定记录!");
        }
        Ac01 ac01 = new Ac01();
        ac01 = ac01Service1.findBy("aac001", aac001);
        Ac01 ac01_s = new Ac01();
        ac01_s = ac01Service1.findBy("aac001", aac001_s);


        JSONObject json = new JSONObject();
        json.put("AAE135", ac01.getAae135());
        json.put("AAC003", ac01.getAac003());
        json.put("AAC001", ac01.getAac001());
        json.put("AAE135_S", ac01_s.getAae135());
        json.put("AAC003_S", ac01_s.getAac003());
        json.put("AAC001_S", ac01_s.getAac001());
        return json;
    }

    /**
     * 家庭共济
     *
     * @param map
     * @return
     * @throws IOException
     */
    public JSONObject trade6012(Map<String, Object> map) throws IOException {
        try {
            map.get("AAE135").toString();//授权人
            map.get("AAE135_S").toString();//被授权人
            map.get("AAC001").toString();//授权人ID
            map.get("AAC001_S").toString();//被授权人ID
            map.get("AKA012").toString();//两者的关系（0：父母；1：配偶；2：子女）
        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.putAll(map);
        trade6011(map1);
        Ac01 ac01 = new Ac01();
        ac01 = ac01Service1.findBy("aac001", Long.valueOf(map.get("AAC001").toString()));
        Ac01 ac01_s = new Ac01();
        ac01_s = ac01Service1.findBy("aac001", Long.valueOf(map.get("AAC001_S").toString()));
        Ka27 ka27 = new Ka27();
        ka27.setAac001(ac01_s.getAac001());
        ka27.setAac001S(ac01.getAac001());
        ka27.setAka027(publicMapper.querySequenceByParam("ssim.sq_aka027"));
        ka27.setAka012(map.get("AKA012").toString());
        ka27.setAae100("1");
        ka27.setAae030(Integer.valueOf(DateFormatUtils.format(publicMapper.queryDBdate(), "yyyyMMdd")));
        ka27.setAae031(29991231);
        ka27.setAae036(publicMapper.queryDBdate());
        ka27.setAae135(ac01_s.getAae135());
        ka27.setAae135S(ac01.getAae135());
        ka27.setAac003(ac01_s.getAac003());
        ka27.setAac003S(ac01.getAac003());
        ka27Service.save(ka27);
        JSONObject json = new JSONObject();
        json.put("AAE135", ac01.getAae135());
        json.put("AAC003", ac01.getAac003());
        json.put("AAC001", ac01.getAac001());
        json.put("AAE135_S", ac01_s.getAae135());
        json.put("AAC003_S", ac01_s.getAac003());
        json.put("AAC001_S", ac01_s.getAac001());
        return json;
    }

    /**
     * 常住异地备案校验
     *
     * @param map
     * @return
     */
    public JSONObject trade6013(Map<String, Object> map) throws Exception {
        try {
            map.get("AAE135").toString();//身份证

        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }
        Long aac001 = queryPerson(map.get("AAE135").toString(), null);
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.putAll(map);
        map1.put("_ODA_TRANSMIT_OBJECT", "{\"chrDTO\":{\"aac001\":" + aac001 + ",\"aka083\":\"31\",\"flaglbf\":\"\"}}");
        JSONObject json = LoginInsiis.returnInsiisGet("6013", map1);
        if (!json.get("mainMessage").toString().equals("ok")) {
            throw new ServiceException("社保系统返回出错！");
        }
        json = JSONObject.parseObject(json.get("data").toString());
        if (json.get("flag").toString().equals("-1")) {
            throw new ServiceException("该人员当前时间不享受医疗待遇！");
        }

        json.clear();
        json.put("AAC001", aac001);
        json.put("AAE135", map.get("AAE135").toString());

        return json;
    }

    /**
     * 常住异地备案
     *
     * @param map
     * @return
     * @throws IOException
     */
    public JSONObject trade6014(Map<String, Object> map) throws IOException {
        String PHONE_NUMBER_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";//手机号码正则
        try {
            map.get("AAC001").toString();
            map.get("AAE135").toString();
            map.get("JSXM").toString();//家属姓名
            map.get("STARTDATE").toString();//开始时间 yyyymmdd
            map.get("PHONE").toString();//联系手机
            map.get("CITY").toString();//市
            map.get("PROVINCE").toString();//省
            map.get("ADDR").toString();//地址
            map.get("JSGX").toString();//与本人关系
            map.get("DDJG1").toString();//定点机构1
        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("akb021_1", "");
        map1.put("akb0201", "");
        map1.put("akb021_2", "");
        map1.put("akb0202", "");
        map1.put("akb021_3", "");
        map1.put("akb0203", "");
        map1.put("akb021_4", "");
        map1.put("akb0204", "");
        Map<String, String> mapSql = new HashMap<>();
        mapSql.clear();
        mapSql.put("SQL", "select aae044,akb020 from kb01  where aae100='1' and  aae044 like'%" + map.get("DDJG1").toString() + "%'");
        List<HashMap<String, Object>> listkb011 = dataSharingMapper.commQuery(mapSql);
        if (listkb011.size() != 1) {
            int j = 0;
            for (int i = 0; i < listkb011.size(); i++) {
                if (listkb011.get(i).get("AAE044").toString().equals(map.get("DDJG1").toString())) {
                    map1.put("akb021_1", listkb011.get(i).get("AAE044").toString());
                    map1.put("akb0201", listkb011.get(i).get("AKB020").toString());
                    j++;
                }
            }
            if (j == 0) {
                throw new ServiceException("对不起系统中找不到" + map.get("DDJG1").toString() + "对应的医疗机构,请尽量完整的输入机构名称！");
            }
        } else {
            map1.put("akb021_1", listkb011.get(0).get("AAE044").toString());
            map1.put("akb0201", listkb011.get(0).get("AKB020").toString());
        }

        if (map.get("DDJG2") != null && !map.get("DDJG2").toString().isEmpty()) {
            mapSql.clear();
            mapSql.put("SQL", "select aae044,akb020 from kb01  where aae100='1' and  aae044 like'%" + map.get("DDJG2").toString() + "%'");
            List<HashMap<String, Object>> listkb012 = dataSharingMapper.commQuery(mapSql);
            if (listkb012.size() != 1) {
                int j = 0;
                for (int i = 0; i < listkb012.size(); i++) {
                    if (listkb012.get(i).get("AAE044").toString().equals(map.get("DDJG2").toString())) {
                        map1.put("akb021_2", listkb012.get(i).get("AAE044").toString());
                        map1.put("akb0202", listkb012.get(i).get("AKB020").toString());
                        j++;
                    }
                }
                if (j == 0) {
                    throw new ServiceException("对不起系统中找不到" + map.get("DDJG2").toString() + "对应的医疗机构,请尽量完整的输入机构名称！");
                }
            } else {
                map1.put("akb021_2", listkb012.get(0).get("AAE044").toString());
                map1.put("akb0202", listkb012.get(0).get("AKB020").toString());
            }
        }
        if (map.get("DDJG3") != null && !map.get("DDJG3").toString().isEmpty()) {
            mapSql.clear();
            mapSql.put("SQL", "select aae044,akb020 from kb01  where aae100='1' and  aae044 like'%" + map.get("DDJG3").toString() + "%'");
            List<HashMap<String, Object>> listkb013 = dataSharingMapper.commQuery(mapSql);
            if (listkb013.size() != 1) {
                int j = 0;
                for (int i = 0; i < listkb013.size(); i++) {
                    if (listkb013.get(i).get("AAE044").toString().equals(map.get("DDJG3").toString())) {
                        map1.put("akb021_3", listkb013.get(i).get("AAE044").toString());
                        map1.put("akb0203", listkb013.get(i).get("AKB020").toString());
                        j++;
                    }
                }
                if (j == 0) {
                    throw new ServiceException("对不起系统中找不到" + map.get("DDJG3").toString() + "对应的医疗机构,请尽量完整的输入机构名称！");
                }
            } else {
                map1.put("akb021_3", listkb013.get(0).get("AAE044").toString());
                map1.put("akb0203", listkb013.get(0).get("AKB020").toString());
            }
        }
        mapSql.clear();
        mapSql.put("SQL", "select 1 from kc41 t where aka083='31' and '" + map.get("STARTDATE").toString() + "' <= aae031 and aac001=" + map.get("AAC001").toString());
        List listkc41 = dataSharingMapper.commQuery(mapSql);
        if (listkc41.size() > 0) {
            throw new ServiceException("此段时间已存在登记记录");
        }
        String now_date = DateFormatUtils.format(publicMapper.queryDBdate(), "yyyyMM");
        if (Long.valueOf(map.get("STARTDATE").toString()) <= Long.valueOf(now_date + "31")) {
            throw new ServiceException("开始时间必须大于当前月！");
        }
        map1.put("aae030", map.get("STARTDATE").toString().substring(0,4)+"-"+map.get("STARTDATE").toString().substring(4,6)
                +"-"+map.get("STARTDATE").toString().substring(6,8));
        if( map.get("ENDDATE")!=null&&!map.get("ENDDATE").toString().equals("")) {
            map1.put("aae031", map.get("ENDDATE").toString().substring(0,4)+"-"+map.get("ENDDATE").toString().substring(4,6)
                    +"-"+map.get("ENDDATE").toString().substring(6,8));
        }
        if (map.get("AAE007") != null) {
            map1.put("aae007", map.get("AAE007").toString());
        }
        map1.put("eae055", map.get("PROVINCE").toString());//省
        map1.put("aaa170", map.get("CITY").toString());//市
        map1.put("aae006", map.get("ADDR").toString());//地址
        map1.put("ake004_1", map.get("JSXM").toString());//家属姓名
        map1.put("ake005_1", map.get("PHONE").toString());//手机
        map1.put("gx1", map.get("JSGX").toString());//关系
        map1.put("aka083", "31");//
        map1.put("aac001", map.get("AAC001").toString());//
        map1.put("flag", "2");//
        map1.put("ake004_2", "");//家属姓名
        map1.put("ake005_2", "");//手机
        map1.put("ake004_3", "");//家属姓名
        map1.put("ake005_3", "");//手机
        Ac01 ac01 = new Ac01();
        ac01 = ac01Service1.findBy("aac001", Long.valueOf(map.get("AAC001").toString()));
        JSONObject json = LoginInsiis.returnInsiisPost("6014", Long.valueOf(map.get("AAC001").toString()), "0",
                "姓名:" + ac01.getAac003() + " 身份证:" + map.get("AAE135").toString() + " 开始时间:" + map.get("STARTDATE").toString(),
                map1);//向社保系统发起参保请求

        if (json.get("mainMessage").equals("ok")) {
            json.clear();
            json.put("AAC003", ac01.getAac003());
            json.put("AAE135", ac01.getAae135());
            json.put("mainMessage", "申请成功，等待中心审核!");
        } else {
            throw new ServiceException("保存出错！" + json.get("mainMessage").toString());
        }


        return json;
    }


    /**
     * 参保登记表打印
     *
     * @param map AAC001
     *            FILETYPE 返回的文件类型 pdf image
     *            PRINTTYPE 打印类型 1 灵活就业（个体）参保 2 城乡医保 3城乡养老
     * @return
     */
    public JSONObject trade6015(Map<String, Object> map) {
        try {
            map.get("AAE135").toString();
            map.get("FILETYPE").toString();
            map.get("PRINTTYPE").toString();
        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        String aae135 = map.get("AAE135").toString();
        String fileType = map.get("FILETYPE").toString();//返回的文件类型 pdf image
        String printType = map.get("PRINTTYPE").toString();//打印类型 1 灵活就业（个体）参保 2 城乡医保 3城乡养老
        if (!fileType.equals("pdf") && !fileType.equals("image")) {
            throw new ServiceException("FILETYPE 传入有误 请传入 pdf或image");
        }
        if (!printType.equals("1") && !printType.equals("2")&& !printType.equals("3")) {
            throw new ServiceException("PRINTTYPE 传入有误 请传入 1 灵活就业（个体）参保 2 城乡医保 3城乡养老");
        }
        String urlstr = "http://10.255.5.100:8399/WebReport/ReportServer?reportlet=/casi/CA00003" + (printType.equals("1") ? "6" : printType.equals("2") ? "5":"4") + ".cpt&aae135="
                + aae135 + "&format=" + fileType;
        String saveDir = "d://springBoot_tmp";
        String fileName = date + "_" + aae135 + "_turnOutPrint" + "." + (fileType.equals("image") ? "png" : "pdf");
        System.out.printf(aae135 + " " + fileName);
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
     * 灵活就业登记校验
     *
     * @param map
     * @return
     */
    public JSONObject trade6016(Map<String, Object> map)  {
        try {
            map.get("AAE135").toString();//身份证

        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }
        Long aac001 = queryPerson(map.get("AAE135").toString(), null);
        if(aac001.equals(0L)){
            throw new ServiceException("社保系统中找不到该人员基本信息");
        }
        Map<String, String> mapSql = new HashMap<>();
        mapSql.put("SQL", "select a.aae140 from ac02 a,ac20 b where a.aaz159=b.aaz159 and a.aae140='20' and aac031='1' and aac008='1' and a.aac001="+aac001);
        List list = dataSharingMapper.commQuery(mapSql);
        if(list.size()>0){
            throw new ServiceException("该人员不符合线上办理的条件！请到窗口办理");
        }
        JSONObject json = new JSONObject();

        Ac01 ac01=new Ac01();
        ac01=ac01Service1.findBy("aac001",aac001);
        json.clear();
        json.put("AAC001", aac001);
        json.put("AAE135", ac01.getAae135());
        json.put("AAC003", ac01.getAac003());

        return json;
    }
    /**
     * 灵活就业登记
     *
     * @param map
     * @return
     */
    public JSONObject trade6017(Map<String, Object> map)  {
        try {
            map.get("AAC001");
            map.get("AAE135");//身份证
            map.get("AAC009");//户口性质
            map.get("AAC011");//文化程度
            map.get("AAE006");//地址
            map.get("EAC101");//手机
            map.get("GZDD"  );//工作地点
            map.get("BDC033");//工作内容

        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }
        trade6016(map);
        Cc76 cc76=new Cc76();
        cc76.setAaz308(publicMapper.querySequenceByParam("sq_aaz308"));
        cc76.setAac001(Long.valueOf(map.get("AAC001").toString()));
        cc76.setAae135(map.get("AAE135").toString());
        cc76.setAac009(map.get("AAC009").toString());
        cc76.setAac011(map.get("AAC011").toString());
        cc76.setAae006(map.get("AAE006").toString());
        cc76.setEac101(map.get("EAC101").toString());
        cc76.setGzdd( map.get("GZDD"  ).toString());
        cc76.setBdc033(map.get("BDC033").toString());
        if(map.get("Projid")!=null&&!map.get("Projid").toString().equals("")){
            cc76.setProjid(map.get("Projid").toString());
        }
        cc76.setFlag("0");
        cc76.setAae036(publicMapper.queryDBdate());
        cc76Service.save(cc76);
        JSONObject json = new JSONObject();
        json.put("mainMSG","成功");
        json.put("AAE135",cc76.getAae135());

        return json;

    }

    /**
     * 人员基本信息查询(人员搜索框)
     *
     * @param map personId 人员ID aac001
     * @return
     * @throws IOException
     */
    public JSONObject trade6032(Map<String, Object> map) throws IOException {
        try {
            map.get("personId").toString();

        } catch (Exception e) {
            throw new ServiceException("传入参数有误！" + JSON.toJSONString(map));
        }
        HashMap<String, Object> map1 = new HashMap<String, Object>();
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
        if (aac003 == null || aac003.equals("")) {
            condition.createCriteria().andCondition("aae135 ='" + aae135.trim() + "'");
        } else {
            condition.createCriteria().andCondition("aae135 ='" + aae135.trim() + "' and aac003='" + aac003.trim() + "'");
        }
        List<Ac01> ac01 = ac01Service1.findByCondition(condition);
        if (ac01.size() > 1) {
            throw new ServiceException("该人员有多条基本信息，请到社保中心做人员合并业务！");
        } else if (ac01.size() == 0) {
            return 0L;
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

    /**
     * 查询公安数据
     *
     * @param sfz
     * @return
     * @throws IOException
     */
    public List<HashMap<String, String>> queryGA(String sfz) throws IOException {
        oracle_jdbc db = new oracle_jdbc();
        List<HashMap<String, String>> listmap = db.createSQLQuery("select * from AC01_FROMPOLICE where gmsfhm='" + sfz + "' and ryzt='0' and jlbz='1' and cxbz='0' and rownum=1 ");
        if (listmap.size() == 0) {
            throw new ServiceException("非杭州地区常住人口或身份证有误！");
        }
        return listmap;
    }

}
