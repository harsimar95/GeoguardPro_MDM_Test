/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.afwsamples.testdpc.policy.wifimanagement;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public class WifiConfigUtil {

    /**
     * Save or replace the wifi configuration.
     *
     * @param context
     * @param wifiConfiguration
     * @return success to add/replace the wifi configuration
     */
    public static boolean saveWifiConfiguration(Context context, WifiConfiguration
            wifiConfiguration) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        final int networkId;

        if (wifiConfiguration.networkId == -1) {
            // new wifi configuration, add it and then save it.
            networkId = wifiManager.addNetwork(wifiConfiguration);
        } else {
            // existing wifi configuration, update it and then save it.
            networkId = wifiManager.updateNetwork(wifiConfiguration);
        }

        if (networkId == -1) {
            return false;
        }

        // Added successfully, try to save it now.
        wifiManager.enableNetwork(networkId, /* disableOthers */ false);
        if (wifiManager.saveConfiguration()) {
            return true;
        } else {
            // Remove the added network that fail to save.
            wifiManager.removeNetwork(networkId);
            return false;
        }
    }

}
