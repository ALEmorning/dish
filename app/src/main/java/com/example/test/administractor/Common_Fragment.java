package com.example.test.administractor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.test.R;
import com.example.test.adapter.UniversalAdapter;
import com.example.test.database.CommonDatabase;
import com.example.test.database.sqlite.Caidan;
import com.example.test.database.sqlite.SqliteUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Common_Fragment extends Fragment {
    String listview_state = "";
    SQLiteDatabase db;
    private Toolbar toolbar;
    private ListView listView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.common_toolbar_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guanliyuan_backup_add:
                startActivity(new Intent(getActivity(), add_admin.class));

                break;

        }
        return true;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Toolbar设置
        toolbar = view.findViewById(R.id.toolbar_fragment_common);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        //生成菜单的button
        Button button_make_caidan = view.findViewById(R.id.f_make_menu);

        //查询收藏的Button
        Button button_look_collect = view.findViewById(R.id.f_look_collect);

        //查看留言的button
        Button button_look_message = view.findViewById(R.id.f_query_liuyan);

        //查看管理员信息
        Button button_look_admin = view.findViewById(R.id.look_admin);

        db = new CommonDatabase().getSqliteObject(getActivity(), "test_db");
        listView = view.findViewById(R.id.f_c_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listview_state.equals("collect_set")) {

                } else if (listview_state.equals("make_menu")) {

                } else if (listview_state.equals("message")) {

                } else if (listview_state.equals("admin")) {
                    HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);

                    Intent intent_delete = new Intent(getActivity(), change_account_admin.class);
                    //获取map中的三项数据，并放入intent
                    intent_delete.putExtra("account", map_item.get("account") + "");
                    intent_delete.putExtra("password", map_item.get("password") + "");


                    startActivity(intent_delete);
                }

            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    /******查看收藏情况******///
                    /*
                    case R.id.f_look_sumcourse:
                        listview_state = "collect_set";

                        Cursor cursor_look_course = db.query("collect", null, null, null, null, null, null);
                        ArrayList<Map<String, String>> arrayList_look_course = new ArrayList<Map<String, String>>();
                        //对游标进行遍历
                        while (cursor_look_course.moveToNext()) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("", cursor_look_course.getString(cursor_look_course.getColumnIndex("")));
                            map.put("", cursor_look_course.getString(cursor_look_course.getColumnIndex("")));
                            map.put("", cursor_look_course.getString(cursor_look_course.getColumnIndex("")));
                            map.put("", cursor_look_course.getString(cursor_look_course.getColumnIndex("")));
                            map.put("", cursor_look_course.getString(cursor_look_course.getColumnIndex("")));

                            arrayList_look_course.add(map);

                        }

                        //设置适配器
                        SimpleAdapter simpleAdapter_look_collect = new SimpleAdapter(getActivity(), arrayList_look_collect, R.layout.list_item_all,
                                new String[]{"", "", "","", ""}, new int[]{R.id.text, R.id.text, R.id.text,R.id.text, R.id.text});
                        listView.setAdapter(simpleAdapter_look_collect);

                        break;

                     */
                    case R.id.f_look_collect:
                        Toast.makeText(getActivity(), "此功能后期开放~", Toast.LENGTH_SHORT).show();
                        break;

                    /*******生成菜单*******/
                    case R.id.f_make_menu:
                        listview_state = "make_menu";
                        List<Caidan> caidanList = SqliteUtil.getInstance().generateCaidan(getActivity());
                        Log.d("TAG", "onClick: " + caidanList);
                        UniversalAdapter universalAdapter = new UniversalAdapter(getActivity(), R.layout.menu_item, caidanList);
                        universalAdapter.setInitViewCallBack(new UniversalAdapter.InitViewCallBack() {
                            @Override
                            public void initView(int i, View view, Object object) {
                                Caidan caidan = (Caidan) object;
                                TextView tvPm = (TextView) view.findViewById(R.id.tv_pm);
                                TextView tvHs = (TextView) view.findViewById(R.id.tv_hs);
                                TextView tvCx = (TextView) view.findViewById(R.id.tv_cx);
                                TextView tvYycf = (TextView) view.findViewById(R.id.tv_yycf);
                                tvPm.setText(caidan.get名称());
                                tvHs.setText(caidan.get荤素());
                                tvCx.setText(caidan.get菜系());
                                //输出对应品名的营养成分

                                tvYycf.setText(caidan.get可做());
                            }
                        });

                        listView.setAdapter(universalAdapter);
                        break;


                    /****查看留言*******/
                    case R.id.f_query_liuyan:
                        listview_state = "message";
                        Cursor cursor_look_message = db.rawQuery("select * from  message inner join user on user.id = message.id ", new String[]{});
                        if (cursor_look_message.getCount() == 0) {
                            Toast.makeText(getActivity(), "还没有任何留言哦~", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<Map<String, String>> arrayList_look_message = new ArrayList<Map<String, String>>();
                            //对游标进行遍历
                            while (cursor_look_message.moveToNext()) {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("id", cursor_look_message.getString(cursor_look_message.getColumnIndex("id")));

                                map.put("message", cursor_look_message.getString(cursor_look_message.getColumnIndex("message")));

                                arrayList_look_message.add(map);

                            }

                            //设置适配器
                            SimpleAdapter simpleAdapter_look_message = new SimpleAdapter(getActivity(), arrayList_look_message, R.layout.list_item_message,
                                    new String[]{"id", "message"}, new int[]{R.id.message_id, R.id.message_content});
                            listView.setAdapter(simpleAdapter_look_message);

                        }

                        break;

                    case R.id.look_admin:
                        listview_state = "admin";
                        Cursor cursor = db.query("administractor", null, null, null, null, null, null);
                        ArrayList<Map<String, String>> arrayList_look_admin = new ArrayList<Map<String, String>>();
                        while (cursor.moveToNext()) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("account", cursor.getString(cursor.getColumnIndex("account")));
                            map.put("password", cursor.getString(cursor.getColumnIndex("password")));
                            arrayList_look_admin.add(map);

                        }
                        SimpleAdapter simpleAdapter_look_admin = new SimpleAdapter(getActivity(), arrayList_look_admin, R.layout.list_item_account,
                                new String[]{"account", "password"}, new int[]{R.id.account_t, R.id.account_tv});
                        listView.setAdapter(simpleAdapter_look_admin);
                        break;
                    default:
                        break;

                }
            }


        };



        button_look_message.setOnClickListener(listener);
        button_look_admin.setOnClickListener(listener);
        button_make_caidan.setOnClickListener(listener);
        button_look_collect.setOnClickListener(listener);
    }

}
