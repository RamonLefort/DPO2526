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

	public Game(String nameGame, String username) {
		this.nameGame = nameGame;
		this.username = username;
		this.money = 0.0;
		this.minutes = 0;
		this.seconds = 0;
		this.coffeePerClick = 1;
	}


	public int getIdGame() {
		return idGame;
	}

	public String getNameGame() {
		return nameGame;
	}

	public double getMoney() {
		return money;
	}

	public void addMoney(double amount) {
		this.money += amount;
	}

	public int getCoffeePerClick() {
		return coffeePerClick;
	}

	public String getUsername() {
		return username;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getSeconds() {
		return seconds;
	}
}