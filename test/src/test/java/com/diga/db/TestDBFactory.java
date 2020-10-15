package com.diga.db;

import com.diga.db.core.DB;
import com.diga.db.core.Result;
import com.diga.db.core.ResultMap;
import com.diga.db.core.factory.DefaultResultMapFactory;
import com.diga.db.entity.Course;
import com.diga.db.factory.DBFactory;
import com.diga.db.pojo.FoodCategory;
import com.diga.db.pojo.FoodCategoryMap;
import com.diga.generic.utils.ClassUtils;
import com.diga.generic.utils.CollectionUtils;
import com.diga.generic.utils.PropUtils;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.List;

public class TestDBFactory {

    private DBFactory dbFactory;

    @Before
    public void init() throws SQLException {
        ClassUtils.tryForName("com.mysql.jdbc.Driver");
        PropUtils.KV kv = PropUtils.load("classpath:db.properties");
        Connection connection = DriverManager.getConnection(kv.getString("jdbc.url"), kv.getString("jdbc.username"), kv.getString("jdbc.password"));
        dbFactory = new DBFactory(connection);
    }


    @Test
    public void testGetDB() throws SQLException {
        DB db = dbFactory.getDB();
        ResultSet resultSet = db.select("SELECT * FROM food_category");
        while (resultSet.next()) {
            ResultSetMetaData metaData = resultSet.getMetaData();

            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnLabel(i);
                Object value = resultSet.getObject(columnName);
                System.err.println(columnName + "=" + value);
            }
            System.out.println();
        }

