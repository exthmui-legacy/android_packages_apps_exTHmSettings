/*
 * Copyright (C) 2020 ShapeShiftOS
 * Copyright (C) 2020-2021 exTHmUI Project
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

package org.exthmui.settings.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.Indexable;
import com.android.settingslib.search.SearchIndexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import com.android.internal.logging.nano.MetricsProto;

import org.exthmui.settings.preferences.CustomSeekBarPreference;
import org.exthmui.settings.preferences.SecureSettingListPreference;
import org.exthmui.settings.preferences.SecureSettingSwitchPreference;

import java.util.ArrayList;
import java.util.List;

@SearchIndexable
public class TrafficSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private static final String TAG = "TrafficSettings";

    private CustomSeekBarPreference mNetTrafficRefreshInterval;
    private SecureSettingListPreference mNetTrafficLocation;
    private SecureSettingListPreference mNetTrafficMode;
    private SecureSettingListPreference mNetTrafficUnits;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.traffic_settings);

        PreferenceScreen prefSet = getPreferenceScreen();
        final ContentResolver resolver = getActivity().getContentResolver();

        int location = Settings.Secure.getIntForUser(resolver,
                Settings.Secure.NETWORK_TRAFFIC_LOCATION, 0, UserHandle.USER_CURRENT);
        mNetTrafficLocation = (SecureSettingListPreference) findPreference("network_traffic_location");
		mNetTrafficLocation.setValue(String.valueOf(location));
        mNetTrafficLocation.setSummary(mNetTrafficLocation.getEntry());
        mNetTrafficLocation.setOnPreferenceChangeListener(this);

        int mode = Settings.Secure.getIntForUser(resolver,
                Settings.Secure.NETWORK_TRAFFIC_MODE, 0, UserHandle.USER_CURRENT);
        mNetTrafficMode = (SecureSettingListPreference) findPreference("network_traffic_mode");
        mNetTrafficMode.setValue(String.valueOf(mode));
        mNetTrafficMode.setSummary(mNetTrafficMode.getEntry());
        mNetTrafficMode.setOnPreferenceChangeListener(this);

        int intervalValue = Settings.Secure.getInt(resolver,
                Settings.Secure.NETWORK_TRAFFIC_REFRESH_INTERVAL, 2);
        mNetTrafficRefreshInterval =
                (CustomSeekBarPreference) findPreference("network_traffic_refresh_interval");
        mNetTrafficRefreshInterval.setValue(intervalValue);
        mNetTrafficRefreshInterval.setOnPreferenceChangeListener(this);

        int unitValue = Settings.Secure.getIntForUser(resolver,
                Settings.Secure.NETWORK_TRAFFIC_UNITS, 0, UserHandle.USER_CURRENT);
        mNetTrafficUnits = (SecureSettingListPreference) findPreference("network_traffic_units");
        mNetTrafficUnits.setValue(String.valueOf(unitValue));
        mNetTrafficUnits.setSummary(mNetTrafficUnits.getEntry());
        mNetTrafficUnits.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getContext().getContentResolver();
        if (preference == mNetTrafficLocation) {
            int location = Integer.valueOf((String) newValue);
            int index = mNetTrafficLocation.findIndexOfValue((String) newValue);
            Settings.Secure.putIntForUser(getActivity().getContentResolver(),
                    Settings.Secure.NETWORK_TRAFFIC_LOCATION,
                    location, UserHandle.USER_CURRENT);
            mNetTrafficLocation.setSummary(mNetTrafficLocation.getEntries()[index]);
            // Preference enablement checks
            mNetTrafficMode.setEnabled(netTrafficEnabled());
            mNetTrafficRefreshInterval.setEnabled(netTrafficEnabled());
            mNetTrafficUnits.setEnabled(netTrafficEnabled());
            return true;
        } else if (preference == mNetTrafficMode) {
            int mode = Integer.valueOf((String) newValue);
            int index = mNetTrafficMode.findIndexOfValue((String) newValue);
            Settings.Secure.putIntForUser(getActivity().getContentResolver(),
                    Settings.Secure.NETWORK_TRAFFIC_MODE,
                    mode, UserHandle.USER_CURRENT);
            mNetTrafficMode.setSummary(mNetTrafficMode.getEntries()[index]);
            mNetTrafficRefreshInterval.setEnabled(netTrafficEnabled());
            return true;
        } else if (preference == mNetTrafficUnits) {
            int mode = Integer.valueOf((String) newValue);
            int index = mNetTrafficUnits.findIndexOfValue((String) newValue);
            Settings.Secure.putIntForUser(getActivity().getContentResolver(),
                    Settings.Secure.NETWORK_TRAFFIC_UNITS,
                    mode, UserHandle.USER_CURRENT);
            mNetTrafficUnits.setSummary(mNetTrafficUnits.getEntries()[index]);
            mNetTrafficRefreshInterval.setEnabled(netTrafficEnabled());
            return true;
        } else if (preference == mNetTrafficRefreshInterval) {
            int interval = (Integer) newValue;
            Settings.Secure.putIntForUser(getContentResolver(),
                    Settings.Secure.NETWORK_TRAFFIC_REFRESH_INTERVAL,
                    interval, UserHandle.USER_CURRENT);
            return true;
        }
        return false;
    }

    private boolean netTrafficEnabled() {
        final ContentResolver resolver = getActivity().getContentResolver();
        return Settings.Secure.getInt(resolver,
                Settings.Secure.NETWORK_TRAFFIC_LOCATION, 0) != 0;
	}

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.EXTHMUI_SETTINGS;
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
        new BaseSearchIndexProvider() {
            @Override
            public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                    boolean enabled) {
                final ArrayList<SearchIndexableResource> result = new ArrayList<>();
                final SearchIndexableResource sir = new SearchIndexableResource(context);
                sir.xmlResId = R.xml.traffic_settings;
                result.add(sir);
                return result;
            }

            @Override
            public List<String> getNonIndexableKeys(Context context) {
                final List<String> keys = super.getNonIndexableKeys(context);
                return keys;
            }
    };
}