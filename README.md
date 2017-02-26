# ePayApp
The task is to create an backend application that provides a set of APIs to be called from a frontend UI.
# API Requirements
The API will have 6 endpoints and will communicate JSON with at least the following functionality for each end point:

* /login -  a POST request that will accept no input and return a token (which need to be used in subsequent calls to the API, in the Authorization header). Every call  to /login will return a new token and every invocation to this endpoint creates a new user, gives them a preset balance in a preset currency. 

* /balance -  a GET request that will accept an Authorization header (with the token value output from /login) and  will return the current balance along with the currency code.

* /transactions - a GET request that will accept an Authorization header (with the token value output from /login) and  will return a list of transactions done by the user with atleast the date, description, amount, currency for each transaction.

* /spend - a POST request that will accept an Authorization header (with the token value output from /login), JSON content representing one spend transaction with the transaction date, description, amount, currency.

* /update/profile - a POST request that will accept an Authorization header (with the token value output from /login),  input parameter  content representing user name, email, mobile number, gender and other details.

* /recharge/wallet - a POST request that will accept an Authorization header (with the token value output from /login),  input parameter  content representing amount to be added, with currency and other optional detail like coupan code, remark etc.



# Tutorial

### Technology Used

+ Used Java and related framework (JPA,Spring,Hibernate,Spring Rest etc) and postgres for the database. 
+ Used Memcache for caching frequently used data, Application build using Tomcat server, However any other server can also be used.
+ Databse is configurable, just add maven dependency of db in pom and db configuration in persistence.properties file, you are ready to go. 
+ The backend will run as a standalone java process. 

### Setting Database Configuration
  + Create a db in postgres, and enter the db details like userName, password and db name in persistence.properties file as shown below
```java
    jdbc.username=abc   
    jdbc.password=***   
    jdbc.url=jdbc:postgresql://localhost:5432/dbName?autoReconnect=true&useUnicode=true&characterEncoding=UTF-   8&connectionCollation=utf8_unicode_ci&characterSetResults=UTF-8   
    jdbc.database=POSTGRESQL  
    jdbc.driver.classname=org.postgresql.Driver   
    jdbc.show.sql=false   
    jdbc.generate.ddl=true  
    hibernate.jdbc.batch_size=100   
    jdbc.database.platform=org.hibernate.dialect.PostgreSQLDialect  
```    
    Other than these, below are some of the configurable constant/values which needs to be loaded in db(Under DbConfig table). 
    Although default values are hardcoded , which will be loaded in case configurable values are not found in db/table, such as., 
    
```java        
         PRESET_AMOUNT: 99.0
         PRESET_CURRENCY: "INR"
         PRESET_DESCRIPTION: "Welcome offer! preset Balance and currency added to account."
         TRAXN_SUCCESS_MESSAGE: "Cheers! Your transaction was successful, and your transaction Id is : "
         TRAXN_FAILURE_MESSAGE: "Oops! Something went wrong, your transaction was unsuccessful."
         INSUFFICIENT_BALANCE:  "Sorry! Insufficient balance, transaction could not be completed."
 ```
    
  
### Configuring Memcache
  + install and start memcache server, the memcache host and port need to be entered in memcached.properties file, as shown below.              MEMCACHED_SERVERS=localhost:11211
  
###  API's

  + Request
  ```java
    POST /epayApp/api/v1/login HTTP/1.1
    Host: localhost:8080
  ```
    API is responsible for creating a new user, and adding some preset balance to the user Account. It simply returns the token which       can be used in subsequent API calls for retrieving user details and other info. sample token response is mentioned below.
 
  + Response
```java   
   {
    "AUTH_ACCESS_TOKEN": "1tNOYF9qeyAqeuk2RRuZPMKvwnR3skCK6y/Vu9/3UGNzi/+Ezob1t6YLCtc46FJs",
    "TIME_OF_CALL": "1441177929934000"
  }
```  
  + Request
