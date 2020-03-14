# Account Service Test

### What is this repository for? ###
Demo for Account CRUD and Transaction operations. API Rest

* Summary: 
This repository was created to test API Rest service based on Account CRUD and transactions between Account.

1. Create:
...We can create and Account that could be started with the Treasury property to false or true (Only on creation). 
2.- Retrieve: 
...We can retrieve all accounts and only one filtered by Id. 
3.- Update: 
...We can update the Account, changing it's properties but not Treasure property as we explained on step 1.
4.- Delete: 
...We can delete an Account.
5.- Transactions: 
...We can do transactions of balance from an Account to another Account. 
...We have to controll if Account profile Treasury property allows to set balance to negative values. 

### How do I get set up? ###

#### Spring boot and Spring 5 dependencies used ####

* H2-database as embedded database. Mode MySQL.
* Swagger2 with OpenAPI3 for documentation. 
* Spring Data JPA to dabase persistense.
* Lombok for getters, setters and constructor for Entity dataset.
* Hateoas for uri response references on each request.
* Junit 5 and Mockito to Tests.

#### Reference Documentation ####
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/maven-plugin/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#using-boot-devtools)
* [Spring HATEOAS](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#boot-features-spring-hateoas)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.2.5.RELEASE/reference/htmlsingle/#production-ready)


### Resolution Steps: ###

* First I created the configuration of embedded database.
* On this commit for branch: swagger_accounttable_account_entity
...Creation of Entity Model and Repository with Tests.

### How to run Application: ###

* Run tests. Go to root project path and run next command line:
...mvn test