SELECT UPPER(p.nome) as nome_ma, p.*
  FROM participante p 
 WHERE upper(p.cidade) = 'blumenau';
 
SELECT lower(p.nome) as nome_me, p.*
  FROM participante p 
 WHERE LOWER(p.cidade) = 'blumenau';

SELECT p.*
  FROM participante p 
 WHERE upper(p.cidade) = 'BLUMENAU' and p.id = 2;
 
 SELECT p.*
  FROM participante p 
 WHERE upper(p.cidade) = 'BLUMENAU' or p.id = 2;
 
  SELECT p.*
  FROM participante p 
 WHERE not p.cidade = 'BLUMENAU';
 
SELECT p.*
  FROM participante p 
 WHERE UPPER(p.cidade) in ('BLUMENAU','JOINVILLE');
  
  SELECT p.*
  FROM participante p 
 WHERE UPPER(p.cidade) = 'BLUMENAU' or 
 UPPER(p.cidade) = 'JOINVILLE';
 
SELECT p.*
  FROM participante p 
 WHERE UPPER(p.cidade) in ('BLUMENAU','JOINVILLE') LIMIT 1;

SELECT p.*
  FROM participante p LIMIT 3;
  
  SELECT p.*
  FROM participante p WHERE P.ID <=50;
  
  SELECT p.*
  FROM participante p WHERE P.ID between 2 AND 5;
  
  SELECT p.*
  FROM participante p WHERE P.ID > 2 AND P.ID < 5;

 SELECT * FROM organizador LIMIT 5 OFFSET 7;
 SELECT * FROM organizador;
 select * from organizador o where o.nome like '%ei%';
 select * from organizador o where o.nome like 'r%';
 select * from organizador o where o.nome like 't%%s';
 select * from organizador o 
  where o.nome like '%dimas%'
    or o.nome like '%santos%';

 select * from organizador o 
  where o.nome not like '%Souza%';    

 select o.nome from organizador o
  where  o.nome not like '_a%a' order by o.nome desc; 
 
 select * from organizador o 
  where o.nome like '%a%'
   and o.nome not like '%santos%'
  order by nome desc; 
  
  select o.nome 
    from organizador o 
   where length(o.nome) >= 15;
 
   select p.* from participante p where p.cidade is null;
   
   select e.data_evento, curdate(), sysdate(),
   ((curdate() - e.data_evento)/30)/12 as data_teste
   from evento e;
 
 
SELECT * from organizador o 
 WHERE length(o.nome) >= 10 
   and o.id between 5 and 10 
order by o.nome desc limit 5;   