## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Documentation](#documentation)
## General info
Web based application, made to optimize processes around organisating delegations - including creating business trip requests, storing settlement progress information and creating raports out of stored data.
## Technologies
- Java 11
- Spring Boot 2.3.4
- Spring Security
- MySQL
- Lombok
- Thymeleaf
- Html, CSS & Bootstrap
- JPA & Hibernate
- iText PDF
- Spring Email
## Setup
To run this app locally, first create MySQL database:
```
$ create database delegacje
```
Then in `application.properties` change `spring.datasource.username`, `spring.datasource.password` and `spring.mail` config.</br>
Finally use:
```
mvn spring-boot:run
```
The app will start running at http://localhost:8080.
## Documentation
<p align="center">
  <img width="560" height="270" src="https://i.postimg.cc/RCJFHFr3/home1.png"><br/>
  <i>Owner homepage</i>
</p>

<p align="center">
  <img width="560" height="270" src="https://i.postimg.cc/dVjDVSb4/employees.png"><br/>
  <i>Table with employees data</i>
</p>

<p align="center">
  <img width="560" height="270" src="https://i.postimg.cc/ydVdRXHs/delegations.png"><br/>
  <i>Employee Delegation expenses</i>
</p>

<p align="center">
  <img width="560" height="270" src="https://i.postimg.cc/ZRCqjYCM/expenses.png"><br/>
  <i>Expenses preview</i>
</p>
