package iu9.bmstu.ru.l6_sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;


import android.app.Fragment;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.security.auth.login.LoginException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SettingsFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context ctx = this.getContext();

        findPreference("news_api_key").setOnPreferenceChangeListener((pref, apiKey) -> {
            Pattern keyPtr = Pattern.compile("[0-9a-f]{32}");
            if(!keyPtr.matcher((String)apiKey).matches()) {
                Toast.makeText(ctx, "Invalid API key. Using default value", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        });

        PreferenceScreen prefScreen = this.getPreferenceScreen();
        SharedPreferences sharedPreferences = prefScreen.getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        for (int i = 0; i < prefScreen.getPreferenceCount(); i++) {
            Preference pref = prefScreen.getPreference(i);
            setPrefSummary(sharedPreferences, pref);
        }

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void setPrefSummary(SharedPreferences sharedPreferences, Preference pref) {
        Log.i(TAG, "setPrefSummary: sharedPreference: " + pref);
        if(pref != null && !(pref instanceof CheckBoxPreference) && !(pref instanceof SwitchPreference)) {
            Log.i(TAG, "setPrefSummary: " + pref.getClass().getCanonicalName());
            if (pref instanceof MultiSelectListPreference) {
                Set<String> defaultSources = new HashSet<>(Arrays.asList(getResources().getStringArray(R.array.sources_default_values)));
                Set<String> sourcesSet = sharedPreferences.getStringSet("news_sources", defaultSources);
                Log.i(TAG, "setPrefSummary: set summary: " + String.join(",", sourcesSet));
                pref.setSummary(String.join(",", sourcesSet));
            } else if(pref instanceof ListPreference) {
                ListPreference listPref = (ListPreference) pref;
                String val = sharedPreferences.getString(pref.getKey(), "");
                int valIdx = listPref.findIndexOfValue(val);
                if(valIdx >= 0) {
                    Log.i(TAG, "setPrefSummary: set summary: " + listPref.getEntries()[valIdx]);
                    listPref.setSummary(listPref.getEntries()[valIdx]);
                }
            } else {
                String summary = sharedPreferences.getString(pref.getKey(), "");
                Log.i(TAG, "setPrefSummary: set summary: " + summary);
                pref.setSummary(summary);
            }
        }
    }

//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.activity_preference, container, false);
//    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_frag);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroy() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i(TAG, "onSharedPreferenceChanged: preference changed: " + key);
        setPrefSummary(sharedPreferences, findPreference(key));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
