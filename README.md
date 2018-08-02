# <b>playPORTAL Android (Java) SDK</b></br>
playPORTAL<sup>TM</sup> provides a service to app developers for managing users of all ages and the data associated with the app and the app users, while providing compliance with required COPPA laws and guidelines. The playPORTAL service is easily accessed via our SDKs, this doc describes the Java SDK for use in Android apps.


## Getting Started
The playPORTAl service requires setting up your app in the playPORTAL.

* ### <b>Step 1:</b> Create playPORTAL Partner Account

	* Navigate to [playPORTAL Partner Dashboard](https://partner.iokids.net)
	* Click on <b>Sign Up For Developer Account</b>
	* After creating your account, email us at [info@playportal.io](mailto:info@playportal.io?subject=Developer%20Sandbox%20Access%20Request) to verify your account.
  </br>

* ### <b>Step 2:</b> Register your App with playPORTAL

	* After confirmation, log in to the [playPORTAL Partner Dashboard](https://partner.iokids.net)
	* In the left navigation bar click on the <b>Apps</b> tab.
	* In the <b>Apps</b> panel, click on the "+ Add App" button.
	* Add an icon, name & description for your app.
	* For "Environment" leave "Sandbox" selected.
	* Click "Add App"
  </br>

* ### <b>Step 3:</b> Generate your Client ID and Client Secret

	* Tap "Client IDs & Secrets"
	* Tap "Generate Client ID"
	* Copy these and save them to a secure place accessible by your app. Be careful not to share them or store them in public version control - they uniquely identify your app and grant the permissions to your app as defined in the [playPORTAL Partner Dashboard](https://partner.iokids.net).
  </br>

* ### <b>Step 4:</b> Add your Redirect URI

	* Add a [Custom URL Scheme for your app](https://developer.apple.com/documentation/uikit/core_app/communicating_with_other_apps_using_custom_urls?language=objc)
	* From the [playPORTAL Partner Dashboard](https://partner.iokids.net) navigate to your app and tap <b>Registered Redirect URIs</b>
	* Enter the your Custom URL Scheme
  </br>

* ### <b>Step 5:</b> Install the SDK

    * Add the following to your project level build.gradle:
    ```java
    repositories {
        maven{url "https://dynepic.bintray.com/maven"}
    }
    ```
    
    * Add the following to your app/module level build.gradle:
    ```java
    implementation 'com.dynepic.ppsdk_android:ppsdk_android:0.0.5@aar'
    ```

# Using the playPORTAL Manager

## Setup:


### * Initialize the manager, and specify context:

You need to specify the context of the activity you are in. PPManager methods cannot be referenced from static context. Any usage of the PPManager requires context. After initializing and configuring the manager, everything else is mostly straight forward.

```java
Activity ACTIVITY_CONTEXT = this;
Context CONTEXT = this;
PPManager ppManager = new PPManager(CONTEXT, ACTIVITY_CONTEXT);
```

### Set your Configuration parameters

Obtain your client KEY, SECRET, and REDIRECT URL from when you setup the playPORTAL developer account. If you need to check to see if the PPManager has been initialized, you can check using the isConfigured() method.

Example:
```java
    ppManager.configure("UNIQUE_KEY_STRING",
    "UNIQUE_SECRET_STRING",
    "unique://redirect/string", 
    "SANDBOX",
    "yourappname");

```


## Using the SSO Login:

This method requires an intent for where you intend to send the user after login. It is called from the PPManager using showSSOLogin().

Example:
```java
Intent intent = new Intent(CONTEXT, yourActivity.class);
ppManager.showSSOLogin(intent);
```

Then, add an "authentication listener". This method in your app will be invoked on changes in authentication status (NB: shown here as a lambda function.)

```
  ppManager.addAuthListener((Boolean isAuthd) -> {
            Log.d("authListener invoked authState:", isAuthd.toString());
            if(!authd) redirectUItoSSOLogin();
        });
```

Next, check if this user is already auth'd into your app and take appropriate action (simple example shown):
```
        if (ppManager.isAuthenticated()) {
            if(ppManager.getUserData().hasUser()){ 
                Intent intent = new Intent(CONTEXT, UserProfileActivity.class);
                CONTEXT.startActivity(intent);
                ACTIVITY_CONTEXT.finish();
            } else { 
                // Redirect to SSO Login screen
                Intent intent = new Intent(CONTEXT, LoginActivity.class);
                CONTEXT.startActivity(intent);
                ACTIVITY_CONTEXT.finish();
            }
        }
    }
```    





## Getting Basic User Data:
Each user has two auto-configured data stores; 

* their App Data (private)
* Global App Data (shared by all users of this app)

These data stores accept reads and writes of JSON formatted data.

```
  public void readData(String bucketname, String key, _CallbackFunction._Data cb)


  parms:
    bucketname - string name of the bucket to be read 
    key - each JSON data is stored as K:V pair and referenced by the Key 
    cb - Callback function invoked on read completion or error
  

Example:
  
  ppManager.getDataManager().readData(userData.getMyDataStorage(), 
                                      "TestData", 
                                      (JsonObject data, String error) -> {
      if (error == null) {
          Log.d("Read bucketName:", userData.getMyDataStorage() + " key:" + "TestData" + " value:" + data.toString());
      } else {
          Log.e("Data read error:", error);
      }
  });
```
NB - lambda functions are utilized to make the examples more concise.