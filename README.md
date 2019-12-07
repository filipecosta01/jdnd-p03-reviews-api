# Reviews API 
Supports operations for writing reviews and listing reviews for a product but with no sorting or filtering.

### Prerequisites
MySQL needs to be installed and configured.
Default MySQL configuration was followed in this project, meaning that username is "root" without any password. Change "application.properties" file accordingly to your configurations.

**Important**: Create a database named "ecommerce" before running the project. Check instructions online on how to do that.
Flyway is used to run migrations. The file for migration can be found in "resources/db/migration". It contains table creations plus all necessary constraints.

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing JPA Data with REST](https://spring.io/guides/gs/accessing-data-rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
