package com.app.youwei.myapp;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.CircleView;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ColorChooserDialog.ColorCallback {

    CollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;
    TabLayout mTabLayout;
    Toolbar toolbar;
    ColorChooserDialog colorChooserDialog;
    DrawerLayout drawer;
    NavigationView navigationView;
    int themeColor = R.color.colorPrimary;
    ActivityManager.TaskDescription taskDesc;
    FloatingActionsMenu menuMultipleActions;
    com.getbase.floatingactionbutton.FloatingActionButton add_single_item;
    com.getbase.floatingactionbutton.FloatingActionButton add_cycle_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDemoCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        initTabLayout();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headView = navigationView.getHeaderView(0);
       // ImageView user_pic = (ImageView) headView.findViewById(R.id.imageView);


        int[][] states = new int[][]{new int[]{ android.R.attr.state_checked},new int[]{ -android.R.attr.state_checked} };
        int[] colors = new int[]{ getResources().getColor(R.color.checked_color),  getResources().getColor(R.color.uncheck_color) };
        ColorStateList csl = new ColorStateList(states, colors);
        navigationView.setItemTextColor(csl);
        navigationView.setItemIconTintList(csl);

//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, CreatItemActivity.class));
//            }
//        });
        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        add_single_item = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.add_single_item);
        add_single_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreatItemActivity.class));
            }
        });
        add_cycle_item = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.add_cycle_item);
        add_cycle_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateRecycleItemActivity.class));
            }
        });

        if(getSharedPreferences("config", MODE_PRIVATE).getInt("themeColor", R.color.colorPrimary)!=R.color.colorPrimary) {
            SharedPreferences pref = getSharedPreferences("config", MODE_PRIVATE);
            themeColor = pref.getInt("themeColor", 0);
            initTheme(themeColor);
        }

    }

    private void initTabLayout() {
        for(int i = 0; i< mDemoCollectionPagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(R.layout.tablayout_item);
            View tabview = tab.getCustomView();
            ImageView tl_icon = (ImageView) tabview.findViewById(R.id.tl_icon);
            tl_icon.setImageResource(mDemoCollectionPagerAdapter.getIcon(i));
            TextView tl_title = (TextView) tabview.findViewById(R.id.tl_title);
            tl_title.setText(mDemoCollectionPagerAdapter.getPageTitle(i));
        }
        TabLayout.Tab first_item = mTabLayout.getTabAt(0);
        ((ImageView)first_item.getCustomView().findViewById(R.id.tl_icon)).
                setImageResource(mDemoCollectionPagerAdapter.getIcon_on(0));
        ((TextView)first_item.getCustomView().findViewById(R.id.tl_title)).setTextColor(Color.WHITE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View tabview = tab.getCustomView();
                ImageView tl_icon = (ImageView) tabview.findViewById(R.id.tl_icon);
                TextView tl_title = (TextView) tabview.findViewById(R.id.tl_title);
                if (tab == mTabLayout.getTabAt(0)) {
                    mViewPager.setCurrentItem(0);
                    tl_icon.setImageResource(mDemoCollectionPagerAdapter.getIcon_on(0));
                    tl_title.setTextColor(Color.WHITE);
                } else if (tab == mTabLayout.getTabAt(1)) {
                    mViewPager.setCurrentItem(1);
                    tl_icon.setImageResource(mDemoCollectionPagerAdapter.getIcon_on(1));
                    tl_title.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View tabview = tab.getCustomView();
                ImageView tl_icon = (ImageView) tabview.findViewById(R.id.tl_icon);
                TextView tl_title = (TextView) tabview.findViewById(R.id.tl_title);
                if (tab == mTabLayout.getTabAt(0)) {
                    tl_icon.setImageResource(mDemoCollectionPagerAdapter.getIcon(0));
                    tl_title.setTextColor(Color.parseColor("#9E9E9E"));
                } else if (tab == mTabLayout.getTabAt(1)) {
                    tl_icon.setImageResource(mDemoCollectionPagerAdapter.getIcon(1));
                    tl_title.setTextColor(Color.parseColor("#9E9E9E"));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//
//            startActivity(new Intent(MainActivity.this,SettingsActivity.class));
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.nav_theme) {
            startColorPicker();
        }/* else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));
        } */else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this,AboutActivity.class));
        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startColorPicker() {
        colorChooserDialog = new ColorChooserDialog.Builder(this, R.string.color_palette)
                .titleSub(R.string.colors)  // title of dialog when viewing shades of a color
                .customButton(R.string.custom_label)
                .doneButton(R.string.done_label)  // changes label of the done button
                .presetsButton(R.string.presets_label)
                .cancelButton(R.string.cancel_label)  // changes label of the cancel button
                .backButton(R.string.back_label)  // changes label of the back button
                .dynamicButtonColor(true)  // defaults to true, false will disable changing action buttons' color to currently selected color
                .show();
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        if(Color.alpha(selectedColor) != 255) {
            Toast.makeText(MainActivity.this, "自定义颜色的Alpha值只能为255", Toast.LENGTH_LONG).show();
        } else {
        setThemeColor(selectedColor);
        SharedPreferences.Editor editor = getSharedPreferences("config",
                MODE_PRIVATE).edit();
        editor.putInt("themeColor", selectedColor);
        editor.commit();
        initTheme(selectedColor);
        }
    }

    public void setThemeColor(int color) {
        themeColor = color;
    }

    public int getThemeColor() {
        return themeColor;
    }


    private void changeNavItemColor(int color) {
        int[][] reset_states = new int[][]{new int[]{ android.R.attr.state_checked},new int[]{ -android.R.attr.state_checked} };
        int[] reset_colors = new int[]{ color,  getResources().getColor(R.color.uncheck_color) };
        ColorStateList reset_csl = new ColorStateList(reset_states, reset_colors);
        navigationView.setItemTextColor(reset_csl);
        navigationView.setItemIconTintList(reset_csl);
    }

    private void changeThemeColor(int color) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(CircleView.shiftColorDown(color));
        }
        mTabLayout.setBackgroundColor(color);
    }

    private void changeTaskManager(int color) {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            taskDesc = new ActivityManager.TaskDescription("简单记事", bm, color);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MainActivity.this.setTaskDescription(taskDesc);
        }
    }

//    private void changeFloatingColor(int color) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            menuMultipleActions.setBackgroundTintList(ColorStateList.valueOf(color));
//        }
//    }


    private void initTheme(int color) {
        changeNavItemColor(color);
        changeThemeColor(color);
        changeTaskManager(color);
//        changeFloatingColor(color);
    }
}
