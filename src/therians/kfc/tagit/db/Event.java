package therians.kfc.tagit.db;

public class Event {
	//EName text, EStart text, EEnd text, EPoint int, EDesc text
	private String name, start, end, desc, status;
	private int point;
	public Event() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Event(String name, String start, String end, String desc,
			String status, int point) {
		super();
		this.name = name;
		this.start = start;
		this.end = end;
		this.desc = desc;
		this.status = status;
		this.point = point;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	
	
}
