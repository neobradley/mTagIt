package therians.kfc.tagit.db;

public class Log {
	//LogId int, LogUId int, LogDesc text, LogDT text, LogType text
	private int id, uid;
	private String desc, dt, type;
	public Log() {
		super();
	}
	public Log(int id, int uid, String desc, String dt, String type) {
		super();
		this.id = id;
		this.uid = uid;
		this.desc = desc;
		this.dt = dt;
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDt() {
		return dt;
	}
	public void setDt(String dt) {
		this.dt = dt;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
