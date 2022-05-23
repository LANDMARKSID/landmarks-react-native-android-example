package com.reactnativewrappertest;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.landmarksid.lo.core.LandmarksID;
import com.landmarksid.lo.logging.EventLogListener;

import static com.reactnativewrappertest.wrapper.Const.ERROR;
import static com.reactnativewrappertest.wrapper.Const.MESSAGE;
import static com.reactnativewrappertest.wrapper.Const.ON_CONFIG;
import static com.reactnativewrappertest.wrapper.Const.ON_INIT;
import static com.reactnativewrappertest.wrapper.Const.ON_LORE;
import static com.reactnativewrappertest.wrapper.Const.SUCCESS;

public class MainActivity extends ReactActivity implements EventLogListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static LandmarksID landmarksId;

    /**
     * Returns the name of the main component registered from JavaScript. This is used to schedule
     * rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "ReactNativeWrapperTest";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

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
     * Here we'll setup native events that can be subscribed in react-native code for android
     * to receive live events.
     */
    @Override
    public void msg(String tag, String message) {
        Log.i(TAG, "msg: " + "tag:" + tag + "message:" + message);

        WritableMap params = Arguments.createMap();
        params.putString("tag", tag);
        params.putString("message", message);

        getReactInstanceManager().getCurrentReactContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(MESSAGE, params);
    }

    @Override
    public void success(String tag, String message) {
        Log.i(TAG, "success: " + "tag:" + tag + "message:" + message);
        WritableMap params = Arguments.createMap();
        params.putString("tag", tag);
        params.putString("message", message);

        getReactInstanceManager().getCurrentReactContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(SUCCESS, params);
    }

    @Override
    public void error(String tag, String message) {
        Log.e(TAG, "error: " + "tag:" + tag + "message:" + message);
        WritableMap params = Arguments.createMap();
        params.putString("tag", tag);
        params.putString("message", message);

        getReactInstanceManager().getCurrentReactContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(ERROR, params);

    }

    @Override
    public void onLore(String tag, String time, String message, String type) {
        Log.i(TAG, "onLore: " + "tag:" + tag + "message:" + message);
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
        Log.i(TAG, "onInIt: " + "tag:" + tag + "message:" + message);
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
}
