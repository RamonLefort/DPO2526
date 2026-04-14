package Bussiness.Entities;

public class Stat {

	private int idStat;
	private double money;
	private int minutes;
	private int idGame;


	public Stat(double money, int minutes, int idGame) {
		this.money = money;
		this.minutes = minutes;
		this.idGame = idGame;
	}


	public Stat(int idStat, double money, int minutes, int idGame) {
		this.idStat = idStat;
		this.money = money;
		this.minutes = minutes;
		this.idGame = idGame;
	}

	public int getIdStat() {
		return idStat;
	}

	public double getMoney() {
		return money;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getIdGame() {
		return idGame;
	}
}