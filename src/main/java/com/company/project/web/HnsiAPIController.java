package com.company.project.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.dao.HnsiAPIMapper;
import com.company.project.dao.PublicMapper;
import com.company.project.model.*;
import com.company.project.service.*;
import oracle.sql.DATE;
import org.apache.axis2.AxisFault;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * author maoxj
 */
@RestController
@RequestMapping("/hnsiapi")
public class HnsiAPIController {
    @Resource
    private HnsiAPIMapper hnsiAPIMapper;
    @Resource
    private PublicMapper publicMapper;
    @Resource
    private Ac01Service ac01Service;
    @Resource
    private Ag02Service ag02Service;
    @Resource
    private Ade8Service ade8Service;
    @Resource
    private Ac11Service ac11Service;
    @Resource
    private Ade4Service ade4Service;
    private static HashMap<String ,String> phone=new HashMap<String ,String>();
    /**author maoxj
     * 缴费登记申请
     * @param cardno 身份证
     * @param name 姓名
     * @param paymentmethod 缴费方式 12 银行批量待扣 13 银行自主缴费
     * @return
     */
    @PostMapping("/insurancereg")
    public Result insurancereg(@RequestParam String cardno,@RequestParam String name,
                               @RequestParam String paymentmethod,
                               @RequestParam String eab009,@RequestParam String eab030 ) {
        Condition condition=new Condition(Ac01.class);
        condition.createCriteria().andCondition("aae135 ='"+cardno+"'");
        List<Ac01> ac01 = ac01Service.findByCondition(condition);
      if(ac01.size()==0){
          throw new ServiceException("社保系统中找不到该人员基本信息！");
      }else if(ac01.size()>1){
          throw new ServiceException("社保系统中该人员有多条信息，请前往社保中心做人员合并业务！");
      }else if(ac01.size()==1&&!ac01.get(0).getAac003().equals(name)){
          throw new ServiceException("社保系统中姓名"+ac01.get(0).getAac003()+" 传入姓名："+name);
      }
      Long cbflag=hnsiAPIMapper.countac02ac20(ac01.get(0));
      if(cbflag==0){
          throw new ServiceException("非续保人员不能录入,请到乡镇街道或社保登记！");
      }

        Condition conditionag02=new Condition(Ag02.class);
        conditionag02.createCriteria().andCondition("aac001 ='"+ac01.get(0).getAac001()+"'");
        List<Ag02> ag02 = ag02Service.findByCondition(conditionag02);
        if(ag02.size()==0){
            throw new ServiceException("该人员没有家庭户！请到乡镇街道或社保登记！");
        }


        Condition conditionade8=new Condition(Ade8.class);
        conditionade8.createCriteria().andCondition("aac001 ='"+ac01.get(0).getAac001()+"' and aae140='25' and aae003=to_char(sysdate,'yyyy')||'07'");
        List<Ade8> ade8l = ade8Service.findByCondition(conditionade8);
        if(ade8l.size()>0){
            throw new ServiceException("该人员今年有登记记录请核对！");
        }
        List<HashMap>list=hnsiAPIMapper.checkinfo(ac01.get(0));
        if(list.size()==0){
            throw new ServiceException("人员不符合缴费条件或已经缴费登记");
        }else if(list.get(0).get("AAC031").toString().equals("1")){
            throw new ServiceException("该人员参加职工医保或者土保大病，请核实");
        }else if(list.get(0).get("AAE010")==null||list.get(0).get("AAE010").toString().length()<3){
            if(paymentmethod.equals("12")){
                throw new ServiceException("该人员没有银行账号信息只能选择银行自主缴费！");
            }

        }else if(paymentmethod.equals("12")&&list.get(0).get("AAE010")!=null&&
                !list.get(0).get("AAE010").toString().substring(0,6).equals("623091")){

            throw new ServiceException("该人员银行账号非农商银行只能选择银行自主缴费！");
        }else if(list.get(0).get("EAB030")!=null&&
                publicMapper.getCodeValue("EAB030",list.get(0).get("EAB030").toString()) .contains("新居民")){
            throw new ServiceException("新居民请到乡镇街道进行缴费！");
        }
        //System.out.println(list.get(0).get("AAE010").toString().substring(0,6));
        if(!paymentmethod.equals("12")&&!paymentmethod.equals("13")){
            throw new ServiceException("传入的缴费方式编码有误！");
        }

        Date date=publicMapper.queryDBdate();
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy");//yyyy-MM-dd HH:mm:ss
        String year = format0.format(date.getTime());//这个就是把时间戳经过处理得到期望格式的时间
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMM");
        String yearm = format1.format(date.getTime());//这个就是把时间戳经过处理得到期望格式的时间

        Ade8 ade8=new Ade8();
        ade8.setAac001(Long.valueOf(ac01.get(0).getAac001()));
        ade8.setAaz002(publicMapper.querySequenceByParam("SQ_AAZ002"));
        ade8.setAac003(name);
        ade8.setAae135(cardno);
        ade8.setAae001(Short.valueOf(year));
        ade8.setAae002(Integer.valueOf(yearm));
        ade8.setAae003(Integer.valueOf(year+"07"));
        ade8.setAae140("25");
        ade8.setAae036(date);
        ade8.setAae016("0");
        ade8.setAab033(paymentmethod);
        ade8.setEab009(eab009);
        ade8.setEab030(eab030);
        ade8Service.save(ade8);
     //Date date= publicMapper.queryDBdate();
        //System.out.println(date.getYear());
        ade8.setAae140(publicMapper.getCodeValue("AAE140","25"));
        ade8.setEab009(publicMapper.getCodeValue("EAB009",eab009));
        ade8.setEab030(publicMapper.getCodeValue("EAB030",eab030));
        return ResultGenerator.genSuccessResult(ade8);
    }

