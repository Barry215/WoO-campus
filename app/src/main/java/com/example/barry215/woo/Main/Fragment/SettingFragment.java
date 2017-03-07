package com.example.barry215.woo.Main.Fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.barry215.woo.R;
import com.jenzz.materialpreference.Preference;

/**
 * Created by barry215 on 2016/4/15.
 */
public class SettingFragment extends PreferenceFragment {

    private Preference clean = (Preference)findPreference("清理缓存");
    private Preference exchange = (Preference)findPreference("切换主题");
    private Preference suggest = (Preference)findPreference("意见反馈");
    private Preference pull = (Preference)findPreference("给应用点赞");
    private Preference update = (Preference)findPreference("检测更新");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preference_xml);
//        initClick();
    }

//    private void initClick(){
//        clean.setOnPreferenceClickListener(new android.preference.Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(android.preference.Preference preference) {
//                Toast.makeText(getActivity(),clean.getTitle(),Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
//        exchange.setOnPreferenceClickListener(new android.preference.Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(android.preference.Preference preference) {
//                Toast.makeText(getActivity(),clean.getTitle(),Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
//        suggest.setOnPreferenceClickListener(new android.preference.Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(android.preference.Preference preference) {
//                Toast.makeText(getActivity(),clean.getTitle(),Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
//        pull.setOnPreferenceClickListener(new android.preference.Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(android.preference.Preference preference) {
//                Toast.makeText(getActivity(),clean.getTitle(),Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
//        update.setOnPreferenceClickListener(new android.preference.Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(android.preference.Preference preference) {
//                Toast.makeText(getActivity(),clean.getTitle(),Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
//    }
}
