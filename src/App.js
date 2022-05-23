import React, {useEffect, useState, useRef, useCallback} from 'react';
import {NativeModules, NativeEventEmitter} from 'react-native';
import DeviceInfo from 'react-native-device-info';
import IntentLauncher, { IntentConstant } from 'react-native-intent-launcher'
import {
  Button,
  Linking,
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  useColorScheme,
  Text,
  View,
  PermissionsAndroid,
  
} from 'react-native';

import {Colors, LearnMoreLinks} from 'react-native/Libraries/NewAppScreen';

const Separator = () => (
  <View style={styles.separator} />
);
const {RNLandmarksidSDK} = NativeModules;
// get event names for the SDK
const package2= DeviceInfo.getBundleId();
const openAppSettings = () => {
  if (Platform.OS === 'ios') {
    Linking.openURL('app-settings:')
  } else {
    IntentLauncher.startActivity({
      action: 'android.settings.APPLICATION_DETAILS_SETTINGS',
      data: 'package:' + package2
    })
  }
}

const requestLocationPermission = async()=> {
  const chckLocationPermission = PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION);
  /*if (chckLocationPermission === PermissionsAndroid.RESULTS.GRANTED) {
      alert("You've access for the location");
  } else {*/
      try {
          const granted = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
              {
                  'title': 'App requires Location permission',
                  'message': 'We required Location permission in order to give you Awesome value ' 
              }
          )
          if (granted === PermissionsAndroid.RESULTS.GRANTED) {
              alert("You've provided access for the location");
          } else {
              alert("You don't have access for the location");
          }
      } catch (err) {
          alert(err)
      }
 // }
};
const App = () => {
  const isDarkMode = useColorScheme() === 'dark';
  const [textValue, setTextValue] = useState('');
  const textValueRef = useRef({value: ''});

  const backgroundStyle = {
    backgroundColor: Colors.lighter,
    flex: 1,
  };

  const container = {
    flex: 1,
  };

  const textStyle = {
    fontSize: 14,
    color: 'black',
    backgroundColor: 'white',
    height: '100%',
    padding: '10%',
  };

  const saveValue = useCallback(
    value => {
      let newVal = textValueRef.current.value + value;
      console.log('🚀 ~ file: App.js ~ line 25 ~ App ~ textValue', textValue);
      setTextValue(newVal);
      textValueRef.current.value = newVal;
    },
    [textValue, setTextValue],
  );

  const onMessageCallback = useCallback(
    message => {
      saveValue(`${message?.tag} \n${message?.message}\n\n`);
      console.log(
        '🚀 ~ file: App.js ~ line 36 ~ eventEmitterAndroid.addListener ~ message',
        message?.message,
      );
    },
    [saveValue],
  );

  const onLoreCallback = useCallback(
    lore => {
      saveValue(
        `LORE AT ${lore?.time}  of type  ${lore?.type} with message ${lore?.message}\n\n`,
      );
      console.log(
        '🚀 ~ file: App.js ~ line 49 ~ eventEmitterOnLore.addListener ~ lore',
        lore?.message,
      );
    },
    [saveValue],
  );

  const onSuccessCallback = useCallback(
    success => {
      saveValue(`${success?.tag} \n${success?.message}\n\n`);
      console.log(
        '🚀 ~ file: App.js ~ line 60 ~ eventEmitterOnSuccess.addListener ~ success',
        success?.message,
      );
    },
    [saveValue],
  );

  const onErrorCallback = useCallback(
    error => {
      saveValue(`${error?.tag} \n${error?.message}\n\n`);
      console.log(
        '🚀 ~ file: App.js ~ line 71 ~ eventEmitterOnError.addListener ~ error',
        error?.message,
      );
    },
    [saveValue],
  );

  useEffect(() => {
    //start SDK here from android.
    RNLandmarksidSDK.startSDK();

    //register events to get latest updates from the SDK
    // TODO: add events for all types and trigger events for all callbacks from android.
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
  }, []);

  return (
    <>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}>
        <View
          style={{
            backgroundColor: isDarkMode ? Colors.black : Colors.white,
          }}>
          
      <View style={styles.textContainer}>
        
      <View>
          <Text style={{color: 'white',fontSize: 20}}>LANDMARKS React Native Example</Text> 
          
          </View>
            <Separator />
          <View>
          <Button
              onPress={() => Linking.openURL('https://gitlab.com/landmarksid/mobile-development/landmarksid-sdk-react-native')}
              title="Documentation"
              color="#841584"
              accessibilityLabel="Documentation Link"
            />

          </View>
            <Separator />
          <View>
          <Button
                onPress={() => {openAppSettings()}}
              title="Settings"
              color="#841584"
              accessibilityLabel="Settings"
            />
            </View>
            <Separator />
            <View>
            <Button
                onPress={() => {requestLocationPermission()}}
              title="Request Location Permission"
              color="#841584"
              accessibilityLabel="Request Location Permission"
            />
            </View>
            </View>
          
        </View>
      </ScrollView>
    </>
  );
};

const styles = StyleSheet.create({
  textContainer: {
    flex: 1,
    justifyContent: 'center',
    padding: 20
  },
  text: {
    fontSize: 10,
    textAlign: 'center',
    lineHeight: 1,
  },
  container: {
    flex: 1,
    justifyContent: 'center',
    marginHorizontal: 16,
  },
  title: {
    textAlign: 'center',
    marginVertical: 8,
  },
  fixToText: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  separator: {
    marginVertical: 8,
    borderBottomColor: '#737373',
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
});

export default App;
