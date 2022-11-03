package com.example.test.user;

import android.annotation.SuppressLint;
import android.content.ContentValues;
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
import com.example.test.database.DBOpenHelper;
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
    private EditText edit_queryhunsu;

    //记录listview显示状态，方便设置触发器
    private String listview_state = "";
    private ListView listView;

    private String data1="";//全部可做菜谱
    private String data2="";//按需求生成的菜谱（最终菜谱）
    private String data3="";//更新菜谱的中间字符
    private String data4="";//点完的菜
    private int money=0;//账单

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
    private TextView text;

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


        db = new CommonDatabase().getSqliteObject(activity_user.this, "test_db");
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
        edit_queryhunsu = findViewById(R.id.f_edit_queryhunsu);

        text=(TextView)findViewById(R.id.text);//用户看到的菜单
        Button querycaidan = findViewById(R.id.f_query_caidan);
        Button queryitem = findViewById(R.id.f_select_caidan);
        Button collect = findViewById(R.id.f_collect_menu);
        Button jiesuan = findViewById(R.id.jiesuan);



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


                        break;

                    default:
                        break;
                }
                return true;
            }
        });


        //为listview设定监听器
        View.OnClickListener listener = new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    //根据条件筛选菜单
                    case R.id.f_select_caidan:

                        Updata();
                        //查询表中所有数据
                        Cursor cursor1 = db.query("Food",null,null,null,null,null,null);
                        Cursor cursor2 = db.query("caidan",null,null,null,null,null,null);
                        data2="筛选菜单：\n";
                        int message1=0,message2=0,message3=0;//判断是否为空
                        //如果为空，对应参数设为1
                        if(edit_queryhunsu.getText().toString().isEmpty())    message1=1;
                        if(edit_querybynutri.getText().toString().isEmpty())     message2=1;
                        if(edit_querybycaixi.getText().toString().isEmpty())        message3=1;
                        data2+="名称        荤素   菜系   营养成分\n";
                        if (cursor2.moveToFirst()){
                            do {
                                //若无输入，则直接查找，若有输入，则根据条件查找，且必须都满足状态为可做
                                if("是".equals(cursor2.getString(cursor2.getColumnIndex("可做")))&&
                                        (message1==1||edit_queryhunsu.getText().toString().equals(GetHunsu(cursor2.getString(cursor2.getColumnIndex("名称")))))&&
                                        (message3==1||edit_querybycaixi.getText().toString().equals(GetCaixi(cursor2.getString(cursor2.getColumnIndex("名称"))))))
                                    if(message2==0)//输入了营养成分需求
                                    {
                                        if(cursor1.moveToFirst()){
                                            do{
                                                if(cursor1.getString(cursor1.getColumnIndex("品名")).equals(cursor2.getString(cursor2.getColumnIndex("主料")))&&
                                                        edit_querybynutri.getText().toString().equals(cursor1.getString(cursor1.getColumnIndex("营养成分"))))
                                                {
                                                    String name = cursor2.getString(cursor2.getColumnIndex("名称"));
                                                    //String caixi = cursor.getString(cursor.getColumnIndex("菜系"));
                                                    String caixi = GetCaixi(cursor2.getString(cursor2.getColumnIndex("名称")));
                                                    //String hunsu = cursor.getString(cursor.getColumnIndex("荤素"));
                                                    String hunsu = GetHunsu(cursor2.getString(cursor2.getColumnIndex("名称")));
                                                    String yingyang=GetYingyang(cursor2.getString(cursor2.getColumnIndex("主料")));
                                                    data2+=name+"    "+hunsu+"    "+caixi+"       "+yingyang+"    "+"\n";
                                                }
                                            }while (cursor1.moveToNext());
                                        }
                                    }
                                    else
                                    {
                                        String name = cursor2.getString(cursor2.getColumnIndex("名称"));
                                        //String caixi = cursor.getString(cursor.getColumnIndex("菜系"));
                                        String caixi = GetCaixi(cursor2.getString(cursor2.getColumnIndex("名称")));
                                        //String hunsu = cursor.getString(cursor.getColumnIndex("荤素"));
                                        String hunsu = GetHunsu(cursor2.getString(cursor2.getColumnIndex("名称")));
                                        String yingyang=GetYingyang(cursor2.getString(cursor2.getColumnIndex("主料")));
                                        data2+=name+"    "+hunsu+"    "+caixi+"       "+yingyang+"    "+"\n";
                                    }
                            }while (cursor2.moveToNext());
                        }
                        cursor1.close();
                        cursor2.close();
                        text.setText(data2);

                        break;


                    //查看所有可点菜单信息
                    case R.id.f_query_caidan:

                        Cursor cursor = db.query("caidan",null,null,null,null,null,null);
                        data1="菜单：\n";
                        data1+="名称        荤素   菜系   营养成分\n";
                        Updata();
                        if (cursor.moveToFirst()){
                            do {
                                if("是".equals(cursor.getString(cursor.getColumnIndex("可做"))))
                                {
                                    String name = cursor.getString(cursor.getColumnIndex("名称"));
                                    //String caixi = cursor.getString(cursor.getColumnIndex("菜系"));
                                    String caixi = GetCaixi(cursor.getString(cursor.getColumnIndex("名称")));
                                    //String hunsu = cursor.getString(cursor.getColumnIndex("荤素"));
                                    String hunsu = GetHunsu(cursor.getString(cursor.getColumnIndex("名称")));
                                    String yingyang=GetYingyang(cursor.getString(cursor.getColumnIndex("主料")));
                                    data1+=name+"    "+hunsu+"    "+caixi+"       "+yingyang+"    "+"\n";
                                }
                            }while (cursor.moveToNext());
                        }
                        cursor.close();
                        text.setText(data1);

                        break;
                    //点菜谱按钮绑定
                    case R.id.f_collect_menu:
                        //再将从登陆界面接受的ID传给收藏活动
