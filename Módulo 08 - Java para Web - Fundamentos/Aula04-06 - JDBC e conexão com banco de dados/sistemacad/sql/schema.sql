-- =========================================================
-- SistemaCad - Script de criação do banco de dados
-- Aula 4 - JDBC e Conexão com Banco de Dados
-- =========================================================
-- Como executar:
--   1) Abra o MySQL Workbench (ou o cliente de sua preferência)
--   2) Conecte-se ao servidor local (usuário root)
--   3) Execute este script completo (Ctrl+Shift+Enter no Workbench)
-- =========================================================

CREATE DATABASE IF NOT EXISTS sistemacad
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE sistemacad;

CREATE TABLE IF NOT EXISTS cliente (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nome   VARCHAR(100) NOT NULL,
    email  VARCHAR(150) NOT NULL,
    cidade VARCHAR(100)
);

-- Dados de exemplo, opcionais, apenas para já ter algo na listagem.
INSERT INTO cliente (nome, email, cidade) VALUES
    ('João Silva',  'joao@email.com',  'Timbó'),
    ('Maria Souza', 'maria@email.com', 'Blumenau'),
    ('Pedro Costa', 'pedro@email.com', 'Timbó');

-- Conferindo o resultado:
SELECT * FROM cliente;
