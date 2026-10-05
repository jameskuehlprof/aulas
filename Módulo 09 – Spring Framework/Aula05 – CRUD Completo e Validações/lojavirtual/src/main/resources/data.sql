-- Dados de exemplo, carregados toda vez que a aplicacao sobe (spring.sql.init.mode=always).
DELETE FROM produto;
INSERT INTO produto (id, nome, preco, quantidade_em_estoque) VALUES (1, 'Notebook', 3500.00, 10);
INSERT INTO produto (id, nome, preco, quantidade_em_estoque) VALUES (2, 'Mouse', 59.90, 50);
INSERT INTO produto (id, nome, preco, quantidade_em_estoque) VALUES (3, 'Teclado Mecânico', 289.00, 25);

DELETE FROM cliente;
INSERT INTO cliente (id, nome, email, cpf) VALUES (1, 'Ana Souza', 'ana.souza@exemplo.com', '11122233344');
INSERT INTO cliente (id, nome, email, cpf) VALUES (2, 'Bruno Lima', 'bruno.lima@exemplo.com', '22233344455');
