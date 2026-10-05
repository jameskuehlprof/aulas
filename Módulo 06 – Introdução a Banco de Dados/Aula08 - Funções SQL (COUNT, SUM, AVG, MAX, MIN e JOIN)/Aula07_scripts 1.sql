CREATE DATABASE empresa;
USE empresa;

CREATE TABLE cliente(
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100),
    cidade VARCHAR(60),
    telefone VARCHAR(20),
    email VARCHAR(100)
);

INSERT INTO cliente(nome,cidade,telefone,email)
VALUES
('Maria','Blumenau','4799991111','maria@email.com'),
('João','Indaial','4798882222','joao@email.com'),
('Pedro','Timbó','4797773333','pedro@email.com'),
('Ana','Pomerode','4796664444','ana@email.com');

CREATE TABLE produto(
id INT AUTO_INCREMENT PRIMARY KEY,
descricao VARCHAR(100),
preco DECIMAL(10,2),
estoque INT
);

INSERT INTO produto(descricao,preco,estoque)
VALUES
('Notebook',4200,15),
('Mouse',80,100),
('Teclado',150,50),
('Monitor',950,20);

select * from cliente;
UPDATE cliente SET cidade = 'Gaspar' where id = 1;
UPDATE cliente c 
   SET c.nome = 'Luiz' 
 where c.id = 1 /*and c.nome like '%Ana%'*/;
UPDATE cliente c 
   SET c.nome   = 'Wellington', 
       c.cidade = 'Rio dos Cedros'
 where c.id = 2; 
select * from produto; 

UPDATE produto p
   SET p.descricao = concat(p.descricao,' Gamer') 
 WHERE id = 1;
 
 UPDATE produto p
   SET p.descricao = 'Notebook Gamer'
 WHERE id = 1;

UPDATE produto p
   SET p.descricao = concat(p.descricao,' Gamer') 
 WHERE id = 1;

UPDATE produto SET preco = 105.90 WHERE id=2;

select * from produto;
commit;

UPDATE produto SET preco = preco*1.10 WHERE id=3;
UPDATE produto SET preco = preco+(preco*0.1) WHERE id=3;

UPDATE produto SET preco = preco * 0.85 where id =1;
UPDATE produto SET preco = preco-(preco*0.15) where id=2;

select * from produto;

UPDATE produto p
   SET p.preco = p.preco+(p.preco*0.10) 
 where p.estoque between 50 and 100
   and p.id >= 1;
   
select * from cliente;   
delete from cliente c where c.id = 4;

INSERT INTO cliente(id,nome,cidade,telefone,email)
VALUES
(4,'Nick','Blumenau','4799991111','maria@email.com');
select * from cliente;
update cliente c 
   set c.cidade = 'Blumenau' 
 where id = 2;
 
 update cliente c 
   set c.telefone = '123456789' 
 where id = 1;
select * from produto;
update produto p set p.preco = 4500  where id = 1;
update produto p 
   set p.estoque = p.estoque+20
 where p.id = 2; 
 
update produto p 
   set p.preco = p.preco+(p.preco*0.05)
 where p.id>=1; 

update produto p 
   set p.preco = p.preco-(p.preco*0.10)
 where p.id>=1 and p.preco > 1000; 
 select * from cliente;
 update cliente c set c.email = 'xxx@yyyy.com.br'
  where c.id=3;
delete from cliente c where c.id=1;   
select * from produto;
delete from produto p 
 where p.id >= 1 and p.estoque =0;  
select * from cliente;
update cliente c set c.telefone = '555555' 
where id=3;

update cliente c 
   set c.telefone =  concat('0',c.telefone) 
 where id>=1;
 
update cliente c 
   set c.cidade = (CASE WHEN ID = 1 THEN 'TIMBÓ'
             WHEN ID = 2 THEN 'GASPAR'
             ELSE 'INDAIAL' END)
 where ID BETWEEN 1 AND 3; 
 /*
 Atualize todos os produtos com estoque
 menor que 20 para estoque igual a 20.
 */
 SELECT * FROM PRODUTO;
 UPDATE PRODUTO P 
    SET P.ESTOQUE = 20
  WHERE P.ESTOQUE < 20 
    AND P.ID >= 1;
/*Teste com CASE --> Caso for*/
UPDATE PRODUTO P 
   set p.estoque =
   (case when p.descricao ='Monitor' then
          p.estoque + 10      
         when p.estoque = 20 then
         p.estoque + 30 else 20     
   end)
 where p.id >= 1;  
 