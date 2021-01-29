package org.exthmui.settings.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.content.Context;
import android.os.Bundle;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import android.provider.SearchIndexableResource;

import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

import com.android.settingslib.search.SearchIndexable;

import java.util.List;
import java.util.ArrayList;

@SearchIndexable
public class SoundSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.exthm_settings_sound);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {

        return false;
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
                    sir.xmlResId = R.xml.exthm_settings_sound;
                    result.add(sir);

                    return result;
                }
            };
}
