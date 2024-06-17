### Setup Instructions

1. **Initialize Database:**
    - Use Docker Compose to start the database:
      ```bash
      docker-compose -f compose.yaml up -d
      ```

2. **Access Swagger Documentation:**
    - Once the application is running, you can access the Swagger UI for API documentation at:
      [http://localhost:8443/swagger-ui/index.html](http://localhost:8443/swagger-ui/index.html)

### 
I wasn't entirely certain about the "originalContent" property in PublisherContent class, I assumed it referred to unedited content from the description <description>. 

# Scraping homework

Your goal is to write code that will scrape RSS content feed from an external publisher that is available under this url address: https://connect.thairath.co.th/ws/kaikai/content/mirror. The resulsts of the scraping should be converted to PublisherContent class and stored in the database.

Make sure that subsequent scraping runs will not be duplicated. It is not needed to update existing PublisherContents that were stored previously, if the content already exists it can be ignored.

Make sure to remove links to other articles from the content if there are any.

The use of third party libraries is recommended.

Consider implementing unit tests.

Copy this repository and provide us with the link to your version after you're done.

Good luck!
