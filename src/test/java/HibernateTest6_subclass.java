import com.alberta.hibernate.entities.subclass.Person;
import com.alberta.hibernate.entities.subclass.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


public class HibernateTest6_subclass {
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
        if (transaction != null)   transaction.commit();
        if (session != null) session.close();
        if (sessionFactory != null) sessionFactory.close();
    }

//    插入操作：
//    1。對於子類對象只需要把紀錄插入到一張數據表中。
//    2。辨別者列有hibernate 自動維護
    @Test
    public void testSave(){
        Person person = new Person();
        person.setName("AA");
        person.setAge(11);
        session.save(person);

        Student student = new Student();
        student.setAge(22);
        student.setName("BB");
        student.setSchool("ATGUIGU");

        session.save(student);
    }


//    1.查詢父類紀錄，只需要查詢一張數據表
//    2.對於子類紀錄，也只需要查詢一張數據表
    @Test
    public void testQuery(){
        List<Person> people = session.createQuery("FROM Person").list();
        System.out.println(people.size());

        List<Student> students = session.createQuery("FROM Student").list();
        System.out.println(students.size());
    }

//    缺點：
//    1。使用辨別者列。
//    2。子類獨有的字段不能添加非空約束。
//    3。若繼承層次較深，則數據表的字段也會較多


//    join_subclass
//    插入操作
//    1。對於子類對象至少需要插入到兩張數據表
@Test
public void testSave2(){
    Person person = new Person();
    person.setName("AA");
    person.setAge(11);
    session.save(person);

    Student student = new Student();
    student.setAge(22);
    student.setName("BB");
    student.setSchool("ATGUIGU");

    session.save(student);
}

//    1.查詢父類紀錄，做一個左外鏈接查詢
//    2.對於子類紀錄，做一個內連結查詢
    @Test
    public void testQuery2(){
        List<Person> people = session.createQuery("FROM Person").list();
        System.out.println(people.size());

        List<Student> students = session.createQuery("FROM Student").list();
        System.out.println(students.size());
    }
//    優點：
//    1。不需要使用辨別者列。
//    2。子類特有的字段能添加非空約束。
//    3。沒有冗余的字段。


//    UNION_SUBCLASS

    @Test
    public void testSave3(){
        Person person = new Person();
        person.setName("AA");
        person.setAge(11);
        session.save(person);

        Student student = new Student();
        student.setAge(22);
        student.setName("BB");
        student.setSchool("ATGUIGU");

        session.save(student);
    }

    //    1.查詢父類紀錄，需把父表和子表紀錄匯總在一起再做查詢，性能稍差
//    2.對於子類紀錄，只需要查一張表
    @Test
    public void testQuery3(){
        List<Person> people = session.createQuery("FROM Person").list();
        System.out.println(people.size());

        List<Student> students = session.createQuery("FROM Student").list();
        System.out.println(students.size());
    }

    //    優點：
//    1。不需要使用辨別者列。
//    2。子類特有的字段能添加非空約束。

//     缺點：
//    1。存在冗余的字段
//    2。若更新父表的字段，更新 效率較低
}