    /**author maoxj
     * 缴费及审核信息查询
     * @param cardno 身份证
     * @return
     */
    @PostMapping("/queryinsurancereg")
    public Result queryinsurancereg(@RequestParam String cardno) {
        Condition condition=new Condition(Ac01.class);
        condition.createCriteria().andCondition("aae135 ='"+cardno+"'");
        List<Ac01> ac01 = ac01Service.findByCondition(condition);
        if(ac01.size()==0){
            throw new ServiceException("社保系统中找不到该人员基本信息！");
        }else if(ac01.size()>1){
            throw new ServiceException("社保系统中该人员有多条信息，请前往社保中心做人员合并业务！");
        }
        Condition conditionade8=new Condition(Ade8.class);
        conditionade8.createCriteria().andCondition("aac001 ='"+ac01.get(0).getAac001()+"' and aae140='25' and aae003=to_char(sysdate,'yyyy')||'07'");
        conditionade8.orderBy("aae036").desc();//排序
        List<Ade8> ade8l = ade8Service.findByCondition(conditionade8);
        if(ade8l.size()==0){
            throw new ServiceException("找不到登记信息，请先缴费登记");
        }

        List<InsuranceRegDTO>list=new ArrayList<InsuranceRegDTO>();
        for(int i=0;i<ade8l.size();i++){
            InsuranceRegDTO dto=new InsuranceRegDTO();
            dto.setAaz002(ade8l.get(i).getAaz002().intValue());
            dto.setAac003(ade8l.get(i).getAac003());
            dto.setAae135(ade8l.get(i).getAae135());
            dto.setAae001(ade8l.get(i).getAae001().toString()+"07-"+(ade8l.get(i).getAae001()+1)+"06");
            dto.setAae140(publicMapper.getCodeValue("AAE140",ade8l.get(i).getAae140()));
            //dto.setAae016(ade8l.get(i).getAae016().equals("1")?"审核通过":ade8l.get(i).getAae016().equals("0")?"未审核":"审核不通过");
            dto.setAae016(ade8l.get(i).getAae016());
            dto.setAae013(ade8l.get(i).getAae013());
            dto.setEad184(ade8l.get(i).getEad184());
            dto.setAab033(ade8l.get(i).getAab033());
            dto.setAae002(ade8l.get(i).getAae002().toString());
            dto.setEab009(publicMapper.getCodeValue("EAB009",ade8l.get(i).getEab009()==null?"无":ade8l.get(i).getEab009()));
            dto.setEab030(publicMapper.getCodeValue("EAB030",ade8l.get(i).getEab030()==null?"无":ade8l.get(i).getEab030()));
            SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//yyyy-MM-dd HH:mm:ss
            String aae036 = format0.format(ade8l.get(i).getAae036());//这个就是把时间戳经过处理得到期望格式的时间
            dto.setAae036(aae036);
            Ac11 ac11=new Ac11();
            try {
                ac11=   ac11Service.findBy("aac001",ade8l.get(i).getAac001());
            }catch (Exception e) {
                ac11 = null;
            }
            if (ac11!=null){
                dto.setAae010(ac11.getAae010());
                dto.setAae009(ac11.getAae009());
                dto.setAaa082(ac11.getAaa082());
            }

            if(ade8l.get(i).getAae016().equals("1")&&ade8l.get(i).getEad184().length()>0){
                Ade4 ade4=new Ade4();
                try {
                    ade4=   ade4Service.findBy("ead184",ade8l.get(i).getEad184());
                }catch (Exception e) {
                    ade4 = null;
                }
                if(ade4!=null){
                    String aae037 = format0.format(ade4.getAae036());
                    dto.setAae037(aae037);
                    dto.setAab033(ade4.getAab033());
                    dto.setEad186(ade4.getEad186().doubleValue());
                    //dto.setEab009(publicMapper.getCodeValue("EAB009",ade4.getEab009()));
                    //dto.setEab030(publicMapper.getCodeValue("EAB030",ade4.getEab030()));
                    dto.setEad189(publicMapper.getCodeValue("EAD189",ade4.getEad189()));
                    dto.setAae010(ade4.getAae010());
                    dto.setAaa082(ade4.getAaa082());

                }


            }


            list.add(dto);
        }

        return ResultGenerator.genSuccessResult(list);
    }

