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


public class add_menu extends AppCompatActivity {
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        db = new CommonDatabase().getSqliteObject(add_menu.this,"test_db");

        final EditText et_menu_id = findViewById(R.id.edit_menu_id);
        final EditText et_menu_name = findViewById(R.id.edit_menu_name);
        final EditText et_menu_zhuliao = findViewById(R.id.edit_menu_zhuliao);
        final EditText et_menu_zhuhan = findViewById(R.id.edit_menu_zhuhan);
        final EditText et_menu_peiliao = findViewById(R.id.edit_menu_peiliao);
        final EditText et_menu_peihan = findViewById(R.id.edit_menu_peihan);
        final EditText et_menu_zuofa = findViewById(R.id.edit_menu_make);
        final EditText et_menu_caixi = findViewById(R.id.edit_menu_caixi);
        final EditText et_menu_hunsu = findViewById(R.id.edit_menu_hunsu);
        final EditText et_menu_kezuo = findViewById(R.id.edit_menu_kezuo);
        final EditText et_menu_price = findViewById(R.id.edit_menu_price);


        Button button_add = findViewById(R.id.button_add_ok);
        Button button_back = findViewById(R.id.button_finish_add);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.button_add_ok:
                        ContentValues values = new ContentValues();
                        values.put("ID",et_menu_id.getText().toString());
                        values.put("名称",et_menu_name.getText().toString());
                        values.put("主料",et_menu_zhuliao.getText().toString());
                        values.put("主料含量",et_menu_zhuhan.getText().toString());
                        values.put("配料",et_menu_peiliao.getText().toString());
                        values.put("配料含量",et_menu_peihan.getText().toString());
                        values.put("做法步骤",et_menu_zuofa.getText().toString());
                        values.put("菜系",et_menu_caixi.getText().toString());
                        values.put("荤素",et_menu_hunsu.getText().toString());
                        values.put("可做",et_menu_kezuo.getText().toString());
                        values.put("价格",et_menu_price.getText().toString());
                        db.insert("Menu",null,values);
                        finish();


                        break;
                    case R.id.button_finish_add:
                        finish();
                        break;
                }
            }
        };


        button_add.setOnClickListener(listener);
        button_back.setOnClickListener(listener);
    }
}
