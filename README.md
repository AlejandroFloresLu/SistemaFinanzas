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
