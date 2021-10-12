import com.alberta.hibernate.entities.*;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.jdbc.Work;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class HibernateTest2 {
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
    public void testManyToOneSave(){
        Customer customer = new Customer();
        customer.setCustomerName("AAA");
        Order order = new Order();
        order.setOrderName("ORDER-1");

        Order order1 = new Order();
        order1.setOrderName("ORDER-2");

//        設定關聯關係
        order.setCustomer(customer);
        order1.setCustomer(customer);

//        執行 save 操作：先插入Customer，再插入Order，3條insert語句
        session.save(customer);
        session.save(order);
        session.save(order1);


//        若先插入Order，再插入Customer，3條Insert，2條update

    }

    @Test
    public  void testMany2OneGet(){
        Order order = session.get(Order.class, 1);
//        1.只發送SELECT語句查詢many端的對象，而沒有查詢關聯對象
        System.out.println(order.getOrderName());

//        4. 獲取Order 對象時，默認情況下，其關聯的Customer對象是一個代理對象
        System.out.println(order.getCustomer().getClass());

//       3. 在查詢關聯對象（查One端）時，可能會發生懶加載異常
//        session.close();

//        2.懶加載：只有當需要使用到關聯對象時，才發送對應的SQL語句
        System.out.println(order.getCustomer().getCustomerName());
    }

    @Test
    public void testUpdate(){
        Customer customer = session.get(Customer.class, 1);
        customer.setCustomerName("BBB");
    }

    @Test
    public void testDelete(){
//        在沒設定級聯關係的情況下，且One端的對象有多個對象在引用，不能直接刪除one端
        Customer customer = session.get(Customer.class, 1);
        session.delete(customer);
    }




}