```java    
   GET /epayApp/api/v1/balance HTTP/1.1
   Host: localhost:8080
   AUTH_ACCESS_TOKEN: v+/03DCjBWs8GUP/JVyMPvbwcUrj/ojGcY2KP1mcZJikWF/V6ZJSvtEy6DP2jy+2
   TIME_OF_CALL: 1441177929934000
```
    A GET request that will accept an Authorization header (with the token value output from /login) and  will return the current           balance along with the currency code and other info. sample response is shown below.
    
  + Response
  ```java
    {
    "userId": 27,
    "currentBalance": 49,
    "currencyCode": "INR",
    "lastTraxnType": "DEBIT"
    }
 ```   
  + Request
```java
POST /epayApp/api/v1/spend?payload={"date":"123456782141","description":"test Transaction", "debitedAmount": 101.0, "currency":0 } HTTP/1.1
Host: localhost:8080
AUTH_ACCESS_TOKEN: v+/03DCjBWs8GUP/JVyMPvbwcUrj/ojGcY2KP1mcZJikWF/V6ZJSvtEy6DP2jy+2
TIME_OF_CALL: 1441177929934000  ```

    A POST request that will accept an Authorization header (with the token value output from /login), JSON payload content representing one spend transaction with the transaction date, description, amount, currency etc. It will simply return status of Your transaction with transaaction Id(In case of successful transaction).

  + Response in case of successful purchase
```java
    {
    "status": "Cheers! Your transaction was successful, and your transaction Id is : 271488004967497"
    }
 ```
  + Response in case of unsuccessful purchases
 ```java 
    {
  "status": "Sorry! Insufficient balance, transaction could not be completed."
    }
``` 
   or
```java  
   {
  "status": "Oops! Something went wrong, your transaction was unsuccessful."
   }
```
  + Request
```java
    GET /epayApp/api/v1/transactions HTTP/1.1
    Host: localhost:8080
    AUTH_ACCESS_TOKEN: v+/03DCjBWs8GUP/JVyMPvbwcUrj/ojGcY2KP1mcZJikWF/V6ZJSvtEy6DP2jy+2
    TIME_OF_CALL: 1441177929934000
```
    API takes access token as input in header and returns list of transaction done on a particular account by user or system.
    
  + Response
```java
     [
      {
        "date": 1488004917102,
        "description": "Welcome offer! preset Balance and currency added to account.",
        "amountSpent": 0,
        "currency": "INR",
        "lastTransactionType": "CREDIT",
        "transactionId": "SYSTEM",
        "userId": 27
    },
    {
        "date": 1488004972633,
        "description": "Test Transaction",
        "amountSpent": 50,
        "currency": "INR",
        "lastTransactionType": "DEBIT",
        "transactionId": "271488004967497",
        "userId": 27
      }
    ]
```
  + Request
```java
      POST /epayApp/api/v1/update/profile?firstName=asad&amp;lastName=ali&amp;email=asadali@gmail.com&amp;mobileNumber=123456789&amp;
      gender=male HTTP/1.1
      Host: localhost:8080
      AUTH_ACCESS_TOKEN: v+/03DCjBWs8GUP/JVyMPvbwcUrj/ojGcY2KP1mcZJikWF/V6ZJSvtEy6DP2jy+2
      TIME_OF_CALL: 1441177929934000
```
     Takes user details as input with access token in header and updates user deatils in db. returns the dto of saved information of particular user.
 
  + Response
```java
   {
    "gender": "MALE",
    "email": "abcpqr@gmail.com",
    "phoneNumber": 9876543210,
    "firstName": "abc",
    "lastName": "pqr"
   }
```
  + Request
```java
      POST /epayApp/api/v1/recharge/wallet?amount=100&amp;currency=inr HTTP/1.1
      Host: localhost:8080
      AUTH_ACCESS_TOKEN: v+/03DCjBWs8GUP/JVyMPvbwcUrj/ojGcY2KP1mcZJikWF/V6ZJSvtEy6DP2jy+2
      TIME_OF_CALL: 1441177929934000
```
      API is responsible for adding amount to wallet, takes amount and currency as mandatory parameter and returns the status of transaction similar to /spend api
  + Response
```java
    {
    "status": "Cheers! Your transaction was successful, and your transaction Id is : 141488022418114"
    }
```
