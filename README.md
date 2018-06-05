# PPSDK-Android

# <b>playPORTAL Android (Java) SDK</b></br>
playPORTAL <sup>TM</sup> provides a service to app developers for managing users of all ages and the data associated with the app and the app users, while providing compliance with required COPPA laws and guidelines.


## Getting Started

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
	* Unzip the PPSDK-Android.jar file and drag the folders in to the top level of your Android Studio project.

---
## Configure
* Be sure to import the playPORTAL SDK.
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

        // other java code here
	}
	```
---
## Login
* Implement the following function in AppDelegate.m
	```
	- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url {

		[[PPManager sharedInstance] handleOpenURL:url];

		return YES;

	}
	```
* Implement the following method and provide a callback to receive a user once the login flow is completed:
	```
	[PPManager sharedInstance].PPusersvc.addUserListener = ^(PPUserObject *user, NSError *error){
		if (error) {

			NSLog(@"%@ error: %@", NSStringFromSelector(_cmd), error);

		} else {

			// When user is returned you can parse keys and values here
			NSLog(@"handle=%@", user.handle);

		}
	};
	```
* Call the following method to allow a user to log in:
	```
	[[PPManager sharedInstance].PPusersvc login];
	```
---
## Anonymous Login

* Call the following method to allow a user to log in anonymously:
	```
	int age = 12;
	[[PPManager sharedInstance].PPusersvc loginAnonymously:age];
	```
---
## Storage
* When a user logs in to your app for the first time, we create a PRIVATE storage bucket just for them. This is where you can store app-specific information for that user.
```
[[PPManager sharedInstance].PPdatasvc readBucket:[PPManager sharedInstance].PPuserobj.myDataStorage andKey:(NSString*)@"SOME_KEY" handler:^(NSDictionary* d, NSError* error) {
      if(error) {
      	NSLog(@"%@ error: %@", NSStringFromSelector(_cmd), error);
      } else if (d) {
					//Get the value from the returned dictionary
				[[d valueForKey:@"SOME_KEY"]
      }
  }];
```
* We also create a global PUBLIC storage bucket that all users can write to and read from.
```
[[PPManager sharedInstance].PPdatasvc readBucket:[PPManager sharedInstance].PPuserobj.myAppGlobalDataStorage andKey:(NSString*)@"SOME_OTHER_KEY" handler:^(NSDictionary* d, NSError* error) {
      if(error) {
          NSLog(@"%@ error: %@", NSStringFromSelector(_cmd), error);
      } else if (d) {
				//Get the value from the returned dictionary
				[[d valueForKey:@"SOME_OTHER_KEY"]
      }
  }];
```
* Call the following method to write to the user's private storage bucket:
```
[[PPManager sharedInstance].PPdatasvc
	writeBucket:[PPManager sharedInstance].PPuserobj.myDataStorage
	andKey:(NSString*)@"SOME_KEY"
	andValue:(NSString*)@"SOME_VALUE"]
	push:FALSE
	handler:^(NSError *error) {
		if(error) {
			NSLog(@"%@ error: %@", NSStringFromSelector(_cmd), error);
		} else {
			//Bucket was written to successfully
		}
	}
];
```

* Call the following method to write to the global public storage bucket:
```
[[PPManager sharedInstance].PPdatasvc
	writeBucket:[PPManager sharedInstance].PPuserobj.myAppGlobalDataStorage
	andKey:(NSString*)@"SOME_KEY"
	andValue:(NSString*)@"SOME_VALUE"]
	push:FALSE
	handler:^(NSError *error) {
		if(error) {
			NSLog(@"%@ error: %@", NSStringFromSelector(_cmd), error);
		} else {
			//Bucket was written to successfully
		}
	}
];
```
---
## Friends
* Call the following method to retrieve a user's friend's list:
```
[[PPManager sharedInstance].PPusersvc getFriendsProfiles:^(NSError *error) {
        if (error) {
            NSLog(@"%@ error: %@", NSStringFromSelector(_cmd), error);
        } else {
					//Get the user's number of friends
					[[PPManager sharedInstance].PPfriendsobj getFriendsCount]
					//Get a particular friend
					[[PPManager sharedInstance].PPfriendsobj getFriendAtIndex:0];
					[NSString stringWithFormat:@"%@ %@", [d valueForKey:@"firstName"], [d valueForKey:@"lastName"]];
        }
    }];
```
