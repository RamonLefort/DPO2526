# ☕ Coffee Clicker - Proyecto DPOO 25/26

## 📖 Descripción
Coffee Clicker es un videojuego incremental desarrollado en Java, inspirado en el popular género "Clicker". Este proyecto destaca por su implementación rigurosa de una **arquitectura multicapa** (Presentación, Business y Persistencia), diseñada para mantener el código limpio, escalable y facilitar su mantenimiento, aplicando buenas prácticas de Ingeniería de Software.

## ✨ Características Principales
- **Sistema de Cuentas:** Registro e inicio de sesión de usuarios con seguridad integrada (hashing de contraseñas mediante `jBCrypt`).
- **Mecánicas de Juego (Core Loop):** Obtención de "café" mediante clics manuales, y automatización a través de la compra de Generadores y Mejoras (Upgrades).
- **Dashboard de Estadísticas:** Visualización del rendimiento y progreso del jugador a lo largo del tiempo mediante gráficas personalizadas (`StatGraphPanel`).
- **Persistencia Híbrida:** - Almacenamiento del progreso, inventario y cuentas en una base de datos relacional (**MySQL**).
    - Carga de configuraciones de entorno dinámicas mediante archivos JSON.

## 🏗️ Arquitectura del Sistema
El proyecto está estrictamente desacoplado en tres capas (separación de responsabilidades):
1. **Presentation (Presentación):** Interfaces gráficas nativas construidas con Java Swing (ej. `GameView`, `LoginWindow`) y sus controladores (`GameController`, `LoginController`).
2. **Business (Lógica de Negocio):** Corazón de la aplicación. Contiene las entidades del dominio (`User`, `Game`, `Generator`, `Upgrade`) y los gestores lógicos (`GameLogic`, `UserLogic`, hilos con `GeneratorThread`).
3. **Persistence (Persistencia):** Implementación del patrón DAO (Data Access Object) para abstraer el acceso a datos (`MySQLDAO`, `UserDAO`, `JsonConfigurationDAO`), facilitando el intercambio de la fuente de datos si fuera necesario en el futuro.

## 🛠️ Tecnologías y Dependencias
- **Lenguaje:** Java
- **UI:** Java Swing
- **Base de Datos:** MySQL
- **Librerías (incluidas en `/lib`):**
    - `mysql-connector-j-8.3.0.jar`: Driver JDBC para conexión con la base de datos.
    - `gson-2.11.0.jar`: Serialización y deserialización de la configuración en JSON.
    - `mindrot_jbcrypt.xml` (dependencia configurada): Para el salting y hashing seguro de credenciales.

## 🚀 Guía de Instalación y Despliegue

### Requisitos Previos
- **Java Development Kit (JDK)** 23.
- **Servidor MySQL:** XAMPP.
- **IDE:** IntelliJ IDEA.

### Pasos de Configuración
1. **Preparar la Base de Datos:**
   Importa y ejecuta el script `coffeeclicker.sql` en tu gestor de base de datos MySQL. Esto creará el esquema, las tablas necesarias y cualquier dato semilla requerido para el juego.
> El programa tiene una función que se encargará de crear la base de datos automáticamente, solo hay que seguir estos pasos si la generación automática falla.
2. **Configurar el Entorno:**
   Abre el archivo `config.json` situado en la raíz del proyecto y ajusta los parámetros de conexión a tu base de datos local (puerto, usuario, contraseña e IP).
> El programa ya tiene los datos introducidos, pero en caso de querer cambiarlos deberá seguir estos pasos
3. **Vincular Librerías:**
   Asegúrate de que los archivos `.jar` de la carpeta `lib/` estén correctamente añadidos al *Classpath* / *Libraries* de tu entorno de desarrollo.
> Las librerías deberían instalarse al descargar el proyecto, pero en caso de no suceder deberá seguir estos pasos
4. **Ejecutar:**
   Inicia la aplicación ejecutando la clase `Main.java` ubicada en el directorio raíz.

## 📂 Estructura de Directorios Principal
```
DPO2526-master/
├── assets/          # Recursos gráficos e imágenes (iconos, GIFs)
├── Bussiness/       # Entidades de dominio, excepciones lógicas y Managers
├── lib/             # Dependencias de terceros (.jar)
├── Persistance/     # Implementación del patrón DAO (SQL y JSON)
├── Presentation/    # Vistas (Java Swing) y Controladores
├── config.json      # Archivo de configuración del sistema
├── coffeeclicker.sql# Script de inicialización de la Base de Datos
└── Main.java        # Punto de entrada de la aplicación
```