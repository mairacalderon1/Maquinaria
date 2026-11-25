

DROP DATABASE IF EXISTS maquinaria;
CREATE DATABASE maquinaria CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE maquinaria;

-- Catálogo de tipos
CREATE TABLE tipo_maquina (
    id_tipo      INT          NOT NULL,
    descripcion  VARCHAR(30)  NOT NULL,
    CONSTRAINT pk_tipo_maquina PRIMARY KEY (id_tipo),
    CONSTRAINT uq_tipo_maquina_descripcion UNIQUE (descripcion)
);


CREATE TABLE maquina (
    id                    INT           NOT NULL AUTO_INCREMENT,
    id_tipo               INT           NOT NULL,
    codigo                VARCHAR(80)   NOT NULL,
    horas_uso             INT           NOT NULL,
    potencia              DECIMAL(10,2) NOT NULL,
    peso_toneladas        DECIMAL(10,2) NULL,
    mantencion_al_dia     BOOLEAN       NULL,
    certificacion         BOOLEAN       NULL,
    capacidad_balde       DECIMAL(10,2) NULL,
    CONSTRAINT pk_maquina PRIMARY KEY (id),
    CONSTRAINT fk_maquina_tipo FOREIGN KEY (id_tipo) REFERENCES tipo_maquina (id_tipo),
    CONSTRAINT ck_maquina_horas_uso CHECK (horas_uso BETWEEN 0 AND 20000),
    CONSTRAINT ck_maquina_potencia CHECK (potencia > 0),
    CONSTRAINT ck_maquina_peso_toneladas CHECK (peso_toneladas > 0),
    CONSTRAINT ck_maquina_capacidad_balde CHECK (capacidad_balde > 0)
);

-- Datos iniciales
INSERT INTO tipo_maquina (id_tipo, descripcion) VALUES (1, 'Excavadora');
INSERT INTO tipo_maquina (id_tipo, descripcion) VALUES (2, 'Cargador Frontal');
