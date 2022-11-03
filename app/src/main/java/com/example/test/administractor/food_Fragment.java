package com.example.test.administractor;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.test.R;
import com.example.test.database.CommonDatabase;
import com.example.test.utils.Common_toolbarColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class food_Fragment extends Fragment {
    private SQLiteDatabase db;
    private EditText edit_querybyid;
    private EditText edit_querybyname;
    private EditText edit_querybyleibie;
    //记录listview显示状态，方便设置触发器
    private String listview_state = "";
    private ListView listView;
    private Toolbar toolbar;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.food_toolbar_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.food_backup_qingchu:
                listView.setAdapter(null);
                break;

            case R.id.food_backup_add:
                Intent intent_add1 = new Intent(getActivity(), add.class);
                startActivity(intent_add1);
                break;
                //-----------------------------

        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_fragment, container,false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Toolbar设置
        toolbar = view.findViewById(R.id.toolbar_fragment_food);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        //状态栏颜色设置
        new Common_toolbarColor().toolbarColorSet(getActivity());

        //查询所有信息的按钮
        Button button_query = view.findViewById(R.id.f_query);

        //查询用户登录账号密码的按钮
        Button button_query_account = view.findViewById(R.id.f_query_account);
        //按照条件查询的按钮
        Button button_query_byterm = view.findViewById(R.id.f_query_byterm);

        //按照条件查询的编辑框
        edit_querybyid = view.findViewById(R.id.f_edit_querybyid);
        edit_querybyname = view.findViewById(R.id.f_edit_querybyname);
        edit_querybyleibie = view.findViewById(R.id.f_edit_querybyleibie);


        /*****建立数据库，并建表*******/
        CommonDatabase commonDatabase = new CommonDatabase();
        db = commonDatabase.getSqliteObject(getActivity(), "test_db");
        /***********listview设置******************/
        listView = view.findViewById(R.id.f_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listview_state.equals("food_byterm")||listview_state.equals("food_all")) {
                    //Map <String,Object> map= new HashMap<String,Object>();
                    HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);

                    Intent intent_delete = new Intent(getActivity(), delete_change.class);
                    //获取map中的三项数据，并放入intent
                    intent_delete.putExtra("id", map_item.get("id") + " ");
                    intent_delete.putExtra("品名", map_item.get("品名") + " ");
                    intent_delete.putExtra("产品类别", map_item.get("产品类别") + " ");
                    intent_delete.putExtra("食品生产许可证号", map_item.get("食品生产许可证号") + " ");
                    intent_delete.putExtra("产品标准号", map_item.get("产品标准号") + " ");
                    intent_delete.putExtra("保质期", map_item.get("保质期") + " ");
                    intent_delete.putExtra("贮存条件", map_item.get("贮存条件") + " ");
                    intent_delete.putExtra("制造商", map_item.get("制造商") + " ");
                    intent_delete.putExtra("地址", map_item.get("地址") + " ");
                    intent_delete.putExtra("产地", map_item.get("产地") + " ");
                    intent_delete.putExtra("营养成分", map_item.get("营养成分") + " ");
                    intent_delete.putExtra("净含量", map_item.get("净含量") + " ");
                    intent_delete.putExtra("库存", map_item.get("库存") + " ");
                    startActivity(intent_delete);
                } else if (listview_state.equals("account")) {
                    HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);

                    Intent intent = new Intent(getActivity(), change_account.class);
                    intent.putExtra("account ", map_item.get("account") + " ");
                    intent.putExtra("password ", map_item.get("password") + " ");
                    startActivity(intent);
                }
            }
        });



        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*****设置按钮点击监听器******/
                switch (v.getId()) {

                    case R.id.f_query_byterm:
                        listview_state = "food_byterm";
                        listview_set(listview_state);
                        break;


                    /***************************************查询所有食材信息*********************************/
                    case R.id.f_query:
                        //查询的是食材表,所以
                        listview_state = "food_all";
                        listview_set(listview_state);
                        break;


                    /******查询所有用户账号密码*******/
                    case R.id.f_query_account:
                        //查询的是账户，所以设置listview为查询账户状态
                        listview_state = "account";
                        listview_set(listview_state);
                        break;


                    default:

                        break;

                }

            }


        };


        button_query.setOnClickListener(listener);

        //查询用户登录账号密码的按钮
        button_query_account.setOnClickListener(listener);
        //按照ID查询的按钮
        button_query_byterm.setOnClickListener(listener);

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.user_swipe_refresh);
        //进度条刷新旋钮的颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        //设置下拉刷新的监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // refresh ui 的操作代码
                                listview_set(listview_state);

                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    @SuppressLint("Range")
    public void query_byitem(Cursor cursor_query_byid, ListView listView) {
        ArrayList<Map<String, String>> arrayList_query = new ArrayList<Map<String, String>>();
        if (cursor_query_byid.getCount() == 0) {
            listView.setAdapter(null);
            Toast.makeText(getActivity(), "没有查到任何结果", Toast.LENGTH_SHORT).show();
        }
        //对游标进行遍历
        else {
            while (cursor_query_byid.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();


                map.put("id", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("id")));
                map.put("品名", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("品名")));
                map.put("产品类别", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("产品类别")));
                map.put("食品生产许可证号", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("食品生产许可证号")));
                map.put("产品标准号", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("产品标准号")));
                map.put("保质期", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("保质期")));
                map.put("贮存条件", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("贮存条件")));
                map.put("制造商", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("制造商")));
                map.put("地址", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("地址")));
                map.put("产地", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("产地")));
                map.put("营养成分", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("营养成分")));
                map.put("净含量", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("净含量")));
                map.put("库存", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("库存")));

                arrayList_query.add(map);

            }
            //设置适配器
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), arrayList_query, R.layout.list_item,
                    new String[]{"id", "品名","产品类别","食品生产许可证号","产品标准号","保质期",
                            "贮存条件", "制造商","地址","产地", "营养成分" ,"净含量","库存"}, new int[]{R.id.iid, R.id.iname, R.id.ileibie,
                            R.id.ilicense, R.id.istandard, R.id.idate, R.id.istore, R.id.imake, R.id.iaddress, R.id.ifrom,
                            R.id.inutri, R.id.icontent, R.id.istock});
            listView.setAdapter(simpleAdapter);
        }

    }
    @SuppressLint("Range")
    public void listview_set(String state)
    {
        if(state.equals("food_all"))
        {
            Cursor cursor = db.query("Food", null, null, null, null, null, null);
            ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
            //对游标进行遍历
            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();

                map.put("id", cursor.getString(cursor.getColumnIndex("id")));
                map.put("品名", cursor.getString(cursor.getColumnIndex("品名")));
                map.put("产品类别", cursor.getString(cursor.getColumnIndex("产品类别")));
                map.put("食品生产许可证号", cursor.getString(cursor.getColumnIndex("食品生产许可证号")));
                map.put("产品标准号", cursor.getString(cursor.getColumnIndex("产品标准号")));
                map.put("保质期", cursor.getString(cursor.getColumnIndex("保质期")));
                map.put("贮存条件", cursor.getString(cursor.getColumnIndex("贮存条件")));
                map.put("制造商", cursor.getString(cursor.getColumnIndex("制造商")));
                map.put("地址", cursor.getString(cursor.getColumnIndex("地址")));
                map.put("产地", cursor.getString(cursor.getColumnIndex("产地")));
                map.put("营养成分", cursor.getString(cursor.getColumnIndex("营养成分")));
                map.put("净含量", cursor.getString(cursor.getColumnIndex("净含量")));
                map.put("库存", cursor.getString(cursor.getColumnIndex("库存")));



                arrayList.add(map);

            }
            //设置适配器
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), arrayList, R.layout.list_item,
                    new String[]{"id", "品名","产品类别","食品生产许可证号","产品标准号","保质期",
                            "贮存条件", "制造商","地址","产地", "营养成分" ,"净含量","库存"}, new int[]{R.id.iid, R.id.iname, R.id.ileibie,
                    R.id.ilicense, R.id.istandard, R.id.idate, R.id.istore, R.id.imake, R.id.iaddress, R.id.ifrom,
                    R.id.inutri, R.id.icontent, R.id.istock});
            listView.setAdapter(simpleAdapter);
        }
        else if(state.equals("food_byterm"))
        {
            final String e_id = edit_querybyid.getText().toString();
            final String e_name = edit_querybyname.getText().toString();
            final String e_leibie = edit_querybyleibie.getText().toString();
            /****按照id查询****/
            if (!e_id.equals("") && e_name.equals("") && e_leibie.equals("")) {

                Cursor cursor_query_byid = db.query("Food", null, "ID LIKE?", new String[]{"%"+e_id+"%"}, null, null, null);
                query_byitem(cursor_query_byid,listView);

            }
            /****按照品名查询****/
            else if (e_id.equals("") && !e_name.equals("") && e_leibie.equals("")) {

                //Cursor cursor_query_byid = db.query("user", null, "name = ?", new String[]{e_name}, null, null, null);
                Cursor cursor_query_byid = db.rawQuery("select * from Food where 品名 LIKE ?",new String[]{"%"+e_name+"%"});
                query_byitem(cursor_query_byid,listView);


            }
            /****按照类别查询****/
            else if(e_id.equals("") && e_name.equals("") && !e_leibie.equals(""))
            {

                Cursor cursor_query_byid = db.query("Food", null, "产品类别 LIKE ?", new String[]{"%"+e_leibie+"%"}, null, null, null);
                query_byitem(cursor_query_byid,listView);
            }
            /****按照id+name查询****/
            else if(!e_id.equals("") && !e_name.equals("") && e_leibie.equals(""))
            {
                Cursor cursor_query_byid = db.query("Food", null, "ID LIKE ? AND 品名 LIKE ?" , new String[]{"%"+e_id+"%","%"+e_name+"%"}, null, null, null);
                query_byitem(cursor_query_byid,listView);
            }
            /****按照id+类别查询****/
            else if(!e_id.equals("") && e_name.equals("") && !e_leibie.equals(""))
            {
                Cursor cursor_query_byid = db.query("Food", null, "ID LIKE ? AND  产品类别 LIKE ?", new String[]{"%"+e_id+"%","%"+e_leibie+"%"}, null, null, null);
                query_byitem(cursor_query_byid,listView);
            }
            /****按照name+类别查询****/
            else if(e_id.equals("") && !e_name.equals("") && !e_leibie.equals(""))
            {
                Cursor cursor_query_byid = db.query("Food", null, "品名 LIKE ? AND 产品类别 LIKE ?", new String[]{"%"+e_name+"%" ,"%"+e_leibie+"%"}, null, null, null);
                query_byitem(cursor_query_byid,listView);
            }
            else if(e_id.equals("") && e_name.equals("") && e_leibie.equals(""))
            {
                Toast.makeText(getActivity(), "您的查询条件为空!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(state.equals("account"))
        {
            Cursor cursor_account = db.query("load_account", null, null, null, null, null, null);
            ArrayList<Map<String, String>> arrayList_account = new ArrayList<Map<String, String>>();
            //对游标进行遍历
            while (cursor_account.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("account", cursor_account.getString(cursor_account.getColumnIndex("account")));
                map.put("password", cursor_account.getString(cursor_account.getColumnIndex("password")));
                arrayList_account.add(map);

            }

            //设置适配器
            SimpleAdapter simpleAdapter_account = new SimpleAdapter(getActivity(), arrayList_account, R.layout.list_item_account,
                    new String[]{"account", "password"}, new int[]{R.id.account_t, R.id.account_tv});
            listView.setAdapter(simpleAdapter_account);
        }

    }


}

