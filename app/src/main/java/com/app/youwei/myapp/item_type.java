package com.app.youwei.myapp;

/**
 * Created by YouWei on 2016/7/25.
 */
public class item_type {
    private String[] type_name = {"工作", "休息", "运动","娱乐", "吃饭", "其他"};
    private int[] icon = {R.drawable.ic_briefcase_grey600_24dp, R.drawable.ic_sleep_grey600_24dp, R.drawable.ic_run_grey600_24dp,R.drawable.ic_gamepad_variant_grey600_24dp,R.drawable.ic_food_grey600_24dp, R.drawable.ic_format_list_bulleted_type_grey600_24dp};
    private int[] icon_on= {R.drawable.ic_briefcase_black_24dp, R.drawable.ic_sleep_black_24dp, R.drawable.ic_run_black_24dp, R.drawable.ic_gamepad_variant_black_24dp,R.drawable.ic_food_black_24dp,R.drawable.ic_format_list_bulleted_type_black_24dp};

    public int getIcon(int position) {
        return icon[position];
    }

    public int getIcon_on(int position) {
        return icon_on[position];
    }

    public String getType_name(int position) {
        return type_name[position];
    }

    public int getNum() {
        return type_name.length;
    }
}
