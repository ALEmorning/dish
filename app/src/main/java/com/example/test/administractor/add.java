package com.example.test.administractor;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.R;
import com.example.test.database.CommonDatabase;


/***
 * 管理员用于增加食材信息
 */
public class add extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //实现add_activity中的按钮绑定
        Button button_add_add = findViewById(R.id.add_add);

        Button button_finish_add = findViewById(R.id.finish_add);

        //为这两个按钮实现Click监听器
        button_finish_add.setOnClickListener(this);

        button_add_add.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        CommonDatabase commonDatabase = new CommonDatabase();
        SQLiteDatabase db =commonDatabase.getSqliteObject(add.this,"test_db");



        //获取输入的ID
        EditText edit_ID = findViewById(R.id.edit_ID);
        String ID = edit_ID.getText().toString();

        //获取输入的品名
        EditText edit_pinming = findViewById(R.id.edit_pinming);
        String pinming = edit_pinming.getText().toString();

        //获取输入的类别
        EditText edit_cate = findViewById(R.id.edit_cate);
        String cate = edit_cate.getText().toString();

        //获取输入的许可证号
        EditText edit_license = findViewById(R.id.edit_license);
        String license = edit_license.getText().toString();

        //获取输入的标准号
        EditText edit_standard = findViewById(R.id.edit_standard);
        String standard = edit_standard.getText().toString();

        //获取输入的保质期
        EditText edit_date = findViewById(R.id.edit_date);
        String date = edit_date.getText().toString();

        //输入的贮存条件
        EditText edit_store = findViewById(R.id.edit_store);
        String store = edit_store.getText().toString();

        //输入的制造商
        EditText edit_make = findViewById(R.id.edit_make);
        String make = edit_make.getText().toString();

        //输入的地址
        EditText edit_address = findViewById(R.id.edit_address);
        String address = edit_address.getText().toString();

        //输入的产地
        EditText edit_from = findViewById(R.id.edit_from);
        String from = edit_from.getText().toString();

        //输入的营养成分
        EditText edit_nutrition = findViewById(R.id.edit_nutrition);
        String nutrition = edit_nutrition.getText().toString();

        //输入的净含量
        EditText edit_content = findViewById(R.id.edit_content);
        String content = edit_content.getText().toString();

        //输入的库存
        EditText edit_stock = findViewById(R.id.edit_stock);
        String stock = edit_stock.getText().toString();


        {

            switch (v.getId())
            {
                case R.id.add_add:

                    ContentValues values = new ContentValues();
                    values.put("ID",ID);
                    values.put("品名",pinming);
                    values.put("产品类别",cate);
                    values.put("食品生产许可证号",license);
                    values.put("产品标准号",standard);
                    values.put("保质期",date);
                    values.put("贮存条件",store);
                    values.put("制造商",make);
                    values.put("地址",address);
                    values.put("产地",from);
                    values.put("营养成分",nutrition);
                    values.put("净含量",content);
                    values.put("库存",stock);
                    db.insert("Food", null, values);
                    finish();
                    break;
                case R.id.finish_add:
                    finish();
                    break;
                default:
                    break;
            }
        }

    }
}
