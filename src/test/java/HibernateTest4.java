import com.alberta.hibernate.entities.both.Customer;
import com.alberta.hibernate.entities.both.Order;
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

public class HibernateTest4 {
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
        Department department = new Department();
        department.setDepartmentName("DEPT-AA");

        Manager manager = new Manager();
        manager.setManagerName("MGR-AA");

//        設定關聯關係
        department.setManager(manager);
        manager.setDepartment(department);

//        保存操作
        session.save(manager);  // 先保存沒有外鍵列的那個對象，減少update語句
        session.save(department);
    }

    @Test
    public void testGet(){
//        1。默認情況下對關聯對象使用懶加載
//        2。所以會出現懶加載異常問題
        Department department = session.get(Department.class, 1);
        System.out.println(department.getDepartmentName());

//        3. 查詢Manager對象的連結條件應該是dept.manager_id = mgr.manager_id
        Manager manager= department.getManager();
        System.out.println(manager.getManagerName());
    }

    @Test
    public void testGet2(){
//        在查詢沒有外鍵的實體對象時，使用的左外連結查詢，一併查詢出其關聯的對象，並已經初始化

        Manager manager = session.get(Manager.class, 1);
        System.out.println(manager.getManagerName());
        System.out.println(manager.getDepartment().getDepartmentName());

    }

}
