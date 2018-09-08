package com.company.project.dao;

import com.company.project.model.Ac01;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据共享数据库查询方法
 */
public interface DataSharingMapper {
    /**
     * 是否有双低
     *
     * @param aac001
     * @return
     */
    @Select("Select aac001,aaz001,aae003,eac003,aic020,aic020*0.08-aae022 aae022,aic020*0.08-aae022 aae023 " +
            "From (Select aac001,0 aaz001,aae003,sum(eac003) eac003," +
            "Sum(Decode(aaa115, '1', Aic020,'6',aic020, 0)) aic020 ,sum(aae022) aae022 " +
            "From ac43 Where aaz159 = (Select aaz159 From ac02 " +
            "Where aac001=#{aac001} And aae140='10' )" +
            " And aae003>=200901 And aae017='0' And eac002='0' " +
            "Group By aae003,aac001 ) " +
            "Where aic020*0.08>aae022+0.004 Order By aae003")
    public List<HashMap> checkinfosd(Long aac001) ;

    /**
     * 是否有中断
     *
     * @param aac001
     * @return
     */
    @Select("Select aac031 from Ac20 a where a.aac001=#{aac001}  and a.aac031='1'  and a.aae140 in ('10','11','20')")
    public List<HashMap> checkinfozd(Long aac001) ;

    /**
     * 养老是否有欠费
     *
     * @param aac001
     * @return
     */
    @Select("Select aaz223 from ac43 a where a.aac001=#{aac001}  and aae017='0' and aae078='0' and eac002='0' and a.aae140 in ('10','11')")
    public List<HashMap> checkinfoqf(Long aac001) ;
    /**
     * 医保是否有欠费
     *
     * @param aac001
     * @return
     */
    @Select("Select aae140 from Ac58 a where a.aac001=#{aac001} and aae140='20' and aae001=to_char(sysdate,'yyyy') and a.aae100='1'" +
            " and ekb028+ekb029<ekb030+ekb031")
    public List<HashMap> checkinfoybqf(Long aac001) ;

    /**
     * 通用查询
     *注意：结果字段不能为空 如果为空 空字段不加入到HashMap中
     * Map<String,String> map1=new HashMap<>();
     *         map1.put("SQL","select aac003,aae135 from ac01 where aac001="+aac001);
     *         List aa=dataSharingMapper.commQuery(map1);
     *         System.out.println(aa.get(0));
     */
    @Select("${SQL}")
    public List<HashMap<String,Object>> commQuery(Map<String,String> map) ;
}
