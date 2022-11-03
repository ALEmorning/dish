package com.example.test.database.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.test.database.CommonDatabase;

import org.litepal.LitePal;

import java.util.List;

public class SqliteUtil {
    private static String TAG = " SqliteUtil ";
    private static SqliteUtil sqliteUtil;

    public static SqliteUtil getInstance() {
        if (sqliteUtil == null) {
            sqliteUtil = new SqliteUtil();
        }
        return sqliteUtil;
    }

    /**
     * 查询所有食物（保质期升序）
     *
     * @return
     */
    public List<Food> selectFood() {
        return LitePal.order("保质期  asc").find(Food.class);
    }


    /**
     * 查询菜单（库存升序）
     *
     * @param 主料
     * @return
     */
    public List<Menu> selectMenu(String 主料) {
        return LitePal.where("主料 = ?", 主料).order("主料含量 asc").find(Menu.class);

    }

    /**
     * 查询菜单（库存升序）
     *
     * @return
     */
    public List<Menu> selectMenu() {
        return LitePal.order("主料含量 asc").find(Menu.class);

    }


    public List<Caidan> selectCaidan() {
        return LitePal.findAll(Caidan.class);
    }

    public void deleteCaidan() {
        LitePal.deleteAll(Caidan.class);
    }


    /**
     * 生成菜单
     *
     * @return
     */
    public List<Caidan> generateCaidan(Context context) {
        SQLiteDatabase db = new CommonDatabase().getSqliteObject(context, "test_db");
        if (selectCaidan().size() > 0) {
            deleteCaidan();
        }

        //查询食材表（保质期升序）
        List foodList = selectFood();
        Log.d(TAG, "generateCaidan: " + foodList);

        for (int i = 0; i < foodList.size(); i++) {
            //取一条 食材
            Food food = (Food) foodList.get(i);
            Log.d(TAG, "generateCaidan:food " + food);
            if (food.get库存() == 0) {
                continue;
            }
            if (food.get保质期() == null) {
                continue;
            }
            //获取食材库存
            double 食物库存 = food.get库存();
            //食材品名
            String 品名 = food.get品名();

            //查询对应菜单（库存升序）
            List<Menu> menuList = selectMenu(品名);
            for (Menu menu : menuList) {
                Log.d(TAG, "generateCaidan:menu " + menu);
                double 菜单库存 = menu.get主料含量();
                double 配料库存 = menu.get配料含量();
                String 配料 = menu.get配料();//获取配料
                /********************找寻食材中的配料*********************/
                for (int j = 0; j < foodList.size(); j++) {
                    Food food_p = (Food) foodList.get(j);  //创一个配料集合
                    Log.d(TAG, "generateCaidan:food_p.get品名() " + food_p.get品名());
                    Log.d(TAG, "generateCaidan:配料 " + 配料);
                    if (food_p.get品名().equals(配料)) {

                        if (食物库存 - 菜单库存 > 0 && food_p.get库存() - 配料库存 > 0) {
                            String sql_insertm6 = "insert into caidan(id,名称,主料,配料,主料含量,配料含量,做法步骤,菜系,荤素,可做,价格) values" +
                                    " ('" + menu.getId() + "','" + menu.get名称() + "','" + menu.get主料() + "','" + menu.get配料() + "','" + menu.get主料含量() + "','" + menu.get配料含量() + " ','" + menu.get做法步骤() + "','" + menu.get菜系() + "','" + menu.get荤素() + "','" + menu.get可做() + "','" + menu.get价格() + "');";


                            Log.d(TAG, "generateCaidan:sql_insertm6: " + sql_insertm6);
                            db.execSQL(sql_insertm6);
                            食物库存 = 食物库存 - 菜单库存;
                            //更新配菜中的库存
                            food_p.set库存(food_p.get库存() - 配料库存);
                            //更新集合中的库存
                            foodList.set(j, food_p);
                        }
                        break;
                    }

                }
            }

        }

        return selectCaidan();
    }

}