    /**author maoxj
     * 缴费方式选择
     * @param paymentmethod 缴费方式 12批量 13自主缴费
     * @param aaz002 queryinsurancereg返回的aaz002
     * @return
     */
    @PostMapping("/setPayment")
    public Result setPayment(@RequestParam String paymentmethod,@RequestParam Long aaz002) {
        InsuranceRegDTO dto=new InsuranceRegDTO();
        Date date=publicMapper.queryDBdate();
        SimpleDateFormat formatym = new SimpleDateFormat("MMdd");//yyyy-MM-dd HH:mm:ss
        String ym = formatym.format(date);//这个就是把时间戳经过处理得到期望格式的时间
        if(Long.valueOf(ym)>=627){//6月27号后
            throw new ServiceException("6月27号后不允许缴费方式变更！");
        }
        Ade8 ade8=new Ade8();
        try {
            ade8=   ade8Service.findBy("aaz002",aaz002);
        }catch (Exception e) {
            ade8 = null;
        }
        if(ade8==null){
            throw new ServiceException("找不到登记信息，请先缴费登记");
        }
        if(!ade8.getAae016().equals("1")){
            throw new ServiceException("该条信息未审核，操作失败");
        }
        if(ade8.getEad184()==null||ade8.getEad184().length()==0){
            throw new ServiceException("找不到对应的审核信息");
        }
        Ade4 ade4=new Ade4();
        try {
            ade4=   ade4Service.findBy("ead184",ade8.getEad184());
        }catch (Exception e) {
            ade4 = null;
        }
        if(ade4==null){
            throw new ServiceException("找不到对应的审核信息2");
        }
        if(!ade4.getEad189().equals("0")){
            throw new ServiceException("该信息不是待扣款状态不能进行操作");
        }
        if(ade4.getAab033()!=null&&ade4.getAab033().equals("11")){
            throw new ServiceException("该人员是免缴人员不需要进行操作");
        }
        if((ade4.getAae010()==null||ade4.getAae010().length()==0)&&paymentmethod.equals("12")){
            throw new ServiceException("该人员没有银行信息，只能选择银行自主缴费");
        }
        if(!paymentmethod.equals("12")&&!paymentmethod.equals("13")){
            throw new ServiceException("传入的缴费方式代码有误");
        }
        if(paymentmethod.equals("12")&&ade4.getAae010()!=null&&
                !ade4.getAae010().substring(0,6).equals("623091")){

            throw new ServiceException("该人员银行账号非农商银行只能选择银行自主缴费！");
        }



        ade8.setAab033(paymentmethod);
        ade8.setAae037(date);
        ade8Service.update(ade8);
        ade4.setAab033(paymentmethod);
        ade4Service.update(ade4);

        dto.setAaz002(ade8.getAaz002().intValue());
        dto.setAac003(ade8.getAac003());
        dto.setAae135(ade8.getAae135());
        dto.setAae001(ade8.getAae001().toString());
        dto.setAae140(publicMapper.getCodeValue("AAE140",ade8.getAae140()));
        //dto.setAae016(ade8l.get(i).getAae016().equals("1")?"审核通过":ade8l.get(i).getAae016().equals("0")?"未审核":"审核不通过");
        dto.setAae016(ade8.getAae016());
        dto.setAae013(ade8.getAae013());
        dto.setEad184(ade8.getEad184());
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//yyyy-MM-dd HH:mm:ss
        String aae036 = format0.format(ade8.getAae036());//这个就是把时间戳经过处理得到期望格式的时间
        dto.setAae036(aae036);
        Ac11 ac11=new Ac11();
        try {
            ac11=   ac11Service.findBy("aac001",ade8.getAac001());
        }catch (Exception e) {
            ac11 = null;
        }
        if (ac11!=null){
            dto.setAae010(ac11.getAae010());
            dto.setAae009(ac11.getAae009());
            dto.setAaa082(ac11.getAaa082());
        }
        String aae037 = format0.format(ade4.getAae036());
        dto.setAae037(aae037);
        dto.setAab033(ade4.getAab033());
        dto.setEad186(ade4.getEad186().doubleValue());
        dto.setEab009(publicMapper.getCodeValue("EAB009",ade4.getEab009()));
        dto.setEab030(publicMapper.getCodeValue("EAB030",ade4.getEab030()));
        dto.setEad189(publicMapper.getCodeValue("EAD189",ade4.getEad189()));
        dto.setAae010(ade4.getAae010());
        dto.setAaa082(ade4.getAaa082());

        return ResultGenerator.genSuccessResult(dto);
    }

