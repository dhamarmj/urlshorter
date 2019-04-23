package edu.pucmm.dhamarmj.Services;

import edu.pucmm.dhamarmj.Encapsulation.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class UserServices  extends DatabaseServices<User> {
    private static UserServices instancia;
    int pageSize = 5;
    private UserServices() {
        super(User.class);
    }

    public static UserServices getInstancia() {
        if (instancia == null) {
            instancia = new UserServices();
        }
        return instancia;
    }

    public User getUser(String username, String password) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select e from User e where e.username like :username and e.password like :password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        List<User> list = query.getResultList();
        if (list.size() >0)
            return list.get(0);
        else
            return null;

    }

}
