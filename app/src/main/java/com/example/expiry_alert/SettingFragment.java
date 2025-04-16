//package com.example.expiry_alert;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//public class SettingFragment extends Fragment {
//
//    private SeekBar seekBar;
//    private TextView tvDays;
//    private SharedPreferences prefs;
//    private static final String PREF_NAME = "NotificationPrefs";
//    private static final String KEY_NOTIFY_DAYS = "notifyBeforeDays";
//
//    public SettingFragment() {}
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_setting, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        seekBar = view.findViewById(R.id.seekBar);
//        tvDays = view.findViewById(R.id.tvDays);
//
//        prefs = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        int savedDays = prefs.getInt(KEY_NOTIFY_DAYS, 3);  // default 3
//
//        tvDays.setText("Notify me " + savedDays + " day(s) before expiry");
//        seekBar.setProgress(savedDays);
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                tvDays.setText("Notify me " + progress + " day(s) before expiry");
//            }
//
//            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
//            @Override public void onStopTrackingTouch(SeekBar seekBar) {
//                int days = seekBar.getProgress();
//                prefs.edit().putInt(KEY_NOTIFY_DAYS, days).apply();
//                Toast.makeText(getContext(), "Notification setting saved!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
package com.example.expiry_alert;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {

    private SeekBar seekBar;
    private TextView tvDays;
    private Button btnSave;
    private int selectedDays = 3;

    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_NOTIFY_BEFORE_DAYS = "notifyBeforeDays";

    public SettingFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        seekBar = view.findViewById(R.id.seekBar);
        tvDays = view.findViewById(R.id.tvDays);
        btnSave = view.findViewById(R.id.btnSave);

        // Load saved days
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        selectedDays = prefs.getInt(KEY_NOTIFY_BEFORE_DAYS, 3);
        seekBar.setProgress(selectedDays);
        updateText(selectedDays);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedDays = progress;
                updateText(progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnSave.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(KEY_NOTIFY_BEFORE_DAYS, selectedDays);
            editor.apply();
            Toast.makeText(getContext(), "Preference saved: " + selectedDays + " day(s) before", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void updateText(int days) {
        days = Math.max(1, days); // Avoid 0
        tvDays.setText("Notify me " + days + " day" + (days > 1 ? "s" : "") + " before expiry");
    }
}
