# ePayApp
The task is to create an backend application that provides a set of APIs to be called from a frontend UI.
# API Requirements
The API will have 4 endpoints and will communicate JSON with at least the following functionality for each end point:

* /login -  a POST request that will accept no input and return a token (which need to be used in subsequent calls to the API, in the Authorization header). Every call  to /login will return a new token and every invocation to this endpoint creates a new user, gives them a preset balance in a preset currency. 

* /balance -  a GET request that will accept an Authorization header (with the token value output from /login) and  will return the current balance along with the currency code.

* /transactions - a GET request that will accept an Authorization header (with the token value output from /login) and  will return a list of transactions done by the user with atleast the date, description, amount, currency for each transaction.

* /spend - a POST request that will accept an Authorization header (with the token value output from /login), JSON content representing one spend transaction with the transaction date, description, amount, currency.

* /update/profile - a POST request that will accept an Authorization header (with the token value output from /login),  input parameter  content representing user name, email, mobile number, gender and other details.

* /recharge/wallet - a POST request that will accept an Authorization header (with the token value output from /login),  input parameter  content representing amount to be added, with currency and other optional detail like coupan code, remark etc.

# Technology stack

* Used Java and related framework (JPA,Spring,Hibernate,Spring Rest etc) and postgres for the database. 
* Used Memcache for caching frequently used data, Application build using Tomcat server, However any other server can also be used.

the backend will run as a standalone java process. 

# Tutorial

* Setting Database Configuration
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
    Other than these below are some of the configurable constant/values which needs to be loaded in db. Default values are hardcoded , which will be loaded in case configurable values are not found in db, such as., 
      +   PRESET_AMOUNT: 99.0
      +   PRESET_CURRENCY: "INR"
      +   PRESET_DESCRIPTION: "Welcome offer! preset Balance and currency added to account."
      +   TRAXN_SUCCESS_MESSAGE: "Cheers! Your transaction was successful, and your transaction Id is : "
      +   TRAXN_FAILURE_MESSAGE: "Oops! Something went wrong, your transaction was unsuccessful."
      +   INSUFFICIENT_BALANCE:  "Sorry! Insufficient balance, transaction could not be completed."
    
  
* Configuring Memcache
  + install and start memcache server, the memcache host and port need to be entered in memcached.properties file, as shown below.              MEMCACHED_SERVERS=localhost:11211
  
*  API's
  + Request
    curl -X POST -H "Cache-Control: no-cache" -H "Postman-Token: c43da818-b4aa-1b39-31ae-e63e7d4783d4" "{{host}}/epayApp/api/v1/login"
    API is responsible for creating a new user, and adding some preset balance to the user Account. It simply returns the token which       can be used in subsequent API calls for retrieving user details and other info. sample token response is mentioned below.
  + Response
   {
    "AUTH_ACCESS_TOKEN": "1tNOYF9qeyAqeuk2RRuZPMKvwnR3skCK6y/Vu9/3UGNzi/+Ezob1t6YLCtc46FJs",
    "TIME_OF_CALL": "1441177929934000"
  }
  
  + Request
    
    curl -X GET -H "AUTH_ACCESS_TOKEN: 1tNOYF9qeyAqeuk2RRuZPMKvwnR3skCK6y/Vu9/3UGNzi/+Ezob1t6YLCtc46FJs" -H "TIME_OF_CALL:                  1441177929934000" -H "Cache-Control: no-cache" -H "Postman-Token: 28288cf1-001d-78b3-4c4b-48491d8234a0" "{{host}}/epayApp/api/v1/balance"
    A GET request that will accept an Authorization header (with the token value output from /login) and  will return the current           balance along with the currency code and other info. sample response is shown below.
    
  + Response
    {
    "userId": 27,
    "currentBalance": 49,
    "currencyCode": "INR",
    "lastTraxnType": "DEBIT"
    }
    
  + Request

  curl -X POST -H "AUTH_ACCESS_TOKEN: 1tNOYF9qeyAqeuk2RRuZPMKvwnR3skCK6y/Vu9/3UGNzi/+Ezob1t6YLCtc46FJs" -H "TIME_OF_CALL: 1441177929934000" -H "Cache-Control: no-cache" -H "Postman-Token: 1224fa94-bfe4-e288-fd80-cfbe19ff2f84" -d '{"date":"" ,
  "description":"1st Transaction",
  "amountSpent": 50.0,
  "currency":"inr"
  }' "{{host}}/epayApp/api/v1/spend?payload=            {%22date%22:%22%22%2C%22description%22:%22Transaction%22%2C%20%22amountSpent%22:%2050.0%2C%20%22currency%22:0%20}"

    A POST request that will accept an Authorization header (with the token value output from /login), JSON payload content representing one spend transaction with the transaction date, description, amount, currency etc. It will simply return status of Your transaction with transaaction Id(In case of successful transaction).

  + Response in case of successful purchase

    {
    "status": "Cheers! Your transaction was successful, and your transaction Id is : 271488004967497"
    }
  + Response in case of unsuccessful purchases
  
    {
  "status": "Sorry! Insufficient balance, transaction could not be completed."
    }
 
   or
  
   {
  "status": "Oops! Something went wrong, your transaction was unsuccessful."
   }

  + Request

      curl -X GET -H "AUTH_ACCESS_TOKEN: 1tNOYF9qeyAqeuk2RRuZPMKvwnR3skCK6y/Vu9/3UGNzi/+Ezob1t6YLCtc46FJs" -H "TIME_OF_CALL: 1441177929934000" -H "Cache-Control: no-cache" -H "Postman-Token: 75c9f06c-4503-35a3-4f2c-a03dddd87ee2" "{{host}}/epayApp/api/v1/transactions"

    API takes access token as input in header and returns list of transaction done on a particular account by user or system.
    
  + Response

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

  + Request

      curl -X POST -H "AUTH_ACCESS_TOKEN: eVcnHTqMilOI2tC1PN4GaGauAF6ZyCEhRlgONmrLabShRf7NJGgsPQW9lXaCjA19" -H "TIME_OF_CALL: 1441177929934000" -H "Cache-Control: no-cache" -H "Postman-Token: bd0e392c-c551-59c9-cdf5-c2d64543f819" "{{host}}/epayApp/api/v1/update/profile?firstName=ABC&lastName=PQR&email=abcpqr@gmail.com&mobileNumber=9876543210&gender=male"
     Takes user details as input with access token in header and updates user deatils in db. returns the dto of saved information of particular user.
 
  + Response

   {
    "gender": "MALE",
    "email": "abcpqr@gmail.com",
    "phoneNumber": 9876543210,
    "firstName": "abc",
    "lastName": "pqr"
   }

  + Request

      curl -X POST -H "AUTH_ACCESS_TOKEN: JLlxnrfCJ8Y4ibkY5aVlLs2xWVkmVtJGCqrxAJlddJMJ0LN6QlJVkP//VRcVBNsz" -H "TIME_OF_CALL: 1441177929934000" -H "Cache-Control: no-cache" -H "Postman-Token: 934da843-00e6-d986-00ba-a13103928e1e" "{{host}}/epayApp/api/v1/recharge/wallet?amount=100&currency=inr&email=abcpqr@gmail.com&remark=test"

      API is responsible for adding amount to wallet, takes amount and currency as mandatory parameter and returns the status of transaction similar to /spend api
  + Response

    {
    "status": "Cheers! Your transaction was successful, and your transaction Id is : 141488022418114"
    }
