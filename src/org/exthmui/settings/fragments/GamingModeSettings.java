/*
 * Copyright (C) 2020 The exTHmUI Open Source Project
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

import com.android.internal.logging.nano.MetricsProto;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;

import androidx.preference.SwitchPreference;
import androidx.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import android.provider.SearchIndexableResource;

import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

import com.android.settingslib.search.SearchIndexable;

import java.util.List;
import java.util.ArrayList;

import org.exthmui.settings.preferences.PackageListPreference;

import lineageos.hardware.LineageHardwareManager;

@SearchIndexable
public class GamingModeSettings extends SettingsPreferenceFragment implements Indexable {

    private SwitchPreference mHardwareKeysDisable;
    private PackageListPreference mGamingPrefList;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.exthm_settings_gaming);
        
        final PreferenceScreen prefScreen = getPreferenceScreen();

        mHardwareKeysDisable = findPreference(Settings.System.GAMING_MODE_DISABLE_HW_KEYS);
        LineageHardwareManager mLineageHardware = LineageHardwareManager.getInstance(getActivity());
        if (!mLineageHardware.isSupported(LineageHardwareManager.FEATURE_KEY_DISABLE)) {
            prefScreen.removePreference(mHardwareKeysDisable);
        }

        mGamingPrefList = (PackageListPreference) findPreference("gaming_mode_app_list");
        mGamingPrefList.setRemovedListKey(Settings.System.GAMING_MODE_REMOVED_APP_LIST);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.EXTHMUI_SETTINGS;
    }

    /**
     * For search
     */
    public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    ArrayList<SearchIndexableResource> result =
                            new ArrayList<SearchIndexableResource>();
                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.exthm_settings_gaming;
                    result.add(sir);

                    return result;
                }
            };
}
