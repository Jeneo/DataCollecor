package com.quartz.support;

public enum TriggerStateEnum {
	 
	WAITING("WAITING", "µÈ´ý"),
 
	PAUSED("PAUSED", "ÔÝÍ£"),
 
    ACQUIRED("ACQUIRED", "Õý³£Ö´ÐÐ"),
    
    BLOCKED("BLOCKED", "×èÈû"),
	
	ERROR("ERROR", "´íÎó"),
	
	NORUN("NORUN", "Î´¿ªÆô");
 
    String key;
 
    String desc;
    
    public static String getDescByKey(String key) {
        if (key==null) {
            return "";
        }
 
        for (TriggerStateEnum triggerStateEnum : TriggerStateEnum.values()) {
            if (triggerStateEnum.getKey().equals(key)) {
                return triggerStateEnum.getDesc();
            }
        }
        return key;
    }
 
    private TriggerStateEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }
 
    
    public String getKey() {
		return key;
	}
 
 
	public void setKey(String key) {
		this.key = key;
	}
 
 
	public String getDesc() {
        return desc;
    }
 
    public void setDesc(String desc) {
        this.desc = desc;
    }
 
}
