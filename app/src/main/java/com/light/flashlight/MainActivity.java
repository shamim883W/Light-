package com.light.flashlight;

import android.app.Activity;
import android.os.Bundle;
import android.hardware.camera2.CameraManager;
import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

public class MainActivity extends Activity {
    CameraManager cameraManager;
    String cameraId;
    boolean isOn = false;
    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String id : cameraManager.getCameraIdList()) {
                if (cameraManager.getCameraCharacteristics(id)
                    .get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                    cameraId = id;
                    break;
                }
            }
        } catch (Exception e) {}

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(Color.rgb(18, 18, 28));

        TextView title = new TextView(this);
        title.setText("LIGHT");
        title.setTextColor(Color.WHITE);
        title.setTextSize(32);
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        button = new Button(this);
        button.setText("TURN ON");
        button.setTextSize(22);
        layout.addView(button, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, 150));

        button.setOnClickListener(v -> toggleFlash());

        setContentView(layout);
    }

    void toggleFlash() {
        if (cameraId == null) {
            Toast.makeText(this, "Flash not available", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            isOn = !isOn;
            cameraManager.setTorchMode(cameraId, isOn);
            button.setText(isOn ? "TURN OFF" : "TURN ON");
        } catch (Exception e) {
            isOn = false;
            button.setText("TURN ON");
            Toast.makeText(this, "Cannot control flash", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (cameraId != null) cameraManager.setTorchMode(cameraId, false);
        } catch (Exception e) {}
    }
}
