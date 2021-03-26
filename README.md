## Java Rest API
***
### Introdução

Projeto desenvolvido para o bootcamp Everis FullStack Developer da Everis em parceria com a Digital Innovation One.

### Sobre o Projeto

Uma REST API desenvolvida com Java Spring Boot em que é possível gerenciar um catalogo de pessoas em um banco de dados SQL a partir dos métodos `GET, POST, PUT, DELETE`.

#### Dependências
   - Spring Boot 2.4.4
   - Spring Boot Dev Tools 2.4.4
   - H2 Database
   - Lombok 1.18.18
   - MapStruct 1.4.2
   - Hibernate 7.0.1

### Cloud Deploy no Heroku

O deploy da aplicação foi realizado na plataforma Heroku, a partir deste repositório.

### Fazendo requisições

As requisições HTTP podem ser realizadas a partir do endpoint `bookrest-api.herokuapp.com/api/v1/people`.  

Por padrão o corpo das requisições, quando necessário, é definido no formato JSON. <br>
A seguir exemplos de requisições e suas respectivas respostas.

``Nota: Exceções geram códigos de status HTTP``

* ###### Método POST
  Requisição:
    `bookrest-api.herokuapp.com/api/v1/people`
  
  Corpo da requisição:
  ```JSON
  {
      "firstName" : "João",
      "lastName" : "da Silva",
      "cpf" : "111.222.333-45",
      "birthDate" : "01-01-1990",
      "phones" : [
          {
              "type" : "HOME",
              "number" : "11999999999"
          }
      ]
  }
  ```

  Resposta:
  ```JSON
  { "message": "Created person with Id 1" }
  ```

* ###### Método GET   
    Requisição genérica:
        `bookrest-api.herokuapp.com/api/v1/people`
    
    Resposta: 
    ```JSON
    [
      {
            "id": 1,
            "firstName" : "João",
            "lastName" : "da Silva",
            "cpf" : "111.222.333-45",
            "birthDate" : "01-01-1990",
            "phones" : [
                {
                    "id": 1,
                    "type" : "HOME",
                    "number" : "11999999999"
                }
            ]
        },
        {
            "id": 2,
            "firstName" : "Maria",
            "lastName" : "Pereira",
            "cpf" : "123.456.789-10",
            "birthDate" : "21-12-2012",
            "phones" : [
                {
                    "id": 2,
                    "type" : "HOME",
                    "number" : "11999999999"
                }
            ]
        }
    ]
    ```
    
    Requisição com ***id*** especificada:
        `bookrest-api.herokuapp.com/api/v1/people/1`
  
    Resposta:
    ```JSON
        {
            "id": 1,
            "firstName" : "João",
            "lastName" : "da Silva",
            "cpf" : "111.222.333-45",
            "birthDate" : "01-01-1990",
            "phones" : [
                {
                    "id": 1,
                    "type" : "HOME",
                    "number" : "11999999999"
                }
            ]
        }
    ```


* ###### Método PUT
    Requisição:
        `bookrest-api.herokuapp.com/api/v1/people/1`, obrigatório id do objeto.
  
    Corpo da requisição:
    ```JSON
    {
      "id": 1,
      "firstName" : "João",
      "lastName" : "da Silva Santos",
      "cpf" : "111.222.333-45",
      "birthDate" : "01-01-1990",
      "phones" : [
          {
              "id": 1,
              "type" : "HOME",
              "number" : "11999999999"
          }
      ]
    }
    ```

    Resposta:
    ```JSON
    { "message": "Updated person with Id 1" }
    ```
* ###### Método DELETE
    Requisição:
        `bookrest-api.herokuapp.com/api/v1/people/1`, obrigatório id do objeto.  
    Resposta: `void`
  
### Rodando o projeto localmente

O projeto foi desenvolvido utilizando o Maven com gerenciador de pacotes.  
Para executar o projeto localmente execute ``mvn spring-boot:run``

O endpoint local: ``http://localhost:8080/api/v1/people``