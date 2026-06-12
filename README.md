# Estándares y Arquitectura Base del Proyecto

Este documento proporciona una guía detallada sobre la arquitectura, estructura, estándares de codificación y tecnologías extraídas del proyecto "Gestión de Cuentas". El objetivo de este README no es replicar la misma aplicación, sino servir como **base de conocimiento y guía de estilo estricta** para el trabajo en grupo y para que cualquier Inteligencia Artificial o desarrollador pueda crear **nuevos proyectos** asegurando exactamente la misma filosofía de código, patrones de diseño y estándares visuales.

## 1. Arquitectura del Proyecto

Los proyectos bajo este estándar deben estructurarse utilizando un patrón de diseño basado en capas, separando claramente la vista, el modelo y el acceso a datos. 
Las capas principales a implementar son:
- **`vista`**: Contiene todas las clases relacionadas con la interfaz gráfica de usuario (GUI). Ninguna lógica de negocio o de base de datos debe estar aquí.
- **`modelo`**: Contiene las clases que representan las entidades del negocio (ej. `EntidadPrincipal`). Estas clases manejan la lógica de validación de negocio y delegan la persistencia a la capa de datos.
- **`datos`**: Contiene la lógica de acceso a la base de datos (CRUD). Las clases aquí manejan sentencias SQL y conexiones. Se identifican por el sufijo `MD` (Model Data, ej. `EntidadPrincipalMD`).

## 2. Tecnologías y Herramientas Estándar
- **Lenguaje**: Java.
- **Interfaz Gráfica**: Java Swing (`JFrame`, `JPanel`, `JTabbedPane`, `JTable`, etc.).
- **Base de Datos**: PostgreSQL.
- **Conectividad a BD**: JDBC (Java Database Connectivity) usando el driver `org.postgresql.Driver`.
- **Configuración**: Uso obligatorio de un archivo `db.properties` ubicado en el classpath (raíz de los recursos) para gestionar credenciales y URL de la base de datos de manera externa al código fuente.

## 3. Estructura de Directorios (Ejemplo de Referencia)

```text
src/
├── datos/
│   ├── Conexion.java       # Maneja la conexión Singleton a PostgreSQL
│   ├── EntidadUnoMD.java   # Persistencia de la primera entidad
│   └── EntidadDosMD.java   # Persistencia de la segunda entidad
├── modelo/
│   ├── EntidadUno.java     # Lógica de negocio y definición de entidad
│   └── EntidadDos.java     # Lógica de negocio y definición de entidad
└── vista/
    ├── MenuPrincipal.java  # Ventana principal o menú de navegación
    ├── VentanaEntidadUno.java # CRUD visual interactivo
    └── VentanaEntidadDos.java # CRUD visual interactivo
```

## 4. Patrones de Diseño a Utilizar
1. **Singleton**: Utilizado exclusivamente en la clase `Conexion.java` para asegurar que solo exista una instancia de conexión a la base de datos en toda la aplicación y manejar reconexiones automáticas si es necesario.
2. **Delegación y Capas (Data Access)**: La capa `modelo` **no ejecuta** código SQL directamente. Utiliza métodos de las clases `MD` instanciadas en la capa `datos`. Por ejemplo, la clase modelo delega a su clase `MD` para las operaciones transaccionales y retorna los resultados booleanos o colecciones de datos a la vista.

## 5. Estándares de Codificación (Estrictos)

### 5.1 Nomenclatura (Naming Conventions)
- **Clases e Interfaces**: PascalCase (Ej. `VentanaClientes`, `ClienteMD`).
- **Atributos y Variables Locales**: camelCase (Ej. `idCliente`, `tipoCliente`).
- **Constantes**: UPPER_SNAKE_CASE (Ej. `ESTADO_ACTIVO`, `ARCHIVO_CONFIG`).
- **Componentes de Interfaz Gráfica (Prefijos)**: Se debe utilizar una notación estricta para identificar el tipo de componente en la vista:
  - `btn` para `JButton` (Ej. `btnGuardarAgregar`).
  - `txt` para `JTextField` (Ej. `txtNombreAgregar`).
  - `cbo` para `JComboBox` (Ej. `cboTipoAgregar`).
  - `lbl` para `JLabel` (Ej. `lblMensajeConsulta`).
  - `tabla` para `JTable` (Ej. `tablaModificar`).
  - `modelo` para `DefaultTableModel` (Ej. `modeloModificar`).

### 5.2 Estructura de las Clases (Entidades del Modelo)
- **Atributos privados** inicializados con valores por defecto.
- **Constantes** para valores predeterminados estrictos (ej. arreglos con estados válidos).
- **Getters y Setters** para el encapsulamiento. Los setters para textos deben contener lógica de normalización y limpieza (ej. aplicar `.trim().toLowerCase()`).
- **Métodos de Verificación (Lógica de Negocio)**: Validación de los datos antes de invocar la capa de persistencia (Ej. `verificarDP()`, `verificarDependencias()`).
- **Métodos de Delegación (Sufijo DP)**: Métodos que hacen puente con la capa de datos. La abreviatura "DP" se utiliza a nivel interno para designar peticiones que se delegan a la capa "MD" (Ej. `grabarDP()`, `modificarDP()`, `borrarDP()`).

