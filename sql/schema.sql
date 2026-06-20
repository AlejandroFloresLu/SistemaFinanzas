-- ============================================================
-- Schema DDL — Sistema de Gestión Financiera
-- Base de datos: gestion_financiera (PostgreSQL)
-- ============================================================

-- Tabla: usuario
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario   SERIAL       PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    email        VARCHAR(100),
    telefono     VARCHAR(20),
    password     VARCHAR(100),
    estado       BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Tabla: cuenta (FK → usuario)
CREATE TABLE IF NOT EXISTS cuenta (
    id_cuenta    SERIAL        PRIMARY KEY,
    id_usuario   INT           NOT NULL,
    tipo_cuenta  VARCHAR(50)   NOT NULL,
    nombre       VARCHAR(100)  NOT NULL,
    estado       BOOLEAN       NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_cuenta_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- Tabla: categoria
CREATE TABLE IF NOT EXISTS categoria (
    id_categoria SERIAL       PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    estado       BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Tabla: servicio (FK → categoria)
CREATE TABLE IF NOT EXISTS servicio (
    id_servicio    SERIAL        PRIMARY KEY,
    id_categoria   INT           NOT NULL,
    nombre         VARCHAR(100)  NOT NULL,
    estado         BOOLEAN       NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_servicio_categoria
        FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

-- Tabla: pago (estructura base para FK futuras, no se implementa en Ciclo 1)
CREATE TABLE IF NOT EXISTS pago (
    id_pago      SERIAL        PRIMARY KEY,
    id_cuenta    INT           NOT NULL,
    id_servicio  INT           NOT NULL,
    monto        DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    fecha        DATE          NOT NULL DEFAULT CURRENT_DATE,
    descripcion  TEXT,
    estado       BOOLEAN       NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_pago_cuenta
        FOREIGN KEY (id_cuenta) REFERENCES cuenta(id_cuenta),
    CONSTRAINT fk_pago_servicio
        FOREIGN KEY (id_servicio) REFERENCES servicio(id_servicio)
);

-- Usuario por defecto (sesión hardcodeada id=1)
INSERT INTO usuario (nombre, email, telefono, password, estado)
VALUES ('administrador', 'admin@sistema.com', '0000000000', 'admin', TRUE);
