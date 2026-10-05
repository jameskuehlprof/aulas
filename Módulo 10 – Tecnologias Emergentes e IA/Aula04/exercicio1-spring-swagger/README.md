# Exercício 1 – Consumindo uma API pública (Spring Boot + Swagger)

Módulo 10 – Tecnologias Emergentes e IA. Versão em Spring Boot do Exercício 1
dos exercícios da Aula 04, para poder testar pelo Swagger UI em vez de só
pelo console.

## O que o exercício pedia

- Fazer uma requisição GET para uma API pública gratuita (`https://api.agify.io?name=joao`)
- Imprimir a resposta bruta no console
- Criar o `HttpClient` e o `HttpRequest`
- Enviar a requisição com `.send()`
- Imprimir o status code e o corpo da resposta
- Extrair apenas o campo `age` do JSON usando Jackson `ObjectMapper`

Toda essa lógica está em `AgifyService.consultarIdade(nome)` — é o mesmo
passo a passo do `PrimeiraChamadaHttp.java` da Aula 04, só que chamado por
dentro de um endpoint REST em vez de por um método `main`.

## Como rodar

Pré-requisitos: JDK 17+ e Maven (ou usar o Maven embutido do Eclipse).

**Pelo terminal:**
```bash
cd exercicio1-spring-swagger
mvn spring-boot:run
```

**Pelo Eclipse:** File > Import > Maven > Existing Maven Projects, apontando
para esta pasta. Depois, botão direito em `Exercicio1Application.java` > Run
As > Java Application (ou Spring Boot App, se tiver o STS/plugin instalado).

A aplicação sobe em `http://localhost:8080`.

## Como testar

**Opção 1 – Swagger UI (interativo, sem precisar de Postman):**

Acesse no navegador: **http://localhost:8080/swagger-ui.html**

Lá vai aparecer o endpoint `GET /api/idade`, com um botão "Try it out" —
preencha o parâmetro `nome` (ex.: `joao`) e clique em "Execute" para ver a
resposta.

**Opção 2 – direto pela URL / pelo navegador:**

```
http://localhost:8080/api/idade?nome=joao
```

**Opção 3 – contrato OpenAPI em JSON** (útil se quiser importar no Postman):

```
http://localhost:8080/v3/api-docs
```

## Onde olhar o "console" pedido pelo exercício

O status HTTP e o corpo bruto da resposta da agify.io continuam sendo
impressos com `System.out.println`, exatamente como o enunciado pede — só
que agora aparecem no **console onde a aplicação Spring Boot está rodando**
(o terminal ou a aba Console do Eclipse), não no console de quem está
chamando o endpoint pelo navegador/Swagger.

## Estrutura do projeto

```
exercicio1-spring-swagger/
├── pom.xml
└── src/main/java/br/com/senac/exercicio1/
    ├── Exercicio1Application.java   (classe main / @SpringBootApplication)
    ├── controller/AgifyController.java   (endpoint REST + anotações Swagger)
    ├── service/AgifyService.java         (HttpClient + HttpRequest + Jackson)
    └── dto/IdadeResponse.java            (objeto de resposta do endpoint)
```