### 5.3 Estructura de las Clases (Vista)
- Heredan de `JFrame`.
- Invocan los métodos `initComponentes()` e `initEventos()` desde el constructor.
- Uso extenso de **Expresiones Lambda** (`e -> {...}`) para capturar los eventos y acciones (ActionListeners / ListSelectionListeners).
- La organización principal de las ventanas CRUD debe basarse en un `JTabbedPane` con cuatro pestañas estándar: **Agregar, Modificar, Eliminar, Consultar**.
- Empleo del administrador `GridBagLayout` para precisión en la ubicación de formularios y `BorderLayout` para la distribución general de la pestaña.
- Se recomienda el uso de iconos en formato texto o emojis en la UI para una estética más moderna y clara (ej. "➕ Agregar", "💾 Guardar").
- Uso de componentes visuales personalizados (ej. colores en botones usando RGB: `new Color(46, 204, 113)`).
- Interacción y validación con el usuario asegurada a través de diálogos informativos centralizados (método `mostrarMensaje` usando `JOptionPane`).

### 5.4 Estructura de las Clases (Datos / MD)
- Se nombran con el nombre de la entidad seguido del sufijo `MD` (Ej. `ClienteMD`).
- Los métodos CRUD reciben como parámetro el objeto del modelo respectivo (Ej. `insertar(Cliente cliente)`).
- Retornan `boolean` para operaciones DML (INSERT, UPDATE, DELETE) y `ResultSet` (o `List`) para consultas (SELECT).
- **Consultas parametrizadas**: Uso obligatorio de `PreparedStatement` para prevenir inyección SQL.

### 5.5 Documentación y Comentarios
- **JavaDoc a nivel de Clase**: Obligatorio, indicando descripción de la clase, la capa a la que pertenece y la autoría.
- **JavaDoc en Métodos**: Obligatorio en métodos principales y lógicos. Se debe usar `@param` y `@return` con descripciones de comportamiento.
- **Comentarios Separadores In-Line**: Utilizados para dividir visualmente bloques importantes de eventos en la vista (Ej. `// ── Pestaña Modificar ──────────────────────────────────────────────`).

## 6. Lógica Específica y Comportamientos Esperados
- **Prevención de Errores Visuales**: Las tablas (`JTable`) se deben hacer inmutables sobrescribiendo dinámicamente el método `isCellEditable(int row, int col)` para devolver `false`, impidiendo que el usuario pueda escribir en ellas.
- **Borrado Inteligente / Controlado**: Antes de permitir un borrado de una entidad, el sistema debe verificar si tiene elementos hijos o historial asociado. Solo después se permite el borrado usando `JOptionPane.showConfirmDialog()` para reafirmar la acción.
- **Formularios Dinámicos**: Los componentes en pestañas como "Modificar" se deben autocompletar escuchando las selecciones dentro de la tabla mediante `ListSelectionListener`.

## 7. Estándares de Programación

Con el fin de garantizar un código legible, mantenible y coherente a lo largo de todo el proyecto, se establecen los siguientes estándares de programación que deben ser aplicados de manera obligatoria por todos los integrantes del equipo de desarrollo.

### 7.1 Reglas de Nomenclatura

**Clases**
Los nombres de las clases se escriben en formato PascalCase, comenzando siempre con letra mayúscula. El nombre debe ser un sustantivo que describa claramente la entidad o responsabilidad de la clase.

| Correcto | Incorrecto |
|----------|------------|
| Cuenta | cuenta |
| CuentaMD | cuentaMD |
| VentanaCuentas | ventana_cuentas |
| MenuPrincipal | menuprincipal |

### 7.2 Métodos

Los métodos se escriben en formato camelCase, comenzando con letra minúscula. El nombre debe iniciar con un verbo que indique la acción que realiza.

| Correcto | Incorrecto |
|----------|------------|
| verificarDP() | VerificarDP() |
| limpiarDatos() | limpiar_datos() |
| getNombre() | ObtenerNombre() |
| setTipoCuenta() | Set_TipoCuenta() |

### 7.3 Variables y atributos

Las variables locales y los atributos de instancia se escriben en formato camelCase. Los nombres deben ser descriptivos y evitar abreviaciones ambiguas.

| Correcto | Incorrecto |
|----------|------------|
| idCuenta | id |
| tipoCuenta | tc |
| resultSet | rs2 |
| nombreServicio | nS |

### 7.4 Paquetes

Los paquetes se escriben completamente en minúsculas, sin guiones ni caracteres especiales.

```java
package vista;
package modelo;
package datos;
```

### 7.5 Estructura del Proyecto

La estructura de carpetas y archivos del proyecto es la siguiente:

```text
GestionDeCuentas/
├── .vscode/
│   ├── settings.json
├── bin/
│   ├── db.properties
└── src/
    ├── vista/
    ├── modelo/
    └── datos/
```

El archivo `db.properties` centraliza todos los parámetros de conexión a la base de datos y se ubica fuera del código fuente para permitir su modificación sin necesidad de recompilar el proyecto. Su contenido es el siguiente:

