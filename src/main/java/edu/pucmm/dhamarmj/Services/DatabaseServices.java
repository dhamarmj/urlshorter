package edu.pucmm.dhamarmj.Services;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.List;

public class DatabaseServices<T> {
    private Class<T> claseEntidad;
    private static EntityManagerFactory emf;

    public DatabaseServices(Class<T> claseEntidad) {
        if(emf == null) {
            emf = Persistence.createEntityManagerFactory("PersistenceUnit");
        }
        this.claseEntidad = claseEntidad;
    }

    public EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    private Object getValorCampo(T entidad){
        if(entidad == null){
            return null;
        }
        //aplicando la clase de reflexión.
        for(Field f : entidad.getClass().getDeclaredFields()) {  //tomando todos los campos privados.
            if (f.isAnnotationPresent(Id.class)) {
                try {
                    f.setAccessible(true);
                    Object valorCampo = f.get(entidad);
                    return valorCampo;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     *
     * @param entidad
     */
    public void insert(T entidad){
        EntityManager em = getEntityManager();

        try {
            if (em.find(claseEntidad, getValorCampo(entidad)) != null) {
                System.out.println("ERROR: La entidad a guardar existe, no creada.");
                return;
            }
        }catch (IllegalArgumentException ie){
            //
            System.out.println("ERROR: Parametro ilegal.");
        }

        em.getTransaction().begin();
        try {
            em.persist(entidad);
            em.getTransaction().commit();

        }catch (Exception ex){
            em.getTransaction().rollback();
            throw  ex;
        } finally {
            em.close();
        }
    }

    /**
     *
     * @param entidad
     */
    public void modificar(T entidad){
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(entidad);
            em.getTransaction().commit();
        }catch (Exception ex){
            em.getTransaction().rollback();
            throw  ex;
        } finally {
            em.close();
        }
    }

    /**
     *
     * @param entidadId
     */
    public void eliminar(Object entidadId){
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            T entidad = em.find(claseEntidad, entidadId);
            em.remove(entidad);
            em.getTransaction().commit();
        }catch (Exception ex){
            em.getTransaction().rollback();
            throw  ex;
        } finally {
            em.close();
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public T buscar(Object id) {
        EntityManager em = getEntityManager();
        try{
            return em.find(claseEntidad, id);
        } catch (Exception ex){
            throw  ex;
        } finally {
            em.close();
        }
    }


    /**
     *
     * @return
     */
    public List<T> buscarTodos(){
        EntityManager em = getEntityManager();
        try{
            CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(claseEntidad);
            criteriaQuery.select(criteriaQuery.from(claseEntidad));
            return em.createQuery(criteriaQuery).getResultList();
        } catch (Exception ex){
            throw  ex;
        }finally {
            em.close();
        }
    }
    public List<T> buscarPaginado(int page, int size){
        EntityManager em = getEntityManager();
        try{
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = cb.createQuery(claseEntidad);
            Root<T> from = criteriaQuery.from(claseEntidad);
            CriteriaQuery<T> select = criteriaQuery.select(from);
            Query query = em.createQuery(select);
            query.setFirstResult((page-1) * size);
            query.setMaxResults(size);
            return  query.getResultList();
        } catch (Exception ex){
            throw  ex;
        }finally {
            em.close();
        }
    }

}
