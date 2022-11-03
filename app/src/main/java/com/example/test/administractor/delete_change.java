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

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.R;
import com.example.test.database.CommonDatabase;


/***
 * 这里主要是点击listview中的食材信息时，对应的相应activity
 */
public class delete_change extends AppCompatActivity implements View.OnClickListener {
    EditText edit_ID_d;
    EditText edit_pinming_d;
    EditText edit_leibie_d;
    EditText edit_license_d;
    EditText edit_standard_d;
    EditText edit_date_d;
    EditText edit_store_d;
    EditText edit_make_d;
    EditText edit_address_d;
    EditText edit_from_d;
    EditText edit_nutri_d;
    EditText edit_content_d;
    EditText edit_stock_d;
    String  t_d_id ;
    String  t_d_pinming ;
    String  t_d_leibie ;
    //false代表没点删除，点的是
    boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_change);

        //按钮绑定
        Button button_delete = findViewById(R.id.button_x_delete);

        Button button_baocun = findViewById(R.id.button_x_baocun);
        //监听器
        button_delete.setOnClickListener(this);
        button_baocun.setOnClickListener(this);
        //编辑框绑定
        edit_ID_d = findViewById(R.id.edit_x_ID);
        edit_pinming_d = findViewById(R.id.edit_x_pinming);
        edit_leibie_d = findViewById(R.id.edit_x_cate);
        edit_license_d = findViewById(R.id.edit_x_license);
        edit_standard_d = findViewById(R.id.edit_x_standard);
        edit_date_d = findViewById(R.id.edit_x_date);
        edit_store_d = findViewById(R.id.edit_x_store);
        edit_make_d = findViewById(R.id.edit_x_make);
        edit_address_d = findViewById(R.id.edit_x_address);
        edit_from_d = findViewById(R.id.edit_x_from);
        edit_nutri_d = findViewById(R.id.edit_x_nutrition);
        edit_content_d = findViewById(R.id.edit_x_content);
        edit_stock_d = findViewById(R.id.edit_x_stock);

        Intent intent_receive = getIntent();



        //把intent中携带的信息设置成为编辑框的初始内容，方便修改
        edit_ID_d.setText(intent_receive.getStringExtra("id"));
        edit_pinming_d.setText(intent_receive.getStringExtra("品名"));
        edit_leibie_d.setText(intent_receive.getStringExtra("产品类别"));
        edit_license_d.setText(intent_receive.getStringExtra("食品生产许可证号"));
        edit_standard_d.setText(intent_receive.getStringExtra("产品标准号"));
        edit_date_d.setText(intent_receive.getStringExtra("保质期"));
        edit_store_d.setText(intent_receive.getStringExtra("贮存条件"));
        edit_make_d.setText(intent_receive.getStringExtra("制造商"));
        edit_address_d.setText(intent_receive.getStringExtra("地址"));
        edit_from_d.setText(intent_receive.getStringExtra("产地"));
        edit_nutri_d.setText(intent_receive.getStringExtra("营养成分"));
        edit_content_d.setText(intent_receive.getStringExtra("净含量"));
        edit_stock_d.setText(intent_receive.getStringExtra("库存"));



        //获取初始值
        t_d_id = edit_ID_d.getText().toString();
        t_d_pinming = edit_pinming_d.getText().toString();
        t_d_leibie = edit_leibie_d.getText().toString();

    }

    @Override
    public void onClick(View v) {
        CommonDatabase commonDatabase = new CommonDatabase();
        final SQLiteDatabase db = commonDatabase.getSqliteObject(delete_change.this,"test_db");

        switch (v.getId())
        {
            case R.id.button_x_delete:
                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(delete_change.this);
                //    设置Title的图标
                builder.setIcon(R.drawable.warn1);
                //    设置Title的内容
                builder.setTitle("弹出警告框");
                //    设置Content来显示一个信息
                builder.setMessage("确定删除吗？");
                //    设置一个PositiveButton
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        db.delete("Food","ID=?",new String[]{t_d_id});
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
            case R.id.button_x_baocun:

                //获取这三项数据，并写入contentvalues中
                ContentValues values2 = new ContentValues();
                values2.put("id", edit_ID_d.getText().toString());
                values2.put("品名", edit_pinming_d.getText().toString());
                values2.put("产品类别", edit_leibie_d.getText().toString());
                values2.put("食品生产许可证号", edit_license_d.getText().toString());
                values2.put("产品标准号", edit_standard_d.getText().toString());
                values2.put("保质期", edit_date_d.getText().toString());
                values2.put("贮存条件", edit_store_d.getText().toString());
                values2.put("制造商", edit_make_d.getText().toString());
                values2.put("地址", edit_address_d.getText().toString());
                values2.put("产地", edit_from_d.getText().toString());
                values2.put("营养成分", edit_nutri_d.getText().toString());
                values2.put("净含量", edit_content_d.getText().toString());
                values2.put("库存", edit_stock_d.getText().toString());

                db.update("Food", values2, "ID = ? ", new String[]{t_d_id});
                finish();
                break;

            default:
                break;




        }
    }


}
