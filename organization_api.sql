CREATE DATABASE organisation_api;
\c organisation_api;
CREATE TABLE departments(id SERIAL PRIMARY KEY,depDescription VARCHAR,depName VARCHAR,nbrEmployees INTEGER);
CREATE TABLE news(id SERIAL PRIMARY KEY,header VARCHAR,contents VARCHAR,deptId INTEGER);
CREATE TABLE users(id SERIAL PRIMARY KEY,name VARCHAR,userPosition VARCHAR,role VARCHAR);
CREATE DATABASE organisation_api_test WITH TEMPLATE organisation_api;
CREATE TABLE users_in_department(
    id SERIAL PRIMARY KEY ,
    userId INTEGER,
    departmentId INTEGER
);