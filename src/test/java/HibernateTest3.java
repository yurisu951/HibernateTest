import com.alberta.hibernate.entities.both.Customer;
import com.alberta.hibernate.entities.both.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HibernateTest3 {
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

    @Test
//    測試雙向關聯
    public void testManyToOneSave(){
        Customer customer = new Customer();
        customer.setCustomerName("ABB");
        Order order = new Order();
        order.setOrderName("ORDER-3");

        Order order1 = new Order();
        order1.setOrderName("ORDER-4");

//        設定關聯關係
        order.setCustomer(customer);
        order1.setCustomer(customer);

        customer.getOrders().add(order);
        customer.getOrders().add(order1);

//        執行 save 操作：先插入Customer，再插入Order，3條insert語句，2條update語句
        // 因為one端和many端都維護關聯關係，所以多出update
        // 可以在one端的集合映射 set節點，inverse=true來使one端放棄關聯關係
//        session.save(customer);
//        session.save(order);
//        session.save(order1);


//        先插入Order，再插入Customer，3條Insert，4條update
        session.save(order);
        session.save(order1);
        session.save(customer);

    }

    @Test
    //    雙向關聯的獲取
    public void testOne2ManyGet(){
//        1. 對many端集合使用延遲加載
        Customer customer = session.get(Customer.class, 1);
        System.out.println(customer.getCustomerName());
//        2. 返回的many端集合是hibernate內置集合類型，該類型具有延遲加載和存放代理對象的功能
        System.out.println(customer.getOrders().getClass());
//        3. 可能會發生懶加載異常
//        4. 在需要使用集合中元素的時候進行初始化
    }

    @Test
    public void testUpdate2(){
        Customer customer = session.get(Customer.class, 2);
//        customer.getOrders().iterator().next().setOrderName();
    }

    @Test
    public void testUpdate(){
        Order order = session.get(Order.class, 2);
        order.getCustomer().setCustomerName("aaa");
    }

    @Test
    public void testCascade(){
        Customer customer = session.get(Customer.class, 1);
//        解除和customer的关联关系
        customer.getOrders().clear();
    }

    @Test
    public void testCascadeSave(){
        Customer customer = new Customer();
        customer.setCustomerName("ABB");
        Order order = new Order();
        order.setOrderName("ORDER-3");

        Order order1 = new Order();
        order1.setOrderName("ORDER-4");

//        設定關聯關係
        order.setCustomer(customer);
        order1.setCustomer(customer);

        customer.getOrders().add(order);
        customer.getOrders().add(order1);

//        執行 save 操作：先插入Customer，再插入Order，3條insert語句，2條update語句
        // 因為one端和many端都維護關聯關係，所以多出update
        // 可以在one端的集合映射 set節點，inverse=true來使one端放棄關聯關係
        session.save(customer);
        session.save(order);
        session.save(order1);


//        先插入Order，再插入Customer，3條Insert，4條update
//        session.save(order);
//        session.save(order1);
//        session.save(customer);

    }






}
