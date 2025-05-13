📅 eventosTecAPI

API RESTful desenvolvida com Java 21, Spring Boot e Maven, integrada com PostgreSQL, AWS S3 e EC2, para gerenciar eventos de tecnologia. O sistema permite o cadastro e consulta de eventos, bem como a criação de cupons promocionais associados a esses eventos.
🚀 Tecnologias Utilizadas

    Java 21

    Spring Boot

    Maven

    PostgreSQL

    AWS EC2 (deploy)

    AWS S3 (armazenamento de imagens)

    JPA/Hibernate

    REST API

📦 Entidades Principais

    Event: representa um evento de tecnologia, com título, descrição, imagem, datas etc.

    Coupon: representa um cupom promocional vinculado a um evento.

    Address: representa o endereço onde o evento ocorrerá.

📡 Endpoints da API
🎫 Events

    Criar evento (com imagem)
    POST /api/event
    Content-Type: multipart/form-data
    Envia os dados do evento e uma imagem para ser armazenada no S3.

    Listar todos os eventos
    GET /api/event/listAll

    Filtrar eventos por parâmetros (ex: nome, data)
    GET /api/event/filter

    Buscar evento por ID
    GET /api/event/{event_id}

💸 Coupons

    Criar cupom para um evento específico
    POST /api/coupon/event/{event_id}

📂 Estrutura do Projeto

src/
 └── main/
     ├── java/
     │   └── com.eventostec.api/
     │       ├── config/
     │       ├── controller/
     │       ├── domain/
     │       ├── repositories/
     │       └── service/
     └── resources/
         ├── application.properties
         └── db.migration/

☁️ Integrações AWS

    S3: Upload e recuperação de imagens dos eventos.

    EC2: Aplicação hospedada em uma instância EC2 para acesso público.

🛠 Como Executar Localmente

    Clone o repositório:

git clone https://github.com/lucashrt/eventosTecAPI.git
cd eventosTecAPI

Configure o banco de dados PostgreSQL no arquivo application.properties.

Compile e rode o projeto:

    ./mvnw spring-boot:run

✅ Próximos Passos

    Autenticação e autorização com Spring Security

    Testes unitários e de integração

    Documentação Swagger

    Painel de administração para gestão dos eventos e cupons