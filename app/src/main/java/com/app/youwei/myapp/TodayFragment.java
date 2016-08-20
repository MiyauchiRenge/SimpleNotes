package com.app.youwei.myapp;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by YouWei on 2016/7/24.
 */
public class TodayFragment extends Fragment  {

    View view;
    IntentFilter intentFilter;
    ItemBroadcastReceiver receiver;

    RecyclerView today_rv;
    TodayAdapter todayAdapter;
    List<Item> item_list = new ArrayList<>();
    ImageView today_item_icon;

    TextView today_item_name, today_item_time;

    ItemDatabaseHelper dbHelper;

    int edit_pos;

    class SortComparator implements Comparator {

        @Override
        public int compare(Object lhs, Object rhs) {
            Item a = (Item) lhs;
            Item b = (Item) rhs;
            if(a.getBegin_time().before(b.getBegin_time())) {
                return -1;
            } else if (a.getBegin_time().after(b.getBegin_time())) {
                return 1;
            } else {
                return 0;
            }

        }
    }

    private void sort_item() {
        Comparator comp = new SortComparator();
        Collections.sort(item_list, comp);
//        for(int i =0; i< list.size(); i++) {
//            Log.d("sort_item", String.valueOf(list.get(i).getName()));
//        }
    }

    class ItemBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.app.youwei.myapp.ADD")) {
                Item new_item = intent.getParcelableExtra("broad_new_item");
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", new_item.getName());
                values.put("detail", new_item.getDetail());
                values.put("type", new_item.getType());
                values.put("begin_time", new_item.getBegin_time().getTime());
                values.put("end_time", new_item.getEnd_time().getTime());
                values.put("color", new_item.getColor());
                values.put("begin_week", new_item.getBegin_week());
                values.put("end_week", new_item.getEnd_week());
                values.put("notification", new_item.getNotification());
                db.insert("Item", null, values);
                db.close();
                item_list.add(new_item);
                sort_item();

            } else if (intent.getAction().equals("com.app.youwei.myapp.EDIT")) {

                Item edit_item = intent.getParcelableExtra("broad_edit_item");
                SQLiteDatabase edit_db = dbHelper.getWritableDatabase();
                ContentValues edit_values = new ContentValues();
                edit_values.put("name", edit_item.getName());
                edit_values.put("detail", edit_item.getDetail());
                edit_values.put("type", edit_item.getType());
                edit_values.put("begin_time", edit_item.getBegin_time().getTime());
                edit_values.put("end_time", edit_item.getEnd_time().getTime());
                edit_values.put("color", edit_item.getColor());
                edit_values.put("begin_week", edit_item.getBegin_week());
                edit_values.put("end_week", edit_item.getEnd_week());
                edit_values.put("notification", edit_item.getNotification());
//                Log.d("edit_temp",item_list.get(edit_pos).getName());
                edit_db.update("Item", edit_values, "name = ? AND detail= ? AND type= ? AND begin_time= ? AND end_time = ? AND color = ? AND notification = ?", new String[]{item_list.get(edit_pos).getName(), item_list.get(edit_pos).getDetail(),
                        String.valueOf(item_list.get(edit_pos).getType()), String.valueOf(item_list.get(edit_pos).getBegin_time().getTime()), String.valueOf(item_list.get(edit_pos).getEnd_time().getTime()),
                        String.valueOf(item_list.get(edit_pos).getColor()),String.valueOf(item_list.get(edit_pos).getNotification())});
                edit_db.close();
                item_list.add(edit_item);
                item_list.remove(edit_pos);

//                Log.d("edit_pos", String.valueOf(edit_pos));
                sort_item();
//                for(int i =0; i< item_list.size(); i++) {
//                     Log.d("sort_item", String.valueOf(item_list.get(i).getName()));
//                }
            } else if(intent.getAction().equals("com.app.youwei.myapp.DELETE")) {
//                Item delete_item = intent.getParcelableExtra("broad_delete_item");
                dbHelper = new ItemDatabaseHelper(getActivity(),"Items.db", null ,1);
                dbHelper.getWritableDatabase();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //Log.d("id",String.valueOf(item_list.get(edit_pos).getId()));
                db.delete("Item", "name = ? AND detail= ? AND type= ? AND begin_time= ? AND end_time = ? AND color = ? AND notification = ?", new String[]{item_list.get(edit_pos).getName(), item_list.get(edit_pos).getDetail(),
                        String.valueOf(item_list.get(edit_pos).getType()), String.valueOf(item_list.get(edit_pos).getBegin_time().getTime()), String.valueOf(item_list.get(edit_pos).getEnd_time().getTime()),
                        String.valueOf(item_list.get(edit_pos).getColor()),String.valueOf(item_list.get(edit_pos).getNotification())});
                item_list.remove(edit_pos);
                db.close();
            }
            todayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.app.youwei.myapp.ADD");
        //edit_intentFilter = new IntentFilter();
        intentFilter.addAction("com.app.youwei.myapp.EDIT");
        intentFilter.addAction("com.app.youwei.myapp.DELETE");
        receiver = new ItemBroadcastReceiver();
      //  edit_receiver = new ItemBroadcastReceiver();
        getActivity().registerReceiver(receiver,intentFilter);
        //getActivity().registerReceiver(edit_receiver,edit_intentFilter);
    }

    private void initData() {
        dbHelper = new ItemDatabaseHelper(getActivity(),"Items.db", null ,1);
        dbHelper.getWritableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Item", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.
                        getColumnIndex("name"));
                String detail = cursor.getString(cursor.
                        getColumnIndex("detail"));
                int type = cursor.getInt(cursor.getColumnIndex
                        ("type"));
                long begin_time = cursor.getLong(cursor.getColumnIndex
                        ("begin_time"));
                long end_time = cursor.getLong(cursor.getColumnIndex
                        ("end_time"));
                int color = cursor.getInt(cursor.getColumnIndex
                        ("color"));
                int begin_week = cursor.getInt(cursor.getColumnIndex
                        ("begin_week"));
                int end_week = cursor.getInt(cursor.getColumnIndex
                        ("end_week"));
                int notification = cursor.getInt(cursor.getColumnIndex
                        ("notification"));
                Item temp = new Item();
                temp.setName(name); temp.setDetail(detail); temp.setType(type);
                temp.setBegin_time(new Date(begin_time));
                temp.setEnd_time(new Date(end_time));
                temp.setColor(color);
                temp.setBegin_week(begin_week);
                temp.setEnd_week(end_week);
                temp.setNotification(notification);
                item_list.add(temp);
            } while (cursor.moveToNext());
        }
        sort_item();
        cursor.close();
        db.close();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
      // getActivity().unregisterReceiver(edit_receiver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_today, container, false);
        initData();
        today_rv = (RecyclerView) view.findViewById(R.id.today_recyclerView);
        todayAdapter = new TodayAdapter();
        today_rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        today_rv.setAdapter(todayAdapter);
        return view;
    }


    class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.TodayViewHolder> {

        @Override
        public TodayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TodayViewHolder holder = new TodayViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.today_recycler_row, parent,
                    false));
            holder.setIsRecyclable(false);
            return holder;
        }

        @Override
        public void onBindViewHolder(final TodayViewHolder holder, int position) {
            Item n_item = item_list.get(position);
            today_item_icon.setImageResource(new item_type().getIcon(n_item.getType()));
            today_item_icon.setColorFilter(n_item.getColor());
            today_item_name.setText(n_item.getName());
            Date begin = n_item.getBegin_time();
            Date end = n_item.getEnd_time();
            Calendar begin_cal = Calendar.getInstance();
            begin_cal.setTime(begin);
            Calendar end_cal = Calendar.getInstance();
            end_cal.setTime(end);
            today_item_time.setText((begin_cal.get(Calendar.MONTH)+1)+ "月"+
                    begin_cal.get(Calendar.DAY_OF_MONTH)+"日"+ begin_cal.get(Calendar.HOUR_OF_DAY)+"时" +begin_cal.get(Calendar.MINUTE)+"分" +" 至 "+(end_cal.get(Calendar.MONTH)+1)+ "月"+
                    end_cal.get(Calendar.DAY_OF_MONTH)+"日"+ end_cal.get(Calendar.HOUR_OF_DAY)+"时" +end_cal.get(Calendar.MINUTE)+"分  ");
        }

        @Override
        public int getItemCount() {
            return item_list.size();
        }

        class TodayViewHolder extends RecyclerView.ViewHolder {

            public TodayViewHolder(View itemView) {
                super(itemView);
                today_item_name = (TextView) itemView.findViewById(R.id.today_item_name);
                today_item_time = (TextView) itemView.findViewById(R.id.today_item_time);
                today_item_icon = (ImageView) itemView.findViewById(R.id.today_item_icon);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edit_pos = getLayoutPosition();
                        Item temp = item_list.get(edit_pos);
                        Intent intent = new Intent(getActivity(),EditItemActivity.class);
                        Bundle mbundle = new Bundle();
//                        Log.d("new_item", String.valueOf(temp.getId()));
                        mbundle.putParcelable("edit_item", temp);
                        intent.putExtras(mbundle);
                        startActivity(intent);
                    }
                });
