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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by YouWei on 2016/7/24.
 */
public class NowFragment extends Fragment {
    View view;
    IntentFilter intentFilter;
    RecycleItemBroadcastReceiver receiver;
    List<RecycleItem> item_list = new ArrayList<>();
    ImageView now_item_icon;

    TextView now_item_name, now_item_recycle;

    RecyclerView now_rv;
    NowAdapter nowAdapter;

    RecycleItemDatabaseHelper dbHelper;

    int edit_pos;

    class SortComparator implements Comparator {

        @Override
        public int compare(Object lhs, Object rhs) {
            RecycleItem a = (RecycleItem) lhs;
            RecycleItem b = (RecycleItem) rhs;
            if(a.getBeginTime().getHours()<b.getBeginTime().getHours()) {
                return -1;
            } else if (a.getBeginTime().getHours()>b.getBeginTime().getHours()) {
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

    class RecycleItemBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.app.youwei.myapp.ADD_RECYCLE")) {
                RecycleItem new_item = (RecycleItem) intent.getSerializableExtra("broad_new_recycle_item");
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
           //     Log.d("recycleitem", new_item.getRecycle());
               // Log.d("Begin_time", String.valueOf(new_item.getBeginTime()));
                values.put("name", new_item.getName());
                values.put("detail", new_item.getDetail());
                values.put("type", new_item.getType());
                values.put("time", new_item.getBeginTime().getTime());
                values.put("recycle", new_item.getRecycle());
                values.put("color", new_item.getColor());
                values.put("notification", new_item.getNotification());
                db.insert("RecycleItem", null, values);
                db.close();
                item_list.add(new_item);
                sort_item();
            } else if (intent.getAction().equals("com.app.youwei.myapp.EDIT_RECYCLE")) {
                RecycleItem edit_item = (RecycleItem) intent.getSerializableExtra("broad_edit_recycle_item");
                SQLiteDatabase edit_db = dbHelper.getWritableDatabase();
                ContentValues edit_values = new ContentValues();
                edit_values.put("name", edit_item.getName());
                edit_values.put("detail", edit_item.getDetail());
                edit_values.put("type", edit_item.getType());
                edit_values.put("time", edit_item.getBeginTime().getTime());
                edit_values.put("recycle", edit_item.getRecycle());
                edit_values.put("color", edit_item.getColor());
                edit_values.put("notification", edit_item.getNotification());
               // Log.d("edit_temp",item_list.get(edit_pos).getName());
                edit_db.update("RecycleItem", edit_values, "name = ? AND detail= ? AND type= ? AND time= ? AND recycle = ? AND color = ? AND notification = ?", new String[]{item_list.get(edit_pos).getName(), item_list.get(edit_pos).getDetail(),
                        String.valueOf(item_list.get(edit_pos).getType()), String.valueOf(item_list.get(edit_pos).getBeginTime().getTime()),item_list.get(edit_pos).getRecycle(),
                        String.valueOf(item_list.get(edit_pos).getColor()),String.valueOf(item_list.get(edit_pos).getNotification())});
                edit_db.close();
                item_list.add(edit_item);
                item_list.remove(edit_pos);
                sort_item();
//                for(int i =0; i< item_list.size(); i++) {
//                     Log.d("sort_item", String.valueOf(item_list.get(i).getName()));
//                }
            } else if(intent.getAction().equals("com.app.youwei.myapp.DELETE_RECYCLE")) {
//                RecycleItem delete_item = (RecycleItem) intent.getSerializableExtra("broad_delete_recycle_item");
                dbHelper = new RecycleItemDatabaseHelper(getActivity(),"RecycleItems.db", null ,1);
                dbHelper.getWritableDatabase();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                Log.d("RecycleItem", String.valueOf(delete_item.getId()));
                db.delete("RecycleItem", "name = ? AND detail= ? AND type= ? AND time= ? AND recycle = ? AND color = ? AND notification = ?", new String[]{item_list.get(edit_pos).getName(), item_list.get(edit_pos).getDetail(),
                        String.valueOf(item_list.get(edit_pos).getType()), String.valueOf(item_list.get(edit_pos).getBeginTime().getTime()),item_list.get(edit_pos).getRecycle(),
                        String.valueOf(item_list.get(edit_pos).getColor()),String.valueOf(item_list.get(edit_pos).getNotification())});
                item_list.remove(edit_pos);
                db.close();
            }

            nowAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.app.youwei.myapp.ADD_RECYCLE");
        intentFilter.addAction("com.app.youwei.myapp.EDIT_RECYCLE");
        intentFilter.addAction("com.app.youwei.myapp.DELETE_RECYCLE");
        receiver = new RecycleItemBroadcastReceiver();
        getActivity().registerReceiver(receiver,intentFilter);
    }
    private void initData() {
        dbHelper = new RecycleItemDatabaseHelper(getActivity(),"RecycleItems.db", null ,1);
        dbHelper.getWritableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("RecycleItem", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.
                        getColumnIndex("name"));
                String detail = cursor.getString(cursor.
                        getColumnIndex("detail"));
                int type = cursor.getInt(cursor.getColumnIndex
                        ("type"));
                long begin_time = cursor.getLong(cursor.getColumnIndex
                        ("time"));

                int color = cursor.getInt(cursor.getColumnIndex
                        ("color"));
                String recycle = cursor.getString(cursor.getColumnIndex("recycle"));

                int notification = cursor.getInt(cursor.getColumnIndex
                        ("notification"));
                RecycleItem temp = new RecycleItem();
                temp.setName(name); temp.setDetail(detail); temp.setType(type);
                temp.setBeginTime(new Date(begin_time));
                temp.setColor(color);
                temp.setRecycle(recycle);
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_now, container, false);
        initData();
        now_rv = (RecyclerView) view.findViewById(R.id.now_recyclerView);
        nowAdapter = new NowAdapter();
        now_rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        now_rv.setAdapter(nowAdapter);
        return view;
    }