    /**author maoxj
     * 查询缴费标准，缴费银行，提示信息等信息
     * @return
     */
    @PostMapping("/querybasicinfo")
    public Result querybasicinfo() {
        List<HashMap>list=hnsiAPIMapper.queryAa05();
        List<Aa05DTO> aa05list=new ArrayList<Aa05DTO>();
        for(int i=0;i<list.size();i++){
            Aa05DTO aa05dto=new Aa05DTO();
            aa05dto.setAaa044(list.get(i).get("AAA044").toString());
            aa05dto.setAae040(list.get(i).get("AAE040").toString());
            aa05dto.setEaa007(list.get(i).get("EAA007").toString());
            aa05list.add(aa05dto) ;

        }
        PaybankDTO paybankDTO =new PaybankDTO();
        paybankDTO.setAae008(publicMapper.getCodeValue("APPMSG","aae008"));
        paybankDTO.setAae009(publicMapper.getCodeValue("APPMSG","aae009"));
        paybankDTO.setAae010(publicMapper.getCodeValue("APPMSG","aae010"));
        paybankDTO.setMsg(publicMapper.getCodeValue("APPMSG","bankinfo"));
        BasicinfoDTO dto=new BasicinfoDTO();
        dto.setListaa05(aa05list);
        dto.setPaybankdto(paybankDTO);
        dto.setAae013(publicMapper.getCodeValue("APPMSG","msg1"));
        dto.setAae014(publicMapper.getCodeValue("APPMSG","msg2"));

        return ResultGenerator.genSuccessResult(dto);
    }


