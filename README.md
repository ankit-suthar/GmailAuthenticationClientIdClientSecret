# GmailAuthenticationClientIdClientSecret

Follow below link to generate Client Id and Client Secret for the gmail account.
https://developers.google.com/adwords/api/docs/guides/authentication

'CALLBACK_URI' is the redirect uri where we want to land up after authentication.
Don't enter slash (/) after uri. 

This functionality will generate 'Refresh Token'.

To generate access token :

POST End Point : https://www.googleapis.com/oauth2/v4/token
Content type : x-www-form-urlencoded

client_id : CLIENT_ID
client_secret : CLIENT_SECRET
redirect_uri : http://localhost:8080
grant_type : authorization_code
code : 1/SDBKJLWSDKSNC

'code' is when we redirect to url. It will ask for permission and when we land up on website, along with redirect url there is code=abc..
Get this 'code' and use it in postman.  

In this code, 'code' will be asked at 'authorizationCode' and above url use it to generate code.