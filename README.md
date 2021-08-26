# Project 2- MAAD Group
For Project 2, we have built a car management application, which allows users to register their vehicles, as well as post them for sale and buy other vehicles.
## API
The API is from NHTSA (National Highway Traffic Safety Administration). We will leverage the database to get information on vehicles (e.g. make/model/year) for the users to query cars during registration.
## User Stories
As a user, I can:
- [ ] register a new user account with the system (must be secured with a password)
- [ ] login with my existing credentials.
- [ ] register at least one vehicle.
- [ ] edit the description of my vehicle.
- [ ] report my vehicle as stolen.
- [ ] mark a vehicle as being for sale.
- [ ] view a vehicle's history if vehicle is for sale.
- [ ] make a bid on a vehicle on sale.
- [ ] choose which bid wins my car.
- [ ] transfer ownership of my vehicle without the need of sale on the application.
- [ ] remove vehicle from my current vehicles list.
## Possible Stretch Goals
As a user, I can:
- [ ] report sightings of stolen vehicles
## Minimum Features
- [ ] Basic validation
- [ ] All exceptions are properly caught and handled
- [ ] Proper use of OOP principles
- [ ] Documentation (all classes and methods have basic documentation)
- [ ] SQL Data Persistance (at least 3 tables; all 3NF (normal form))
- [ ] Unit tests for service-layer classes
- [ ] Logging messages and exceptions to a file
- [ ] UI built with Angular
## Tech Stack
- [ ] Java 8
- [ ] Apache Maven
- [ ] PostGreSQL deployed on AWS RDS
- [ ] Git SCM (on GitHub)
- [ ] JUnit
- [ ] Spring 5
- [ ] Angular
## Presentation
- [ ] demostration with powerpoint presentation
- [ ] Presentation date: September 10th, 2021

## Roles
# Git flow manager - Branch management
- Donald
https://github.com/210726-Enterprise/MAAD_project
# Dev-Op engineer - AWS management
- Amit
RDBMS will be deployed to the cloud (AWS RDS)
Java API will be deployed to the cloud (AWS EC2)
UI application will be deployed to the cloud (AWS S3)
# Scrum Master
- Alex
# Testing/Documentation - *possible use of swagger
- Minh
Java API will have >=80% test coverage for service layer
Java API will leverage Spring's MockMvc for integration/e2e tests of controller endpoints
Java API will be adequately documented (Java Docs and web endpoint documentation [Swagger/OpenAPI])
