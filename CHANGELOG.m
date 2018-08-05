# Changelog
All notable changes to the playPORTAL Java project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

----
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
