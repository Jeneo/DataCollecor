package com.quartz.support;

public enum TriggerStateEnum {
	 
	WAITING("WAITING", "�ȴ�"),
 
	PAUSED("PAUSED", "��ͣ"),
 
    ACQUIRED("ACQUIRED", "����ִ��"),
    
    BLOCKED("BLOCKED", "����"),
	
	ERROR("ERROR", "����"),
	
	NORUN("NORUN", "δ����");
 
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
