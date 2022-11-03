package com.example.test.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/*
   该界面主要用于建表，以及设定完整性
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private Context mContext;

    //带全部参数的构造函数，此构造函数必不可少
    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //食材信息
        String sql_food = " create table Food( " +
                 " id int primary key not null , " +
                 " 品名 text , " +
                 " 产品类别 text , " +
                 " 食品生产许可证号 text , " +
                 " 产品标准号 text , " +
                 " 保质期 text , " +
                 " 贮存条件 text , " +
                 " 制造商 text , " +
                 " 地址 text , " +
                 " 产地 text , " +
                 " 营养成分 text , " +
                 " 净含量 text , " +
                 " 库存 double) ;";
        //菜谱信息
        String sql_menu = " create table Menu( " +
                " id int primary key not null , " +
                " 名称 text , " +
                " 主料 text , " +
                " 主料含量 double ," +
                " 配料 text , " +
                " 配料含量 double , " +
                " 做法步骤 text , " +
                " 菜系 text , " +
                " 荤素 text , " +
                " 可做 char(10) , " +
                " 价格 double , " +
                " FOREIGN KEY(主料) REFERENCES Food(品名) , " +
                " FOREIGN KEY(配料) REFERENCES Food(品名) ," +
                " FOREIGN KEY(主料含量) REFERENCES Food(库存) ," +
                " FOREIGN KEY(配料含量) REFERENCES Food(库存) );" ;
        db.execSQL(sql_food);
        db.execSQL(sql_menu);

        String sql_caidan = " create table caidan( " +
                " id int primary key not null , " +
                " 名称 text , " +
                " 主料 text , " +
                " 主料含量 double ," +
                " 配料 text , " +
                " 配料含量 double , " +
                " 做法步骤 text , " +
                " 菜系 text , " +
                " 荤素 text , " +
                " 营养成分 text , " +
                " 可做 char(10) , " +
                " 价格 double , " +
                " FOREIGN KEY(主料) REFERENCES Food(品名) , " +
                " FOREIGN KEY(配料) REFERENCES Food(品名) ," +
                " FOREIGN KEY(主料含量) REFERENCES Food(库存) ," +
                " FOREIGN KEY(配料含量) REFERENCES Food(库存) );" ;
        db.execSQL(sql_caidan);

        //创建成功
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();



        //插入信息
        String sql_insertf1 = "insert into Food(id,品名,库存,保质期,营养成分) values ('001','鸡肉','2000','2','蛋白质');";
        String sql_insertf2 = "insert into Food(id,品名,库存,保质期,营养成分) values ('002','猪肉','800','1','脂肪');";
        String sql_insertf3 = "insert into Food(id,品名,库存,保质期,营养成分) values ('003','青菜','1000','3','维生素');";
        String sql_insertf4 = "insert into Food(id,品名,库存,保质期,营养成分) values ('004','土豆','1000','3','热量');";
        String sql_insertf5 = "insert into Food(id,品名,库存,保质期,营养成分) values ('005','豆腐','1000','3','蛋白质');";
        String sql_insertf6 = "insert into Food(id,品名,库存,保质期,营养成分) values ('006','木耳','1000','3','碳水化合物');";
        String sql_insertf7 = "insert into Food(id,品名,库存,保质期,营养成分) values ('007','辣椒','1200','5','维生素');";
        String sql_insertf8 = "insert into Food(id,品名,库存,保质期,营养成分) values ('008','葱','1000','5','维生素');";
        String sql_insertm1 = "insert into Menu(id,名称,主料,配料,主料含量,配料含量,菜系,荤素,价格) values ('001','辣子鸡','鸡肉','辣椒','3','7','川菜','荤','26');";
        String sql_insertm2 = "insert into Menu(id,名称,主料,配料,主料含量,配料含量,菜系,荤素,价格) values ('002','葱爆鸡块','鸡肉','葱','4','6','鲁菜','荤','25');";
        String sql_insertm3 = "insert into Menu(id,名称,主料,配料,主料含量,配料含量,菜系,荤素,价格) values ('003','土豆炖鸡','鸡肉','土豆','5','5','徽菜','荤','24');";
        String sql_insertm4 = "insert into Menu(id,名称,主料,配料,主料含量,配料含量,菜系,荤素,价格) values ('004','木耳炒猪肉','猪肉','木耳','5','5','苏菜','荤','20');";
        String sql_insertm5 = "insert into Menu(id,名称,主料,配料,主料含量,配料含量,菜系,荤素,价格) values ('005','辣椒炒肉','猪肉','辣椒','4','6','浙菜','荤','18');";
        String sql_insertm6 = "insert into Menu(id,名称,主料,配料,主料含量,配料含量,菜系,荤素,价格) values ('006','豆腐炒青菜','青菜','豆腐','6','4','闽菜','素','12');";


        db.execSQL(sql_insertf1);
        db.execSQL(sql_insertf2);
        db.execSQL(sql_insertf3);
        db.execSQL(sql_insertf4);
        db.execSQL(sql_insertf5);
        db.execSQL(sql_insertf6);
        db.execSQL(sql_insertf7);
        db.execSQL(sql_insertf8);
        db.execSQL(sql_insertm1);
        db.execSQL(sql_insertm2);
        db.execSQL(sql_insertm3);
        db.execSQL(sql_insertm4);
        db.execSQL(sql_insertm5);
        db.execSQL(sql_insertm6);

        //用户信息
        String user = "create table user(" +
                " id int primary key not null , " +
                " name char(20) , " +
                " sex varchar(10) , " +
                " age int , " +
                " phone int) ;";
        //用户——登陆密码表
        String sql_user_load = "create table load_account(account int primary Key not null ,password varchar(20));";
        String sql_insertu1 = "insert into user(id,name,sex,phone) values ('001','赵某某','男','100861');";
        String sql_insertu2 = "insert into user(id,name,sex,phone) values ('002','钱某某','男','100862');";
        String sql_insertl1 = "insert into load_account values ('001','123456')";


        db.execSQL(user);
        db.execSQL(sql_user_load);
        db.execSQL(sql_insertu1);
        db.execSQL(sql_insertu2);
        db.execSQL(sql_insertl1);

        //管理员表
        String sql_admin = "create table administractor(" +
                " account varchar(20)," +
                " password varchar(20));";
        String sql_inserta1 = "insert into administractor values('admin','123456');";





        db.execSQL(sql_admin);
        db.execSQL(sql_inserta1);

        //用户收藏信息表
        String sql_user_collect = "create table user_collect(" +
                " id int , " +
                " menu_name varchar(20) , " +
                " menu_number int , " +
                " menu_caixi varchar(10) , " +
                " menu_hunsu int , " +
                " FOREIGN KEY(menu_name) REFERENCES Menu(名称) , " +
                " FOREIGN KEY(menu_caixi) REFERENCES Menu(菜系) , " +
                " FOREIGN KEY(menu_hunsu) REFERENCES Menu(荤素) ) ;" ;
        db.execSQL(sql_user_collect);
        //留言信息存储表
        String sql_liuyan = "create table message(" +
                " id int primary key not null," +
                " message text);";
        db.execSQL(sql_liuyan);

        //个人资源配置表，比如更改图片之类的啦
        String personal_resource = "create table personal_resource(" +
                "id int primary key not null ," +
                "IMAGE blob);";

        db.execSQL(personal_resource);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
