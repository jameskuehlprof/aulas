CREATE SCHEMA IF NOT EXISTS HOSPITAL;
USE HOSPITAL;
CREATE TABLE paciente (id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                       nome VARCHAR(100) NOT NULL,
                       idade INT);
describe paciente;

CREATE TABLE setor (
     id_setor INT PRIMARY KEY AUTO_INCREMENT,
     nome VARCHAR(100) NOT NULL);

CREATE TABLE funcionario (
 id_funcionario INT PRIMARY KEY AUTO_INCREMENT,
 nome  VARCHAR(100) NOT NULL,
 cargo VARCHAR(50),
 salario DECIMAL(10,2),
 id_setor INT,
 FOREIGN KEY (id_setor) REFERENCES setor(id_setor));

drop table funcionario;

create table cargo(
id_cargo int primary key auto_increment,
nome varchar(50) not null);

ALTER TABLE hospital.funcionario 
ADD COLUMN id_cargo INT NULL DEFAULT NULL AFTER id_setor,
ADD INDEX funcionario_ibfk2_idx (id_cargo ASC) VISIBLE;

ALTER TABLE hospital.funcionario 
ADD CONSTRAINT funcionario_ibfk2
  FOREIGN KEY (id_cargo)
  REFERENCES hospital.cargo (id_cargo)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;
  
INSERT INTO setor (nome)
  VALUES('Recepção'), ('Enfermagem'),('Laboratório'),('Centro Cirúrgico');
commit; 

select * from setor; 

INSERT INTO funcionario (nome, cargo, salario, id_setor, id_cargo)
VALUES('Maria Silva', 'Enfermeira', 4500.00, 2, null),
('João Pereira', 'Recepcionista', 2500.00, 1, null),
('Ana Souza', 'Biomédica', 5200.00, 3, null),
('Carlos Lima', 'Médico', 15000.00, 4, null);
select * from funcionario;
describe funcionario;

insert into cargo(nome) 
values ('Enfermeira'),('Recepcionista'),('Biomédica'),('Médico');
describe funcionario;
select * from cargo;

INSERT INTO funcionario (nome, salario, id_setor, id_cargo)
VALUES('James Kuehl', 4500.00, 2, 4);

/*Exercícios de fixação*/

CREATE DATABASE controle_estoque;
USE controle_estoque;

create table categoria (id_categoria int primary key auto_increment not null,
                        nome varchar(100) not null);  

create table fornecedor (id_fornecedor int primary key auto_increment not null,
                         nome varchar(120) not null, 
                         telefone	  VARCHAR(20),
                         cidade	      VARCHAR(100)
                         );
create table produto (id_produto  INT primary key auto_increment not null,
					  nome	      VARCHAR(120) not null,
					  preco	      DECIMAL(10,2) not null,
					  quantidade  INT not null,
					  id_categoria  INT,
					  id_fornecedor INT,
			foreign key (id_categoria) references categoria(id_categoria),
            foreign key (id_fornecedor) references fornecedor(id_fornecedor)
                      );
                      
/*
Regras:
id_produto é chave primária.
nome obrigatório.
preço obrigatório.
quantidade obrigatória.
id_categoria é chave estrangeira.
id_fornecedor é chave estrangeira.

*/

