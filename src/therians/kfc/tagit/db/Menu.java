package therians.kfc.tagit.db;

public class Menu {
	private String name, type;
	private double price;
	private int qty;
	private int[] owners;
	public Menu() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Menu(String name, String type, double price) {
		super();
		this.name = name;
		this.type = type;
		this.price = price;
		this.qty = 0;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public int[] getOwners() {
		return owners;
	}
	public void setOwners(int[] owners) {
		this.owners = owners;
	}

	
}