```properties
# db.properties — parámetros de conexión a PostgreSQL
db.host=localhost
db.puerto=5432
db.nombre=gestion_cuentas
db.usuario=postgres
db.password=postgres
```

La clase `Conexion.java` es la única del proyecto que lee este archivo, utilizando `getClassLoader().getResourceAsStream()` para cargarlo desde el classpath. Ninguna otra clase accede a parámetros de configuración directamente.

### 7.6 Estructura y Estilo del Código

Se utiliza una indentación de 4 espacios por cada nivel de anidamiento. No se permiten tabuladores. Todo bloque de código encerrado entre llaves debe estar correctamente indentado respecto a su contenedor.

```java
public boolean verificarDP() {
    if (nombre == null || nombre.isEmpty()) {
        return false;
    }
    if (!tipoCuenta.equals("Dinero Disponible") &&
        !tipoCuenta.equals("Tarjetas de Crédito") &&
        !tipoCuenta.equals("Deudas/Préstamos") &&
        !tipoCuenta.equals("Inversiones/Ahorros")) {
        return false;
    }
    return true;
}
```

**Llaves**
Las llaves de apertura se colocan al final de la misma línea de la declaración, nunca en una línea aparte. Las llaves de cierre van siempre en su propia línea.

```java
// Correcto
public void limpiarDatos() {
    idCuenta = 0;
    nombre = "";
}

// Incorrecto
public void limpiarDatos()
{
    idCuenta = 0;
}
```

**Longitud de línea**
Ninguna línea de código debe superar los 100 caracteres. Si una sentencia es demasiado larga, se divide en múltiples líneas aplicando indentación adicional de 8 espacios en las líneas de continuación.

**Espaciado**
Se coloca un espacio antes y después de cada operador. Se deja una línea en blanco entre métodos para mejorar la legibilidad. No se dejan líneas en blanco innecesarias dentro de un mismo método.

### 7.7 Buenas Prácticas

**Comentarios**
Cada clase debe incluir un comentario de encabezado en formato Javadoc que describa su propósito, la capa a la que pertenece y el autor:

```java
/**
 * Clase de acceso a datos para la entidad Cuenta.
 * Capa: Datos (MD).
 * Gestiona las operaciones CRUD sobre la tabla cuenta en PostgreSQL.
 *
 * @author
 */
public class CuentaMD {
```

Los métodos cuya lógica no sea evidente deben incluir un comentario que explique su propósito, parámetros y valor de retorno:

```java
/**
 * Verifica si una cuenta tiene pagos activos asociados en la base de datos.
 * Se utiliza para bloquear la eliminación de cuentas con movimientos.
 *
 * @param cuenta objeto Cuenta con el idCuenta a verificar
 * @return true si existen pagos asociados, false en caso contrario
 */
public boolean verificarMovimientos(Cuenta cuenta) {
```

**Responsabilidad única**
Cada clase tiene una única responsabilidad definida por su capa. Las clases de la vista no contienen lógica de negocio ni sentencias SQL. Las clases MD no contienen lógica de validación de negocio. Las clases del modelo no acceden directamente a la base de datos. La clase `Conexion.java` es la única autorizada para leer el archivo `db.properties` y gestionar la conexión a PostgreSQL.

**Parámetros mediante objetos**
Ningún método acepta tipos primitivos sueltos como parámetros. Toda la información se transmite entre capas mediante objetos instanciados de las clases del modelo.

**Manejo de excepciones**
Todo bloque de código que interactúe con la base de datos debe estar encerrado en un bloque `try-catch` que capture `SQLException`. El error debe registrarse en consola y el método debe retornar `false` o `null` para que la vista notifique al usuario correctamente.

```java
try {
    PreparedStatement ps = Conexion.getConexion().prepareStatement(sql);
    ps.setString(1, cuenta.getNombre());
    return ps.executeUpdate() > 0;
} catch (SQLException e) {
    e.printStackTrace();
    return false;
}
```

---

## 🤖 Contexto para la Inteligencia Artificial (System Prompt)
Si actúas como un asistente de código para un nuevo proyecto de este grupo, **DEBES** seguir estrictamente las siguientes reglas:
1. Implementarás SIEMPRE la arquitectura en tres capas (`modelo`, `vista`, `datos`).
2. Al diseñar vistas en Java Swing, usarás `JTabbedPane` con operaciones CRUD (Agregar, Modificar, Eliminar, Consultar).
3. Aplicarás los prefijos de componentes en todas las variables visuales (`btn`, `txt`, `lbl`, `cbo`, `tabla`, `modelo`).
4. Toda validación de datos se hará en la capa `modelo` mediante métodos como `verificarDP()`.
5. La conexión a base de datos debe ser PostgreSQL, en la capa de `datos`, usando `PreparedStatement` y el patrón Singleton en una clase `Conexion.java` externa.
6. Nunca mezcles sentencias SQL en la capa `vista` ni en la capa `modelo`.
7. Agrega JavaDoc completo en español a las clases y métodos, explicando su propósito, parámetros y retornos.
