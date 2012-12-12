package therians.kfc.tagit.db;

public class User {
	//UserId int, UserName text, UserEmail text, UserAch int, UserRank text, UserPoint int, UserAvatar int, UserLastUpdate text
	private String name, email, rank, lastUpdate;
	private int id, ach, point, avatar;
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(String name, String email, String rank, String lastUpdate,
			int id, int ach, int point, int avatar) {
		super();
		this.name = name;
		this.email = email;
		this.rank = rank;
		this.lastUpdate = lastUpdate;
		this.id = id;
		this.ach = ach;
		this.point = point;
		this.avatar = avatar;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAch() {
		return ach;
	}
	public void setAch(int ach) {
		this.ach = ach;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getAvatar() {
		return avatar;
	}
	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}
	
	
	
}
