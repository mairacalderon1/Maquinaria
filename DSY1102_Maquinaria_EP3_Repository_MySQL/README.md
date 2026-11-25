# DSY1102 – Maquinaria EP3 (versión Repository + MySQL)

Versión adaptada del proyecto Maquinaria_EP3_Base_SOL proporcionado por el docente. Mantiene JavaFX, FXML, herencia/polimorfismo, Certificable, formularios, tabla, cálculos y CRUD. La persistencia se hace **exclusivamente en `MaquinaRepository`**, sin DAO ni capa Service.

## Requisitos
- JDK 21 e IntelliJ IDEA Community
- XAMPP con servicio MySQL iniciado (en muchas instalaciones XAMPP incluye MariaDB; el driver MySQL no garantiza compatibilidad total)
- MySQL Workbench o phpMyAdmin para ejecutar el SQL
- Maven y acceso a las dependencias Maven

## Preparación
1. Abrir XAMPP y activar MySQL. Comprobar el puerto (por defecto 3306).
2. En MySQL Workbench conectarse al servidor local. Si Workbench presenta incompatibilidades con el motor de XAMPP, ejecutar el SQL en phpMyAdmin.
3. Ejecutar `scripts/maquinaria_mysql.sql`. **Advertencia: el script incluye DROP DATABASE IF EXISTS maquinaria, por lo que borra los datos anteriores.**
4. Abrir el proyecto como Maven en IntelliJ, configurar JDK 21 y recargar Maven.
5. Ajustar URL, USUARIO y CLAVE en `src/main/java/cl/duoc/maquinaria/conexion/ConexionBD.java`. Valores de demostración: URL `jdbc:mysql://localhost:3306/maquinaria`, usuario `root`, contraseña vacía. Para producción, usar un usuario dedicado y credenciales fuera del código.
6. Ejecutar `cl.duoc.maquinaria.Launcher` desde el botón Run o `mvn clean javafx:run`.
7. Probar registrar Excavadora y Cargador Frontal; editar, eliminar y listar. Comprobar en Workbench con `SELECT * FROM maquinaria.maquina ORDER BY id;`.

## Organización de código
- `Launcher.java`: punto de entrada para IntelliJ
- `App.java`: carga FXML
- `controller/MainController.java`: eventos, validaciones e interfaz JavaFX
- `model/`: Maquina, Excavadora, CargadorFrontal, Certificable
- `repository/MaquinaRepository.java`: SELECT, INSERT, UPDATE y DELETE con JDBC, PreparedStatement y ResultSet
- `conexion/ConexionBD.java`: creación de conexiones JDBC
- `view/main-view.fxml`: interfaz original
- `scripts/maquinaria_mysql.sql`: tablas y datos iniciales

## Dependencia JDBC
Se usa `com.mysql:mysql-connector-j:8.0.33` (no MariaDB Connector/J). Maven se ocupa de descargarla; no hay que agregar manualmente un .jar.

## Consideraciones
- Se respetan las reglas de edición de la solución original: al editar un registro, el tipo de máquina no puede cambiarse.
- `SQLException` viaja desde Repository al Controller para mostrar errores claros al usuario.
- El script utiliza CHECK y claves foráneas; el comportamiento depende de las versiones del servidor de base de datos.
- Se eliminaron archivos compilados, configuración local de IntelliJ y el script Oracle para mantener una entrega limpia orientada a MySQL.
