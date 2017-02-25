# ePayApp
The task is to create an backend application that provides a set of APIs to be called from a frontend UI.
# API Requirements
The API will have 4 endpoints and will communicate JSON with at least the following functionality for each end point:

* /login -  a POST request that will accept no input and return a token (which need to be used in subsequent calls to the API, in the Authorization header). Every call  to /login will return a new token and every invocation to this endpoint creates a new user, gives them a preset balance in a preset currency. 

* /balance -  a GET request that will accept an Authorization header (with the token value output from /login) and  will return the current balance along with the currency code.

* /transactions - a GET request that will accept an Authorization header (with the token value output from /login) and  will return a list of transactions done by the user with atleast the date, description, amount, currency for each transaction.

* /spend - a POST request that will accept an Authorization header (with the token value output from /login), JSON content representing one spend transaction with the transaction date, description, amount, currency.

# Technology stack

use dropwizard java framework for the server and postgres for the database (using a cache would be beneficial)
the backend will run as a standalone java process.
