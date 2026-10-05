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

-- =========================================================
-- Aula 07 - Complementar: Pedido e ItemPedido
-- =========================================================
-- Relacionamento:
--
--   cliente (1) ────< pedido (N) ────< item_pedido (N)
--
-- pedido.cliente_id      -> chave estrangeira para cliente.id
-- item_pedido.pedido_id  -> chave estrangeira para pedido.id
--
-- ON DELETE CASCADE: se um cliente (ou um pedido) for excluído, os
-- registros dependentes (pedidos / itens) são excluídos automaticamente
-- pelo próprio banco. É prático para o exercício, mas vale discutir em
-- aula: em um sistema real, normalmente preferimos IMPEDIR a exclusão
-- de um cliente que já tem pedidos (ON DELETE RESTRICT), para não
-- perder histórico sem querer.
-- =========================================================

CREATE TABLE IF NOT EXISTS pedido (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id   INT NOT NULL,
    data_pedido  DATE NOT NULL,
    status       VARCHAR(20) NOT NULL DEFAULT 'ABERTO',
    valor_total  DECIMAL(10,2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_pedido_cliente
        FOREIGN KEY (cliente_id) REFERENCES cliente (id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS item_pedido (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id       INT NOT NULL,
    produto         VARCHAR(100) NOT NULL,
    quantidade      INT NOT NULL,
    preco_unitario  DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_item_pedido_pedido
        FOREIGN KEY (pedido_id) REFERENCES pedido (id)
        ON DELETE CASCADE
);

-- Dados de exemplo (valor_total já corresponde à soma dos itens abaixo).
INSERT INTO pedido (cliente_id, data_pedido, status, valor_total) VALUES
    (1, '2026-08-01', 'PAGO',      350.00),
    (1, '2026-08-10', 'ABERTO',     89.90),
    (2, '2026-08-05', 'CANCELADO', 120.00);

INSERT INTO item_pedido (pedido_id, produto, quantidade, preco_unitario) VALUES
    (1, 'Teclado mecânico', 1, 250.00),
    (1, 'Mouse sem fio',    1, 100.00),
    (2, 'Mousepad',         1,  89.90),
    (3, 'Cabo HDMI 2m',     2,  60.00);

-- Conferindo o resultado:
SELECT * FROM pedido;
SELECT * FROM item_pedido;
