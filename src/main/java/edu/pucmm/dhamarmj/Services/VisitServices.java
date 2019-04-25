package edu.pucmm.dhamarmj.Services;

import edu.pucmm.dhamarmj.Encapsulation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class VisitServices extends DatabaseServices<Visit> {
    private static VisitServices instancia;

    private VisitServices() {
        super(Visit.class);
    }

    public static VisitServices getInstancia() {
        if (instancia == null) {
            instancia = new VisitServices();
        }
        return instancia;
    }

    public List<Visit> getVisitbyUrl(long id) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT e FROM Visit e where url_id = :id ", Visit.class);
        query.setParameter("id", id);
        List<Visit> list = query.getResultList();
        return list;
    }
//
//    public void getVisitbyBrowser(long id) {
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("SELECT e.browser as name, count(e.id) as value FROM Visit e group by e.browser");
//        //query.setParameter("id", id);
//        List<Object[]> list = query.getResultList();
//        list.stream().forEach((record) -> {
//            String name = (String) record[0];
//            int value = (int) record[1];
//            System.out.println(name + " " + value);
//        });
//
//    }


}
