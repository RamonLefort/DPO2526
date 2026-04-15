package Bussiness.Entities;

public class Setting {

	private int idSetting;
	private int volume;
	private String background;
	private String skin;
	private String username;



	public Setting(int idSetting, String username, int volume, String skin) {
		this.idSetting = idSetting;
		this.username = username;
		this.volume = volume;
		this.skin = skin;
	}



	public int getIdSetting() {
		return idSetting;
	}

	public int getVolume() {
		return volume;
	}

	public String getBackground() {
		return background;
	}

	public String getSkin() {
		return skin;
	}

	public String getUsername() {
		return username;
	}



	public void setVolume(int volume) {
		this.volume = volume;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}
}
