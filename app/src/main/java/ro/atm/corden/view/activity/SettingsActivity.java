package ro.atm.corden.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerationAndroid;

import java.util.ArrayList;
import java.util.List;

import ro.atm.corden.R;
import ro.atm.corden.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding mBinding;

    List<String> adapterFrontSpinner;
    List<String> adapterBackSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        setAdapterForSpinner();
        setListenerForSpinner();

        setSupportActionBar(mBinding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    /**
     * Sets the adapter for spinners
     * Finds the supported camera formats for build in camera (back camera and front camera)
     */
    private void setAdapterForSpinner() {
        // find resolution list for build in camera
        Camera2Enumerator enumerator = new Camera2Enumerator(this);

        adapterFrontSpinner = new ArrayList<>();
        adapterBackSpinner = new ArrayList<>();

        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.settingSharedPreferences), Context.MODE_PRIVATE);

        String frontQuality = sharedPreferences.getString(getString(R.string.frontCameraQuality), "");
        String backQuality = sharedPreferences.getString(getString(R.string.backCameraQuality), "");

        adapterBackSpinner.clear();
        adapterBackSpinner.add("High quality");
        adapterBackSpinner.add("Medium quality");
        adapterBackSpinner.add("Low quality");

        adapterFrontSpinner.clear();
        adapterFrontSpinner.add("High quality");
        adapterFrontSpinner.add("Medium quality");
        adapterFrontSpinner.add("Low quality");

        ArrayAdapter arrayFrontAdapter = new ArrayAdapter(this, R.layout.item_spinner, adapterFrontSpinner);
        arrayFrontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter arrayBackAdapter = new ArrayAdapter(this, R.layout.item_spinner, adapterBackSpinner);
        arrayBackAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mBinding.backResolutions.setAdapter(arrayBackAdapter);
        mBinding.frontResolutions.setAdapter(arrayFrontAdapter);


        if (!backQuality.trim().isEmpty()) {
            int positionBack = -1;
            switch (frontQuality) {
                case "High quality":
                    positionBack = 0;
                    break;
                case "Medium quality":
                    positionBack = 1;
                    break;
                case "Low quality":
                    positionBack = 2;
                    break;

            }
            mBinding.backResolutions.setSelection(positionBack);
        }
        if (!frontQuality.trim().isEmpty()) {
            int positionFront = 0;
            switch (frontQuality) {
                case "High quality":
                    positionFront = 0;
                    break;
                case "Medium quality":
                    positionFront = 1;
                    break;
                case "Low quality":
                    positionFront = 2;
                    break;
            }
            mBinding.frontResolutions.setSelection(positionFront);
        }
    }


    /**
     * Set on selected item listener.
     * When user sets a new resolution, the application saves it in shared preferences
     */
    private void setListenerForSpinner() {
        SharedPreferences.Editor sharedPref = this.getSharedPreferences(getString(R.string.settingSharedPreferences), Context.MODE_PRIVATE).edit();

        mBinding.frontResolutions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedPref.putString(getString(R.string.frontCameraQuality), adapterFrontSpinner.get(position));
                sharedPref.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBinding.backResolutions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedPref.putString(getString(R.string.backCameraQuality), adapterBackSpinner.get(position));
                sharedPref.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