//                itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        new MaterialDialog.Builder(getActivity())
//                                .content(R.string.delete_item_string)
//                                .positiveText(R.string.agree)
//                                .negativeText(R.string.disagree)
//                                .onAny(new MaterialDialog.SingleButtonCallback() {
//                                    @Override
//                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                        if(which.name() == "POSITIVE") {
//                                            int pos = getLayoutPosition();
//                                            Item temp = item_list.get(pos);
//                                            dbHelper = new ItemDatabaseHelper(getActivity(),"Items.db", null ,1);
//                                            dbHelper.getWritableDatabase();
//                                            SQLiteDatabase db = dbHelper.getWritableDatabase();
////                                          db.delete("Item", "name = ? and detail= ? and type = ? and begin_time = ? and end_time = ? and color = ? and notification = ?", new String[]{temp.getName(), temp.getDetail(), String.valueOf(temp.getBegin_time().getTime()),String.valueOf(temp.getEnd_time().getTime()),
////                                                    String.valueOf(temp.getColor()), String.valueOf(temp.getNotification())});
//                                            db.delete("Item", "name = ?", new String[]{temp.getName()});
//                                            //Log.d("Delete", String.valueOf(pos));
//                                            item_list.remove(pos);
//                                            notifyItemRemoved(pos);
//                                            db.close();
//                                        } else if (which.name() == "NEGATIVE") {
//                                            dialog.dismiss();
//                                        }
//                                    }
//                                })
//                                .show();
//                        return true;
//                    }
//                });
            }
        }

    }

}
