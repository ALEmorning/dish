package com.example.test.administractor;

import android.content.ContentValues;
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


public class change_account_admin extends AppCompatActivity {
    SQLiteDatabase db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_account_admin);

        final EditText edit_account = findViewById(R.id.edit_change_admin_account);
        final EditText edit_password = findViewById(R.id.edit_change_admin_password);
        Button button_account_change = findViewById(R.id.button_admin_account_change);
        Button button_account_back = findViewById(R.id.button_admin_account_back);
        //获取进来这个活动的intent
        final Intent intent = getIntent();

        edit_account.setText(intent.getStringExtra("account"));
//        edit_password.setText(intent.getStringExtra("password"));

        //初始化数据库对象
        CommonDatabase commonDatabase = new CommonDatabase();
        db = commonDatabase.getSqliteObject(change_account_admin.this,"test_db");

        View.OnClickListener listener = new View.OnClickListener(

        ){
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.button_admin_account_change:
                        if(edit_password.getText().toString().equals(""))
                        {
                            Toast.makeText(change_account_admin.this,"登陆密码不能为空！",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            ContentValues values = new ContentValues();
                            values.put("account",edit_account.getText().toString());
                            values.put("password",(edit_password.getText().toString()));

                            db.update("administractor", values, "account = ? ", new String[]{intent.getStringExtra("account")});
                            Toast.makeText(change_account_admin.this,"修改成功",Toast.LENGTH_SHORT).show();
                        }

                        finish();
                        break;
                    case R.id.button_admin_account_back:
                        finish();
                        break;
                }
            }
        };

        button_account_back.setOnClickListener(listener);
        button_account_change.setOnClickListener(listener);

    }
}
