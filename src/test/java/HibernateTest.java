import com.alberta.hibernate.entities.News;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.jdbc.Work;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class HibernateTest {

//    @Test
//    public void helloTest(){
////        Hibernate 5.5版本写法
//        SessionFactory sessionFactory = null;
//        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
//        try {
//            // 创建 sessionFactory 对象
//            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
//            // 创建Session
//            Session session = sessionFactory.openSession();
//            // 开启事务
//            Transaction transaction = session.beginTransaction();
//            // 执行保存操作
//            News news = new News("JAVA", "ATGUIGU",  new Date(new java.util.Date().getTime()));
//            session.save(news);
//            // 提交事务
//            transaction.commit();
//            // 关闭Session 及 SessionFactory 对象
//            session.close();
//            sessionFactory.close();
//        } catch (Exception e) {
//            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
//            // so destroy it manually.
//            StandardServiceRegistryBuilder.destroy( registry );
//        }
//    }

    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @BeforeEach
    public void init(){
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            // 创建Session
        session = sessionFactory.openSession();
            // 开启事务
        transaction = session.beginTransaction();
    }


    @AfterEach
    public void destroy(){
        transaction.commit();
        session.close();
        sessionFactory.close();
    }



    //    因为有Session缓存的存在，只会发送一条select语句
    @Test
    public void testSession(){
        News news = session.get(News.class, 1);
        News news2 = session.get(News.class, 1);
    }

//    flush: 使数据表的记录和Session缓存中的对象状态保持同步状态。 为了保持一致，可能会发送对应的SQL语句。
    // 1。 在Transaction 的commit（）方法中，先调用session的flush方法，再提交事务。
    // 2。 flush（）方法可能会发送SQL语句，但不会提交事务
    // 3。 注意：若在未提交事务或显式地调用session.flush（）方法之前，也有可能会进行flush（）操作。
    //     1）执行HQL或Criteria查询，会先执行flush()操作，以便得到数据表的最新记录
    //     2）（使用native生成ID，且调用save()方法）若记录的ID是由底层数据库使用自增的方式生成的，则在调用save()方法时，就会立即发送insert语句。
    //        因为save()方法后，必须保证对象的ID是存在的

    @Test
    public void testSessionFlush2(){
        News news = new News("JAVA", "ATGUIGU",  new Date(new java.util.Date().getTime()));
        session.save(news);
    }

    @Test
    public void testSessionFlush(){
        News news = session.get(News.class, 1);
        news.setAuthor("SUN");
    }

//    reflush(): 会强制发送SELECT语句，以使得Session 缓存中对象的状态和数据表中对应的记录保持一致。
    @Test
    public void testReflush(){
        News news = session.get(News.class, 1);
        System.out.println(news);

        session.refresh(news);
        System.out.println(news); // 若这里没刷新，是因为hibernate的事务隔离级别设置问题
    }


    // clear() 清理缓存，发送两条Select语句
    @Test
    public void testClear(){
        News news = session.get(News.class, 1);
        session.clear();
        News news2 = session.get(News.class, 1);

    }


//    1. save()方法
    // 1). 使一个临时对象变成持久化对象
    // 2). 为对象分配ID
    // 3). 在flush缓存时会发送一条INSERT语句
    // 4). 在save 方法之前的ID是无效的
    // 5). 持久化对象的ID是不能被修改的
    @Test
    public void testSave(){
        News news = new News();
        news.setAuthor("AA");
        news.setTitle("aa");
        news.setDate(new Date(new java.util.Date().getTime()));

        System.out.println(news);

        session.save(news);
        System.out.println(news);
    }

//    2. persist()方法 - 也会执行insert语句
    // 和save() 的区别：
    // 1). 在调用 persist 方法之前，若对象已经有id了，则不会执行insert，而抛出异常
    // 2). persist 方法把瞬时对象持久化，但"不保证"标识符（identifier主键对应的属性)被立刻填入到持久化实例中，可能被推迟到flush时。
    //     save 方法则立时产生持久化标识符，所以会立刻执行sql insert语句

    @Test
    public void testPersist(){
        News news = new News();
        news.setAuthor("BB");
        news.setTitle("bb");
        news.setDate(new Date(new java.util.Date().getTime()));
        news.setId(30);

        System.out.println(news);

        session.persist(news);
        System.out.println(news);
    }



//    get vs. load
    // 1. 执行get 方法：会立即加载对象，立即加载。
//        执行load 方法，若不使用该对象，则不会立即查询操作，而返回一个代理对象。延迟检索、延迟加载。
    // 2. 若数据表中没有对应的记录，且 Session没有被关闭，同时需要使用对象时
    //    get 返回 null
    //    load 抛出异常
    // 3. load 方法可能会抛出 懒加载异常（LazeInitializationException）：
    //    在需要初始化代理对象之前  已经关闭了session
    @Test
    public void testGet(){
        News news = session.get(News.class,1);
        System.out.println(news);
    }

    @Test
    public void testLoad(){
        News news = session.load(News.class, 1);
        System.out.println(news.getClass().getName());
    }

    // update()
    // 1. 若更新一个持久化对象，不需要显式地调用 update() 方法，
    //    因为在调用 Transaction 的commit() 方法时，会先执行session的 flush()方法
    // 2. 更新一个游离对象，需要显式地调用session 的update() 方法，可以把游离对象变成持久化对象
    // 3. 注意点⚠️：
    //    1). (案例2)无论要更新的游离对象和数据库表的记录是否一致，都会发送update语句。
    //        在.hbm.xml文件的class节点设置select-before-update=true可以解决盲目发送update语句问题，
    //        默认false，通常不需设置该属性，除非有设置update触发器。
    //    2). (案例3) 若数据表中没有对应的记录，但还是调用 update方法，则抛出异常
    //    3). (案例4) 当update() 方法关联一个游离对象时，若在Session缓存内已存在相同OID的持久化对象，则抛出异常
    @Test
    public void testUpdate1(){
        News news = session.get(News.class, 1);
        news.setAuthor("Oracle");

//        session.update(news);   持久化对象在flush的时候自动发送update语句，故可省略
    }

    @Test
    public void testUpdate2(){
        News news = session.get(News.class, 1);
        transaction.commit();
        session.close();  // session关闭，对象变成游离状态

        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        news.setAuthor("Oracle");

        session.update(news);  // 对象进入session缓存中
    }

    @Test
    public void testUpdate3(){
        News news = session.get(News.class, 1);
        transaction.commit();
        session.close();  // session关闭，对象变成游离状态

        news.setId(100);  // 游离对象可以改ID
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();

        session.update(news);  // 对象进入session缓存中
    }

    @Test
    public void testUpdate4(){
        News news = session.get(News.class, 1);
        transaction.commit();
        session.close();  // session关闭，对象变成游离状态

        session = sessionFactory.openSession();
        transaction = session.beginTransaction();

        News news2 = session.get(News.class, 1); // 获取一个持久化对象，对象进入session缓存
        session.update(news);  // 对象进入session缓存中，变成持久化对象
        // 会报错，因为session缓存中不能存在相同OID的 持久化对象
    }

    // saveOrUpdate()
    // 1. 若OID不为空，但数据表中还没有和其对应的记录，会抛出异常
    // 2. 若OID值等于id 的unsaved-value属性值的对象，也被认为是一个临时对象（了解即可）
    public void testSaveOrUpdate(){
        News news = new News("FF", "ff", new Date(new java.util.Date().getTime()));
        session.saveOrUpdate(news);  // insert 语句

        news.setId(1);
        session.saveOrUpdate(news); // update 语句
    }

    // delete()：执行删除操作，只要OID和数据库中的一条记录对应，就会执行delete操作。若OID在数据表没有对应的记录，抛出异常。
    // 1. （案例2）可以通过设置 hibernate配置文件 hibernate.use_identifier_rollback 为true，使删除对象的OID值为null
    public void testDelete1(){
        News news = new News();
        news.setId(1);  // 游离对象
        session.delete(news);

        News news1 = session.get(News.class, 1); // 持久化对象
        session.delete(news1);
    }

    public void testDelete2(){
        News news = session.get(News.class, 1);
        session.delete(news); // 会在session的flush方法时发送delete语句

        System.out.println(news); // 此时对象依旧还有ID资讯
    }

//    evict： 从session 缓存中把指定的持久化对象移除
    @Test
    public void testEvict(){
        News news = session.get(News.class, 1);
        News news1 = session.get(News.class, 2);

        news.setAuthor("AAA");
        news1.setAuthor("BBB");

        session.evict(news);
    }


    @Test
    public void testDoWork(){
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                System.out.println(connection);   // 原生JDBC
//                调用存储过程
            }
        });
    }

}