    class NowAdapter extends RecyclerView.Adapter<NowAdapter.NowViewHolder> {

        @Override
        public NowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            NowViewHolder holder = new NowViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.now_recycler_row, parent,
                    false));
            holder.setIsRecyclable(false);
            return holder;
        }

        @Override
        public void onBindViewHolder(final NowViewHolder holder, int position) {
            RecycleItem n_item = item_list.get(position);
            now_item_icon.setImageResource(new item_type().getIcon(n_item.getType()));
            now_item_icon.setColorFilter(n_item.getColor());
            now_item_name.setText(n_item.getName());
            Date begin = n_item.getBeginTime();
            Calendar begin_cal = Calendar.getInstance();
            begin_cal.setTime(begin);
            String t = setRecycleString(n_item.getRecycle());
            now_item_recycle.setText(begin_cal.get(Calendar.HOUR_OF_DAY)+"时" +begin_cal.get(Calendar.MINUTE)+"分"+"          "+t);
        }

        private String setRecycleString(String s) {
            String[] select_days = s.split(" ");
            String result = "";
            if(s.equals("everyday")) {
                result = "每天 ";
            } else if(s.equals("1 2 3 4 5 ")) {
                result = "工作日 ";
            } else if(s.equals("6 7 ")) {
                result = "周末 ";
            } else {
                for(int i = 0;  i< select_days.length; i++) {
                    if(select_days[i].equals("1")) {
                        result += "周一 ";
                    } else if(select_days[i].equals("2")) {
                        result += "周二 ";
                    } else if(select_days[i].equals("3")) {
                        result += "周三 ";
                    } else if(select_days[i].equals("4")) {
                        result += "周四 ";
                    } else if(select_days[i].equals("5")) {
                        result += "周五 ";
                    } else if(select_days[i].equals("6")) {
                        result += "周六 ";
                    } else if(select_days[i].equals("7")) {
                        result += "周日 ";
                    }
                }
            }
            return result;
        }

        @Override
        public int getItemCount() {
            return item_list.size();
        }

        class NowViewHolder extends RecyclerView.ViewHolder {

            public NowViewHolder(View itemView) {
                super(itemView);
                now_item_name = (TextView) itemView.findViewById(R.id.now_item_name);
                now_item_recycle = (TextView) itemView.findViewById(R.id.now_item_recycle);
                now_item_icon = (ImageView) itemView.findViewById(R.id.now_item_icon);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edit_pos = getLayoutPosition();
                        RecycleItem temp = item_list.get(edit_pos);
                        Intent intent = new Intent(getActivity(),EditRecycleItemActivity.class);
                        Bundle mbundle = new Bundle();
                        mbundle.putSerializable("edit_recycle_item", temp);
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
//                                            RecycleItem temp = item_list.get(pos);
//                                            dbHelper = new RecycleItemDatabaseHelper(getActivity(),"RecycleItems.db", null ,1);
//                                            dbHelper.getWritableDatabase();
//                                            SQLiteDatabase db = dbHelper.getWritableDatabase();
////                                          db.delete("Item", "name = ? and detail= ? and type = ? and begin_time = ? and end_time = ? and color = ? and notification = ?", new String[]{temp.getName(), temp.getDetail(), String.valueOf(temp.getBegin_time().getTime()),String.valueOf(temp.getEnd_time().getTime()),
////                                                    String.valueOf(temp.getColor()), String.valueOf(temp.getNotification())});
//                                            db.delete("RecycleItem", "name = ?", new String[]{temp.getName()});
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
