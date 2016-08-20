package com.app.youwei.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.color.CircleView;
import com.marcoscg.easylicensesdialog.EasyLicensesDialog;

public class AboutActivity extends AppCompatActivity {

    Toolbar about_toolbar;
//    LinearLayout about_changeHistory_layout;
    LinearLayout about_licenses_layout;
    LinearLayout about_rate_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        about_toolbar = (Toolbar) findViewById(R.id.about_toolbar);
        setSupportActionBar(about_toolbar);
        about_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if(getSharedPreferences("config", MODE_PRIVATE).getInt("themeColor", R.color.colorPrimary)!=R.color.colorPrimary) {
            SharedPreferences pref = getSharedPreferences("config", MODE_PRIVATE);
            int themeColor = pref.getInt("themeColor", 0);
            initThemeColor(themeColor);
        }

//        about_changeHistory_layout = (LinearLayout) findViewById(R.id.about_changeHistory_layout);
//        about_changeHistory_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               //
//            }
//        });

        about_licenses_layout = (LinearLayout) findViewById(R.id.about_licenses_layout);
        about_licenses_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyLicensesDialog easyLicensesDialog = new EasyLicensesDialog(AboutActivity.this);
                easyLicensesDialog.setTitle("许可信息"); //by default EasyLicensesDialog comes without any title.
                easyLicensesDialog.setCancelable(true); //true or false
                //easyLicensesDialog.setIcon(R.mipmap.ic_launcher); //add an icon to the title
                easyLicensesDialog.show(); //show the dialog
            }
        });

        about_rate_layout = (LinearLayout) findViewById(R.id.about_rate_layout);
        about_rate_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.coolapk.com/apk/com.app.youwei.myapp");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void initThemeColor(int color) {
       about_toolbar.setBackgroundColor(color);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(CircleView.shiftColorDown(color));
        }
    }
}
