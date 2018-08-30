package com.company.project.service;

import com.alibaba.fastjson.JSONObject;
import com.company.project.core.ServiceException;
import com.company.project.dao.DataSharingMapper;
import com.company.project.dao.HnsiAPIMapper;
import com.company.project.model.Ac01;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component//（把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>）

public class DateSharing {
    @Resource
    private Ac01Service ac01Service1;
    @Resource
    private DataSharingMapper dataSharingMapper;
    /**
     * 转出凭证打印校验
     * @param map
     * AAE135 身份证
     * AAC003  姓名
     * @return
     */
    public JSONObject turnOutVerification(Map<String,Object> map){

        Long aac001=queryPerson(map.get("AAE135").toString(),map.get("AAC003").toString());
        List listsd=dataSharingMapper.checkinfosd(aac001);
        if(listsd.size()>0){
            throw new ServiceException("2009年之后有双低明细不能做转出");
        }
        List listzd=dataSharingMapper.checkinfozd(aac001);
        if(listzd.size()>0){
            throw new ServiceException("非中断状态不能打印转出凭证");
        }
        List listqf=dataSharingMapper.checkinfoqf(aac001);
        if(listqf.size()>0){
            throw new ServiceException("养老有欠费不能打印转出凭证");
        }
        List listybqf=dataSharingMapper.checkinfoybqf(aac001);
        if(listybqf.size()>0){
            throw new ServiceException("医疗有欠费不能打印转出凭证");
        }
        JSONObject json=new JSONObject();
        json.put("AAC001",aac001);

        return json;

    }

    /**
     * 转出证明打印
     * @param map
     * AAC001
     * FILETYPE 返回的文件类型 pdf image
     * PRINTTYPE 打印类型 1 养老 2 医保
     * @return
     */
    public JSONObject turnOutPrint(Map<String,Object> map){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        String date=sdf.format(new Date());
        Long aac001=Long.valueOf(map.get("AAC001").toString());
        String fileType=map.get("FILETYPE").toString();//返回的文件类型 pdf image
        String printType=map.get("PRINTTYPE").toString();//打印类型 1 养老 2 医保
        String urlstr = "http://10.255.5.100:8399/WebReport/ReportServer?reportlet=/casi/CA00002"+(printType.equals("1")?"4":"5")+".cpt&v_aac001="
                + aac001 + "&format="+fileType;
        String saveDir = "d://springBoot_tmp";
        String fileName = date+"_"+aac001 +"_turnOutPrint"+ "."+(fileType.equals("image")?"png":"pdf");
        System.out.printf(aac001+" "+fileName);
        FileDownloadTool.download(urlstr, saveDir, fileName);//下载文件到本地
        DeleteFileDate(saveDir);//删除非今天的文件
        File file = new File(saveDir + File.separator + fileName);
        String strBase64 = Base64_Coder.getFileBinary(file);
        JSONObject json=new JSONObject();
        json.put("FILEBASE64",strBase64);
        json.put("FILETYPE",fileType);
        return json;
    }

    /**
     * 基本信息校验
     * @param aae135
     * @param aac003
     * @return
     */
    public  Long queryPerson(String aae135,String aac003){
        Condition condition=new Condition(Ac01.class);
        condition.createCriteria().andCondition("aae135 ='"+aae135.trim()+"' and aac003='"+aac003.trim()+"'");
        List<Ac01> ac01 =  ac01Service1.findByCondition(condition);
        if(ac01.size()>1){
            throw new ServiceException("该人员有多条基本信息，请到社保中心做人员合并业务！");
        }else if(ac01.size()==0){
            throw new ServiceException("社保系统没有该人员信息!");
        }
        return ac01.get(0).getAac001();

    }

    /**
     * 删除非今天的文件
     * @param savePlace
     * @return
     */
    public int DeleteFileDate(String savePlace){
        int number=0;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        String date=sdf.format(new Date());
        File file=new File(savePlace);
        String[]  tempList  =  file.list();
        File  temp  =  null;
        if(!savePlace.endsWith("/")){
            savePlace=savePlace+"/";
        }
        for  (int  i  =  0;  i  <  tempList.length;  i++)  {
            String path=savePlace+tempList[i];
            temp  =  new  File(path);
            if(temp.exists()&&!temp.getName().startsWith(date)){//
                System.out.println(temp.getName());
                temp.delete();
                number++;

            }

        }
        return number;
    }

}
