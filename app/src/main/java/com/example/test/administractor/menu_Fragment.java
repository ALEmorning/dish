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

import com.example.test.R;
import com.example.test.database.CommonDatabase;
import com.example.test.utils.Common_toolbarColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class menu_Fragment extends Fragment {
    SQLiteDatabase db;
    EditText edit_querybyid;
    private Toolbar toolbar;
    private ListView listView;

    String listview_state ="";


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_menu,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_backup_qingchu:
                listView.setAdapter(null);

                break;
            case R.id.menu_backup_add:
                Intent intent_add_menu = new Intent(getActivity(), add_menu.class);
                startActivity(intent_add_menu);
                break;
                //排序功能接口

        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment,null);
        return view;

    }
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Toolbar设置
        toolbar = view.findViewById(R.id.toolbar_fragment_menu);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        //
        setHasOptionsMenu(true);

        //状态栏颜色设置
        new Common_toolbarColor().toolbarColorSet(getActivity());





        //查询菜谱信息的button
        Button button_query_menu = view.findViewById(R.id.f_t_query_menu);
        //按照条件查询菜谱信息
        Button button_query_menu_byitem = view.findViewById(R.id.button_query_menubyitem);



        final EditText et_menuid = view.findViewById(R.id.f_edit_query_menu_byid);
        final EditText et_menuname= view.findViewById(R.id.f_edit_query_menu_byname);










        View.OnClickListener listener = new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                /*****建立数据库，并建表*******/
                CommonDatabase commonDatabase= new CommonDatabase();
                db=commonDatabase.getSqliteObject(getActivity(),"test_db");
                /******列表视图设置********/
                listView =view.findViewById(R.id.f_t_listview);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //
                         if(listview_state.equals("menu"))
                        {
                            HashMap<String,Object > map_item = (HashMap<String,Object >)listView.getItemAtPosition(position);

                            Intent intent_delete = new Intent(getActivity(), delete_change_menu.class);
                            //获取map中的三项数据，并放入intent
                            intent_delete.putExtra("id",map_item.get("id")+" ");
                            intent_delete.putExtra("名称",map_item.get("名称")+" ");
                            intent_delete.putExtra("主料",map_item.get("主料")+" ");
                            intent_delete.putExtra("主料含量",map_item.get("主料含量")+" ");
                            intent_delete.putExtra("配料",map_item.get("配料")+" ");
                            intent_delete.putExtra("配料含量",map_item.get("配料含量")+" ");
                            intent_delete.putExtra("做法步骤",map_item.get("做法步骤")+" ");
                            intent_delete.putExtra("菜系",map_item.get("菜系")+" ");
                            intent_delete.putExtra("荤素",map_item.get("荤素")+" ");
                            intent_delete.putExtra("可做",map_item.get("可做")+" ");
                            intent_delete.putExtra("价格",map_item.get("价格")+" ");
                            startActivity(intent_delete);

                        }

                    }
                });

                /*****设置按钮点击监听器******/
                switch(v.getId()){

                    /*****查询信息****/
                    case R.id.f_t_query_menu:
                        //设置listview状态
                        listview_state="menu";

                        Cursor cursor_menu = db.query("Menu", null, null, null, null, null, null);
                        ArrayList<Map<String,String>> arrayList_menu= new ArrayList<Map<String,String>>();
                        //对游标进行遍历
                        while(cursor_menu.moveToNext())
                        {

                            Map <String,String> map= new HashMap<String,String>();

                            map.put("id",cursor_menu.getString(cursor_menu.getColumnIndex("id")));
                            map.put("名称",cursor_menu.getString(cursor_menu.getColumnIndex("名称")));
                            map.put("主料",cursor_menu.getString(cursor_menu.getColumnIndex("主料")));
                            map.put("主料含量",cursor_menu.getString(cursor_menu.getColumnIndex("主料含量")));
                            map.put("配料",cursor_menu.getString(cursor_menu.getColumnIndex("配料")));
                            map.put("配料含量",cursor_menu.getString(cursor_menu.getColumnIndex("配料含量")));
                            map.put("做法步骤",cursor_menu.getString(cursor_menu.getColumnIndex("做法步骤")));
                            map.put("菜系",cursor_menu.getString(cursor_menu.getColumnIndex("菜系")));
                            map.put("荤素",cursor_menu.getString(cursor_menu.getColumnIndex("荤素")));
                            map.put("可做",cursor_menu.getString(cursor_menu.getColumnIndex("可做")));
                            map.put("价格",cursor_menu.getString(cursor_menu.getColumnIndex("价格")));

                            arrayList_menu.add(map);

                        }
                        //设置适配器
                        SimpleAdapter simpleAdapter_menu=new SimpleAdapter(getActivity(),arrayList_menu,R.layout.list__item_menu,
                                new String[]{"id","名称","主料","主料含量","配料","配料含量","做法步骤","菜系","荤素","可做","价格"},
                                new int[]{R.id.list_m_id,R.id.list_m_name,R.id.list_m_zhuliao,R.id.list_m_zhuhan,R.id.list_m_peiliao, R.id.list_m_peihan,R.id.list_m_make,
                                        R.id.list_m_caixi,R.id.list_m_hunsu, R.id.list_m_kezuo, R.id.list_m_price});
                        listView.setAdapter(simpleAdapter_menu);
                        break;


                    case R.id.button_query_menubyitem:
                        listview_state = "query";

                        final String e_id = et_menuid.getText().toString();
                        final String e_name = et_menuname.getText().toString();

                        /****按照菜谱id查询****/
                        if (!e_id.equals("") && e_name.equals("") ) {

                            Cursor cursor_query_byid = db.query("Menu", null, "id Like ?", new String[]{"%"+e_id+"%"}, null, null, null);
                            query_byitem(cursor_query_byid,listView);

                        }
                        /****按照名称查询****/
                        else if (e_id.equals("") && !e_name.equals("") ) {

                            Cursor cursor_query_byid = db.query("Menu", null, " 名称 Like ?", new String[]{"%"+e_name+"%"}, null, null, null);
                            query_byitem(cursor_query_byid,listView);


                        }

                        /****按照id+name查询****/
                        else if(!e_id.equals("") && !e_name.equals("") )
                        {
                            Cursor cursor_query_byid = db.query("Menu", null, "id Like ? AND 名称 Like ?" , new String[]{"%"+e_id+"%","%"+e_name+"%"}, null, null, null);
                            query_byitem(cursor_query_byid,listView);
                        }


                        else if(e_id.equals("") && e_name.equals("") )
                        {
                            Toast.makeText(getActivity(), "您的查询条件为空!", Toast.LENGTH_SHORT).show();
                        }




                    default:

                        break;

                }

            }


        };

        button_query_menu.setOnClickListener(listener);

        button_query_menu_byitem.setOnClickListener(listener);

    }
    @SuppressLint("Range")
    public void query_byitem(Cursor cursor_menu, ListView listView) {
        ArrayList<Map<String, String>> arrayList_query = new ArrayList<Map<String, String>>();
        if (cursor_menu.getCount() == 0) {
            listView.setAdapter(null);
            Toast.makeText(getActivity(), "没有查到任何结果", Toast.LENGTH_SHORT).show();
        }
        //对游标进行遍历
        else {
            while (cursor_menu.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();

                map.put("id",cursor_menu.getString(cursor_menu.getColumnIndex("id")));
                map.put("名称",cursor_menu.getString(cursor_menu.getColumnIndex("名称")));
                map.put("主料",cursor_menu.getString(cursor_menu.getColumnIndex("主料")));
                map.put("主料含量",cursor_menu.getString(cursor_menu.getColumnIndex("主料含量")));
                map.put("配料",cursor_menu.getString(cursor_menu.getColumnIndex("配料")));
                map.put("配料含量",cursor_menu.getString(cursor_menu.getColumnIndex("配料含量")));
                map.put("做法步骤",cursor_menu.getString(cursor_menu.getColumnIndex("做法步骤")));
                map.put("菜系",cursor_menu.getString(cursor_menu.getColumnIndex("菜系")));
                map.put("荤素",cursor_menu.getString(cursor_menu.getColumnIndex("荤素")));
                map.put("可做",cursor_menu.getString(cursor_menu.getColumnIndex("可做")));
                map.put("价格",cursor_menu.getString(cursor_menu.getColumnIndex("价格")));
                arrayList_query.add(map);

            }
            //设置适配器
            SimpleAdapter simpleAdapter_menu=new SimpleAdapter(getActivity(),arrayList_query,R.layout.list__item_menu,
                    new String[]{"id","名称","主料","主料含量","配料","配料含量","做法步骤","菜系","荤素","可做","价格"},
                    new int[]{R.id.list_m_id,R.id.list_m_name,R.id.list_m_zhuliao,R.id.list_m_zhuhan,R.id.list_m_peiliao, R.id.list_m_peihan,R.id.list_m_make,
                            R.id.list_m_caixi,R.id.list_m_hunsu, R.id.list_m_kezuo, R.id.list_m_price});
            listView.setAdapter(simpleAdapter_menu);
        }
    }
}
