package com.asterik.tvlite;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final int BG = Color.rgb(5,7,11);
    private LinearLayout root;
    private TextView clock;
    private final String[] names = {"YouTube", "Netflix", "Prime Video", "Play Store"};
    private final String[] packages = {
        "com.google.android.youtube.tv", "com.netflix.ninja",
        "com.amazon.amazonvideo.livingroom", "com.android.vending"
    };

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        buildHome();
    }

    private GradientDrawable shape(int color, int stroke) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color); d.setCornerRadius(24);
        if (stroke != 0) d.setStroke(2, stroke);
        return d;
    }

    private TextView label(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value); t.setTextSize(size); t.setTextColor(color);
        t.setGravity(Gravity.CENTER);
        t.setTypeface(Typeface.create("sans-serif", bold ? Typeface.BOLD : Typeface.NORMAL));
        return t;
    }

    private void buildHome() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(48, 24, 48, 24);
        root.setBackgroundColor(BG);
        setContentView(root);

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        TextView greeting = label("Good evening", 24, Color.WHITE, true);
        greeting.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        top.addView(greeting, new LinearLayout.LayoutParams(0, 64, 1));
        clock = label("", 16, 0xFFB7BEC9, false);
        top.addView(clock, new LinearLayout.LayoutParams(-2, 64));
        root.addView(top);
        updateClock();

        TextView hero = label("TV, beautifully simple.", 34, Color.WHITE, true);
        hero.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        root.addView(hero, new LinearLayout.LayoutParams(-1, 0, 1));

        TextView sub = label("Your entertainment, all in one place", 16, 0xFF929BA8, false);
        sub.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        root.addView(sub, new LinearLayout.LayoutParams(-1, 38));

        LinearLayout actions = new LinearLayout(this);
        actions.setGravity(Gravity.CENTER_VERTICAL);
        actions.setPadding(0, 12, 0, 8);
        actions.addView(action("INPUTS", v -> openInputs()));
        actions.addView(action("WI-FI", v -> openSettings(Settings.ACTION_WIFI_SETTINGS)));
        actions.addView(action("SETTINGS", v -> openSettings(Settings.ACTION_SETTINGS)));
        root.addView(actions);

        LinearLayout dock = new LinearLayout(this);
        dock.setGravity(Gravity.CENTER);
        dock.setPadding(10, 10, 10, 10);
        dock.setBackground(shape(0xFF171B22, 0));
        LinearLayout.LayoutParams dockParams = new LinearLayout.LayoutParams(-1, 92);
        dockParams.topMargin = 10;
        root.addView(dock, dockParams);

        for (int i = 0; i < names.length; i++) {
            final int index = i;
            TextView app = label(names[i], 16, Color.WHITE, true);
            app.setFocusable(true);
            app.setBackground(shape(0xFF252B35, 0));
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, 68, 1);
            p.setMargins(7, 0, 7, 0);
            dock.addView(app, p);
            app.setOnClickListener(v -> launchApp(index));
            app.setOnFocusChangeListener((v, focused) -> {
                v.setBackground(shape(focused ? 0xFF596575 : 0xFF252B35, focused ? Color.WHITE : 0));
                ((TextView)v).setTextSize(focused ? 18 : 16);
            });
        }
        dock.getChildAt(0).requestFocus();
    }

    private TextView action(String title, View.OnClickListener click) {
        TextView t = label(title, 13, Color.WHITE, true);
        t.setFocusable(true);
        t.setBackground(shape(0xFF202630, 0));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(138, 44);
        p.setMargins(0, 0, 12, 0);
        t.setOnClickListener(click);
        t.setOnFocusChangeListener((v, f) -> v.setBackground(shape(f ? 0xFF596575 : 0xFF202630, f ? Color.WHITE : 0)));
        return t;
    }

    private void launchApp(int index) {
        Intent intent = getPackageManager().getLeanbackLaunchIntentForPackage(packages[index]);
        if (intent == null) intent = getPackageManager().getLaunchIntentForPackage(packages[index]);
        if (intent == null) {
            Toast.makeText(this, names[index] + " isn't installed", Toast.LENGTH_SHORT).show();
            return;
        }
        try { startActivity(intent); }
        catch (ActivityNotFoundException e) { Toast.makeText(this, "Unable to open " + names[index], Toast.LENGTH_SHORT).show(); }
    }

    private void openSettings(String action) {
        try { startActivity(new Intent(action)); }
        catch (Exception e) { Toast.makeText(this, "Settings unavailable", Toast.LENGTH_SHORT).show(); }
    }

    private void openInputs() {
        try { startActivity(new Intent("android.settings.TV_INPUT_SETTINGS")); }
        catch (Exception e) { openSettings(Settings.ACTION_SETTINGS); }
    }

    private void updateClock() {
        if (clock != null) clock.setText(new SimpleDateFormat("EEE, d MMM  •  h:mm a", Locale.getDefault()).format(new Date()));
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) { openSettings(Settings.ACTION_SETTINGS); return true; }
        return super.onKeyDown(keyCode, event);
    }
}
