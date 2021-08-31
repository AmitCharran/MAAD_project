# Project 2- MAAD Group
For Project 2, we have built a car management application, which allows users to register their vehicles, as well as post them for sale and buy other vehicles.
## API
The API is from NHTSA (National Highway Traffic Safety Administration). We will leverage the database to get information on vehicles (e.g. make/model/year) for the users to query cars during registration. https://vpic.nhtsa.dot.gov/api/
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
### Front-end
As a user, I can:
- [ ] Access a page on the website to register a new account
- [ ] Access a page on the website to login to my account.
- [ ] While logged in, fill out a form to register at least one vehicle.
- [ ] While logged in, fill out a form to edit the description of my vehicle.
- [ ] While logged in, report my vehicle as stolen.
- [ ] While logged in, mark a vehicle as being for sale.
- [ ] Anyone can view for sale vehicles.
- [ ] Anyone can view a vehicle's history if vehicle is for sale .
- [ ] While logged in, make a bid on a vehicle on sale.
- [ ] While logged in, choose which bid wins my car.
- [ ] While logged in, transfer ownership of my vehicle without the need of sale on the application.
- [ ] While logged in, remove vehicle from my current vehicles list.
### Back-end
As a user, I can:
- [ ] intake user credentials, encrypt them, and send them to the database
- [ ] compare inputted credentials to database to find a match, and if a match is found, return user information.
- [ ] intake user vehicle information and persist it to our database
  - [ ] While user inputs each general descriptor of vehicle (make, model), query 3rd party api to get all available models for a given make, and all available years for a given model
- [ ] update information of vehicle in database with the new information.
- [ ] change column in vehicle table to mark vehicle as stolen.
- [ ] change column in vehicle table to mark vehicle as for sale and add appropriate information to the Sale table in the database.
  - [ ] prompt user to update information for vehicle
- [ ] retrieve vehicle field information based on sale_id when user wants to view information about a vehicle on sale
- [ ] take user input for a bid and persist it to the bid table.
- [ ] send all bid information for a given to vehicle owner for them to view and choose a buyer (user_id -> vehicle_id -> sale_id -> bid information)
- [ ] user inputs a new username for a given vehicle_id that is updates on the vehicle table.
- [ ] delete vehicle from my vehicles table given vehicle_id that corresponds to the same user_id.
## Possible Stretch Goals
As a user, I can:
- [ ] Create archive tables for the Vehicle, Sale, Bid tables
- [ ] Allow transfer recipients to accept or decline the transfer of onwership of a vehicle.
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
### Git flow manager - Branch management
- Donald
- [ ] https://github.com/AmitCharran/MAAD_project
- [ ] Keep github pristine as possible
- [ ] Solve merge conflicts
- [ ] Keep everyone on their branches
### Dev-Op engineer - AWS management
- Amit
- [ ] RDBMS will be deployed to the cloud (AWS RDS)
- [ ] Java API will be deployed to the cloud (AWS EC2)
- [ ] UI application will be deployed to the cloud (AWS S3)
- [ ] Report on deployment health
### Scrum Master
- Alex
- [ ] Manage day to day
- [ ] Conduct Stand-ups each day
- [ ] Keep everything on time
- [ ] Dissolve impediments
### Testing/Documentation
- Minh
- [ ] Java API will have >=80% test coverage for service layer
- [ ] Java API will leverage Spring's MockMvc for integration/e2e tests of controller endpoints
- [ ] Java API will be adequately documented (Java Docs and web endpoint documentation [Swagger/OpenAPI])
- [ ] Make sure people are writing their testing and documentation on their code (No merge without tests and docs)
