![](./readmeAssets/studio.png)
# <b>playPORTAL Android (Java) SDK</b></br>
##### playPORTAL Studio<sup>TM</sup> provides a service to app developers for managing users of all ages and the data associated with the app and the app users, while providing compliance with required COPPA laws and guidelines.

## Getting Started

* ### <b>Step 1:</b> Create playPORTAL Studio Account

	* Navigate to [playPORTAL Studio](https://studio.playportal.io)
	* Click on <b>Sign Up For FREE Accountt</b>
	* After creating your account, email us at [info@playportal.io](mailto:info@playportal.io?subject=Developer%20Sandbox%20Access%20Request) to verify your account.
  </br>

* ### <b>Step 2:</b> Register your App with playPORTAL

	* After confirmation, log in to the [playPORTAL Studio](https://studio.playportal.io)
	* In the left navigation bar click on the <b>Apps</b> tab.
	* In the <b>Apps</b> panel, click on the "+ Add App" button.
	* Add an icon, name & description for your app.
	* For "Environment" leave "Sandbox" selected.
	* Click "Add App"
  </br>

* ### <b>Step 3:</b> Generate your Client ID and Client Secret

	* Tap "Client IDs & Secrets"
	* Tap "Generate Client ID"
	* The values generated will be used in 'Step 5'.
  </br>

* ### <b>Step 4:</b> Add a "Registered Redirect URI"

	* Tap "Registered Redirect URIs"
	* Tap "+ Add Redirect URI"
	* Enter your app's redirect uri (e.g. - helloworld://redirect) in to the prompt and click "Submit".
  </br>

* ### <b>Step 5:</b> Install the SDK

    * Add the following to your project level build.gradle:
    ```java
    repositories {
        maven{url "https://playportal-studio.bintray.com/maven"}
    }
    ```
    
    * Add the following to your app/module level build.gradle:
    ```java
    implementation 'com.dynepic.ppsdk_android:ppsdk_android:0.0.12@aar'
    ```

## Using the playPORTAL SDK

### Initialize the playPORTAL SDK

#### Set your Configuration parameters
Obtain your client ID, Client SECRET, and REDIRECT URL from when you setup the playPORTAL developer account. If you need to check to see if the PPManager has been initialized, you can check using the isConfigured() method.


#### The playPORTAL Manager
The playPORTAL manager provides the SDK services:

  * Authentication using playPORTAL SSO
  * User Profile 
  * Friends Profiles
  * Lightning Data - read/write JSON data to Non-volatile db
    * User's private data - data store that is only accessible by this user
    * Global data - data store shared among all users of your app
    * Other data - app user can create additional "named" buckets (either private or shared) for specific usage
    
  Most operations using playPORTAL require web requests to the playPORTAL servers. These 
  operations are asynchronous in nature, i.e. there is an indeterminate amount of time between a 
  request and an associated response. In addition, requests may fail, so responses may indicate 
  that the associated request failed.
  
  The SDK uses lambda functions for all async operations. 
  
  

## Starting up the SDK

### Initialize the playPORTAL manager (PPManager)

The PPManager needs to be initialized before use. This can be done in your startup activity that's run prior to any 
operations being requested from the playPORTAL cloud. The example belows shows a code snippet in a "StartupActivity.java" 
onCreate() method that performs all necessary init.

```aidl
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONTEXT = this; //getApplicationContext()
        ACTIVITY_CONTEXT = this;

        PPManager ppManager = PPManager.getInstance();
        ppManager.setContextAndActivity(CONTEXT, ACTIVITY_CONTEXT);

        ppManager.addAuthListener((Boolean isAuthd) -> {
            // add auth listener - in this case a lambda function that presents a login dialog if user is not auth'd
            if (!isAuthd) {
                Intent intent = new Intent(CONTEXT, LoginActivity.class);
                CONTEXT.startActivity(intent);
                ACTIVITY_CONTEXT.finish();
            }
        });

        //Configure SDK and check auth state
        ppManager.configure("your-client-id",
                "your-client-secret",
                "appname://redirect",
                "SANDBOX",
                "appname",
                (status) -> { 
        	     // lambda function to catch configure response
                    if (ppManager.isAuthenticated() && ppManager.getUserData().hasUser()) {
                        Intent intent = new Intent(CONTEXT, UserProfileActivity.class);
                        CONTEXT.startActivity(intent);
                        ACTIVITY_CONTEXT.finish();
                    } else {
                        Intent intent = new Intent(CONTEXT, LoginActivity.class);
                        CONTEXT.startActivity(intent);
                        ACTIVITY_CONTEXT.finish();
                    }
                });
    }
```

## Using the SDK

### User 
#### Get User Profile

The logged in user's profile is retrieved when the user logs in (via SSO Login). The user's profile 
is then available to the app via a synchronous method as shown below:

```aidl
UserData userdata = ppManager.getUserData();

// Individual fields in the profile can be retrieved as:
String userHandle = userData.getHandle();
String userName = userData.getName();
String firstName = userData.getFirstName();
String lastName = userData.getLastName();

```

#### Display User Profile Image

The logged in user's profile image is available and can be easily captured into an ImageView as follows (note the Resource "R" id will be specified in your implementation and the 450x450 size is defined for convenience):

```aidl
   
    ImageView imageView = (ImageView) findViewById(R.id.profile_iv_filled); 
    ppManager.loadImageByID(CONTEXT, userData.getProfilePic(),imageView);

```

### Friends
#### Get Friends Profiles:
 Friends profiles can be retrieved from the playPORTAL service.

 ```aidl
     ppManager.friends().get((ArrayList<User>friendsList, String e) -> {
         if(friendsList != null && e == null) {
             for (User f : friendsList) System.out.println("friend: " + f.getHandle() + " - " + f.getFirstName() + " " + f.getLastName());
         } else {
             Log.e("get friends error:", e);
         }
     });

```


### Data
Two data stores are auto-created (or opened) for each user. They are:
* User's private app data
* Global app data (shared)

Data can be stored and retrieved from either of these data stores, in addition other data stores 
can be created as needed. All data read/written must be JSON. The read/write methods utilize JsonObjects  
and supported marshalling routines. 

#### Read
```aidl
    ppManager.data().read(userData.myData(), key, (JsonObject data, String e) -> {
        if (e == null) {
            Log.d("Read bucketName:",  userData.myData() + " key:" + key + " value:" + data.toString());
        } else {
            Log.e("Data read error:", e);
        }
    });
```

#### Write

```aidl    
    JsonObject jo;
     
    ppManager.data().write(userData.myData(), key, jo, (JsonObject data, String error) -> {
        if (error == null) {
            Log.d("Wrote bucketName:", userData.myData() + " key:" + key + " value:" + jo.toString() );
        } else {
            Log.e("Data write error:", error);
        }
    });
```


#### Images
User profile pics, friends profile pics, and all other images can be loaded simply by invoking one of the following methods:

```aidl
	public void loadImageByID(Context context, String imageId, ImageView intoImage);

or to have the image resized into the provided view, intoImage:

	public void loadImageByID(Context context, String imageId, ImageView intoImage, int width, int height);

NB - conflicting constraints will be resolved by the Android ImageView. 
```


#### Push Notifications

Push notifications allow apps to receive messages even when the app is not currently active. 
These notifications are typically used to deliver alerts to the app user, allowing the app 
designer a convenient and compelling way to deliver engagement. In the playPORTAL environment,

* Sending push notifications
Push notifications can be sent by:
  - the management console (either to a single or multiple app users)
  - app users

In both cases, the notification received will appear the same, just the source will 
be different.

   
* Receiving push notifications
Configuration is required to enable receipt of push notifications, much of this is handled by
the playPORTAL SDK.

##### Configuration / Setup
The following steps must be performed to enable push notifications:

<<<<<<< HEAD
=======

>>>>>>> 593b4fd6e1aace1e4606dd803371a5268495516c
* Create a new project at Firebase (https://console.firebase.google.com/) 
* Recommended: Go through the tutorial steps for adding Firebase to Android app ("Get started by adding Firebase to your app")
* Register your app with push notifications enabled via the Google Developer console. 
* Download the FCM key (Project Settings -> Cloud Messaging -> Server key)
* Download the google-services.json file from the portal.
* Upload the FCM key into the playPORTAL studio portal under the app definition screen.
* Add the google-services.json into your project's "app" directory (i.e. at the same level as 
your app src directory).

* Add the following statement at the end of your app 'build.gradle' file:

```aidl
apply plugin: 'com.google.gms.google-services'
```

Note: Depending on your app implementation, you will likely need a JSON converter package. 
Google's Gson is a solid package as is Jackson's package; the primary choice dependent on 
whether you prefer auto marshalling of JSONObject to/from POJO (use Jackson), or an a less
POJO centric approach (use Gson).


Your app level build.gradle will then need to contain something like:

```aidl
  dependencies {
     ...
         implementation 'com.google.code.gson:gson:2.8.4'

and/or

         implementation 'com.fasterxml.jackson.core:jackson-core:2.9.5'
         implementation 'com.fasterxml.jackson.core:jackson-databind:2.9.5'
    ...
    }
```

##### Configuring, Sending and Receiving Push Notifications
In order to bind push notifications to your app, push notifications must be enabled by invoking
the following method:
```aidl
	public void enablePushNotifications();
```	

Receipt of push notificdations can then begin, i.e. the playPORTAL studio console, and users of
your app can send notifications among users of your app.

In order to send a notification to another user:

```aidl
	public void sendPushNotification(String msg, String receiverId, _CallbackFunction._GenericWithError cb);

	where:
	   msg - String to be sent to remote user
	   receiverId - id of user to target with the push (id can be gotten from friends list as friend.getUserId())
	   cb - fnx to receive status of push notification, with invocation (Boolean status, String error) -> {}
 
