# Dockerized Spring Batch

Copyright © 2020 Alexandre BULATOVIC

This project is a simple batch importing data from a CSV file "sample-data.csv" into an [in-memory database](https://en.wikipedia.org/wiki/HSQLDB).

The data comprises 1000 lines of generated data with [Mockaroo](https://www.mockaroo.com/) with an ID, a first name, a last name, an e-mail, a gender and an IP address. 

These data are imported into the database and logged in the console.

In order to run the program, you need [Docker](https://www.docker.com/products/docker-desktop).

## How to use : 
1) Build the image : 
`docker build --tag springbatchexample .`
2) Run the image in a new container : 
`docker run --publish 8000:8080 --name myContainer springbatchexample`
3) Remove the container : 
`docker rm --force myContainer`
