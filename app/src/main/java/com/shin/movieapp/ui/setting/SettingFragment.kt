package com.shin.movieapp.ui.setting

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.shin.movieapp.R


class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val key_popular = findPreference("key_popular") as CheckBoxPreference
        val key_top_rated = findPreference("key_top_rated") as CheckBoxPreference
        val key_upcoming = findPreference("key_upcoming") as CheckBoxPreference
        val key_now_playing = findPreference("key_now_playing") as CheckBoxPreference

        // Check pref to open reminder fragment
        val screenPref: Preference? = findPreference("go_to_reminder_pre")
        screenPref?.setOnPreferenceClickListener {
            val navController =
                Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
            navController.navigate(R.id.reminderFragment)
            true
        }
        // Init event change apply for 4 check box
        val prefChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            if(preference == key_popular && newValue == true){
                key_top_rated.isChecked = false
                key_upcoming.isChecked = false
                key_now_playing.isChecked = false
                requireActivity().intent.putExtra("type", "popular_movie")
            } else if (preference == key_top_rated && newValue == true){
                key_popular.isChecked = false
                key_upcoming.isChecked = false
                key_now_playing.isChecked = false
                requireActivity().intent.putExtra("type", "top_rated_movie")
            } else if (preference == key_upcoming && newValue == true){
                key_popular.isChecked = false
                key_top_rated.isChecked = false
                key_now_playing.isChecked = false
                requireActivity().intent.putExtra("type", "upcoming_movie")
            } else if (preference == key_now_playing && newValue == true){
                key_popular.isChecked = false
                key_top_rated.isChecked = false
                key_upcoming.isChecked = false
                requireActivity().intent.putExtra("type", "now_playing_movie")
            }
            true
        }

        key_popular.setOnPreferenceChangeListener(prefChangeListener)
        key_top_rated.setOnPreferenceChangeListener(prefChangeListener)
        key_now_playing.setOnPreferenceChangeListener(prefChangeListener)
        key_upcoming.setOnPreferenceChangeListener(prefChangeListener)
    }
}