package Bussiness.Entities;

public class Generator {

	private int idGenerator;

	private String name;

	private int idGame;

	private int quantity;

	private int price;

	private int period;

	private int earning;

	public Generator(String name, int idGame) {

	}

	public int getIdGenerator() {
		return 0;
	}

	public int calcNextPrice() {
		return 0;
	}

	public int getTotalProduction() {
		return 0;
	}

	public int getGlobalPercent(int totalProduction) {
		return 0;
	}

	public int getQuantity(){return quantity;}

	public int getPeriod(){return period;}

	public String getName(){return name;}

	public int getPrice(){return price;}

	public void setQuantity(int quantity){this.quantity = quantity;}

	public void setIdGenerator(int idGenerator) {
		this.idGenerator = idGenerator;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public void setEarning(int earning) {
		this.earning = earning;
	}
}
