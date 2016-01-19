## WiFi Manager for React Native (react-native-wifi-manager)
[![npm version](https://badge.fury.io/js/react-native-wifi-manager.png)](http://badge.fury.io/js/react-native-wifi-manager)

List, connect and get the status of the WiFi connection on the device.

## Installation

First you need to install react-native-wifi-manager:

```javascript
npm install react-native-wifi-manager --save
```

* In `android/app/src/main/AndroidManifest.xml` add these permissions inside `<manifest/>`.

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

* In `android/setting.gradle`

```gradle
...
include ':WifiManager', ':app'
project(':WifiManager').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-wifi-manager/android')
```

* In `android/app/build.gradle`

```gradle
...
dependencies {
    ...
    compile project(':WifiManager')
}
```

* register module (in MainActivity.java)

On newer versions of React Native (0.18+):

```java
import com.skierkowski.WifiManager.*;  // <--- import

public class MainActivity extends ReactActivity {
  ......
  
  /**
   * A list of packages used by the app. If the app uses additional views
   * or modules besides the default ones, add more packages here.
   */
    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
        new WifiManager(), // <------ add here
        new MainReactPackage());
    }
}
```

On older versions of React Native:

```java
import com.skierkowski.WifiManager.*;  // <--- import

public class MainActivity extends Activity implements DefaultHardwareBackBtnHandler {
  ......

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mReactRootView = new ReactRootView(this);

    mReactInstanceManager = ReactInstanceManager.builder()
      .setApplication(getApplication())
      .setBundleAssetName("index.android.bundle")
      .setJSMainModuleName("index.android")
      .addPackage(new MainReactPackage())
      .addPackage(new WifiManager())              // <------ add here
      .setUseDeveloperSupport(BuildConfig.DEBUG)
      .setInitialLifecycleState(LifecycleState.RESUMED)
      .build();

    mReactRootView.startReactApplication(mReactInstanceManager, "ExampleRN", null);

    setContentView(mReactRootView);
  }

  ......

}
```

## Example

### Load module
```javascript
var WifiManager = require('react-native-wifi-manager');
```

### List available networks (list)
```javascript
loadWifiListData: function() {
    WifiManager.list(
        (wifiArray) => {
            this.setState({
                // wifiArray is an array of strings, each string being the SSID
                dataSource: this.state.dataSource.cloneWithRows(wifiArray),
            });
        },
        (msg) => {
            console.log(msg);
        },
    );
},
```

### Connect to a new network (connect)
```javascript
// Attempts to connect to the network specified. This is an async call. Listen to connectionStatus for status
WifiManager.connect(ssid,password);
```

### Get status of connection (status)
```javascript
checkConnectionStatus: function() {
    // Possible States: 'CONNECTED', 'CONNECTING', 'DISCONNECTED', 'DISCONNECTING', 'SUSPENDED', 'UNKOWN'
    // from: http://developer.android.com/reference/android/net/NetworkInfo.State.html
    WifiManager.status((status) => {
        if (status == 'CONNECTED') {
            this.navigateToActivation();
        }
    });
},
  ```
