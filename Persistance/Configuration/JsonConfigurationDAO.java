package Persistance.Configuration;

import Bussiness.Entities.Configuration;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;

public class JsonConfigurationDAO {

	private String path = "config.json";
	private Gson gson;

	public JsonConfigurationDAO() {
		this.gson = new Gson();
	}

	public Configuration readJson() {
		try (FileReader reader = new FileReader(path)) {
			return gson.fromJson(reader, Configuration.class);
		} catch (IOException e) {
			System.err.println("Error al leer la configuración: " + e.getMessage());
			return null;
		}
	}
}