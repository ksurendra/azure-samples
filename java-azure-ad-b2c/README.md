# Azure Active Directory B2C - Java based

This application shows how to Authenticate a user using Azure's Active Directory B2C. Azure's AD B2C has several [authentication protocols](https://docs.microsoft.com/en-us/azure/active-directory-b2c/protocols-overview), in this example we will use [OAuth - ROPC (Resource Owner Password Credentials)](https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-oauth-ropc) using the url `https://login.microsoftonline.com/"+tenantid+"/oauth2/v2.0/token` 

This example is built using [https://helidon.io/docs/latest/#/about/02_introduction](Helidon MP) framework on [https://openjdk.java.net/projects/jdk/11/](OpenJDK 11).

## PreRequisites
1. Login to Azure Portal > Azure Active Directory
2. Create a new Tenant
   - We will need the **Tenant ID** to run this example.
3. Register a new application on Azure portal (Using **App Registrations**)
   - We will need the **Application(Client) ID** to run this example.
4. Create a new User - Azure Active Directory > Users
   - We will need **Username** and **Password**

## Using the application
- Build and run the application (see below)
- Open the URL `http://localhost:9090` . Should open a page with "User Login" form.
- Enter the above details and on-submit, should see a response something like this (if connects successfully)

   ```json
   {
     token_type: "Bearer",
     scope: "User.Read profile openid email",
     expires_in: 3599,
     ext_expires_in: 3599,
     access_token: "eyJ0eXAiO...NduBA",
     refresh_token: "0.AS4A7L...tLew",
     id_token: "eyJ0eX...E6Lw"
   }
   ```

## Build and run

With JDK11+
```bash
mvn clean package
java -jar target/java-azure-ad-b2c.jar
```

## Testing the application

```
curl -X GET http://localhost:9090/greet
{"message":"Hello World!"}
```

## Change PORT
Chnage the PORT in `java-azure-ad-b2c/src/main/resources/META-INF/microprofile-config.properties`
