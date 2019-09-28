CREATE DATABASE organisation_api;
\c organisation_api;
CREATE TABLE departments(id SERIAL PRIMARY KEY,depDescription VARCHAR,depName VARCHAR,nbrEmployees INTEGER);
CREATE TABLE news(id SERIAL PRIMARY KEY,contents VARCHAR,deptId INTEGER);
CREATE TABLE users(id SERIAL PRIMARY KEY,name VARCHAR,userPosition VARCHAR,role VARCHAR);
CREATE DATABASE organisation_api_test WITH TEMPLATE organisation_api;
