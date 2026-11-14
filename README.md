# Manutenção API ⚙️

API RESTful desenvolvida em **Java** com **Spring Boot** para a gestão de ordens de serviço, controle de equipamentos industriais e cálculo automatizado de indicadores de confiabilidade — com foco especial no indicador **MTTR** (*Mean Time to Repair* / Tempo Médio de Reparo).

---

## 🚀 Tecnologias Utilizadas

O projeto foi construído utilizando tecnologias modernas do ecossistema Java:

* **Java 17+**
* **Spring Boot 3.x**
* **Spring Web** (Construção de controladores REST)
* **Spring Data JPA** (Persistência e mapeamento objeto-relacional)
* **H2 Database** / **PostgreSQL** (Armazenamento de dados)
* **Maven** (Gerenciamento de dependências e build)

---

## 📁 Arquitetura do Projeto

A aplicação segue os princípios de separação de responsabilidades e camadas bem definidas:

```text
com.manutencao.api
│
├── domain
│   ├── model         # Entidades de domínio (Equipamento, OrdemServico, StatusEquipamento, etc.)
│   ├── repository    # Interfaces de acesso a dados (Spring Data JPA)
│   └── service       # Regras de negócio da aplicação (EquipamentoService, etc.)
│
├── web
│   ├── controller    # Endpoints REST (EquipamentoController, IndicadoresController, etc.)
│   └── dto           # Objetos de Transferência de Dados (Records)
│
└── infrastructure    # Configurações globais, exceções e segurança
