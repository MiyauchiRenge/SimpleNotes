package com.app.youwei.myapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.CircleView;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditItemActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    AppBarLayout edit_item_appbar;
    Toolbar edit_item_toolbar;
    TabLayout type_tablayout;

    EditText item_name;
    EditText item_detail;

    ColorChooserDialog colorChooserDialog;
    item_type t=new item_type();

    LinearLayout layout_item_color_selector;
    CircleImageView imageView_color;

    LinearLayout layout_begin;
    LinearLayout layout_end;


    TextView begin_date_tv;

    TextView end_date_tv;

    DatePickerDialog begin_dpd= new DatePickerDialog();
    TimePickerDialog begin_tpd = new TimePickerDialog();

    DatePickerDialog end_dpd = new DatePickerDialog();
    TimePickerDialog end_tpd = new TimePickerDialog();

    Date begin = new Date();
    Date end = new Date();
    Date begin_temp = new Date();
    Date end_temp = new Date();

    LinearLayout item_notification;
    TextView item_notification_tv;

    Item new_item = new Item();
    int type_color = -12627531;
    int notification = 0;
    String[] notification_type = new String[] {
            "次要:振动,可移除的通知", "一般:振动和铃声,可移除的通知", "重要:振动和铃声,不可移除的通知"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        edit_item_appbar = (AppBarLayout) findViewById(R.id.edit_item_appbar);
        edit_item_toolbar = (Toolbar) findViewById(R.id.edit_item_toolbar);
        setSupportActionBar(edit_item_toolbar);
        edit_item_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        item_notification_tv = (TextView) findViewById(R.id.edit_item_notification_tv);

        item_name = (EditText) findViewById(R.id.edit_item_name);
        item_name.setBackgroundColor(Color.parseColor("#3F51B5"));

        Calendar now = Calendar.getInstance();
        begin_dpd = DatePickerDialog.newInstance(
                EditItemActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        begin_dpd.setAccentColor(Color.parseColor("#3F51B5"));
        begin_tpd = TimePickerDialog.newInstance(EditItemActivity.this,now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),false);
        begin_tpd.setAccentColor(Color.parseColor("#3F51B5"));
        end_dpd = DatePickerDialog.newInstance(
                EditItemActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        end_dpd.setAccentColor(Color.parseColor("#3F51B5"));
        end_tpd = TimePickerDialog.newInstance(EditItemActivity.this,now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),false);
        end_tpd.setAccentColor(Color.parseColor("#3F51B5"));


        item_detail = (EditText) findViewById(R.id.edit_item_detail);

        type_tablayout = (TabLayout) findViewById(R.id.edit_type_tab_layout);

        initTabLayout();

        layout_item_color_selector = (LinearLayout) findViewById(R.id.layout_item_color_selector);
        layout_item_color_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorChooserDialog = new ColorChooserDialog.Builder(EditItemActivity.this, R.string.item_color)
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

        imageView_color = (CircleImageView) findViewById(R.id.edit_imageView_color);
        imageView_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorChooserDialog = new ColorChooserDialog.Builder(EditItemActivity.this, R.string.item_color)
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


        begin_date_tv = (TextView) findViewById(R.id.edit_begindate_tv);

        end_date_tv = (TextView) findViewById(R.id.edit_enddate_tv);

        item_notification = (LinearLayout) findViewById(R.id.edit_item_notification);
        item_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(EditItemActivity.this)
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

        Item edit_item = getIntent().getParcelableExtra("edit_item");
        item_name.setText(edit_item.getName());
        item_detail.setText(edit_item.getDetail());
        type_tablayout.getTabAt(edit_item.getType()).select();
        type_color = edit_item.getColor();
        imageView_color.setImageDrawable(new ColorDrawable(edit_item.getColor()));

        begin = edit_item.getBegin_time();
        end = edit_item.getEnd_time();
        Calendar begin_ = Calendar.getInstance();
        begin_.setTime(edit_item.getBegin_time());
        Calendar end_ = Calendar.getInstance();
        end_.setTime(edit_item.getEnd_time());
        begin_date_tv.setText(begin_.get(Calendar.YEAR)+"/"+(begin_.get(Calendar.MONTH)+1) +"/"+ begin_.get(Calendar.DAY_OF_MONTH)+"/"+begin_.get(Calendar.HOUR_OF_DAY)+"/"+begin_.get(Calendar.MINUTE));
        end_date_tv.setText(end_.get(Calendar.YEAR)+"/"+(end_.get(Calendar.MONTH)+1) +"/"+end_.get(Calendar.DAY_OF_MONTH)+"/"+end_.get(Calendar.HOUR_OF_DAY)+"/"+end_.get(Calendar.MINUTE));
        notification = edit_item.getNotification();
        item_notification_tv.setText(notification_type[edit_item.getNotification()]);


        layout_begin = (LinearLayout) findViewById(R.id.edit_layout_begin);
        layout_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                begin_dpd.show(getFragmentManager(), "Datepickerdialog");
                begin_dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, final int year, final int monthOfYear, final int dayOfMonth) {
                        begin_tpd.show(getFragmentManager(), "Timepickerdialog");
                        begin_tpd.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                                Calendar begin_cal = Calendar.getInstance();
                                begin_cal.set(year,monthOfYear,dayOfMonth,hourOfDay,minute);
                                begin_temp = begin_cal.getTime();
                                if(end.before(begin_temp)||end.equals(begin_temp)) {
                                    Toast.makeText(EditItemActivity.this, "结束时间必须晚于起始时间",Toast.LENGTH_SHORT).show();
                                } else {
                                    begin_date_tv.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth + "/" + hourOfDay + "/" + minute);
                                    begin = begin_cal.getTime();
                                }
                            }
                        });
                    }
                });
            }
        });

        layout_end = (LinearLayout) findViewById(R.id.layout_end);
        layout_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end_dpd.show(getFragmentManager(), "Datepickerdialog");
                end_dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, final int year, final int monthOfYear, final int dayOfMonth) {
                        end_tpd.show(getFragmentManager(), "Timepickerdialog");
                        end_tpd.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                                Calendar end_cal = Calendar.getInstance();
                                end_cal.set(year, monthOfYear, dayOfMonth, hourOfDay, minute);
                                end_temp = end_cal.getTime();
                                if(end_temp.before(begin)||end_temp.equals(begin)) {
                                    Toast.makeText(EditItemActivity.this, "结束时间必须晚于起始时间",Toast.LENGTH_SHORT).show();
                                } else {
                                    end_date_tv.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth+"/"+hourOfDay+"/"+minute);
                                    end = end_cal.getTime();
                                }
                            }
                        });
                    }
                });
            }
        });

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initThemeColor(int color) {
        edit_item_appbar.setBackgroundColor(color);
        edit_item_toolbar.setBackgroundColor(color);
        begin_dpd.setAccentColor(color);
        begin_tpd.setAccentColor(color);
        end_dpd.setAccentColor(color);
        end_tpd.setAccentColor(color);
        type_color = color;
        Log.d("Edit_color", String.valueOf(type_color));
        type_tablayout.setSelectedTabIndicatorColor(color);
        item_name.setBackgroundColor(color);
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
        getMenuInflater().inflate(R.menu.edit_item_menu, menu);
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
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {}


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {}

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_delete) {
            new MaterialDialog.Builder(EditItemActivity.this)
                    .content(R.string.delete_item_string)
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if(which.name() == "POSITIVE") {
                                Intent intent = new Intent("com.app.youwei.myapp.DELETE");
                                Bundle mbundle = new Bundle();
                                mbundle.putParcelable("broad_delete_item", new_item);
                                intent.putExtras(mbundle);
                                sendBroadcast(intent);
                                finish();
                            } else if (which.name() == "NEGATIVE") {
                                dialog.dismiss();
                            }
                        }
                    })
                    .show();
        } else if(id == R.id.action_save) {
            if (item_name.getText().toString().trim().isEmpty()) {
                Toast.makeText(EditItemActivity.this, "请输入事务名称", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                new_item.setName(item_name.getText().toString());
            }
            new_item.setDetail(item_detail.getText().toString());

            new_item.setType(type_tablayout.getSelectedTabPosition());

            new_item.setColor(type_color);

            new_item.setNotification(notification);

            if (begin_date_tv.getText().equals("起始日期") || end_date_tv.getText().equals("结束日期")) {
                Toast.makeText(EditItemActivity.this, "时间填写不完全", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                new_item.setBegin_time(begin);
                new_item.setEnd_time(end);
            }
            int requestCode = (int) SystemClock.uptimeMillis();

            Bundle alarmbundle = new Bundle();
            alarmbundle.putParcelable("item", new_item);
            Calendar c=Calendar.getInstance();
            c.setTime(begin);
            c.set(Calendar.SECOND, 0);
            AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent notification_intent= new Intent(EditItemActivity.this, AlarmReceiver.class);
            notification_intent.putExtras(alarmbundle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(EditItemActivity.this, requestCode, notification_intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar c_end=Calendar.getInstance();
            c_end.setTime(end);
            c_end.set(Calendar.SECOND, 0);
            AlarmManager alarmMgr_end = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            int requestCode_end = (int) SystemClock.uptimeMillis();;
//            Bundle alarmbundle_end = new Bundle();
//            alarmbundle_end.putParcelable("item", new_item);
            Intent notification_intent_end = new Intent(EditItemActivity.this, AlarmEndReceiver.class);
            notification_intent_end.putExtras(alarmbundle);
            PendingIntent pendingIntent_end = PendingIntent.getBroadcast(EditItemActivity.this, requestCode_end, notification_intent_end, PendingIntent.FLAG_UPDATE_CURRENT);


            Calendar now=Calendar.getInstance();
            Date date_now = now.getTime();
            if(begin.before(date_now) && end.before(date_now)) {
                Toast.makeText(EditItemActivity.this,"结束时间比现在要早是收不到结束通知的",Toast.LENGTH_LONG).show();
            }else if(begin.before(date_now) && end.after(date_now)) {
                Toast.makeText(EditItemActivity.this,"起始时间比现在要早是收不到开始通知的",Toast.LENGTH_LONG).show();
            } else {
                alarmMgr.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),  pendingIntent);
                alarmMgr_end.set(AlarmManager.RTC_WAKEUP, c_end.getTimeInMillis(),  pendingIntent_end);
            }

//            Calendar week=Calendar.getInstance();
//            week.setTime(begin);
//            Log.d("week", String.valueOf(week.get(Calendar.DAY_OF_WEEK)));

            Intent intent = new Intent("com.app.youwei.myapp.EDIT");
            Bundle mbundle = new Bundle();
            mbundle.putParcelable("broad_edit_item", new_item);
            intent.putExtras(mbundle);
            sendBroadcast(intent);
            finish();
        }
        return true;
    }

//    private boolean isequal(Calendar begin_cal, Calendar end_cal) {
//        Log.d("begin_cal",String.valueOf(begin_cal));
//        Log.d("end_cal", String.valueOf(end_cal));
//        if(begin_cal.get(Calendar.YEAR) == end_cal.get(Calendar.YEAR)) {
//            if (begin_cal.get(Calendar.MONTH) == end_cal.get(Calendar.MONTH)) {
//                if(begin_cal.get(Calendar.DAY_OF_MONTH) == end_cal.get(Calendar.DAY_OF_MONTH)) {
//                    if (begin_cal.get(Calendar.HOUR) == end_cal.get(Calendar.HOUR)) {
//                        if((end_cal.get(Calendar.MINUTE)-begin_cal.get(Calendar.MINUTE))<5) {
//                            return true;
//                        }
//                    }
//                }
//            }
//        }
//        return false;
//    }


}
