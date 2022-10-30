package com.example.test.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.example.test.R;
import com.example.test.database.CommonDatabase;
import com.example.test.database.image_store;
import com.example.test.utils.Common_toolbarColor;
import com.example.test.utils.alertDialog_builder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class activity_user extends AppCompatActivity {
    private SQLiteDatabase db;

    private EditText edit_querybycaixi;
    private EditText edit_querybyname;
    private EditText edit_querybynutri;
    //记录listview显示状态，方便设置触发器
    private String listview_state = "";
    private ListView listView;

    //切换用户弹出的对话框
    private AlertDialog.Builder builder;

    //Toolbar用于替代原有的actionBar
    private Toolbar toolbar;

    //用于显示用户收藏信息的listview
    private ListView listView_mycollect;

    //侧滑
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    //用于获取NavigationView的headlayout，方便监听子项
    private View headview;

    //headlayout中的textview
    private TextView textView_welcome;

    //headlayout中circleimage
    private CircleImageView circleImageView;

    private static final int TAKE_PHOTO = 1;

    private image_store imageStore;

    private Intent intent_1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        initView();

        //获取登录信息，以锁定用户
        intent_1 = getIntent();

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            //设置左箭头图片
            actionBar.setHomeAsUpIndicator(R.drawable.a);

        }

        //headlayout中的欢迎实现
        textView_welcome.setText(findNameById(intent_1.getStringExtra("id")));


        //菜单栏实现
        navigationView.setCheckedItem(R.id.nav_menu_myinfo);
        navigationView.setCheckedItem(R.id.nav_menu_changeacc);



        //设置标题栏与状态栏颜色保持一致
        new Common_toolbarColor().toolbarColorSet(activity_user.this);

        //头像初始化
        Bitmap bitmap_temp = imageStore.getBmp(db, intent_1.getStringExtra("id"));

        if (bitmap_temp != null) {
            circleImageView.setImageBitmap(bitmap_temp);
        }

        edit_querybyname = findViewById(R.id.f_edit_querybyname);
        edit_querybycaixi = findViewById(R.id.f_edit_querybycaixi);
        edit_querybynutri = findViewById(R.id.f_edit_querybynutri);

        Button querycaidan = findViewById(R.id.f_query_caidan);
        Button queryitem = findViewById(R.id.f_select_caidan);
        Button collect = findViewById(R.id.f_collect_menu);



        //NavigationView的菜单项监听器
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_menu_myinfo:
                        Intent intent_about = new Intent(activity_user.this, about_me.class);
                        intent_about.putExtra("id", intent_1.getStringExtra("id"));
                        startActivity(intent_about);

                        break;
                    case R.id.nav_menu_changeacc:
                        builder = new alertDialog_builder(activity_user.this).build();
                        //   显示出该对话框
                        builder.show();

                        break;
                    //留言
                    case R.id.nav_menu_liuyan:
                        Intent intent_submit = new Intent(activity_user.this, submit_message.class);
                        intent_submit.putExtra("id", intent_1.getStringExtra("id"));
                        startActivity(intent_submit);
                        break;

                    //查看结果
                    case R.id.nav_menu_look_hcollect:
                        /*

                        //两表连接查询
                        Cursor cursor = db.rawQuery(
                                "select * from user_collect inner join Menu " +
                                        "on user_collect.menu_name =Menu.名称 " +
                                        "AND user_collect.menu_caixi = Menu.菜系  " +
                                        "where id = ?", new String[]{intent_1.getStringExtra("id")});
                        ArrayList<Map<String, String>> arrayList_1 = new ArrayList<Map<String, String>>();
                        if (cursor.getCount() == 0) {
                            Toast.makeText(activity_user.this, "您还没有收藏任何菜品！", Toast.LENGTH_SHORT).show();
                        } else {
                            while (cursor.moveToNext()) {
                                Map<String, String> map = new HashMap<String, String>();

                                //map.put("", cursor.getString(cursor.getColumnIndex("")));

                                arrayList_1.add(map);

                            }/*
                            //设置适配器，并绑定布局文件
                            SimpleAdapter simpleAdapter = new SimpleAdapter(activity_user.this, arrayList_1, R.layout.choose_result,
                                    new String[]{"course_name", "teacher_name", "course_time", "course_weight", "course_period"}, new int[]{R.id.result_course_name, R.id.result_teacher_name, R.id.result_time, R.id.result_weight, R.id.result_period});
                            listView_mycourse.setAdapter(simpleAdapter);


                        }

                         */

                        break;

                    default:
                        break;
                }
                return true;
            }
        });


        //为listview设定监听器
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    //进行菜单筛选
                    case R.id.f_select_caidan:

                        break;


                    //查看所以菜单信息
                    case R.id.f_query_caidan:


                        break;
                    //收藏菜谱按钮绑定
                    case R.id.f_collect_menu:
                        //再将从登陆界面接受的ID传给收藏活动
                        Intent intent_2 = new Intent(activity_user.this, collect.class);
                        intent_2.putExtra("id", intent_1.getStringExtra("id"));
                        startActivity(intent_2);

                        break;

                    default:
                        break;
                }
            }
        };

        querycaidan.setOnClickListener(listener);
        queryitem.setOnClickListener(listener);
        collect.setOnClickListener(listener);



        circleImageView.setOnClickListener(listener);


    }


    private void initView() {

        //获取数据库对象
        db = new CommonDatabase().getSqliteObject(activity_user.this, "test_db");

        listView_mycollect = findViewById(R.id.listview_mycollect);

        toolbar = findViewById(R.id.toolbar_user);

        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerlayout_user);

        navigationView = findViewById(R.id.navigation_view);

        headview = navigationView.inflateHeaderView(R.layout.headlayout);


        textView_welcome = headview.findViewById(R.id.welcome_textview);


        circleImageView = headview.findViewById(R.id.circleimage);


        imageStore = new image_store();

    }

    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);

                break;
            default:
                break;
        }
        return true;
    }



    //根据用户的ID去查找姓名
    @SuppressLint("Range")
    public String findNameById(String id) {
        Cursor cursor = db.query("user", null, "id = ?", new String[]{id}, null, null, null, null);

        //如果没查到
        if (cursor.getCount() == 0) {
            return "无法获取您的个人信息";
        } else {
            String str = "";
            while (cursor.moveToNext()) {
                str = cursor.getString(cursor.getColumnIndex("name"));
            }
            return str + " 欢迎您！";
        }

    }

}