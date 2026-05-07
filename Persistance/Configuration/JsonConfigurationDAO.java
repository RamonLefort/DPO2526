package Persistance.Configuration;

import Bussiness.Entities.Configuration;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;

/**
 * Clase responsable de la lectura del archivo de configuración del sistema.
 * Extrae los parámetros desde un archivo local {@code config.json}
 * y los mapea a una entidad {@link Configuration} de la capa de Business.
 */
public class JsonConfigurationDAO {

	private String path = "config.json";
	private Gson gson;

	/**
	 * Constructor por defecto.
	 * Inicializa la instancia de la librería Gson, la cual será utilizada
	 * para la conversión de texto en formato JSON a objetos nativos de Java.
	 */
	public JsonConfigurationDAO() {
		this.gson = new Gson();
	}

	/**
	 * Abre el archivo de configuración, lee su contenido y lo transforma en un objeto {@link Configuration}.
	 *
	 * @return Un objeto {@link Configuration} con los datos del archivo si la lectura es exitosa,
	 * o {@code null} si el archivo no existe, no hay permisos de lectura, o ocurre un error de I/O.
	 */
	public Configuration readJson() {
		try (FileReader reader = new FileReader(path)) {
			return gson.fromJson(reader, Configuration.class);
		} catch (IOException e) {
			System.err.println("Error al leer la configuración: " + e.getMessage());
			return null;
		}
	}
}