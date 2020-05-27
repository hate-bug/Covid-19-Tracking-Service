# Covid-19-Tracking-Service
A public web service that collects anonymous and verified information about social gatherings attended by anonymous infected people. 

## Background
As Covid-19 pandemic is spreading all over the world, contact tracing is becoming a challenge for health institution and government to conduct due to all kinds of privacy issue and staff shortage. 

## Web service

### Data Source
Our web service aims to build a database that can be used to collecte anonymous information about social gatherings attended by infected people. Database can be populated by two types of users: 
1. Verified users: contact tracers and hospitals can enter data about confirmed cases; 
2. Anonymous users: Any anonymous user can voluntarily enter information about themselves. 
First kind of user input will be treated as trusted information. 
Second kind of user input allows the database to be populated more quickly but needs to be treated with caution with respect to its reliability. Queries on the databse will be able to specify whether they only request verfied responses or all responses.
Users will be asked to input social events, location and specific date. 

### Database 
We are planning to use an in-memory database in the early stages for DEMO usage. 
A SQL server will be adopted later for the persistence of data. We will also experiment with spreadsheet-database hybrid systems such as AirTable for prototyping and experimental purposes as well, since this is a mostly a CRUD application.  

### Client-side
Client side allows user to input data and make queries. 
For example: 
* Input known social events, location and date. 
* List events/locations that have more than n infected cases
* Plot a map based on the social events which attended by infected people 
* Raw data available for data-mining 

## Developers & Funding
* [Babak Esfandiari](http://www.sce.carleton.ca/faculty/esfandiari.html) 
* [Zhe Ji](https://www.linkedin.com/in/zhe-ji-ba1a51142/) 
* This project is funded by [Carleton University] (https://carleton.ca/)



