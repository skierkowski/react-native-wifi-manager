## react-native-wifi-manager

[![npm version](https://badge.fury.io/js/react-native-wifi-manager.png)](http://badge.fury.io/js/react-native-wifi-manager)

Device Information for react-native

## Installation

First you need to install react-native-wifi-manager:

```javascript
npm install react-native-wifi-manager --save
```

### Installation (Android)

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


## Release Notes


## Example
