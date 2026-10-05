-- Dados de exemplo, carregados toda vez que a aplicacao sobe (spring.sql.init.mode=always).
-- Em um projeto real isso normalmente fica a cargo de uma ferramenta de
-- migration (Flyway/Liquibase); aqui mantemos simples para fins didaticos.
DELETE FROM produto;
INSERT INTO produto (id, nome, preco, quantidade_em_estoque) VALUES (1, 'Notebook', 3500.00, 10);
INSERT INTO produto (id, nome, preco, quantidade_em_estoque) VALUES (2, 'Mouse', 59.90, 50);
INSERT INTO produto (id, nome, preco, quantidade_em_estoque) VALUES (3, 'Teclado Mecânico', 289.00, 25);
