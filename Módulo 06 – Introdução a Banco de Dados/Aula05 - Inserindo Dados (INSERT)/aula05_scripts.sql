CREATE DATABASE eventos;
USE eventos;
CREATE TABLE organizador(
       id       INT PRIMARY KEY AUTO_INCREMENT,
       nome     VARCHAR(100) NOT NULL,
       telefone VARCHAR(20),
       email    VARCHAR(100));
CREATE TABLE evento(
 id INT PRIMARY KEY AUTO_INCREMENT,
 titulo VARCHAR(150),
 data_evento DATE, local_evento VARCHAR(100), 
 id_organizador INT, 
 FOREIGN KEY(id_organizador) REFERENCES organizador(id));

CREATE TABLE participante(
 id     INT PRIMARY KEY AUTO_INCREMENT,
 nome   VARCHAR(100),
 cidade VARCHAR(80),
 email  VARCHAR(100));

CREATE TABLE inscricao(
    id INT PRIMARY KEY AUTO_INCREMENT,
    data_inscricao DATE,
    valor DECIMAL(10,2),
    id_evento INT,
    id_participante INT,
    FOREIGN KEY(id_evento) REFERENCES evento(id),
    FOREIGN KEY(id_participante) REFERENCES participante(id));

INSERT INTO organizador (nome,telefone,email)
VALUES('Carlos Oliveira','47999887766','carlos@email.com'),
('Ana Souza','47999995555','ana@email.com'),
('João Martins','47991112222','joao@email.com'),
('Fernanda Lima','47996663333','fernanda@email.com');
select * from organizador;
       
INSERT INTO evento(titulo,data_evento,
                   local_evento,id_organizador)
VALUES('Workshop de IA',
        STR_TO_DATE('23/06/2026', '%d/%m/%Y'),
       'Centro Empresarial',1);
select * from evento;

INSERT INTO participante (nome,cidade,email)
VALUES ('Lucas Pereira', 'Blumenau','lucas@email.com');
select * from participante;

INSERT INTO inscricao
(data_inscricao,valor,id_evento,id_participante)
VALUES ('2026-07-05', 150.45,1,1);

select * from inscricao;

INSERT INTO participante (nome,cidade,email)
VALUES('Maria Rosa','Joinville','maria@email.com'),
('Pedro Alves','Jaraguá do Sul','pedro@email.com'),
('Carla Dias','Florianópolis','carla@email.com');

select * from participante;

INSERT INTO evento(titulo,data_evento,
                   local_evento,id_organizador)
VALUES('Workshop de BD',
        STR_TO_DATE('06/07/2026', '%d/%m/%Y'),
       'SENAC',2);
select * from evento;
INSERT INTO participante (nome, cidade,email)
VALUES ('Roberto Lima', NULL, NULL);
select * from participante;
INSERT INTO inscricao
(data_inscricao,valor,id_evento,id_participante)
VALUES ('2026-07-01',120.00,1,2),
       ('2026-07-02',120.00,1,3),
       ('2026-07-03',120.00,2,4);
select * from inscricao;

CREATE TABLE participante_backup(
 id     INT,
 nome   VARCHAR(100),
 cidade VARCHAR(80),
 email  VARCHAR(100));
 select * from participante_backup;
  select * from participante;
 insert into participante_backup (id,nome,cidade,email)
 select id,nome,cidade,email from participante;
 
 select * from participante_backup;
 /*delete from participante_backup;*/
 
 CREATE TABLE contatos_evento(
 nome VARCHAR(100),
 email VARCHAR(100));
SELECT nome, email FROM participante WHERE cidade='Blumenau';
INSERT INTO contatos_evento(nome,email)
SELECT nome, email FROM participante WHERE cidade='Blumenau';
select * from contatos_evento;

CREATE TABLE participantes_evento(
participante VARCHAR(100), evento VARCHAR(150));
select * from participante; 
select * from evento; 

select p.* from participante p; 
select e.* from evento e;
select i.* from inscricao i;
insert into participantes_evento (participante, evento)
SELECT p.nome, e.titulo FROM participante p
INNER JOIN inscricao i ON p.id = i.id_participante
INNER JOIN evento e ON e.id=i.id_evento;

select * from participantes_evento;

INSERT INTO organizador (nome,telefone,email)
VALUES('Carlos Oliveira','4799991111','carlos@email.com'),
('Ana Souza','4799992222','ana@email.com'),
('Ricardo Gomes','4799993333','ricardo@email.com'),
('Fernanda Silva','4799994444','fernanda@email.com'),
('Paulo Mendes','4799995555','paulo@email.com'),
('Juliana Costa','4799996666','juliana@email.com'),
('Marcos Lima','4799997777','marcos@email.com'),
('Patrícia Alves','4799998888','patricia@email.com'),
('Tiago Santos','4799999999','tiago@email.com'),
('Renata Souza','4799888777','renata@email.com');

CREATE TABLE emails_marketing (
    nome VARCHAR(100),
    email VARCHAR(100)
);
INSERT INTO emails_marketing (nome, email)
SELECT nome, email FROM participante;
CREATE TABLE historico_eventos (
    titulo VARCHAR(100),
    data_evento DATE,
    local_evento VARCHAR(100)
);
INSERT INTO historico_eventos (titulo, data_evento, local_evento)
SELECT titulo, data_evento, local_evento FROM evento;
CREATE TABLE agenda_organizadores (
    organizador VARCHAR(100),
    evento VARCHAR(100)
);
INSERT INTO agenda_organizadores (organizador, evento)
SELECT o.nome, e.titulo FROM organizador o INNER JOIN evento e ON o.id = e.id_organizador;
 