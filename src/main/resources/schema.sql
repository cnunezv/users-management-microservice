-- =============================================
-- Script de creación de la base de datos
-- Gestión de Usuarios - Arquitectura Hexagonal
-- =============================================

CREATE DATABASE IF NOT EXISTS crud_usuarios
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE crud_usuarios;

CREATE TABLE IF NOT EXISTS users (
    id          VARCHAR(36)  NOT NULL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        ENUM('ADMIN', 'MEMBER', 'REVIEWER') NOT NULL,
    status      ENUM('ACTIVE', 'INACTIVE', 'PENDING', 'BLOCKED') NOT NULL DEFAULT 'PENDING',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Usuario administrador inicial (password: Admin1234!)
-- INSERT IGNORE hace el script idempotente: se puede ejecutar varias veces.
INSERT IGNORE INTO users (id, name, email, password, role, status)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'Administrador',
    'admin@example.com',
    '$2a$12$sfwkajsls.fktQb0Ngw5cOiSiV3z4LIwbucSAy4sw2wyNlxbUBt4q',
    'ADMIN',
    'ACTIVE'
);

-- =============================================
-- Usuarios de prueba, uno por rol y uno sin activar
--   member@example.com    / Member1234!    MEMBER    ACTIVE
--   reviewer@example.com  / Reviewer1234!  REVIEWER  ACTIVE
--   pending@example.com   / Pending1234!   MEMBER    PENDING
-- El usuario PENDING demuestra que el login rechaza cuentas no activas.
-- =============================================
INSERT IGNORE INTO users (id, name, email, password, role, status)
VALUES
    ('00000000-0000-0000-0000-000000000002', 'María Pérez', 'member@example.com',
     '$2a$12$99VR6uUuQlrfacGFNNh8rO34z8pUsZ6nl3qxOvdK3X7KQ/in/E1t2', 'MEMBER', 'ACTIVE'),
    ('00000000-0000-0000-0000-000000000003', 'Revisor de Prueba', 'reviewer@example.com',
     '$2a$12$peJmKBccaG5wd2QncUwRVOlCbgwLilnErRegGb83nCfQi3ut4tXnm', 'REVIEWER', 'ACTIVE'),
    ('00000000-0000-0000-0000-000000000004', 'Usuario Pendiente', 'pending@example.com',
     '$2a$12$dGIgd7.s.qfFpy3sB4xmOuFBMsmVdNOiy4CUSGs16utmRXF7MmUPS', 'MEMBER', 'PENDING');

-- =============================================
-- Ejercicio 19: carreras de taxi
-- =============================================
CREATE TABLE IF NOT EXISTS carreras_taxi (
    id                  VARCHAR(36)       NOT NULL PRIMARY KEY,
    cliente             VARCHAR(100)      NOT NULL,
    taxi                VARCHAR(6)        NOT NULL COMMENT 'Placa del taxi, formato ABC123',
    kilometros          DECIMAL(8,2)      NOT NULL,
    barrio_inicio       VARCHAR(100)      NOT NULL,
    barrio_llegada      VARCHAR(100)      NOT NULL,
    cantidad_pasajeros  TINYINT UNSIGNED  NOT NULL,
    taxista             VARCHAR(100)      NOT NULL,
    precio              DECIMAL(12,2)     NOT NULL,
    duracion_minutos    INT UNSIGNED      NOT NULL,
    created_at          DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_carreras_taxi_barrio_inicio (barrio_inicio),
    INDEX idx_carreras_taxi_taxi (taxi)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Carreras de ejemplo
INSERT IGNORE INTO carreras_taxi
    (id, cliente, taxi, kilometros, barrio_inicio, barrio_llegada,
     cantidad_pasajeros, taxista, precio, duracion_minutos)
VALUES
    ('10000000-0000-0000-0000-000000000001', 'María Fernanda Pájaro', 'UAK512', 6.40,
     'El Recreo', 'Centro Histórico', 2, 'Jorge Luis Marrugo', 14000.00, 25),
    ('10000000-0000-0000-0000-000000000002', 'Andrés Felipe Torres', 'TZN903', 3.20,
     'Manga', 'Bocagrande', 1, 'Wilson Herrera Julio', 9000.00, 12),
    ('10000000-0000-0000-0000-000000000003', 'Luisa Cabarcas Díaz', 'UAK512', 11.80,
     'Crespo', 'Ternera', 4, 'Jorge Luis Marrugo', 24000.00, 40);
