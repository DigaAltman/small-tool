package com.diga.db.annotation;

import java.lang.annotation.*;

/**
 * 一个 @ResultBean 对应一个返回结果解析器.
 *
 *
 * 案例1:
 * <code>
 *      @ResultBean(id = "app")
 *      public class org.example.pojo.App {
 *          private Integer id;
 *          private String name;
 *      }
 * </code>
 *
 * <p>
 *
 * 解析转换后:
 * <code>
 *     <resultMap id="app" type="org.example.pojo.App">
 *          <result property="id"   column="id"/>
 *          <result property="name" column="name"/>
 *     </resultMap>
 * </code>
 *
 *
 * 案例2:
 *
 * <code>
 *      @ResultBean(id = "app")
 *      public ResultMap app() {
 *          ResultMap resultMap = new ResultMap();
 *          List<Result> resultList = new ArrayList();
 *          resultList.add(new Result("id", "id"));
 *          resultList.add(new Result("name", "name"));
 *          resultMap.setResultList(resultList);
 *          return resultMap;
 *      }
 * </code>
 *
 * <p>
 *
 * 解析转换后:
 * <code>
 *     <resultMap id="app" type="org.example.pojo.App">
 *          <result property="id"   column="id"/>
 *          <result property="name" column="name"/>
 *     </resultMap>
 * </code>
 *
 *
 * @Id 配合使用
 * <code>
 *      @ResultBean(id = "app")
 *      public class org.example.pojo.App {
 *          @Id
 *          private Integer id;
 *          private String name;
 *      }
 * </code>
 *
 * 解析转换后:
 * <code>
 *     <resultMap id="app" type="org.example.pojo.App">
 *          <id     property="id"   column="id"/>
 *          <result property="name" column="name"/>
 *     </resultMap>
 * </code>
 *
 * @Column 配置使用
 * <code>
 *      @ResultBean(id = "app")
 *      public class org.example.pojo.App {
 *          @Id
 *          private Integer id;
 *
 *          @Column("xxx_name")
 *          private String name;
 *      }
 * </code>
 *
 * 解析转换后:
 * <code>
 *     <resultMap id="app" type="org.example.pojo.App">
 *          <id     property="id"   column="id"/>
 *          <result property="name" column="xxx_name"/>
 *     </resultMap>
 * </code>
 *
 *
 * 还有很多用法待完善...
 *
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ResultBean {

    /**
     * 返回结果的唯一标识
     *
     * @return
     */
    String id() default "";
}
