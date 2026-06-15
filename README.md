# Diagnóstico Virtual Orientado a Objetos

Projeto Java/Swing desenvolvido com base no caso de uso/UML da Atividade Integradora.

## Objetivo

Simular um sistema de diagnóstico virtual orientado a objetos, permitindo:

- cadastrar doenças;
- cadastrar sintomas associados às doenças;
- cadastrar exames e tratamentos;
- selecionar sintomas informados pelo usuário;
- gerar diagnósticos possíveis com pontuação de compatibilidade;
- persistir doenças e sintomas em arquivos locais.

## Tecnologias

- Java 17
- Java Swing
- Maven
- Persistência em arquivos CSV simples
- POO, herança, interface, composição, DAO e exceção personalizada

## Como executar

```bash
mvn clean compile
mvn exec:java
```

Ou, sem Maven:

```bash
javac -encoding UTF-8 -d bin $(find src/main/java -name "*.java")
java -cp bin br.com.unicesumar.diagnostico.Main
```

## Estrutura

```text
src/main/java/br/com/unicesumar/diagnostico/
├── Main.java
├── dao/
│   ├── DAO.java
│   ├── DoencaDAO.java
│   └── SintomaDAO.java
├── exception/
│   └── DiagnosticoException.java
├── model/
│   ├── Diagnostico.java
│   ├── Diagnosticavel.java
│   ├── Doenca.java
│   ├── EntidadeBase.java
│   ├── Exame.java
│   ├── SessaoDiagnostico.java
│   ├── Severidade.java
│   ├── Sintoma.java
│   └── Tratamento.java
├── service/
│   └── DiagnosticoService.java
├── util/
│   └── IdGenerator.java
└── view/
    ├── TelaCadastroDoenca.java
    ├── TelaDiagnostico.java
    └── TelaPrincipal.java
```

## Observação acadêmica

Este projeto foi estruturado para corresponder ao UML enviado:
`TelaPrincipal`, `TelaDiagnostico`, `TelaCadastroDoenca`, `DAO<T>`, `DoencaDAO`, `SintomaDAO`, `DiagnosticoService`, `SessaoDiagnostico`, `Diagnostico`, `Diagnosticavel`, `Doenca`, `Sintoma`, `Exame`, `Tratamento`, `EntidadeBase` e `DiagnosticoException`.
