package edu.pucmm.dhamarmj.Services;

import edu.pucmm.dhamarmj.Encapsulation.Url;
import edu.pucmm.dhamarmj.Encapsulation.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class UrlServices  extends DatabaseServices<Url>  {
    private static UrlServices instancia;

    private UrlServices() {
        super(Url.class);
    }

    public static UrlServices getInstancia() {
        if (instancia == null) {
            instancia = new UrlServices();
        }
        return instancia;
    }

//    public Url getUrl(String url, long Userid ) {
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("select e from Header e join e.urls l where l.id like :Userid and url like :url", Url.class);
//        query.setParameter("url", url);
//        query.setParameter("Userid", Userid);
//        List<Url> list = query.getResultList();
//        if (list.size() > 0)
//            return list.get(0);
//        else
//            return null;
//    }

    public Url getUrl(String redirect) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select e from Url e where redirect = :redirect", Url.class);
        query.setParameter("redirect", redirect);
        List<Url> list = query.getResultList();
        if (list.size() > 0)
            return list.get(0);
        else
            return null;
    }
//    public void EliminarbyCode(String id) {
//        EntityManager em = getEntityManager();
//        Query query = em.createQuery("delete from Url where id = :id", Url.class);
//        query.setParameter("id", id);
//        query.executeUpdate();
//    }

}
