package Bussiness.Entities;

public class Upgrade {

	private int idUpgrade;

	private int idGenerator;

	private int idGame;

	private boolean active;

	private int price;

	public Upgrade(int idUpgrade, int idGenerator, int idGame, boolean active, int price) {
		this.idUpgrade = idUpgrade;
		this.idGenerator = idGenerator;
		this.idGame = idGame;
		this.active = active;
		this.price = price;
	}

	public int getIdUpgrade() {
		return idUpgrade;
	}

	public void setIdUpgrade(int idUpgrade) {
		this.idUpgrade = idUpgrade;
	}

	public int getIdGenerator() {
		return idGenerator;
	}

	public int getIdGame() {
		return idGame;
	}

	public void setIdGame(int idGame) {
		this.idGame = idGame;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getPrice() {
		return price;
	}
}
