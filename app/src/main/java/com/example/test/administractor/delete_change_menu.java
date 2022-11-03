package com.example.test.administractor;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.R;
import com.example.test.database.CommonDatabase;


public class delete_change_menu extends AppCompatActivity implements View.OnClickListener {


        EditText edit_m_id ;
        EditText edit_m_name_d;
        EditText edit_m_zhu_d;
        EditText edit_m_zhuhan_d;
        EditText edit_m_pei_d;
        EditText edit_m_peihan_d;
        EditText edit_m_make_d;
        EditText edit_m_caixi_d;
        EditText edit_m_hunsu_d;
        EditText edit_m_kezuo_d;
        EditText edit_m_price_d;
        String  t_d_id ;
        String  t_d_name ;
        String  t_d_zhu ;


        //false代表没点删除，点的是
        boolean flag = false;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_delete_change_menu);

            //按钮绑定
            Button button_delete = findViewById(R.id.button_xm_delete);

            Button button_baocun = findViewById(R.id.button_xm_baocun);
            //监听器
            button_delete.setOnClickListener(this);
            button_baocun.setOnClickListener(this);


            //编辑框绑定
            edit_m_id= findViewById(R.id.edit_xm_ID);
            edit_m_name_d = findViewById(R.id.edit_xm_name);
            edit_m_zhu_d = findViewById(R.id.edit_xm_zhuliao);
            edit_m_zhuhan_d = findViewById(R.id.edit_xm_zhuhan);
            edit_m_pei_d = findViewById(R.id.edit_xm_peiliao);
            edit_m_peihan_d = findViewById(R.id.edit_xm_peihan);
            edit_m_make_d = findViewById(R.id.edit_xm_make);
            edit_m_caixi_d = findViewById(R.id.edit_xm_caixi);
            edit_m_hunsu_d = findViewById(R.id.edit_xm_hunsu);
            edit_m_kezuo_d = findViewById(R.id.edit_xm_kezuo);
            edit_m_price_d = findViewById(R.id.edit_xm_price);
            Intent intent_receive = getIntent();




            //把intent中携带的信息设置成为编辑框的初始内容，方便修改
            edit_m_id.setText(intent_receive.getStringExtra("id"));
            edit_m_name_d.setText(intent_receive.getStringExtra("名称"));
            edit_m_zhu_d.setText(intent_receive.getStringExtra("主料"));
            edit_m_zhuhan_d.setText(intent_receive.getStringExtra("主料含量"));
            edit_m_pei_d.setText(intent_receive.getStringExtra("配料"));
            edit_m_peihan_d.setText(intent_receive.getStringExtra("配料含量"));
            edit_m_make_d.setText(intent_receive.getStringExtra("做法步骤"));
            edit_m_caixi_d.setText(intent_receive.getStringExtra("菜系"));
            edit_m_hunsu_d.setText(intent_receive.getStringExtra("荤素"));
            edit_m_kezuo_d.setText(intent_receive.getStringExtra("可做"));
            edit_m_price_d.setText(intent_receive.getStringExtra("价格"));



            //获取初始值
            t_d_id = edit_m_id.getText().toString();
            t_d_name = edit_m_name_d.getText().toString();
            t_d_zhu = edit_m_zhu_d.getText().toString();

        }

        @Override
        public void onClick(View v) {
            CommonDatabase commonDatabase = new CommonDatabase();
            final SQLiteDatabase db = commonDatabase.getSqliteObject(delete_change_menu.this,"test_db");

            switch (v.getId())
            {
                case R.id.button_xm_delete:
                    //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                    AlertDialog.Builder builder = new AlertDialog.Builder(delete_change_menu.this);
                    //    设置Title的图标
                    builder.setIcon(R.drawable.warn1);
                    //    设置Title的内容
                    builder.setTitle("警告框");
                    //    设置Content来显示一个信息
                    builder.setMessage("确定删除吗？");
                    //    设置一个PositiveButton
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            db.delete("Menu","ID=?",new String[]{t_d_id});
                            Toast.makeText(delete_change_menu.this,"删除成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    //    设置一个NegativeButton
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                        }
                    });

                    //    显示出该对话框
                    builder.show();

                    break;
                /****更改*****/
                case R.id.button_xm_baocun:
                        //获取这三项数据，并写入contentvalues中
                        ContentValues values2 = new ContentValues();

                        values2.put("id", edit_m_id.getText().toString());
                        values2.put("名称", edit_m_name_d.getText().toString());
                        values2.put("主料", edit_m_zhu_d.getText().toString());
                        values2.put("主料含量", edit_m_zhuhan_d.getText().toString());
                        values2.put("配料", edit_m_pei_d.getText().toString());
                        values2.put("配料含量", edit_m_peihan_d.getText().toString());
                        values2.put("做法步骤", edit_m_make_d.getText().toString());
                        values2.put("菜系", edit_m_caixi_d.getText().toString());
                        values2.put("荤素", edit_m_hunsu_d.getText().toString());
                        values2.put("可做", edit_m_kezuo_d.getText().toString());
                        values2.put("价格", edit_m_price_d.getText().toString());

                        db.update("Menu", values2, "ID = ? ", new String[]{t_d_id});
                        finish();


                    break;

                default:
                    break;




            }
        }


    }


