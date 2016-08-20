package com.app.youwei.myapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.CircleView;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateRecycleItemActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback,TimePickerDialog.OnTimeSetListener {

    AppBarLayout create_item_appbar;
    Toolbar create_item_toolbar;
    TabLayout type_tablayout;

    EditText item_name;
    EditText item_detail;

    TextView begin_date_tv;

    ColorChooserDialog colorChooserDialog;
    item_type t=new item_type();

    LinearLayout layout_item_color_selector;
    CircleImageView imageView_color;
    LinearLayout layout_begin;

    TimePickerDialog begin_tpd = new TimePickerDialog();
    Date begin = new Date();

    LinearLayout item_recycle;
    TextView item_recycle_tv;
    String recycle_string;

    LinearLayout item_notification;
    TextView item_notification_tv;

    RecycleItem new_item = new RecycleItem();
    int recycleitem_id = 1;
    int type_color = -12627531;
    int notification = 0;
    String[] notification_type = new String[] {
            "次要:振动,可移除的通知", "一般:振动和铃声,可移除的通知", "重要:振动和铃声,不可移除的通知"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recycle_item);
        create_item_appbar = (AppBarLayout) findViewById(R.id.create_item_appbar);
        create_item_toolbar = (Toolbar) findViewById(R.id.create_item_toolbar);
        setSupportActionBar(create_item_toolbar);
        create_item_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        begin_date_tv = (TextView) findViewById(R.id.begindate_tv);

        item_notification_tv = (TextView) findViewById(R.id.item_notification_tv);

        item_name = (EditText) findViewById(R.id.item_name);
        item_name.setBackgroundColor(Color.parseColor("#3F51B5"));

        Calendar now = Calendar.getInstance();
        begin_tpd = TimePickerDialog.newInstance(CreateRecycleItemActivity.this,now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),false);
        begin_tpd.setAccentColor(Color.parseColor("#3F51B5"));

        item_detail = (EditText) findViewById(R.id.item_detail);

        type_tablayout = (TabLayout) findViewById(R.id.type_tab_layout);
        initTabLayout();

        layout_item_color_selector = (LinearLayout) findViewById(R.id.layout_item_color_selector);
        layout_item_color_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorChooserDialog = new ColorChooserDialog.Builder(CreateRecycleItemActivity.this, R.string.item_color)
                        .titleSub(R.string.colors)  // title of dialog when viewing shades of a color
                        .customButton(R.string.custom_label)
                        .doneButton(R.string.done_label)  // changes label of the done button
                        .presetsButton(R.string.presets_label)
                        .cancelButton(R.string.cancel_label)  // changes label of the cancel button
                        .backButton(R.string.back_label)  // changes label of the back button
                        .dynamicButtonColor(true)  // defaults to true, false will disable changing action buttons' color to currently selected color
                        .show();
            }
        });

        imageView_color = (CircleImageView) findViewById(R.id.imageView_color);
        imageView_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorChooserDialog = new ColorChooserDialog.Builder(CreateRecycleItemActivity.this, R.string.item_color)
                        .titleSub(R.string.colors)  // title of dialog when viewing shades of a color
                        .customButton(R.string.custom_label)
                        .doneButton(R.string.done_label)  // changes label of the done button
                        .presetsButton(R.string.presets_label)
                        .cancelButton(R.string.cancel_label)  // changes label of the cancel button
                        .backButton(R.string.back_label)  // changes label of the back button
                        .dynamicButtonColor(true)  // defaults to true, false will disable changing action buttons' color to currently selected color
                        .show();
            }
        });

        layout_begin = (LinearLayout) findViewById(R.id.layout_begin);
        layout_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                begin_tpd.show(getFragmentManager(), "Timepickerdialog");
                begin_tpd.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        begin_date_tv.setText(hourOfDay+"/"+minute);
                        Calendar begin_cal = Calendar.getInstance();
                        begin_cal.set(begin_cal.get(Calendar.YEAR),begin_cal.get(Calendar.MONTH),begin_cal.get(Calendar.DAY_OF_MONTH),hourOfDay,minute,second);
                        begin = begin_cal.getTime();
                    }
                });
            }
        });

        item_recycle = (LinearLayout) findViewById(R.id.item_recycle);
        item_recycle_tv = (TextView) findViewById(R.id.item_recycle_tv);
        item_recycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(CreateRecycleItemActivity.this)
                        .title("选择重复时间")
                        .items(R.array.recycle_type)
                        .itemsCallbackMultiChoice(new Integer[]{}, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
//                                boolean allowSelection = which.length <= 2; // limit selection to 2, the new selection is included in the which array
//                                if (!allowSelection) {
//                                    //TODO
//                                }
                                if(which.length==0) {
                                    Toast.makeText(CreateRecycleItemActivity.this, "至少要选一项啊",Toast.LENGTH_LONG).show();
                                }
                                boolean hasweekends = false;
                                for (int i = 0; i < which.length; i++) {
                                    if(which[i]==5||which[i]==6)
                                        hasweekends = true;
                                }
                                StringBuilder recycle_time = new StringBuilder();
                                if(which.length == 7) {
                                    item_recycle_tv.setText("每天");
                                    recycle_time.append( "everyday");
                                } else if(which.length ==5 && !hasweekends) {
                                    item_recycle_tv.setText("工作日");
                                    recycle_time.append("1 2 3 4 5 ");
                                } /*else if(which.length==2&&(which[0]==5&&which[1]==6)||(which[1]==5&&which[0]==6)) {
                                    item_recycle_tv.setText("周末");
                                } */else {

                                    StringBuilder str = new StringBuilder();
                                    for (int i = 0; i < which.length; i++) {
//                                        if (i > 0) str.append(' ');
//                                        str.append(which[i]);
//                                        str.append(": ");n
                                        recycle_time.append((which[i]+1)+" ");
                                        str.append(text[i]+" ");
                                    }
                                    item_recycle_tv.setText(str);
                                }
                               // Toast.makeText(CreateRecycleItemActivity.this, recycle_time,Toast.LENGTH_SHORT).show();
                                recycle_string = recycle_time.toString();
//                                return allowSelection;
                                return true;
                            }
                        })
                        .positiveText("确定")
                        .alwaysCallMultiChoiceCallback()// the callback will always be called, to check if selection is still allowed
                        .show();
            }
        });

        item_notification = (LinearLayout) findViewById(R.id.item_notification);
        item_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(CreateRecycleItemActivity.this)
                        .title("选择通知样式")
                        .items(R.array.notifications)
                        .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                notification = which;
                                item_notification_tv.setText(text);
                                return true;
                            }
                        })
                        .positiveText("确定")
                        .show();
            }
        });

        if(getSharedPreferences("config", MODE_PRIVATE).getInt("themeColor", R.color.colorPrimary)!=R.color.colorPrimary) {
            SharedPreferences pref = getSharedPreferences("config", MODE_PRIVATE);
            int themeColor = pref.getInt("themeColor", 0);
            initThemeColor(themeColor);
        }

        if (savedInstanceState != null) {
            String data_item_name = savedInstanceState.getString("data_item_name");
            item_name.setText(data_item_name);
            String data_item_detail = savedInstanceState.getString("data_item_detail");
            item_detail.setText(data_item_detail);
            int data_item_type = savedInstanceState.getInt("data_item_type");
            type_tablayout.getTabAt(data_item_type).select();
            int data_item_color = savedInstanceState.getInt("data_item_color");
            imageView_color.setImageDrawable(new ColorDrawable(data_item_color));
            int date_item_notification = savedInstanceState.getInt("date_item_notification");
            item_notification_tv.setText(notification_type[date_item_notification]);
        }

    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        type_color = selectedColor;
        imageView_color.setImageDrawable(new ColorDrawable(selectedColor));
    }

    private void initThemeColor(int color) {
        create_item_appbar.setBackgroundColor(color);
        create_item_toolbar.setBackgroundColor(color);
        begin_tpd.setAccentColor(color);

        type_tablayout.setSelectedTabIndicatorColor(color);
        item_name.setBackgroundColor(color);
        type_color = color;
        Log.d("Creat_color", String.valueOf(type_color));
        imageView_color.setImageDrawable(new ColorDrawable(color));
        if(color== Color.parseColor("#FFFFFF")) {
            item_name.setTextColor(Color.parseColor("#000000"));
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(CircleView.shiftColorDown(color));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ceate_item_menu, menu);
        return true;
    }

    private void initTabLayout() {
        TabLayout.Tab[] tabs = new TabLayout.Tab[t.getNum()];
        for( int i = 0; i< t.getNum(); i++) {
            tabs[i] = type_tablayout.newTab();
            tabs[i].setIcon(t.getIcon(i));
            tabs[i].setText(t.getType_name(i));
            type_tablayout.addTab(tabs[i]);
        }
        type_tablayout.getTabAt(type_tablayout.getSelectedTabPosition()).setIcon(t.getIcon_on(type_tablayout.getSelectedTabPosition()));
        type_tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab == type_tablayout.getTabAt(0)) {
                    tab.setIcon(t.getIcon_on(0));
                } else if(tab == type_tablayout.getTabAt(1)) {
                    tab.setIcon(t.getIcon_on(1));
                } else if(tab == type_tablayout.getTabAt(2)) {
                    tab.setIcon(t.getIcon_on(2));
                } else if(tab == type_tablayout.getTabAt(3)) {
                    tab.setIcon(t.getIcon_on(3));
                } else if(tab == type_tablayout.getTabAt(4)) {
                    tab.setIcon(t.getIcon_on(4));
                } else if(tab == type_tablayout.getTabAt(5)) {
                    tab.setIcon(t.getIcon_on(5));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab == type_tablayout.getTabAt(0)) {
                    tab.setIcon(t.getIcon(0));
                } else if(tab == type_tablayout.getTabAt(1)) {
                    tab.setIcon(t.getIcon(1));
                } else if(tab == type_tablayout.getTabAt(2)) {
                    tab.setIcon(t.getIcon(2));
                } else if(tab == type_tablayout.getTabAt(3)) {
                    tab.setIcon(t.getIcon(3));
                } else if(tab == type_tablayout.getTabAt(4)) {
                    tab.setIcon(t.getIcon(4));
                } else if(tab == type_tablayout.getTabAt(5)) {
                    tab.setIcon(t.getIcon(5));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String data_item_name = item_name.getText().toString();
        outState.putString("data_item_name", data_item_name);
        String data_item_detail = item_detail.getText().toString();
        outState.putString("data_item_detail", data_item_detail);
        int data_item_type = type_tablayout.getSelectedTabPosition();
        outState.putInt("data_item_type", data_item_type);
        int data_item_color = type_color;
        outState.putInt("data_item_color", data_item_color);
        int date_item_notification = notification;
        outState.putInt("date_item_notification", date_item_notification);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_save) {
            if (item_name.getText().toString().trim().isEmpty()) {
                Toast.makeText(CreateRecycleItemActivity.this, "请输入事务名称", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                new_item.setName(item_name.getText().toString());
            }
            new_item.setDetail(item_detail.getText().toString());

            new_item.setType(type_tablayout.getSelectedTabPosition());

            new_item.setColor(type_color);

            new_item.setRecycle(recycle_string);
//            Log.d("CRIA_RECYCLE",new_item.getRecycle());

            new_item.setNotification(notification);

            recycleitem_id = (int) SystemClock.uptimeMillis();


            if (begin_date_tv.getText().equals("起始日期")) {
                Toast.makeText(CreateRecycleItemActivity.this, "起始时间填写不完全", Toast.LENGTH_SHORT).show();
                return true;
            } else if(item_recycle_tv.getText().equals("重复时间")) {
                Toast.makeText(CreateRecycleItemActivity.this, "重复时间填写不完全", Toast.LENGTH_SHORT).show();
                return true;
            }else {
                new_item.setBeginTime(begin);
            }

            if(new_item.getBeginTime().before(new Date())) {
                Toast.makeText(CreateRecycleItemActivity.this,"设置时间比现在要早，今天收不到通知", Toast.LENGTH_LONG).show();
            }

            Bundle alarmbundle = new Bundle();
            alarmbundle.putSerializable("item", new_item);
            Calendar c=Calendar.getInstance();
            c.setTime(begin);
            c.set(Calendar.SECOND, 0);
            Log.d("c", String.valueOf(c.getTime()));
            AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            int requestCode = (int) SystemClock.uptimeMillis();;
            Intent notification_intent = new Intent(CreateRecycleItemActivity.this, RecycleAlarmReceiver.class);
            notification_intent.putExtras(alarmbundle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(CreateRecycleItemActivity.this, requestCode, notification_intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 24*60*60*1000, pendingIntent);

            Intent intent = new Intent("com.app.youwei.myapp.ADD_RECYCLE");
            Bundle mbundle = new Bundle();
            mbundle.putSerializable("broad_new_recycle_item", new_item);
            intent.putExtras(mbundle);
            sendBroadcast(intent);
            finish();
        }
        return true;
    }
}
