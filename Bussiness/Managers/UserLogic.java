package Bussiness.Managers;

import Bussiness.Entities.User;
import Bussiness.Exceptions.BusinessException;
import Persistance.DAO.UserDAO;
import Persistance.Exceptions.PersistanceException;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Clase de lógica de negocio encargada de la gestión de cuentas de usuario y seguridad.
 */
public class UserLogic {

	private UserDAO userDAO;
	private User currentUser;

	/**
	 * Constructor que inicializa los servicios de persistencia necesarios para la gestión de usuarios.
	 *
	 * @param userDAO Acceso a datos de la entidad de usuario.
	 */
	public UserLogic(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * Procesa el registro de un nuevo usuario aplicando reglas de validación y seguridad.
	 *
	 * @param username Nombre de usuario único deseado.
	 * @param email Correo electrónico de contacto.
	 * @param password Contraseña proporcionada por el usuario.
	 * @param confirm Repetición de la contraseña para validación de coincidencia.
	 * @return true si el registro fue exitoso tras aplicar el hashing de BCrypt; false en caso contrario.
	 * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
	public boolean register(String username, String email, String password, String confirm) throws BusinessException {
		if (!password.equals(confirm) || !validateEmail(email) || !validatePassword(password)) {
			return false;
		}
        try {
            if (userDAO.usernameExists(username)) return false;
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
        try {
            if (userDAO.emailExists(email)) return false;
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }

        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        try {
            return userDAO.create(new User(username, email, hashed));
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }

	/**
	 * Gestiona el proceso de inicio de sesión mediante la comparación de hashes.
	 *
	 * @param usernameOrEmail Identificador proporcionado (usuario o correo).
	 * @param pass Contraseña en texto plano a verificar.
	 * @return El objeto {@link User} autenticado si las credenciales coinciden; null en caso de error.
	 * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
	public User login(String usernameOrEmail, String pass) throws BusinessException {
        User user = null;
        try {
            user = userDAO.loginCheck(usernameOrEmail);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
        if (user != null && BCrypt.checkpw(pass, user.getPassword())) {
			this.currentUser = user;
			return user;
		}
		return null;
	}

	/**
	 * Comprueba la disponibilidad de un nombre de usuario en el sistema.
	 *
	 * @param username Nombre a verificar.
	 * @return true si ya existe en la base de datos; false en caso contrario.
	 * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
	public boolean usernameExists(String username) throws BusinessException {
        try {
            return userDAO.usernameExists(username);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }

	/**
	 * Comprueba si una dirección de correo ya está vinculada a una cuenta.
	 *
	 * @param email Correo a verificar.
	 * @return true si el correo ya está en uso.
	 * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
	public boolean emailExists(String email) throws BusinessException {
        try {
            return userDAO.emailExists(email);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }

	/**
	 * Finaliza la sesión del usuario actual eliminando su referencia en memoria.
	 */
	public void logout() {
		this.currentUser = null;
	}

	/**
	 * Elimina de forma permanente la cuenta de usuario y sus configuraciones asociadas.
	 * Si el usuario eliminado es el que tiene la sesión activa, se procede al cierre automático de la misma.
	 *
	 * @param username Nombre del usuario cuya cuenta se desea eliminar.
	 * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
	public void deleteAccount(String username) throws BusinessException {
        try {
            userDAO.delete(username);
        } catch (PersistanceException e) {
            throw new BusinessException(e.getCause());
        }
        if (currentUser != null && currentUser.getUsername().equals(username)) {
			logout();
		}
	}

	/**
	 * Verifica que el formato del correo electrónico sea válido bajo criterios específicos del dominio.
	 * Requiere que el correo finalice en "@gmail.com" y contenga caracteres previos al símbolo.
	 *
	 * @param email Cadena con el correo a evaluar.
	 * @return true si el formato es correcto según las reglas de negocio.
	 */
	public boolean validateEmail(String email) {
		return email != null
				&& email.endsWith("@gmail.com")
				&& email.indexOf("@") > 0;
	}

	/**
	 * Evalúa la robustez de la contraseña propuesta por el usuario.
	 * Los criterios de seguridad incluyen una longitud mínima de 8 caracteres,
	 * presencia de dígitos, letras minúsculas y letras mayúsculas.
	 *
	 * @param password Cadena con la contraseña a validar.
	 * @return true si cumple con todos los requisitos de complejidad; false en caso contrario.
	 */
	public boolean validatePassword(String password) {
		if (password == null || password.length() < 8){
			return false;
		}

		boolean hasLetter    = false;
		boolean hasDigit     = false;
		boolean hasMLetter = false;

		for (int i = 0; i < password.length(); i++) {
			char c = password.charAt(i);
			if (c >= 'a' && c <= 'z') {
				hasLetter    = true;
			}
			if (c >= 'A' && c <= 'Z') {
				hasLetter  = true; hasMLetter = true; }
			if (c >= '0' && c <= '9'){
				hasDigit     = true;
			}
		}
		if (hasLetter == false) {
			return false;
		}
		if (hasMLetter == false) {
			return false;
		}
		if (hasDigit == false) {
			return false;
		}
		return true;
	}

	/**
	 * Obtiene el perfil del usuario que mantiene la sesión activa.
	 *
	 * @return Objeto {@link User} del usuario actual, o null si no hay sesión iniciada.
	 */
	public User getCurrentUser() {
		return currentUser;
	}
}
