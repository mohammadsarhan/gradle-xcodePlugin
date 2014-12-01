gradle-xcodePlugin
==================

gradle-xcodePlugin makes it easier to build Mac and iOS projects by specifying the build settings in a single configuration file. The goal is to keep the build file as simple as possible, but also enable a great flexibility for the build.

The gradle-xcodePlugin uses the Apple command line tools (like xcodebuild) to perform the build.

Here a brief overview of the features:

* Build iOS and Mac projects
* Override sign settings for iOS builds
* Perform unit tests
* Support for multiple Xcodes (on one machine)
* [Cocoapods](Cocoapods) support
* [Appledoc](http://gentlebytes.com/appledoc/) support
* Code coverage support (using [gcovr](/http://gcovr.com)
* [Hockeykit](http://hockeykit.net/), [HockeyApp](http://hockeyapp.net), [DeployGate](https://deploygate.com/) , [TestFlight](https://www.testflightapp.com) (new Apple Testflight is not supported yet, but on the todo list)
* [Sparkle](http://sparkle-project.org)


## Requirements

* Xcode 5 or greater
* [Gradle](http://gradle.org) 2.0 or greater
* Java 1.6 or greater


**Current stable version is 0.9.15**

0.9.14 and 0.10.+ supports Xcode 5, Xcode 6 and Xcode 6.1


## Documentation

* [Parameters Documentation](Documentation/Parameters.md)


## Usage

Create a build.gradle file and place it in the same directory where xcodeproj file lies.

Here the minimal content you need in your build.gradle file:

```
buildscript {
  repositories {
    maven {
      url('http://openbakery.org/repository/')
    }
    mavenCentral()
  }
  dependencies {
    classpath group: 'org.openbakery', name: 'xcodePlugin', version: '0.9.+'
  }
}
apply plugin: 'xcode'

xcodebuild {
  target = 'MY-TARGET'
}

```

## Example

You find an example project in [example/Example/](example/Example/) with a working build.gradle file.
After you have fetched the example go to the `example/Example` directory and you build the project different targets:

* Build  with `gradle build`
* Run the unit tests with `gradle test` or `gradle continuous`
* Perform a device build and upload it to hockeyapp with `gradle integration`. Here you need to specify your sign settings first (see [Signing](Documentation/Parameters.md#sign-settings) ). Open the build.gradle file an follow the instructions.
* Perform an appstore build with `gradle appstore`. (Also the sign settings are needed).

