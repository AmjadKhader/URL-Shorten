# URL Shortener Service
This project provides a URL shortening service that allows users to shorten long URLs and retrieve the original URL from the shortened version. It utilizes Spring Boot for a lightweight and efficient backend implementation.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [API Endpoints](#api-endpoints)


## Features
- Shortens long URLs using a Base64 encoding scheme.
- Stores shortened URLs and their corresponding long URLs in a database.
- Retrieve the original long URL from a shortened URL.
- Validates URL format before shortening.

##  Installation
1. Clone this repository.
2. Ensure you have Java 17, Maven installed, Docker is installed and running.
3. Configure the application properties in src/main/resources/application.properties if needed (e.g., database
   settings).
4. Build the project using Maven: ```mvn clean install```
5. Build docker image: ```docker build -t url-shorten-service .```
6. Run docker compose: ```docker compose up -d ```
7. The application will start on http://localhost:8092.
   You can now use any API testing tool (e.g., Postman) to interact
   with the endpoints.

## API Endpoints

Send a POST request to /url-shorten-service/api/shorten with the url parameter containing the long URL you want to shorten.

Example Request:

```POST /url-shorten-service/api/shorten
Content-Type: application/json
{
"url": "https://www.example.com/long/url/path"
}

Example Response:
{
"url": "https://www.example.com/long/url/path",
"shortUrl": "http://shortenedUrl"
}
```


Send a GET request to /url-shorten-service/api/expand with the url parameter containing the shortened URL you want to expand.

``` GET /url-shorten-service/api/expand?url=http://shortenedUrl
{
"url": "https://www.example.com/long/url/path"
}
```
