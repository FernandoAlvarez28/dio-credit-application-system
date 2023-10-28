<h1>request-credit-system</h1>
<p align="center">API Rest para um Sistema de Análise de Solicitação de Crédito</p>
<p align="center">Resolução de desafio de projeto da <a href="https://web.dio.me">Digital Innovation One</a></p>
<p align="center">Baseado no repositório original: <a href="https://github.com/cami-la/credit-application-system">cami-la/credit-application-system</a></p>
<p align="center">
     <a alt="Java">
        <img src="https://img.shields.io/badge/Java-v17-blue.svg" />
    </a>
    <a alt="Kotlin">
        <img src="https://img.shields.io/badge/Kotlin-v1.7.22-purple.svg" />
    </a>
    <a alt="Spring Boot">
        <img src="https://img.shields.io/badge/Spring%20Boot-v3.0.3-brightgreen.svg" />
    </a>
    <a alt="Gradle">
        <img src="https://img.shields.io/badge/Gradle-v7.6-lightgreen.svg" />
    </a>
    <a alt="H2 ">
        <img src="https://img.shields.io/badge/H2-v2.1.214-darkblue.svg" />
    </a>
    <a alt="Flyway">
        <img src="https://img.shields.io/badge/Flyway-v9.5.1-red.svg">
    </a>
    <a alt="JaCoCo">
        <img src="https://img.shields.io/badge/JaCoCo-v	0.8.11-green.svg">
    </a>
</p>

<h3>Summary</h3>
<p><a href="https://web.dio.me">Digital Innovation One</a>'s (PT-BR) "Documenting and Testing your API Rest with Kotlin" challenge resolution.</p>

<h3>Descrição do Projeto</h3>
<p><a href="https://gist.github.com/cami-la/560b455b901778391abd2c9edea81286">https://gist.github.com/cami-la/560b455b901778391abd2c9edea81286</a></p>
<figure>
<p align="center">
  <img src="./docs/diagrama-uml.png" height="350" width="450" alt="API para Sistema de Avaliação de Créditos"/><br>
  Diagrama UML Simplificado de uma API para Sistema de Avaliação de Crédito
</p>
</figure>

<h3>Instrução de Uso</h3>
<p>No Terminal/Console:</p>
<ol>
	<li>Faça um clone do projeto na sua máquina: <code>git clone git@github.com:FernandoAlvarez28/dio-credit-application-system.git</code></li>
	<li>Entre na pasta raiz do projeto: <code>cd </code></li> 
	<li>Execute o comando: <code>./gradlew bootrun</code></li>
</ol>
<h6>** Visando facilitar a demostração da aplicação, recomendo instalar apenas o IntelliJ IDEA e executar o projeto através da IDE **</h6>

<h4>Swagger</h4>
<p>Para ver a documentação no Swagger, inicie a aplicação e acesse: <a href="http://localhost:8080/swagger-ui.html">http://localhost:8080/swagger-ui.html</a></p>

<h4>JaCoCo (cobertura de código)</h4>
<p>Para ver a cobertura de código pelo JaCoCo, execute o comando na raiz do projeto e verifique a página <code>./build/jacocoHtml/index.html</code>:</p>

```shell
./gradlew clean check
```

<h4>Postman</h4>

<a href="https://drive.google.com/file/d/1wxwioDHS1sKFPq4G7b24tVZb-XMnoj-l/view?usp=share_link"> 🚀 Collection API - Postman</a><br>
