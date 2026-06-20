-- Archivo de datos de prueba (Contexto Ecuatoriano)
-- Total ~100 registros repartidos en las tablas

-- ==========================================
-- 1. USUARIOS (10 registros)
-- ==========================================
INSERT INTO usuario (nombre, email, telefono, password) VALUES
('carlos zambrano', 'carlos.zambrano@gmail.com', '0991234567', 'password123'),
('maría fernanda lópez', 'mafer.lopez@hotmail.com', '0987654321', 'password123'),
('jorge salto', 'jsalto_gye@yahoo.com', '0971122334', 'password123'),
('andrea menéndez', 'andream.1990@gmail.com', '0969988776', 'password123'),
('luis sánchez', 'luis.sanchezuio@outlook.com', '0995544332', 'password123'),
('paola chávez', 'pao_chavez_ec@gmail.com', '0988877665', 'password123'),
('roberto vera', 'roberto.vera95@hotmail.com', '0977766554', 'password123'),
('diana morales', 'diana.morales.ec@gmail.com', '0966655443', 'password123'),
('kevin villao', 'kevin.villao.dev@gmail.com', '0994433221', 'password123'),
('stefany espinoza', 'stefany.esp@outlook.com', '0983322110', 'password123');

-- ==========================================
-- 2. CATEGORÍAS (10 registros)
-- ==========================================
INSERT INTO categoria (nombre) VALUES
('alimentación'),
('transporte'),
('vivienda'),
('servicios básicos'),
('educación'),
('salud'),
('entretenimiento'),
('ropa y calzado'),
('ahorro e inversiones'),
('pagos financieros');

-- ==========================================
-- 3. SERVICIOS (50 registros)
-- ==========================================
-- Servicios Básicos (Categoría 4)
INSERT INTO servicio (id_categoria, nombre) VALUES
(4, 'agua potable interagua'),
(4, 'agua potable epmaps uio'),
(4, 'agua potable etapa cuenca'),
(4, 'energía eléctrica cnel'),
(4, 'energía eléctrica eeq'),
(4, 'energía eléctrica centrasur'),
(4, 'internet netlife'),
(4, 'internet y tv claro'),
(4, 'internet xtrim tvcable'),
(4, 'internet celerity');

-- Transporte (Categoría 2)
INSERT INTO servicio (id_categoria, nombre) VALUES
(2, 'pasaje metrovía'),
(2, 'pasaje ecovía / trole'),
(2, 'pasaje tranvía cuenca'),
(2, 'carrera uber'),
(2, 'carrera indriver'),
(2, 'gasolina súper (petroecuador)'),
(2, 'gasolina extra/ecopaís'),
(2, 'mantenimiento vehículo'),
(2, 'peajes panamericana'),
(2, 'pasaje bus interprovincial (cooperativa)');

-- Vivienda (Categoría 3)
INSERT INTO servicio (id_categoria, nombre) VALUES
(3, 'arriendo departamento'),
(3, 'alícuota urbanización'),
(3, 'mantenimiento casa'),
(3, 'pago predial municipio'),
(3, 'seguro de hogar');

-- Salud (Categoría 6)
INSERT INTO servicio (id_categoria, nombre) VALUES
(6, 'consulta médica fybeca'),
(6, 'medicinas cruz azul'),
(6, 'medicinas sana sana'),
(6, 'seguro médico saludsa'),
(6, 'seguro médico bmi');

-- Educación (Categoría 5)
INSERT INTO servicio (id_categoria, nombre) VALUES
(5, 'pensión universidad uees'),
(5, 'pensión universidad usfq'),
(5, 'pensión colegio'),
(5, 'cursos de inglés ceca'),
(5, 'útiles escolares paco');

-- Entretenimiento (Categoría 7)
INSERT INTO servicio (id_categoria, nombre) VALUES
(7, 'suscripción netflix'),
(7, 'suscripción spotify premium'),
(7, 'entradas supercines'),
(7, 'entradas cinemark'),
(7, 'cena en kfc');

-- Alimentación (Categoría 1)
INSERT INTO servicio (id_categoria, nombre) VALUES
(1, 'compras supermercado supermaxi'),
(1, 'compras supermercado mi comisariato'),
(1, 'compras tuti'),
(1, 'comida a domicilio pedidosya'),
(1, 'compras mercado central');

-- Pagos Financieros (Categoría 10)
INSERT INTO servicio (id_categoria, nombre) VALUES
(10, 'pago tarjeta pacificard'),
(10, 'pago tarjeta visa pichincha'),
(10, 'pago préstamo biess'),
(10, 'pago cuota auto (chevyplan)'),
(10, 'seguro vehicular (equinoccial)');

-- ==========================================
-- 4. CUENTAS (30 registros)
-- (Repartidas entre los 10 usuarios y el usuario admin id=1)
-- ==========================================
INSERT INTO cuenta (id_usuario, tipo_cuenta, nombre) VALUES
(1, 'Dinero Disponible', 'banco pichincha - ahorros'),
(1, 'Tarjetas de Crédito', 'visa pacificard (banco del pacífico)'),
(1, 'Dinero Disponible', 'banco guayaquil - corriente'),
(1, 'Deudas/Préstamos', 'préstamo quirografario biess'),
(2, 'Dinero Disponible', 'banco produbanco - cuenta ideal'),
(2, 'Dinero Disponible', 'banco bolivariano - cuenta ahorros'),
(2, 'Tarjetas de Crédito', 'mastercard produbanco'),
(3, 'Dinero Disponible', 'banco pichincha - ahorros'),
(3, 'Tarjetas de Crédito', 'visa titanium'),
(3, 'Inversiones/Ahorros', 'póliza banco pichincha'),
(4, 'Dinero Disponible', 'cooperativa jep - ahorros'),
(4, 'Dinero Disponible', 'banco pacífico - ahorros mi banco'),
(4, 'Tarjetas de Crédito', 'diners club'),
(5, 'Dinero Disponible', 'banco internacional - ahorros'),
(5, 'Deudas/Préstamos', 'crédito vehicular banco pichincha'),
(6, 'Dinero Disponible', 'banco austro - ahorros'),
(6, 'Inversiones/Ahorros', 'fondo de inversión fiduoccidente'),
(7, 'Dinero Disponible', 'cooperativa policía nacional - ahorros'),
(7, 'Tarjetas de Crédito', 'visa banco guayaquil'),
(8, 'Dinero Disponible', 'banco solidario - ahorros'),
(8, 'Tarjetas de Crédito', 'alia banco solidario'),
(9, 'Dinero Disponible', 'banco pichincha - corriente'),
(9, 'Deudas/Préstamos', 'crédito educativo pacífico'),
(10, 'Dinero Disponible', 'banco guayaquil - ahorros'),
(10, 'Tarjetas de Crédito', 'american express guayaquil'),
(1, 'Inversiones/Ahorros', 'ahorro programado pichincha'),
(2, 'Deudas/Préstamos', 'hipotecario mutualista pichincha'),
(3, 'Dinero Disponible', 'billetera móvil (bimo)'),
(4, 'Dinero Disponible', 'efectivo en caja'),
(5, 'Tarjetas de Crédito', 'discover');
