import com.alberta.hibernate.entities.n2n.Category;
import com.alberta.hibernate.entities.n2n.Item;
import com.alberta.hibernate.entities.one2one.Department;
import com.alberta.hibernate.entities.one2one.Manager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class HibernateTest5_N2N {
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
    public void testSave(){
        Category category1 = new Category();
        category1.setName("C-AA");
        Category category2 = new Category();
        category2.setName("C-BB");

        Item item1 = new Item();
        item1.setName("I-AA");
        Item item2 = new Item();
        item2.setName("I-AA");

//        設定關聯關係
        category1.getItems().add(item1);
        category1.getItems().add(item2);
        category2.getItems().add(item1);
        category2.getItems().add(item2);

//        設定雙向關聯關係
        item1.getCategories().add(category1);
        item1.getCategories().add(category2);
        item2.getCategories().add(category1);
        item2.getCategories().add(category2);


        session.save(category1);
        session.save(category2);

        session.save(item1);
        session.save(item2);
    }

    @Test
    public void testGet(){
        Category category = session.get(Category.class, 1);
//       需要連結中間表
        Set<Item> items = category.getItems();
        System.out.println(items);
    }

}
