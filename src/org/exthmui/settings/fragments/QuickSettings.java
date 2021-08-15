package org.exthmui.settings.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.content.ContentResolver;
import android.content.res.Resources;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;
import java.util.Locale;
import android.text.TextUtils;
import android.view.View;


import org.exthmui.settings.preferences.CustomSeekBarPreference;

import java.util.List;
import java.util.ArrayList;

public class QuickSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String KEY_COL_PORTRAIT = "qs_columns_portrait";
    private static final String KEY_ROW_PORTRAIT = "qs_rows_portrait";
    private static final String KEY_COL_LANDSCAPE = "qs_columns_landscape";
    private static final String KEY_ROW_LANDSCAPE = "qs_rows_landscape";

    private CustomSeekBarPreference mColPortrait;
    private CustomSeekBarPreference mRowPortrait;
    private CustomSeekBarPreference mColLandscape;
    private CustomSeekBarPreference mRowLandscape;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.exthm_settings_quicksettings);

        PreferenceScreen prefScreen = getPreferenceScreen();
        final ContentResolver resolver = getActivity().getContentResolver();

        mColPortrait = (CustomSeekBarPreference) findPreference(KEY_COL_PORTRAIT);
        mRowPortrait = (CustomSeekBarPreference) findPreference(KEY_ROW_PORTRAIT);
        mColLandscape = (CustomSeekBarPreference) findPreference(KEY_COL_LANDSCAPE);
        mRowLandscape = (CustomSeekBarPreference) findPreference(KEY_ROW_LANDSCAPE);

        Resources res = null;
        Context ctx = getContext();

        try {
            res = ctx.getPackageManager().getResourcesForApplication("com.android.systemui");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        int col_portrait = res.getInteger(res.getIdentifier(
                "com.android.systemui:integer/config_qs_columns_portrait", null, null));
        int row_portrait = res.getInteger(res.getIdentifier(
                "com.android.systemui:integer/config_qs_rows_portrait", null, null));
        int col_landscape = res.getInteger(res.getIdentifier(
                "com.android.systemui:integer/config_qs_columns_landscape", null, null));
        int row_landscape = res.getInteger(res.getIdentifier(
                "com.android.systemui:integer/config_qs_rows_landscape", null, null));

        mColPortrait.setDefaultValue(col_portrait);
        mRowPortrait.setDefaultValue(row_portrait);
        mColLandscape.setDefaultValue(col_landscape);
        mRowLandscape.setDefaultValue(row_landscape);
        
        int mColPortraitVal = Settings.System.getIntForUser(resolver,
                Settings.System.QS_COLUMNS_PORTRAIT, col_portrait, UserHandle.USER_CURRENT);
        int mRowPortraitVal = Settings.System.getIntForUser(resolver,
                Settings.System.QS_ROWS_PORTRAIT, row_portrait, UserHandle.USER_CURRENT);
        int mColLandscapeVal = Settings.System.getIntForUser(resolver,
                Settings.System.QS_COLUMNS_LANDSCAPE, col_landscape, UserHandle.USER_CURRENT);
        int mRowLandscapeVal = Settings.System.getIntForUser(resolver,
                Settings.System.QS_ROWS_LANDSCAPE, row_landscape, UserHandle.USER_CURRENT);

        mColPortrait.setValue(mColPortraitVal);
        mRowPortrait.setValue(mRowPortraitVal);
        mColLandscape.setValue(mColLandscapeVal);
        mRowLandscape.setValue(mRowLandscapeVal);

        mColPortrait.setOnPreferenceChangeListener(this);
        mRowPortrait.setOnPreferenceChangeListener(this);
        mColLandscape.setOnPreferenceChangeListener(this);
        mRowLandscape.setOnPreferenceChangeListener(this);

        }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
            ContentResolver resolver = getContext().getContentResolver();
            Resources res = null;
            Context cont = getContext();

            try {
            res = cont.getPackageManager().getResourcesForApplication("com.android.systemui");
                } catch (NameNotFoundException e) {
            e.printStackTrace();
                }

            if (preference == mColLandscape){
                int col_landscape = res.getInteger(res.getIdentifier("com.android.systemui:integer/config_qs_columns_landscape", null, null));
                Settings.System.putIntForUser(resolver,Settings.System.QS_COLUMNS_LANDSCAPE, col_landscape, UserHandle.USER_CURRENT);
                return true;
            }else if(preference == mRowLandscape){
                int row_landscape = res.getInteger(res.getIdentifier("com.android.systemui:integer/config_qs_rows_landscape", null, null));
                Settings.System.putIntForUser(resolver,Settings.System.QS_ROWS_LANDSCAPE, row_landscape, UserHandle.USER_CURRENT);
                return true;
            }else if(preference == mColPortrait){
                int col_portrait = res.getInteger(res.getIdentifier("com.android.systemui:integer/config_qs_columns_portrait", null, null));
                Settings.System.putIntForUser(resolver,Settings.System.QS_COLUMNS_PORTRAIT, col_portrait, UserHandle.USER_CURRENT);
                return true;
            }else if(preference == mRowPortrait){
                int row_portrait = res.getInteger(res.getIdentifier("com.android.systemui:integer/config_qs_rows_portrait", null, null));
                Settings.System.putIntForUser(resolver,Settings.System.QS_ROWS_PORTRAIT, row_portrait, UserHandle.USER_CURRENT);
                return true;
            }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.EXTHMUI_SETTINGS;
    }

}
