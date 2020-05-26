# Covid-19-Tracking-Service
A public web service that collects anonymous information about social gatherings attended by infected people. 

## Background
As Covid-19 pandemic is spreading all over the world, contact tracing is becoming a challenge for health institution and government to conduct due to all kinds of privacy issue and staff shortage. 

## Web service

### Data Source
Our web service aims to build a database that can be used to collecte anonymous information about social gatherings attended by infected people. Database can be populated by two types of users: 
1. Verifies users: contact tracers and hospitals can enter data about confirmed cases; 
2. Anonymous users: Any anonymous users can volunteerily enter informarion about themselves. 
First kind of user inputs will be treated as trusted information and stored into database immediately. 
Second kind of user input allow database to be populated more quickly but need to be treated with caution with respect to its reliability. 
Users will be asked to input social events, location and specific date. 

### Database 
We are planning to use CRUD in the early stage for DEMO usage. 
A SQL server or spreadsheet-database hybrid system will be adopted later for the persistence of data.  

### Client-side
Client side allows user to input data and make queries. 
For example: 
* Input known social events, location and date. 
* List events/locations that have more than n infected cases
* Plot a map based on the social events which attended by infected people 
* Raw data available for data-mining 

## Developers & Funding
* [Babak Esfandiari] (http://www.sce.carleton.ca/faculty/esfandiari.html) 
* [Zhe Ji] (https://www.linkedin.com/in/zhe-ji-ba1a51142/) 
This project is funded by [Carleton University] (https://carleton.ca/)



