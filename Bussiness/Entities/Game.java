package Bussiness.Entities;

public class Game {

	private int idGame;
	private String nameGame;
	private double money;
	private int minutes;
	private int seconds;
	private int coffeePerClick;
	private String username;

	public Game(int idGame, String nameGame, double money, int minutes, int seconds, int coffeePerClick, String username) {
		this.idGame = idGame;
		this.nameGame = nameGame;
		this.money = money;
		this.minutes = minutes;
		this.seconds = seconds;
		this.coffeePerClick = coffeePerClick;
		this.username = username;
	}

	public int getIdGame()            { return idGame; }
	public String getNameGame()       { return nameGame; }
	public double getMoney()          { return money; }
	public int getMinutes()           { return minutes; }
	public int getSeconds()           { return seconds; }
	public int getCoffeePerClick()    { return coffeePerClick; }
	public String getUsername()       { return username; }

	public void addMoney(double amount)         { this.money += amount; }
	public void setMoney(double money)          { this.money = money; }
	public void setMinutes(int minutes)         { this.minutes = minutes; }
	public void setSeconds(int seconds)         { this.seconds = seconds; }
	public void setCoffeePerClick(int coffeePerClick) { this.coffeePerClick = coffeePerClick; }
}