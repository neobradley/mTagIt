package therians.kfc.tagit.db;

public class Achievement {
	//AName text, ADesc text, AType text, AReq int, APoint int
	private String name, desc, type;
	private int id, req, point;
	private boolean ach;
	public Achievement() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Achievement(int id, String name, String desc, String type, int req, int point) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.type = type;
		this.req = req;
		this.point = point;
		this.ach = false;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getReq() {
		return req;
	}
	public void setReq(int req) {
		this.req = req;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isAch() {
		return ach;
	}
	public void setAch(boolean ach) {
		this.ach = ach;
	}
	
	
}
