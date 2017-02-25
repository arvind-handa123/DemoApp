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

* 
