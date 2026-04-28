package Bussiness.Entities;

public class Stat {
	private int idStat;
	private double money;
	private int minutes;
	private int idGame;
	private String nameGame;  // ← nuevo

	public Stat(double money, int minutes, int idGame, String nameGame) {
		this.money = money;
		this.minutes = minutes;
		this.idGame = idGame;
		this.nameGame = nameGame;
	}

	public Stat(int idStat, double money, int minutes, int idGame, String nameGame) {
		this.idStat = idStat;
		this.money = money;
		this.minutes = minutes;
		this.idGame = idGame;
		this.nameGame = nameGame;
	}

	public int getIdStat()      { return idStat; }
	public double getMoney()    { return money; }
	public int getMinutes()     { return minutes; }
	public int getIdGame()      { return idGame; }
	public String getNameGame() { return nameGame; }
}