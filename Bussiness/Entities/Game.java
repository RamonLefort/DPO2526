package Bussiness.Entities;

public class Game {

	private int idGame;
	private String nameGame;
	private double money;
	private int hours;
	private int minutes;
	private int seconds;
	private int coffeePerClick;
	private String username;
	private float production_per_sec;
	private boolean finished;

	public Game(int idGame, String nameGame, double money, int hours, int minutes, int seconds, int coffeePerClick, float production_per_sec, String username, boolean finished) {
		this.idGame = idGame;
		this.nameGame = nameGame;
		this.money = money;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.coffeePerClick = coffeePerClick;
		this.username = username;
		this.production_per_sec = production_per_sec;
		this.finished = finished;
	}

	public int getIdGame()            { return idGame; }
	public String getNameGame()       { return nameGame; }
	public double getMoney()          { return money; }
	public int getHours()           { return hours; }
	public int getMinutes()           { return minutes; }
	public int getSeconds()           { return seconds; }
	public int getCoffeePerClick()    { return coffeePerClick; }
	public String getUsername()       { return username; }
	public float getProduction_per_sec() {
		return production_per_sec;
	}
	public boolean isFinished() {
		return finished;
	}

	public void addMoney(double amount)         { this.money += amount; }
	public void setMoney(double money)          { this.money = money; }
	public void setHours(int hours)         { this.hours = hours; }
	public void setMinutes(int minutes)         { this.minutes = minutes; }
	public void setSeconds(int seconds)         { this.seconds = seconds; }
	public void setCoffeePerClick(int coffeePerClick) { this.coffeePerClick = coffeePerClick; }
	public void setProduction_per_sec(float production_per_sec) {
		this.production_per_sec = production_per_sec;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}