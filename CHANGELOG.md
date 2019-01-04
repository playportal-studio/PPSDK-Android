# Changelog
All notable changes to the playPORTAL Java project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

----
### 0.1.0 - 2018-09-28
##### Changed
- Fixed issue with Picasso image download use (converted to singleton)
##### Added
- Added methods to support image download and not store those to Android disk although they're still cached
- Converted existing retrieval of profile images to use these image download methods

### 0.0.12 - 2018-08-28
##### Changed
- Fixed issue with image load failing on auth fail
- Support for minSdkVersion 24 (down from 26)
- Changed some logging

##### Added
- Added push notifications using FCM
- Added image load convenience method
- Added JSONObject write bucket method (for convenience on POJO / JSON serialization) 

### 0.0.11 - 2018-08-25
##### Changed
- Migrated jar file to a new Maven repo.

### 0.0.9 - 2018-08-08
##### Changed
- disabled minification / obfuscation via build.gradle
- Update base URLs to support server domain migration
- removed "unused" files

### 0.0.9 - 2018-08-08
##### Added
- minification / obfuscation via build.gradle

### 0.0.8 - 2018-08-07
##### Added
- additional details on read/writes in README

##### Changed
- added data() read/write methods to support JSON primitives (Boolean, Integer, String) in addition to existing support for JsonObject.
- added DEV environment along with SANDBOX and PRODUCTION

### 0.0.7 - 2018-08-05
##### Added
- logout() method
- method for downloading a user's profile image
- ability to download friends
- CHANGELOG
##### Changed
- README to better explain how to use the SDK
- Minor bug fix

### 0.0.6 - 2018-08-02
##### Added
- Retry logic on http 401 errors
- Auth callback on http 403 errors (auth = false)

##### Changed
- Simplified some of the method names (e.g. readBucket is now read, and writeBucket is now write)


### 0.0.5 - 2018-07-27
##### Added
- Lightning Data (read/write/create data stores)

##### Changed
- Fixed issue with marshalling to/from JSON


### 0.0.0.4 - 2018-07-19
##### Changed
- Bug fixes

### 0.0.0.3 - 2018-07-19 . Initial Release
##### Added
- Initial version of this SDK
- Core functionality
  - SSO using playPORTAL Auth gateway
  - User profile 
  - Friends list

----

### Unreleased
##### 0.0.2
##### 0.0.1
