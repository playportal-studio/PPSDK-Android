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
    implementation 'com.dynepic.ppsdk_android:ppsdk_android:0.0.7@aar'
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
  
#### Initialize the playPORTAL manager (PPManager)

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

Add an "authentication listener". This method in your app will be invoked on changes in authentication status (NB: shown here as a lambda function.)
```aidl
        ppManager.addAuthListener((Boolean isAuthd) -> {
            // add auth listener - in this case a lambda function that presents a login dialog if user is not auth'd
            if (!isAuthd) {
                Intent intent = new Intent(CONTEXT, LoginActivity.class);
                CONTEXT.startActivity(intent);
                ACTIVITY_CONTEXT.finish();
            }
        });
```


Next, call the configure method and then check if this user is already auth'd into your app and take appropriate action (simple example shown):
```aidl

        //Configure SDK and check auth state
        ppManager.configure(
                "your-client-id",
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

  
#### SSO Login:
The SSO Login requires an intent for where you intend to send the user after login. It is called from the PPManager using showSSOLogin().

Example:
```aidl
Intent intent = new Intent(CONTEXT, yourActivity.class);
ppManager.showSSOLogin(intent);
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
    Picasso p = new Picasso.Builder(CONTEXT).downloader(ppManager.imageDownloader()).build();
    p.load(ppManager.getPicassoParms()).resize(450, 450).into(imageView, new Callback() {
        @Override
        public void onSuccess() {
            Log.d("Picasso ", "success");
        }

        @Override
        public void onError(Exception e) {
            Log.e("Picasso ", "error:" + e);
        }
    });
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

To read data from the lightning db, use the data() read method.
```aidl
	public void read(String bucketname, String key, _CallbackFunction._Data cb)

	  parms:
	  String bucketname - the name used to create the bucket or userData.myData() for user's private data or userData.myGlobalData() for app global data.
      String key - key in Key:Value pair
      _CallbackFunction._Data cb - the callback function to be invoked on completion of the read (success or failure) with the method signature
      
          public void f(@Nullable JsonObject data, @Nullable String error);
          Alternatively, this callback can be implemented as a lambda function (as shown in the examples). 
```      

Example: read from user's private data at "key".
```aidl
    ppManager.data().read(userData.myData(), key, (JsonObject data, String e) -> {
        if (e == null) {
            Log.d("Read bucketName:",  userData.myData() + " key:" + key + " value:" + data.toString());
        } else {
            Log.e("Data read error:", e);
        }
    });
    
    NB - If a primitive type is read, then the key:value pair (where value is a primitive in { Boolean, Integer, String } will be promoted to a JsonObject, i.e. { key:value } and returned as such. To extract the value, do a "get" on the key.
```

#### Write
To write data to the lightning db, use the data() write method, with the appropriate signature for the data being written.

```aidl
		public void write(String bucketname, String key, JsonObject value, _CallbackFunction._Data cb )

parms:
	  String bucketname - the name used to create the bucket or userData.myData() for user's private data or userData.myGlobalData() for app global data.
      String key - key in Key:Value pair
      JsonObject value - object to store in lightning db. Can also be one of { Boolean, Integer, String } which will use one the matching method signature.
      
      _CallbackFunction._Data cb - the callback function to be invoked on completion of the write (success or failure) with the method signature:
      
          public void f(@Nullable JsonObject data, @Nullable String error);
          Alternatively, this callback can be implemented as a lambda function (as shown in the examples). The callback data can be ignored, but the error should be checked.
```

Example: write a JsonObject "jo" to "key" in user's private data store.

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