//                        Intent intent_2 = new Intent(activity_user.this, collect.class);
//                        intent_2.putExtra("id", intent_1.getStringExtra("id"));
//                        startActivity(intent_2);

                        Updata();
                        Cursor cursor3=db.query("Food",null,null,null,null,null,null);
                        Cursor cursor4=db.query("caidan",null,null,null,null,null,null);
                        double content3; //主料含量
                        double content4; //辅料含量
                        if (cursor4.moveToFirst()){
                            do {
                                content3=cursor4.getDouble(cursor4.getColumnIndex("主料含量"));
                                content4=cursor4.getDouble(cursor4.getColumnIndex("配料含量"));
                                //如果可做，对应食材库存相应减少
                                if(edit_querybyname.getText().toString().equals(cursor4.getString(cursor4.getColumnIndex("名称")))
                                        &&"是".equals(cursor4.getString(cursor4.getColumnIndex("可做"))))
                                {
                                    if(cursor3.moveToFirst()) {
                                        do {
                                            if (cursor3.getString(cursor3.getColumnIndex("品名")).
                                                    equals(cursor4.getString(cursor4.getColumnIndex("主料")))
                                                    &&cursor3.getDouble(cursor3.getColumnIndex("库存"))>=content3)
                                            {
                                                ContentValues values = new ContentValues();
                                                values.put("库存",cursor3.getDouble(cursor3.getColumnIndex("库存"))-content3);
                                                db.update("Food",values,"品名 = ?",new String[]
                                                        {cursor3.getString(cursor3.getColumnIndex("品名"))});
                                                values.clear();
                                            }
                                            if (cursor3.getString(cursor3.getColumnIndex("品名")).equals(cursor4.getString(cursor4.getColumnIndex
                                                    ("配料")))&&cursor3.getDouble(cursor3.getColumnIndex("库存"))>=content4)
                                            {
                                                ContentValues values = new ContentValues();
                                                values.put("库存",cursor3.getDouble(cursor3.getColumnIndex("库存"))-content4);
                                                db.update("Food",values,"品名 = ?",new String[]
                                                        {cursor3.getString(cursor3.getColumnIndex("品名"))});
                                                values.clear();
                                            }
                                        } while (cursor3.moveToNext());
                                    }
                                    data4+=cursor4.getString(cursor4.getColumnIndex("名称"))+"\n";
                                    money+=GetJiage(cursor4.getString(cursor4.getColumnIndex("名称")));
                                    Toast.makeText(activity_user.this,"点单成功！",Toast.LENGTH_SHORT).show();
                                    Updata();
                                }
                                //如果不可做，输出提示
                                else if(edit_querybyname.getText().toString().equals(cursor4.getString(cursor4.getColumnIndex("名称")))
                                        &&"否".equals(cursor4.getString(cursor4.getColumnIndex("可做")))){
                                    Toast.makeText(activity_user.this,"库存不足！",Toast.LENGTH_SHORT).show();
                                }
                            }while (cursor4.moveToNext());
                        }
                        cursor3.close();
                        cursor4.close();
                        break;

                    default:
                        break;
                }
            }
        };

        querycaidan.setOnClickListener(listener);
        queryitem.setOnClickListener(listener);
        collect.setOnClickListener(listener);
        jiesuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data4+="以上是已点菜单，最终价格为："+money;
                text.setText(data4);
                data4="";
                money=0;
            }
        });

        circleImageView.setOnClickListener(listener);


    }

    @SuppressLint("Range")
    public void Updata(){
        Cursor cursor1=db.query("Food",null,null,null,null,null,null);
        Cursor cursor2=db.query("caidan",null,null,null,null,null,null);
        double content1; //主料含量
        double content2; //配料含量
        int i=0;//计数变量
        if (cursor2.moveToFirst()){
            do {
                content1=cursor2.getDouble(cursor2.getColumnIndex("主料含量"));
                content2=cursor2.getDouble(cursor2.getColumnIndex("配料含量"));
                if(cursor1.moveToFirst()) {
                    do {
                        if (cursor1.getString(cursor1.getColumnIndex("品名")).
                                equals(cursor2.getString(cursor2.getColumnIndex("主料")))
                                &&cursor1.getDouble(cursor1.getColumnIndex("库存"))>=content1)
                            i++;
                        if (cursor1.getString(cursor1.getColumnIndex("品名"))
                                .equals(cursor2.getString(cursor2.getColumnIndex("配料")))
                                &&cursor1.getDouble(cursor1.getColumnIndex("库存"))>=content2)
                            i++;
                    } while (cursor1.moveToNext());
                }
                data3=cursor2.getString(cursor2.getColumnIndex("名称"));
                ContentValues values = new ContentValues();
                if(i==2)
                {
                    values.put("可做","是");
                    db.update("caidan",values,"名称 = ?",new String[]{data3});
                    values.clear();
                    i=0;
                }
                else
                {
                    values.put("可做","否");
                    db.update("caidan",values,"名称 = ?",new String[]{data3});
                    values.clear();
                    i=0;
                }
            }while (cursor2.moveToNext());
        }
        cursor1.close();
        cursor2.close();
    }

    @SuppressLint("Range")
    //输出对应品名的营养成分
    public String GetYingyang(String str)
    {
        //获取数据库对象
        Cursor cursor1=db.query("Food",null,null,null,null,null,null);
        String name="";
        if(cursor1.moveToFirst()){
            do{
                if(cursor1.getString(cursor1.getColumnIndex("品名")).equals(str))
                {
                    name = cursor1.getString(cursor1.getColumnIndex("营养成分"));
                }
            }while (cursor1.moveToNext());
        }
        return name;
    }
    //获取菜系
    @SuppressLint("Range")
    public String GetCaixi(String str)
    {
        //获取数据库对象
        Cursor cursor1=db.query("Menu",null,null,null,null,null,null);
        String name="";
        if(cursor1.moveToFirst()){
            do{
                if(cursor1.getString(cursor1.getColumnIndex("名称")).equals(str))
                {
                    name = cursor1.getString(cursor1.getColumnIndex("菜系"));
                }
            }while (cursor1.moveToNext());
        }
        return name;
    }
    @SuppressLint("Range")
    public Double GetJiage(String str)
    {
        //获取数据库对象
        Cursor cursor1=db.query("Menu",null,null,null,null,null,null);
        double name=0;
        if(cursor1.moveToFirst()){
            do{
                if(cursor1.getString(cursor1.getColumnIndex("名称")).equals(str))
                {
                    name = cursor1.getDouble(cursor1.getColumnIndex("价格"));
                }
            }while (cursor1.moveToNext());
        }
        return name;
    }
    //获取荤素
    @SuppressLint("Range")
    public String GetHunsu(String str)
    {
        //获取数据库对象
        Cursor cursor1=db.query("Menu",null,null,null,null,null,null);
        String name="";
        if(cursor1.moveToFirst()){
            do{
                if(cursor1.getString(cursor1.getColumnIndex("名称")).equals(str))
                {
                    name = cursor1.getString(cursor1.getColumnIndex("荤素"));
                }
            }while (cursor1.moveToNext());
        }
        return name;
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