        db.close();
    }


    @Test
    public void testSelectListByReturnClass() throws SQLException {
        DB db = dbFactory.getDB();
        List<FoodCategory> foodCategoryList = db.selectList("SELECT * FROM food_category", FoodCategory.class);
        CollectionUtils.forEach(foodCategoryList, (index, data, dataList) -> System.out.println(data));

        /**
         * 输出结果:
         *
         * FoodCategory(id=1, name=甜点/蛋糕, type=1, fatherId=0, logo=img/cake.png, slogan=每一道甜品都打开你的味蕾, catImage=http://122.152.205.72:88/foodie/category/cake.png, bgColor=#fe7a65, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategory(id=2, name=饼干/膨化, type=1, fatherId=0, logo=img/cookies.png, slogan=噶喷脆, 一听到声音就开吃, catImage=http://122.152.205.72:88/foodie/category/cookies.png, bgColor=#f59cec, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategory(id=3, name=熟食/肉类, type=1, fatherId=0, logo=img/meat.png, slogan=食肉者最受绝味美食, catImage=http://122.152.205.72:88/foodie/category/meat.png, bgColor=#b474fe, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategory(id=4, name=素食/卤味, type=1, fatherId=0, logo=img/juwei.png, slogan=香辣甜味麻辣, 辣了才有味, catImage=http://122.152.205.72:88/foodie/category/duck.png, bgColor=#82ceff, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategory(id=5, name=坚果/炒货, type=1, fatherId=0, logo=img/jiango.png, slogan=酥脆无比, 休闲最佳, catImage=http://122.152.205.72:88/foodie/category/nut.png, bgColor=#c6a868, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategory(id=6, name=糖果/蜜饯, type=1, fatherId=0, logo=img/sweet.png, slogan=甜味才是爱美者的最爱, catImage=http://122.152.205.72:88/foodie/category/mango.png, bgColor=#6bdea7, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategory(id=7, name=巧克力, type=1, fatherId=0, logo=img/chocolate.png, slogan=美容养颜, 男女都爱, catImage=http://122.152.205.72:88/foodie/category/chocolate.png, bgColor=#f8c375, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategory(id=8, name=海鲜/海味, type=1, fatherId=0, logo=img/lobster.png, slogan=吃货们怎么能少了海鲜呢？, catImage=http://122.152.205.72:88/foodie/category/crab.png, bgColor=#84affe, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategory(id=9, name=花菜/果茶, type=1, fatherId=0, logo=img/tea.png, slogan=绿茶红茶怎能少得了, catImage=http://122.152.205.72:88/foodie/category/tea.png, bgColor=#ff9229, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategory(id=10, name=生鲜/蔬菜, type=1, fatherId=0, logo=img/food.png, slogan=新鲜少不了,每日蔬菜生鲜, catImage=http://122.152.205.72:88/foodie/category/meat2.png, bgColor=#6cc67c, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         */
    }


    @Test
    public void testSelectListByResultMap() throws SQLException {
        DB db = dbFactory.getDB();
        ResultMap resultMap = dbFactory.getResultMapFactory().build(FoodCategoryMap.class);
        List<FoodCategoryMap> foodCategoryMapList = db.selectList("SELECT * FROM food_category", resultMap);
        CollectionUtils.forEach(foodCategoryMapList, (index, data, dataList) -> System.out.println(data));

        /**
         * 输出结果:
         *
         * FoodCategoryMap(id=1, name=甜点/蛋糕, type=1, fatherId=0, logo=img/cake.png, slogan=每一道甜品都打开你的味蕾, catImage=http://122.152.205.72:88/foodie/category/cake.png, bgColor=#fe7a65, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategoryMap(id=2, name=饼干/膨化, type=1, fatherId=0, logo=img/cookies.png, slogan=噶喷脆, 一听到声音就开吃, catImage=http://122.152.205.72:88/foodie/category/cookies.png, bgColor=#f59cec, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategoryMap(id=3, name=熟食/肉类, type=1, fatherId=0, logo=img/meat.png, slogan=食肉者最受绝味美食, catImage=http://122.152.205.72:88/foodie/category/meat.png, bgColor=#b474fe, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategoryMap(id=4, name=素食/卤味, type=1, fatherId=0, logo=img/juwei.png, slogan=香辣甜味麻辣, 辣了才有味, catImage=http://122.152.205.72:88/foodie/category/duck.png, bgColor=#82ceff, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategoryMap(id=5, name=坚果/炒货, type=1, fatherId=0, logo=img/jiango.png, slogan=酥脆无比, 休闲最佳, catImage=http://122.152.205.72:88/foodie/category/nut.png, bgColor=#c6a868, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategoryMap(id=6, name=糖果/蜜饯, type=1, fatherId=0, logo=img/sweet.png, slogan=甜味才是爱美者的最爱, catImage=http://122.152.205.72:88/foodie/category/mango.png, bgColor=#6bdea7, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategoryMap(id=7, name=巧克力, type=1, fatherId=0, logo=img/chocolate.png, slogan=美容养颜, 男女都爱, catImage=http://122.152.205.72:88/foodie/category/chocolate.png, bgColor=#f8c375, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategoryMap(id=8, name=海鲜/海味, type=1, fatherId=0, logo=img/lobster.png, slogan=吃货们怎么能少了海鲜呢？, catImage=http://122.152.205.72:88/foodie/category/crab.png, bgColor=#84affe, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategoryMap(id=9, name=花菜/果茶, type=1, fatherId=0, logo=img/tea.png, slogan=绿茶红茶怎能少得了, catImage=http://122.152.205.72:88/foodie/category/tea.png, bgColor=#ff9229, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         * FoodCategoryMap(id=10, name=生鲜/蔬菜, type=1, fatherId=0, logo=img/food.png, slogan=新鲜少不了,每日蔬菜生鲜, catImage=http://122.152.205.72:88/foodie/category/meat2.png, bgColor=#6cc67c, createTime=2020-10-14 22:02:16.0, updateTime=2020-10-14 22:02:16.0)
         */
    }


    @Test
    public void testSelectListBySelfResultMap() throws SQLException {
        DB db = dbFactory.getDB();
        ResultMap resultMap = new ResultMap();
        resultMap.setId("selfMap");
        resultMap.setType("com.diga.db.pojo.FoodCategoryMap");
        List<Result> resultList = resultMap.getResultList();
        resultList.add(new Result("id", "id", long.class));
        resultList.add(new Result("name", "name", String.class));

        // 这里我们只是设置了映射 id 和 name 字段, 所以其他字段不会进行映射. 这也符合我们需要的标准
        List<FoodCategoryMap> foodCategoryMapList = db.selectList("SELECT * FROM food_category", resultMap);
        CollectionUtils.forEach(foodCategoryMapList, (index, data, dataList) -> System.out.println(data));

        /**
         * 输出结果:
         *
         * FoodCategoryMap(id=1, name=甜点/蛋糕, type=null, fatherId=null, logo=null, slogan=null, catImage=null, bgColor=null, createTime=null, updateTime=null)
         * FoodCategoryMap(id=2, name=饼干/膨化, type=null, fatherId=null, logo=null, slogan=null, catImage=null, bgColor=null, createTime=null, updateTime=null)
         * FoodCategoryMap(id=3, name=熟食/肉类, type=null, fatherId=null, logo=null, slogan=null, catImage=null, bgColor=null, createTime=null, updateTime=null)
         * FoodCategoryMap(id=4, name=素食/卤味, type=null, fatherId=null, logo=null, slogan=null, catImage=null, bgColor=null, createTime=null, updateTime=null)
         * FoodCategoryMap(id=5, name=坚果/炒货, type=null, fatherId=null, logo=null, slogan=null, catImage=null, bgColor=null, createTime=null, updateTime=null)
         * FoodCategoryMap(id=6, name=糖果/蜜饯, type=null, fatherId=null, logo=null, slogan=null, catImage=null, bgColor=null, createTime=null, updateTime=null)
         * FoodCategoryMap(id=7, name=巧克力, type=null, fatherId=null, logo=null, slogan=null, catImage=null, bgColor=null, createTime=null, updateTime=null)
         * FoodCategoryMap(id=8, name=海鲜/海味, type=null, fatherId=null, logo=null, slogan=null, catImage=null, bgColor=null, createTime=null, updateTime=null)
         * FoodCategoryMap(id=9, name=花菜/果茶, type=null, fatherId=null, logo=null, slogan=null, catImage=null, bgColor=null, createTime=null, updateTime=null)
         * FoodCategoryMap(id=10, name=生鲜/蔬菜, type=null, fatherId=null, logo=null, slogan=null, catImage=null, bgColor=null, createTime=null, updateTime=null)
         */
    }


    @Test
    public void testSelectList() {
        DB db = dbFactory.getDB();
        List<Course> courseList = db.selectList("SELECT c.id AS course_id, c.name AS course_name, s.id AS student_id, s.name AS student_name FROM course c LEFT JOIN student s ON c.id = s.course_id", Course.class);

        long start = System.currentTimeMillis();
        CollectionUtils.forEach(courseList, (index, data, dataList) -> System.out.println(data));
        System.out.println(System.currentTimeMillis() - start + "ms");
        db.close();
    }

}
