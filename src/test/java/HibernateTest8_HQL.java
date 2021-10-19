import com.alberta.hibernate.entities.hql.Department;
import com.alberta.hibernate.entities.hql.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;


public class HibernateTest8_HQL {
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

    @Test
    public void testHQL(){
//  1。創建Query對象
//        基於位置參數
//        String hql = "FROM Employee e WHERE e.id > ?1 and e.email like ?2";
//        基於命名參數
//        String hql = "FROM Employee e WHERE e.id > :id and e.email like :email";
        String hql = "FROM Employee e WHERE e.id > :id and e.email like :email and e.department = ?1";


        Query query = session.createQuery(hql);

//  2。綁定參數
//        Query對象調用setParameter支持方法的編成風格
//        query.setParameter(1, 5).setParameter(2, "%qq%");
//        query.setParameter("id", 5).setParameter("email", "%qq%");
        Department department = new Department();
        department.setId(1);
        query.setParameter("id", 5).setParameter("email", "%qq%").setParameter(1, department);
//        3。執行查詢
        List<Employee> list = query.list();
        System.out.println(list);

    }

    @Test
    public void testPageQuery(){
        String hql = "from Employee";
        Query query = session.createQuery(hql);
        int pageNo = 2;
        int pageSize = 3;

        query.setFirstResult((pageNo - 1) * pageSize)
                .setMaxResults(pageSize);
        List<Employee> list = query.list();
        System.out.println(list);
    }

    @Test
    public void testNamedQuery(){
        Query query = session.getNamedQuery("lastNameEmps");
        List list = query.setParameter("startAlphat", "a").setParameter("endAlphat", "e").list();
        System.out.println(list);
    }

    @Test
    public void testFieldQuery(){
        String hql = "SELECT e.email, e.lastName from Employee e WHERE e.department = :dept";
        Query query = session.createQuery(hql);
        Department department = new Department();
        department.setId(2);
        List<Object[]> result = query.setParameter("dept", department).list();
        for (Object[] objs : result){
            System.out.println(Arrays.asList(objs));
        }
    }

    @Test
    public void testFieldQuery2(){
        String hql = "SELECT new Employee(e.email, e.lastName) from Employee e WHERE e.department = :dept";
        Query query = session.createQuery(hql);
        Department department = new Department();
        department.setId(2);
        List<Employee> result = query.setParameter("dept", department).list();
        System.out.println(result);
    }

    @Test
    public void testGroupBy(){
        String hql = "SELECT min(e.salary) , max(e.salary)"
                + "FROM Employee e GROUP BY e.dept" +
                "Having min(salary) > :minSal";
    }

    @Test
    public void testLeftJoinFetch(){
        String hql = "FROM Department d LEFT JOIN FETCH d.emps";
//        String hql = "select distinct  d FROM Department d LEFT JOIN FETCH d.emps";   去重方法1

        List<Department> list = session.createQuery(hql).list();
//        list = new ArrayList<>(new LinkedHashSet<>(list));   去重方法2
        System.out.println(list.size());
    }

    @Test
    public void testLeftJoin(){
        String hql = "SELECT distinct d FROM Department d LEFT JOIN d.emps";

//        String hql = "FROM Department d LEFT JOIN d.emps";

//        List<Object[]> list = session.createQuery(hql).list();
//        list = new ArrayList<>(new LinkedHashSet<>(list));  無法去重，因每個數組不一樣
        List<Department> list = session.createQuery(hql).list();
        System.out.println(list.size());
    }

    @Test
    public void testQBC(){
//        1. 創建一個Criteria對象
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Employee> query = criteriaBuilder.createQuery(Employee.class);
//        2. 添加查詢條件
        Predicate restriction = query.getRestriction();

//        3. 執行查詢
    }


}
