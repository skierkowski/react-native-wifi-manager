package com.skierkowski.WifiManager;

import com.facebook.react.uimanager.*;
import com.facebook.react.bridge.*;
import com.facebook.systrace.Systrace;
import com.facebook.systrace.SystraceMessage;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;

import android.os.Bundle;
import android.content.Context;
import java.util.List;
import com.facebook.systrace.Systrace;
import com.facebook.systrace.SystraceMessage;

import com.facebook.react.LifecycleState;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class WifiManagerModule extends ReactContextBaseJavaModule {
  public WifiManagerModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "WifiManager";
  }

  @ReactMethod
  public void loadWifiList(Callback successCallback, Callback errorCallback) {
    try {
      WifiManager mWifiManager = (WifiManager) getReactApplicationContext().getSystemService(Context.WIFI_SERVICE);
      List < ScanResult > results = mWifiManager.getScanResults();
      WritableArray wifiArray =  Arguments.createArray();
      for (ScanResult result: results) {
        if(!result.SSID.equals("")){
          wifiArray.pushString(result.SSID);
        }
      }
      successCallback.invoke(wifiArray);
    } catch (IllegalViewOperationException e) {
      errorCallback.invoke(e.getMessage());
    }
  }

  @ReactMethod
  public void findAndConnect(String ssid, String password) {
    WifiManager mWifiManager = (WifiManager) getReactApplicationContext().getSystemService(Context.WIFI_SERVICE);
    List < ScanResult > results = mWifiManager.getScanResults();
    for (ScanResult result: results) {
      if (ssid.equals(result.SSID)) {
        connectTo(result, password, ssid);
      }
    }
  }

  public void connectTo(ScanResult result, String password, String ssid) {
    //Make new configuration
    WifiConfiguration conf = new WifiConfiguration();
    conf.SSID = "\"" + ssid + "\"";
    String Capabilities = result.capabilities;
    if (Capabilities.contains("WPA2")) {
      conf.preSharedKey = "\"" + password + "\"";
    } else if (Capabilities.contains("WPA")) {
      conf.preSharedKey = "\"" + password + "\"";
    } else if (Capabilities.contains("WEP")) {
      conf.wepKeys[0] = "\"" + password + "\"";
      conf.wepTxKeyIndex = 0;
      conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
      conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
    } else {
      conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
    }
    //Remove the existing configuration for this netwrok
    WifiManager mWifiManager = (WifiManager) getReactApplicationContext().getSystemService(Context.WIFI_SERVICE);
    List<WifiConfiguration> mWifiConfigList = mWifiManager.getConfiguredNetworks();
    String comparableSSID = ('"' + ssid + '"'); //Add quotes because wifiConfig.SSID has them
    for(WifiConfiguration wifiConfig : mWifiConfigList){
      if(wifiConfig.SSID.equals(comparableSSID)){
        int networkId = wifiConfig.networkId;
        mWifiManager.removeNetwork(networkId);
        mWifiManager.saveConfiguration();
      }
    }
    //Add configuration to Android wifi manager settings...
     WifiManager wifiManager = (WifiManager) getReactApplicationContext().getSystemService(Context.WIFI_SERVICE);
     mWifiManager.addNetwork(conf);
    //Enable it so that android can connect
    List < WifiConfiguration > list = mWifiManager.getConfiguredNetworks();
    for (WifiConfiguration i: list) {
      if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
        wifiManager.disconnect();
        wifiManager.enableNetwork(i.networkId, true);
        wifiManager.reconnect();
        break;
      }
    }
  }

  @ReactMethod
  public void connectionStatus(Callback connectionStatusResult) {
    ConnectivityManager connManager = (ConnectivityManager) getReactApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

    connectionStatusResult.invoke(mWifi.getState().toString());
  }

  private static Integer findNetworkInExistingConfig(WifiManager wifiManager, String ssid) {
   List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
   for (WifiConfiguration existingConfig : existingConfigs) {
     if (existingConfig.SSID.equals(ssid)) {
       return existingConfig.networkId;
     }
   }
   return null;
 }
}