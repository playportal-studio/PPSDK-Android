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
	* Add dependency com.dynepic.ppsdk_android-0.0.1 to build.gradle

---
## Configure
* Be sure to import the playPORTAL SDK into your "main" activity. 
	```
	import com.dynepic.ppsdk_android.*;
	```
* Copy your clientID, clientSecret & redirectURI from the playPORTAL website into your app
* In your app init, call the SDK configure (e.g. from your apps MainActivity.m):
	```
    public class MainActivity extends AppCompatActivity {

        // The next 3 strings are taken from your "app" definition in the playPORTAL web.
        String id = "iok-cid-444ad028be7e16c1157962992ccd35e33823d6728a33d8fa";
        String sec = "iok-cse-93714e707231284f4d30de950c5d6580434c0b7ae6e7d7f5";
        String uri = "yourapp://redirect";
	      
        PPManager ppsdk;
        ppsdk = PPManager.getInstance(); // playPORTAL is managed as a singleton class
        ppsdk.configure(id, sec, uri, getApplicationContext());

        // Run the playPORTAL SDK user auth check. If user isn't logged in, the SDK will present SSO login
        // and return from that will be to MainActivity
        if(!ppsdk.isAuthenticated()) {
			Intent myIntent = new Intent(this, LoginActivity.class);
            MainActivity.this.startActivity(myIntent);
        }


        // continue with your app's java code here
        
	}
	```

---
## Storage
* When a user logs in to your app, the SDK creates (or opens) a PRIVATE storage bucket just for them. This is secure storage for storing app-specific information for that user.
Data is returned (on no Error) via a lambda function. See Storage details for more info on reading/writing to storage.
```
	ppsdk.PPdatasvc.readBucket(ppsdk.PPuserobj.myUserObject.getMyDataStorage(), "TestData", (String readKey, String readValue, String readData, String readError) -> {
		if (readError == null) {
			Log.d("Testdata read key:", readKey + " value:" + readValue + " data:" + readData);
		} else {
    		Log.d("private readBucket Error:", readError);
		}
	});
```

* The SDK also creates a global PUBLIC storage bucket that all users can write to and read from.
```
	ppsdk.PPdatasvc.readBucket(ppsdk.PPuserobj.myUserObject.getMyGlobalDataStorage(), "TestData", (String readKey, String readValue, String readData, String readError) -> {
		if (readError == null) {
			Log.d("Testdata read key:", readKey + " value:" + readValue + " data:" + readData);
		} else {
    		Log.d("global readBucket Error:", readError);
		}

	});
```
* Call the following method to write to the user's private storage bucket:
```
    ppsdk.PPdatasvc.writeBucket(ppsdk.PPuserobj.myUserObject.getMyDataStorage(), "TestData", gson.toJson(td), false, (String writeKey, String writeValue, String writeData, String writeError) -> {
		if (writeError != null) {
			Log.e("Error writing data to global bucket:", writeError);
		}
	});
```

* Call the following method to write to the global public storage bucket:
```
	ppsdk.PPdatasvc.writeBucket(ppsdk.PPuserobj.myUserObject.getMyGlobalDataStorage(), "TestData", gson.toJson(td), false, (String writeKey, String writeValue, String writeData, String writeError) -> {
    		if (writeError != null) {
    			Log.e("Error writing data to global bucket:", writeError);
    		}
    	});
```
---

### Storage Details

---


## Friends
* Call the following method to retrieve a user's friend's list:
```
```
