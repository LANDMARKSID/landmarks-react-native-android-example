# Landmarksid LO SDK react native example

This react-native application demostrates how we can use the Landmarksid LO Android SDK into our react-native application via native-bridge wrapper.

## Features

- Implement landmarksid LO sdk for android
- Use Native Modules to use features from the SDK into react-native

## Getting Started

### Pre-requisits
* Node.js
* React native cli
* Yarn
* Android/IOS SDK setup

## Installation Guide

>Steps to run this sample react-native project.
# Android

```
yarn install
yarn android
```

## Compilation 

Ensure that the `LANDMARKS_TO_PROVIDE` has been swapped for the token provided by our Support team and is added to your `android/app/build.gradle` file.

```
allprojects {
    repositories {
        ...
        maven {
            url "https://jitpack.io"
            credentials { username "LANDMARKS_TO_PROVIDE" }
        }
    }
}
```

Compile via

```
npm run android-compile-no-device
```

# IOS
```
yarn install
```
>in the IOS folder
```
pod install
```
>run from root folder
```
yarn ios
```

## Setup your own project
>Follow the react-native installion guide to setup your react-native project:
* [`React-Native`](https://reactnative.dev/docs/environment-setup)

>Follow the SDK setup guide from the LandmarksID LO Android SDK docs from the link below:
* [`LandmarksID LO SDK Android`](https://docs.landmarksid.com/docs/android_lo)

>To use `Landmarksid LO Android SDK` in react-native application, copy the [`WRAPPER`](android/app/src/main/java/com/reactnativewrappertest/wrapper) folder from this sample application into your own project.

> Add the following line in your `MainApplication.Java` class to register the custom module.

```
                @Override
                protected List<ReactPackage> getPackages() {
                    @SuppressWarnings("UnnecessaryLocalVariable")
                    List<ReactPackage> packages = new PackageList(this).getPackages();
                    // Packages that cannot be autolinked yet can be added manually here, for example:
                    // packages.add(new MyReactNativePackage());
                    packages.add(new RNLandmarksidSdkPackage()); <------
                    return packages;
                }
```

> Add the following code into your `MainActivity.java` class.

```

    private static LandmarksID landmarksId;
    
public void startSDK(LandmarksID.Options options) {
        landmarksId = LandmarksID.getInstance().start(this, options, this);
    }

    public LandmarksID getLandmarksId() {
        return landmarksId;
    }

    public void stopSDK() {
        if (landmarksId != null)
            landmarksId.stop(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        landmarksId.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }


    /**
     * Here we'll setup native events that can be subscribed in react-native code for 
     * android to receive live events.
     */
    @Override
    public void msg(String tag, String message) {
        WritableMap params = Arguments.createMap();
        params.putString("tag", tag);
        params.putString("message", message);

        getReactInstanceManager().getCurrentReactContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(MESSAGE, params);
    }

    @Override
    public void success(String tag, String message) {
        WritableMap params = Arguments.createMap();
        params.putString("tag", tag);
        params.putString("message", message);

        getReactInstanceManager().getCurrentReactContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(SUCCESS, params);
    }

    @Override
    public void error(String tag, String message) {
        WritableMap params = Arguments.createMap();
        params.putString("tag", tag);
        params.putString("message", message);

        getReactInstanceManager().getCurrentReactContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(ERROR, params);
    }

    @Override
    public void onLore(String tag, String time, String message, String type) {
        WritableMap params = Arguments.createMap();
        params.putString("tag", tag);
        params.putString("time", time);
        params.putString("message", message);
        params.putString("type", type);

        getReactInstanceManager().getCurrentReactContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(ON_LORE, params);
    }

    @Override
    public void onInit(String tag, String message) {
        WritableMap params = Arguments.createMap();
        params.putString("tag", tag);
        params.putString("message", message);

        getReactInstanceManager().getCurrentReactContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(ON_INIT, params);
    }

    @Override
    public void onConfig(final boolean androidEnabled, final long timeInterval, final long distanceInterval, final long minSpeedKph, final long maxSpeedKph) {
        WritableMap params = Arguments.createMap();
        params.putBoolean("androidEnabled", androidEnabled);
        params.putDouble("timeInterval", timeInterval);
        params.putDouble("distanceInterval", distanceInterval);
        params.putDouble("minSpeedKph", minSpeedKph);
        params.putDouble("maxSpeedKph", maxSpeedKph);

        getReactInstanceManager().getCurrentReactContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(ON_CONFIG, params);
    }
```

## Usage in React Native


>To use the wrapper in your react-native application follow the code sample below.

1: `import {NativeModules, NativeEventEmitter} from 'react-native';`

2: `const {RNLandmarksidSDK} = NativeModules;`

```

  const onMessageCallback = message => {
      //do something with message
  };

  const onSucessCallback = success => {
      //do something with success
  };

    const onLoreCallback = lore => {
      //do something with lore data
  };

    const onErrorCallback = error => {
      //do something with error
  };


  useEffect(() => {
    //intialize SDK here from android.
    RNLandmarksidSDK.startSDK();

    //register events to get latest updates from the SDK
    //add events for all types and trigger events for all callbacks from android.
    const eventEmitter = new NativeEventEmitter(RNLandmarksidSDK);

    eventEmitter.addListener('message', onMessageCallback);
    eventEmitter.addListener('on_lore', onLoreCallback);
    eventEmitter.addListener('success', onSuccessCallback);
    eventEmitter.addListener('error', onErrorCallback);

    return () => {
      //remove all events in this cleanup function.
      eventEmitter.removeAllListeners('message', 'on_lore', 'success', 'error');
      //stop sdk
      RNLandmarksidSDK.stopSDK();
    };
  },[]);
```

>To add `CustomData` in your react-native application follow the code sample below. You can send String, Integer, boolean and Float values using this method.

```
    RNLandmarksidSDK.setCustomData("sample text", "this is sample data");
```
