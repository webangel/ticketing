-- Migración: Separar qtoken_path_pdf en qtoken + path_pdf
-- Ejecutar contra la base de datos ticketing_db

-- 1. Agregar nuevas columnas
ALTER TABLE tickets ADD COLUMN qtoken VARCHAR(255) NULL AFTER queue_position;
ALTER TABLE tickets ADD COLUMN path_pdf VARCHAR(255) NULL AFTER qtoken;

-- 2. Migrar datos existentes (si hay datos en qtoken_path_pdf)
UPDATE tickets SET path_pdf = qtoken_path_pdf WHERE qtoken_path_pdf IS NOT NULL;

-- 3. Eliminar columna antigua
ALTER TABLE tickets DROP COLUMN qtoken_path_pdf;
