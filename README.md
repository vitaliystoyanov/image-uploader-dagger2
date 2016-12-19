# Image uploader for [pic.co.ua](http://pic.co.ua) ![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)

pic.co.ua is a photo hosting that allows you to upload and share images.

<img src="https://github.com/vitaliystoyanov/image-uploader-dagger2/raw/master/demo/demo.gif" width="320">

<a href="https://play.google.com/store/apps/details?id=ua.co.pic.imageuploader">
<img alt="Get it on Google Play" src="http://steverichey.github.io/google-play-badge-svg/img/en_get.svg" height="76px" />
</a>

## Features
- Take a photo or pick it from gallery
- Share via social networks or email
- Open links in browser
- Copy links to clipboard
- Notifications
- Works in a background service
- Supports multi languages

## Libraries
* [Dagger 2](http://google.github.io/dagger/)
* [Retrofit 2](http://square.github.io/retrofit/)
* [Mosby](http://hannesdorfmann.com/mosby/)
* [Butterknife](http://jakewharton.github.io/butterknife/)
* [Picasso](http://square.github.io/picasso/)
* [Google Support Libraries](http://developer.android.com/tools/support-library/index.html)

## Testing Libraries
* [JUnit](http://junit.org/junit4/)

## Requirements
To compile and run the project you'll need:

- [Android SDK](http://developer.android.com/sdk/index.html)
- [Android N (API 24)](http://developer.android.com/tools/revisions/platforms.html)
- Android SDK Tools
- Android SDK Build Tools `24.0.0`
- Android Support Repository

Building
--------

To build, install and run a debug version, run this from the root of the project:

```
./gradlew assembleDebug
```

Testing
-------

To run **unit** tests on your machine:

```
./gradlew test
```

To run **instrumentation** tests on connected devices:

```
./gradlew connectedAndroidTest
```
