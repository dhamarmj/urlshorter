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

    public List<Visit> getVisitbyBrowser(long id) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT e FROM Visit e where url_id = :id", Visit.class);
        query.setParameter("id", id);
        List<Visit> list = query.getResultList();
        return list;
    }


}
