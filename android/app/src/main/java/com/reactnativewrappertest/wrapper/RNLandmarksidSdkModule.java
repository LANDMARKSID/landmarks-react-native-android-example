
package com.reactnativewrappertest.wrapper;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.landmarksid.lo.core.LandmarksID;
import com.landmarksid.lo.types.CustomData;
import com.reactnativewrappertest.BuildConfig;
import com.reactnativewrappertest.MainActivity;

import org.jetbrains.annotations.NotNull;

import static com.reactnativewrappertest.wrapper.Const.RN_LANDMARKSID_SDK;


public class RNLandmarksidSdkModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    //TODO : secure the keys in .properties file for safety.
    private static final String API_KEY = "REPLACE_ME";
    private static final String APP_ID = "REPLACE_ME";
    private static final String APP_SECRET = "REPLACE_ME";

    private static final boolean IS_DEBUG_MODE = BuildConfig.DEBUG;

    private CustomData customData = new CustomData();


    public RNLandmarksidSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    public void startSDK() {

        LandmarksID.Options options = new LandmarksID.Options()
                .setApiKey(API_KEY)
                .setAppMetadata(APP_ID, APP_SECRET)
                .setDebugMode(IS_DEBUG_MODE)
                // TODO set the unique id of your user
                .setCustomerId("ID of your Customer")// TODO add actual customer Id here.
                .setCustomData(getDefaultData()); // TODO remove this when you are using your own custom data

        ((MainActivity) reactContext.getCurrentActivity()).startSDK(options);
    }

    @ReactMethod
    public void setCustomData(String name, String value) {
        customData.add(name, value);
        ((MainActivity) reactContext.getCurrentActivity()).getLandmarksId().setCustomData(customData);
    }

    @ReactMethod
    public void setCustomData(String name, Float value) {
        customData.add(name, value);
        ((MainActivity) reactContext.getCurrentActivity()).getLandmarksId().setCustomData(customData);
    }

    @ReactMethod
    public void setCustomData(String name, Integer value) {
        customData.add(name, value);
        ((MainActivity) reactContext.getCurrentActivity()).getLandmarksId().setCustomData(customData);
    }

    @ReactMethod
    public void setCustomData(String name, Boolean value) {
        customData.add(name, value);
        ((MainActivity) reactContext.getCurrentActivity()).getLandmarksId().setCustomData(customData);
    }

    @ReactMethod
    public void stopSDK(){
        ((MainActivity) reactContext.getCurrentActivity()).stopSDK();
    }

    // Required for rn built in EventEmitter Calls else gives warning in console
    @ReactMethod
    public void addListener(String eventName) {

    }
    // Required for rn built in EventEmitter Calls else gives warning in console
    @ReactMethod
    public void removeListeners(Integer count) {

    }

    private CustomData getDefaultData(){
        CustomData customData = new CustomData();
        customData.add("country", "AU");
        customData.add("env", "production");
        customData.add("test", true);
        return customData;
    }

    @Override
    public @NotNull String getName() {
        return RN_LANDMARKSID_SDK;
    }

}