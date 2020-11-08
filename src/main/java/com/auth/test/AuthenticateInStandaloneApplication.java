package com.auth.test;

import com.google.auth.oauth2.ClientId;
import com.google.auth.oauth2.UserAuthorizer;
import com.google.auth.oauth2.UserCredentials;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class AuthenticateInStandaloneApplication {

    // Follow the steps to generate Client Id and CLient Secret for the gmail account.
    // https://developers.google.com/adwords/api/docs/guides/authentication

    // Scopes for the generated OAuth2 credentials. The list here only contains the AdWords scope,
    // but you can add multiple scopes if you want to use the credentials for other Google APIs.

    private static final List<String> SCOPES = Collections.singletonList("https://mail.google.com"); // add multiple scope for various accessibility

    private static final String CALLBACK_URI = "http://localhost:8080"; // no slash at the end

    private static final String CLIENT_ID = "ENTER_CLIENT_ID_HERE";

    private static final String CLIENT_SECRET = "ENTER_CLIENT_SECRET_HERE";

    public static void main(String[] args) throws IOException {
        // Generates the client ID and client secret from the Google Cloud Console:
        // https://console.cloud.google.com
        String clientId;
        String clientSecret;

        clientId = CLIENT_ID;
        clientSecret = CLIENT_SECRET;

        new AuthenticateInStandaloneApplication().runExample(clientId, clientSecret);
    }

    public void runExample(String clientId, String clientSecret) throws IOException {
        UserAuthorizer userAuthorizer =
                UserAuthorizer.newBuilder()
                        .setClientId(ClientId.of(clientId, clientSecret))
                        .setScopes(SCOPES)
                        .setCallbackUri(URI.create(CALLBACK_URI))
                        .build();
        URL authorizationUrl = userAuthorizer.getAuthorizationUrl(null, null, null);
        System.out.printf("Paste this url in your browser:%n%s%n", authorizationUrl);

        // Waits for the authorization code.
        System.out.println("Type the code you received here: ");
        @SuppressWarnings("DefaultCharset") // Reading from stdin, so default charset is appropriate.
        String authorizationCode = new BufferedReader(new InputStreamReader(System.in)).readLine();

        // Exchanges the authorization code for credentials and print the refresh token.
        UserCredentials userCredentials =
                userAuthorizer.getCredentialsFromCode(authorizationCode, null);
        System.out.printf("Your refresh token is: %s%n", userCredentials.getRefreshToken());

        // Prints the configuration file contents.
        Properties adsProperties = new Properties();
        adsProperties.put("api.googleads.clientId", clientId);
        adsProperties.put("api.googleads.clientSecret", clientSecret);
        adsProperties.put("api.googleads.refreshToken", userCredentials.getRefreshToken());
        adsProperties.put("api.googleads.developerToken", "INSERT_DEVELOPER_TOKEN_HERE");

        showConfigurationFile(adsProperties);
    }

    private void showConfigurationFile(Properties adsProperties) throws IOException {
        System.out.printf(
                "Copy the text below into a file named %s in your home directory, and replace "
                        + "INSERT_XXX_HERE with your configuration:%n",
                "ads.properties");
        System.out.println(
                "######################## Configuration file start ########################");
        adsProperties.store(System.out, null);
        System.out.printf(
                "# Required for manager accounts only: Specify the login customer ID used to%n"
                        + "# authenticate API calls. This will be the customer ID of the authenticated%n"
                        + "# manager account. You can also specify this later in code if your application%n"
                        + "# uses multiple manager account + OAuth pairs.%n"
                        + "#%n");
        System.out.println(
                "# " + "api.googleads.loginCustomerId" + "=INSERT_LOGIN_CUSTOMER_ID");
        System.out.println(
                "######################## Configuration file end ##########################");
    }
}
