package com.spiderman.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppEnum {
	
    
    /**
	 * 公司行业
	 * @author Administrator
	 *
	 */
 	public enum Industry{
		cmputer_ruanjian(0,"计算机软件",100),
		cmputer_yingjian(0,"计算机硬件",102), 
		cmputer_fuwu(0,"计算机服务(系统、数据服务、维修)",103), 
		cmputer_tongxinwangluo(0,"通信/电信/网络设备 ",104),
		cmputer_tongxinyunying(0,"通信/电信运营、增值服务",105), 
		cmputer_hulianwang(0,"互联网/电子商务",106),
		cmputer_wangyou(0,"网络游戏",107),
		cmputer_dianzi(0,"电子技术/半导体/集成电路",108),
		cmputer_yiqi(0,"仪器仪表/工业自动化",109),

		kuaiji_kuaiji(1,"会计/审计",200),
		kuaiji_jinrong(1,"金融/投资/证券",201), 
		kuaiji_yinhang(1,"银行",202), 
		kuaiji_baoxian(1,"保险",203), 
		kuaiji_xintuo(1,"信托/担保/拍卖/典当",204);
		
		
		
		
 		
		/**
		 * 权限数组
		 */
		private static final  String[] IndustryGroupList = {"计算机 | 互联网 | 通信 | 电子","会计/金融/银行/保险"};
		/**
		 * 枚举值缓存
		 */
		private static HashMap<Integer, Industry> AuthorityCache = new HashMap<>();
		
		static{
			Industry[] authArray = Industry.values();
			for (int i = 0; i < authArray.length; i++) {
				final Industry auth = authArray[i];
				AuthorityCache.put(auth.getValue(), auth);
			}
		}
		
		/**
		 * 返回一个group by的数组
		 * @return
		 */
		@SuppressWarnings("serial")
		public static HashMap<String, List<Industry>> getIndustryMap() {
			HashMap<String, List<Industry>> authMap = new HashMap<>();
			
			Industry[] authArray = Industry.values();
			for (int i = 0; i < authArray.length; i++) {
				final Industry auth = authArray[i];
				if(authMap.containsKey(auth.getGroupName())){
					authMap.get(auth.getGroupName()).add(auth);
				}else{
					authMap.put(auth.getGroupName(),new ArrayList<Industry>(){{add(auth);}});
				}
			}
			
			return authMap;
		}
		
		/**
		 * 根据value,返回对应的枚举值
		 * @param value
		 * @return
		 */
		public static Industry valueOf(int value){
			return AuthorityCache.get(value);
		}
		
		int groupId;
		public int getGroup() {
			return groupId;
		}
		
		public String getGroupName() {
			return IndustryGroupList[groupId];
		}
		
		public void setGroup(int groupId) {
			this.groupId = groupId;
		}
		public String getName() {
			return authName;
		}
		public void setName(String authName) {
			this.authName = authName;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		String authName;
		int value;
		private Industry(int groupId,String authName,int value){
			
			this.groupId = groupId;
			this.authName = authName;
			this.value = value;
		}
	}
    

    /**
     * 公司规模
     * @author Administrator
     *
     */
    public enum FirmSize {
    	
    	LESS50("少于50人",1),
    	MORE50("50-150人",2),
    	MORE150("150-500人",3),
    	MORE500("500-1000人",4),
    	MORE1000("1000-5000人",5),
    	MORE5000("5000-10000人",6),
    	MORE10000("10000人以上",7);

    	
        public static List<KeyValuePair<Integer,String>> getList() {
        	List<KeyValuePair<Integer,String>> list = new ArrayList<>();
        	for (FirmSize c : FirmSize.values()) {
        		list.add(new KeyValuePair<Integer, String>(c.getIndex(), c.getName()));
            }
        	return list;
        }
    	
        // 成员变量
        private String name;
        private int index;

        // 构造方法
        private FirmSize(String name, int index) {
            this.name = name;
            this.index = index;
        }

        // 普通方法
        public static String getName(int index) {
            for (FirmSize c : FirmSize.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

        // get set 方法
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
    
    
    /**
     * 公司性质
     * @author Administrator
     *
     */
    public enum CompanyNature {
        //RED("红色", 1), GREEN("绿色", 2), BLANK("白色", 3), YELLO("黄色", 4);
    	
    	WAIZI_OUMEI("外资(欧美)",1),
    	WAIZI("外资(非欧美)",2),
    	HEZI("合资",3),
    	GOUQI("国企",4),
    	MINYING("民营公司",5),
    	WAIQIDAIBIAOCHU("外企代表处",6),
    	ZHENGFU("政府机关",7),
    	SHIYEDANWEI("事业单位",8),
    	FEIYINGLIJIGOU("非营利机构",9),
    	SHANGSHIGONGSI("上市公司",10),
    	CHUANGYEGONGSI("创业公司",11);
    	
    	

        // 成员变量
        private String name;
        private int index;

        // 构造方法
        private CompanyNature(String name, int index) {
            this.name = name;
            this.index = index;
        }

        // 普通方法
        public static String getName(int index) {
            for (CompanyNature c : CompanyNature.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }
        
        public static List<KeyValuePair<Integer,String>> getList() {
        	List<KeyValuePair<Integer,String>> list = new ArrayList<>();
        	for (CompanyNature c : CompanyNature.values()) {
        		list.add(new KeyValuePair<Integer, String>(c.getIndex(), c.getName()));
            }
        	return list;
        }


        // get set 方法
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

/**
 * 职位类型
 * @author Administrator
 *
 */
public enum PositionType {

    	QUANZHI("全职",1),
    	JIANZHI("兼职 ",2),
    	SHIXIQUANZHI("实习全职",3),
    	SHIXIJIANZHI("实习兼职",4);
	
	public static List<KeyValuePair<Integer,String>> getList() {
    	List<KeyValuePair<Integer,String>> list = new ArrayList<>();
    	for (PositionType c : PositionType.values()) {
    		list.add(new KeyValuePair<Integer, String>(c.getIndex(), c.getName()));
        }
    	return list;
    }
	
        // 成员变量
        private String name;
        private int index;

        // 构造方法
        private PositionType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        // 普通方法
        public static String getName(int index) {
            for (PositionType c : PositionType.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

        // get set 方法
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
    
    
/**
 * 薪资区间
 * @author Administrator
 *
 */
public enum SalaryRange {

    LESS2000("2K以下",1),
	MORE2000("2K-3K",2),
	MORE3000("3K-4.5K",3),
	MORE4500("4.5K-8K",4),
	MORE8000("8K-10K",5),
	MORE1w("10K-15K",6),
	MORE15000("15K-20K",7),
	MORE2w("20K-30K",8),
	MORE3w("30K-50K",9),
	MORE5w("50K以上",10),
	MIANYI("面谈",11);

	public static List<KeyValuePair<Integer,String>> getList() {
    	List<KeyValuePair<Integer,String>> list = new ArrayList<>();
    	for (SalaryRange c : SalaryRange.values()) {
    		list.add(new KeyValuePair<Integer, String>(c.getIndex(), c.getName()));
        }
    	return list;
    }
	
    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private SalaryRange(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (SalaryRange c : SalaryRange.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

/**
 * 学历
 * @author Administrator
 *
 */
public enum Education {
    CHUOZHONG("初中及以下",1),
	GONGZHONG("高中/中技/中专",2),
	DAZHUANG("大专",3),
	BENKE("本科",4),
	SHUOSHI("硕士",5),
	BOSHI("博士",6);

	public static List<KeyValuePair<Integer,String>> getList() {
    	List<KeyValuePair<Integer,String>> list = new ArrayList<>();
    	for (Education c : Education.values()) {
    		list.add(new KeyValuePair<Integer, String>(c.getIndex(), c.getName()));
        }
    	return list;
    }
	
    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private Education(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (Education c : Education.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

/**
 * 工作经验
 * @author Administrator
 *
 */
public enum WorkExperience {

    NONE("无经验",1),
	MORE1("1-3年",2),
	MORE3("3-5年",3),
	MORE5("5-10年",4),
	MORE10("10年以上",5);

	public static List<KeyValuePair<Integer,String>> getList() {
    	List<KeyValuePair<Integer,String>> list = new ArrayList<>();
    	for (WorkExperience c : WorkExperience.values()) {
    		list.add(new KeyValuePair<Integer, String>(c.getIndex(), c.getName()));
        }
    	return list;
    }
	
    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private WorkExperience(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (WorkExperience c : WorkExperience.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

/**
 * 职位特性
 * @author Administrator
 *
 */
public enum PositionFeature {

    MINGQI("名企",1),
	DAIZHAO("代招",2),
	HUIYUAN("会员",3),
	NEITUI("内推",4);

	public static List<KeyValuePair<Integer,String>> getList() {
    	List<KeyValuePair<Integer,String>> list = new ArrayList<>();
    	for (PositionFeature c : PositionFeature.values()) {
    		list.add(new KeyValuePair<Integer, String>(c.getIndex(), c.getName()));
        }
    	return list;
    }
	
    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private PositionFeature(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (PositionFeature c : PositionFeature.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}


}
