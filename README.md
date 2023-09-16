# Smartwallet

ziniD's Smartwallet is an API developed to make your financial life easier. You can use it to track your finances and keep those pesky credit card installments under control.

## Tech stack

ziniD's Smartwallet was developed with version 3.0.2 of the Spring Boot framework, along with Gradle 7.6, Java 17 and Kotlin JVM plugin 1.7.22.

This application makes use of a PostgreSQL docker container, and relies on it to run.

## Usage

```bash
# run the database container
docker-compose up

# build the application (run lint check, unit and arch tests)
gradle clean build

# run the application
gradle clean bootRun
```

This project features Swagger2 support. With the project running, you can read its documentation [here](http://localhost:8080/swagger-ui/index.html).

## License

[MIT](https://choosealicense.com/licenses/mit/)