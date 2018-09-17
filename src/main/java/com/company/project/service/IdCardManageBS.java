package com.company.project.service;
/**
 * 身份证管理
 * @author
 * 验证身份证,获取性别与出生日期
 */
public class IdCardManageBS {
    final int[] Weight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};			//加权因子
    final String[] Verifycode = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};	//校验码

    /**
     * 判断身份证号码有效性
     * @param idCard 身份证号码
     * @return	校验结果 false表示无效,true表示有效

     */
    public String checkIdCard(String idCard) {
        String newIdCard = idCard;
        if(idCard.length() == 15){				//如果是15位,转换为18位
            newIdCard = proIdCard15to18(idCard);
        }
        if(idCard.length() == 17){				//如果是17位,转换为18位
            idCard = idCard.substring(0, 6)+ idCard.substring(8, idCard.length());
            newIdCard = proIdCard15to18(idCard);
        }
        if(newIdCard.length() != 18){ 				//位数不对,返回Error
            return "Error";
        }
        String verify = newIdCard.substring(17, 18);
        if(verify.equals(getVerify(newIdCard))){ 	//检查有效性,并返回检查结果

//			QMCB_BS qmcb =new QMCB_BS();///全民参保上传
//			if (qmcb.GetqmcbFlag().equals("1")){
//			qmcb.QMCB6320(  newIdCard, 0l);//查询人员基本信息
//			HBSession sess = HBUtil.getHBSession();
//			String sql = "select * from caqmcb.qmcb_cx_bc31  Where "
//					+ "  aac147 = " + "'" + newIdCard + "'" + " and rownum=1  ";
//
//			CommonQueryBS query = new CommonQueryBS();
//			query.setConnection(sess.connection());
//			query.setQuerySQL(sql);
//			Vector<?> vector = query.query();
//			Iterator<?> iterator = vector.iterator();
//			ArchiveDTO dto = new ArchiveDTO();
//			// List<ArchiveQueryDTO> pLst = new ArrayList<ArchiveQueryDTO>();
//			if (iterator.hasNext()) {
//				while (iterator.hasNext()) {
//					HashMap tmp = (HashMap) iterator.next();
//					newIdCard=newIdCard+"|"+tmp.get("aac003").toString();
//				}
//
//			}
//			}


            return newIdCard; 						//有效,返回true
        }
        else{
            return "Error";						//无效,返回Error
        }
    }

    public  String  idCard15to18(String idCard){
        String newIdCard = idCard;
        if(idCard.length() == 15){				//如果是15位,转换为18位
            newIdCard = proIdCard15to18(idCard);
        }
        if(idCard.length() == 17){				//如果是17位,转换为18位
            idCard = idCard.substring(0, 6)+ idCard.substring(8, idCard.length());
            newIdCard = proIdCard15to18(idCard);
        }
        return newIdCard;
    }


    /**
     * 从身份证号码中获取性别
     * @param idCard 身份证号码
     * @return	性别,'F'为女性,'M'为男性
     */
    public char getSexFromIdCard(String idCard){
        if(idCard.length() == 15){					//如果是15位,转换为18位
            idCard = proIdCard15to18(idCard);
        }
        int a = Integer.parseInt(idCard.substring(16, 17));		//取倒数第2位
        if(a % 2 == 0){								//偶数为女性,基数为男性
            return '2';
        }
        else{
            return '1';
        }
    }

    /**
     * 从身份证号码中获取出生日期
     * @param idCard 身份证号码
     * @return	出生日期
     */
    public String getBirthdayFromIdCard(String idCard){
        if(idCard.length() == 15){					//如果是15位,转换为18位
            idCard = proIdCard15to18(idCard);
        }
        String birthday = idCard.substring(6, 14);	//获取表示出生日期的第7-14位
        return birthday.substring(0,4) + "-" + birthday.substring(4,6) + "-" + birthday.substring(6,8);
    }



    /**
     * 15位身份证号码转换为18位
     * @param idCard 15位身份证号码
     * @return newidCard 扩充后的18位身份证号码
     */
    public String proIdCard15to18(String idCard){
        int i, j, s = 0;
        String newidCard;
        newidCard = idCard;
        newidCard = newidCard.substring(0, 6) + "19" + newidCard.substring(6, idCard.length());
        for( i = 0; i<newidCard.length() ;i++ ){
            j = Integer.parseInt(newidCard.substring(i, i+1)) * Weight[i];
            s = s + j;
        }
        s = s % 11;
        newidCard = newidCard + Verifycode[s];
        return newidCard;
    }

    /**
     * 获取身份证校验码
     * @param idCard 身份证号码
     * @return 身份证号码的校验码
     */
    private String getVerify(String idCard){
        int[] ai = new int[18];
        int remaining = 0;
        if(idCard.length() == 18){
            idCard = idCard.substring(0, 17);
        }
        if(idCard.length() == 17){
            int sum = 0;
            for(int i = 0; i < 17; i++){
                String k = idCard.substring(i, i+1);
                ai[i] = Integer.parseInt(k);
            }
            for(int i = 0; i < 17; i++){
                sum = sum + Weight[i] * ai[i];
            }
            remaining = sum % 11;
        }
        return remaining == 2 ? "X" : String.valueOf(Verifycode[remaining]);
    }
}