    /**author maoxj
     * 社保查询统一入口 调用SBP_APP中的包体
     * @param method SBP_APP中的方法名 如sbcx_grjbxx
     * @param intext 传入参数 一般为身份证号码-姓名 比如 330481199308132446-倪梦岚
     * @param aae013 他参数的字符串拼接
     * @param pageno 第几页
     * @param pagesize 每页大小
     * @return
     */
    @PostMapping("/sbcx")
    public JSON sbcx(@RequestParam String method, @RequestParam String intext,
                     @RequestParam String aae013,
                     @RequestParam Long pageno, @RequestParam Long pagesize) {
        SBCXDTO dto=new SBCXDTO();
        dto.setV_intext(intext);
        dto.setV_aae013(aae013);
        dto.setV_pageno(pageno);
        dto.setV_pagesize(pagesize);
        dto.setV_method(method);
        hnsiAPIMapper.callSBCX(dto);
        JSON json=JSON.parseObject(dto.getV_rettext());
        ((JSONObject) json).put("code","200");
        ((JSONObject) json).put("message","SUCCESS");
        return json;
    }

    /**author maoxj
     * 社保查询统一入口 调用SBP_APP中的包体
     * @param method SBP_APP中的方法名 如sbcx_grjbxx
     * @param intext 传入参数 一般为身份证号码-姓名 比如 330481199308132446-倪梦岚
     * @param aae013 他参数的字符串拼接
     * @param pageno 第几页
     * @param pagesize 每页大小
     * @return
     */
    @PostMapping("/sbcx/{method}")
    public JSON sbcx2(@PathVariable("method") String method, @RequestParam String intext,
                      @RequestParam String aae013,
                     @RequestParam Long pageno, @RequestParam Long pagesize) {
        SBCXDTO dto=new SBCXDTO();
        dto.setV_intext(intext);
        dto.setV_aae013(aae013);
        dto.setV_pageno(pageno);
        dto.setV_pagesize(pagesize);
        dto.setV_method(method);
        hnsiAPIMapper.callSBCX(dto);
        JSON json=JSON.parseObject(dto.getV_rettext());
        ((JSONObject) json).put("code","200");
        ((JSONObject) json).put("message","SUCCESS");
        return json;
    }

    /**author maoxj
     *  调用SBP_APP中的包体
     * @param method SBP_APP中的方法名 如sbcx_grjbxx
     * @param intext 传入参数 一般为身份证号码-姓名 比如 330481199308132446-倪梦岚
     * @param aae013 他参数的字符串拼接
     * @param pageno 第几页
     * @param pagesize 每页大小
     * @return
     */
    @PostMapping("/sbpapp/{method}")
    public JSON sbpapp(@PathVariable("method") String method, @RequestParam String intext,
                      @RequestParam String aae013,
                      @RequestParam Long pageno, @RequestParam Long pagesize) {
        SBCXDTO dto=new SBCXDTO();
        dto.setV_intext(intext);
        dto.setV_aae013(aae013);
        dto.setV_pageno(pageno);
        dto.setV_pagesize(pagesize);
        dto.setV_method(method);
        hnsiAPIMapper.callSBCX(dto);
        JSON json=JSON.parseObject(dto.getV_rettext());
        ((JSONObject) json).put("code","200");
        ((JSONObject) json).put("message","SUCCESS");

        if(((JSONObject) json).getString("Ri_Ret")!=null){
            if(!((JSONObject) json).getString("Ri_Ret").equals("0")){
                ((JSONObject) json).put("code","400");
                //((JSONObject) json).remove("Ri_Ret");
                if(((JSONObject) json).getString("Rv_Msg")!=null){
                    ((JSONObject) json).put("message",((JSONObject) json).getString("Rv_Msg"));
                    //((JSONObject) json).remove("Rv_Msg");
                }

            }


        }
        return json;
    }

    @PostMapping("/sendSms")
    @CrossOrigin(origins = "*", maxAge = 3600)//浏览器跨域访问 origins 允许访问的地址如：http://domain2.com *为全部 maxAge过期时间 3600秒
    public Result sendSms(@RequestParam String phoneNumber ,String msg) throws AxisFault {
        BasicinfoDTO dto=new BasicinfoDTO();
        MasSMS msm=new MasSMS();
        //String ss="验证码:11223";
        String[] pnums=null;
        pnums=phoneNumber.split(",");
       // pnums[0]="18958335703";
        msm.sendSms(pnums,msg);
        phone.put(pnums[0]+String.valueOf(System.currentTimeMillis()),String.valueOf(System.currentTimeMillis()));
        dto.setAae013("ww");
        dto.setAae014("eeee");
        System.out.println(phone);
        return ResultGenerator.genSuccessResult("OK");
    }



}
