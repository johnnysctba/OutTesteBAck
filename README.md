# Golden Raspberry Awards API

API RESTful para consulta de dados dos indicados e vencedores da categoria "Pior Filme" do Golden Raspberry Awards.

## Descrição

Esta aplicação foi desenvolvida em Java com Spring Boot para fornecer uma API que permite consultar informações sobre os intervalos de prêmios dos produtores do Golden Raspberry Awards. A API identifica o produtor com maior intervalo entre dois prêmios consecutivos e o que obteve dois prêmios mais rapidamente.

## Tecnologias Utilizadas

- **Java 11**
- **Spring Boot 2.7.18**
- **Spring Data JPA**
- **H2 Database** (banco em memória)
- **OpenCSV** (processamento de arquivos CSV)
- **Gradle** (gerenciamento de dependências)
- **JUnit 5** (testes)

## Arquitetura

A aplicação segue uma arquitetura em camadas com separação clara de responsabilidades:

- **Controller**: Camada de apresentação (API REST)
- **Service**: Camada de lógica de negócio
- **Repository**: Camada de persistência
- **Model/Entity**: Entidades de domínio
- **DTO**: Objetos de transferência de dados

## Princípios Aplicados

- **Clean Code**: Código limpo, legível e bem documentado
- **SOLID**: Todos os cinco princípios aplicados
- **Design Patterns**: Repository, Service Layer, Dependency Injection
- **Nível 2 de Richardson**: API RESTful com uso adequado de métodos HTTP e códigos de status

## Pré-requisitos

- Java 11 ou superior
- Gradle 7.x ou superior

## Como Executar

### 1. Clone ou baixe o projeto

```bash
git clone <url-do-repositorio>
cd golden-raspberry-awards
```

### 2. Execute a aplicação (via Gradle)

```bash
./gradlew bootRun
```

Ou compile e execute o JAR:

```bash
./gradlew build
java -jar build/libs/golden-raspberry-awards-1.0.0.jar
```

### 3. Acesse a aplicação

A aplicação estará disponível em: `http://localhost:8080`

## Endpoints da API

### Obter Intervalos de Prêmios dos Produtores

```
GET /api/producers/award-intervals
```

Retorna o produtor com maior intervalo entre dois prêmios consecutivos e o que obteve dois prêmios mais rapidamente.

**Exemplo de Resposta:**

```json
{
  "min": [
    {
      "producer": "Producer 1",
      "interval": 1,
      "previousWin": 2008,
      "followingWin": 2009
    }
  ],
  "max": [
    {
      "producer": "Producer 2",
      "interval": 99,
      "previousWin": 1900,
      "followingWin": 1999
    }
  ]
}
```

### Health Check

```
GET /api/health
```

Verifica se a API está funcionando corretamente.

## Como Executar os Testes

### Executar todos os testes (via Gradle)

```bash
./gradlew test
```

### Executar testes com relatório de cobertura (via Gradle)

```bash
./gradlew jacocoTestReport
```

## Banco de Dados

A aplicação utiliza o H2 Database em memória. Os dados são carregados automaticamente a partir do arquivo `movielist.csv` localizado em `src/main/resources/`.

### Console do H2

Para acessar o console do H2 durante o desenvolvimento:

1. Acesse: `http://localhost:8080/h2-console`
2. Use as configurações:
    - JDBC URL: `jdbc:h2:mem:testdb`
    - User Name: `sa`
    - Password: (deixe em branco)

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/br/com/johnny/
│   │   ├── controller/          # Controladores REST
│   │   ├── service/             # Lógica de negócio
│   │   ├── repository/          # Camada de persistência
│   │   ├── model/               # Entidades
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── exception/           # Exceções personalizadas
│   │   └── GoldenRaspberryAwardsApplication.java
│   └── resources/
│       ├── application.yml      # Configurações da aplicação
│       └── movielist.csv        # Dados dos filmes
└── test/
    └── java/br/com/johnny/
        └── GoldenRaspberryAwardsIntegrationTest.java
```

## Desenvolvimento no IntelliJ IDEA

### Importar o Projeto

1.  Abra o IntelliJ IDEA.
2.  Selecione `File -> Open`.
3.  Navegue até a pasta `golden-raspberry-awards` (após descompactar o ZIP).
4.  Selecione o arquivo `build.gradle` (ou a pasta raiz do projeto) e clique em `Open`.
5.  O IntelliJ deve reconhecer o projeto Gradle e importá-lo automaticamente. Confirme a importação se solicitado.

### Sincronizar Gradle

Certifique-se de que o projeto Gradle esteja sincronizado. Você pode fazer isso clicando no ícone "Gradle" na barra lateral direita e depois no ícone de "Reload All Gradle Projects".

### Executar a Aplicação no IntelliJ

1.  Abra a classe `GoldenRaspberryAwardsApplication.java`.
2.  Clique na seta verde ao lado da declaração da classe `GoldenRaspberryAwardsApplication` e selecione `Run 'GoldenRaspberryAwardsApplication'`. A aplicação será iniciada.

### Executar Testes no IntelliJ

1.  Abra o arquivo `GoldenRaspberryAwardsIntegrationTest.java`.
2.  Clique na seta verde ao lado da declara declaração da classe `GoldenRaspberryAwardsIntegrationTest` e selecione `Run 'GoldenRaspberryAwardsIntegrationTest'`. Todos os testes de integração serão executados.

## Testes

Os testes de integração validam:

- Carregamento correto dos dados do CSV
- Funcionamento dos endpoints da API
- Consistência dos cálculos de intervalos
- Formato correto das respostas JSON
- Conformidade com os requisitos especificados

## Logs

A aplicação gera logs informativos sobre:

- Carregamento dos dados do CSV
- Processamento das requisições
- Erros e exceções

Os logs podem ser visualizados no console durante a execução.

## Considerações de Desenvolvimento

- **Tratamento de Erros**: Exceções são tratadas adequadamente com logs detalhados
- **Validação de Dados**: Dados do CSV são validados antes da persistência
- **Performance**: Uso de índices e consultas otimizadas
- **Manutenibilidade**: Código bem estruturado e documentado
- **Testabilidade**: Arquitetura facilita a criação de testes

## Licença

Este projeto foi desenvolvido para fins de avaliação técnica.