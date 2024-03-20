package com.fpoly.asm.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class JPAUtils {
    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;


    static {
        emf = Persistence.createEntityManagerFactory("OEToHong");
        em = getEntityManager();
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void shutdown() {
        emf.close();
    }

    public static <T> T create (T entity) {
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.flush();
            em.getTransaction().commit();
            return entity;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println(e.getMessage());
        }
        em.close();
        return null;
    }

    public static <T> T update (T entity) {
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.flush();
            em.getTransaction().commit();
            return entity;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println(e.getMessage());
        }
        em.close();
        return null;
    }

    public static <T,K> T remove (Class<T> tClass,K id) {
        try {
            em.getTransaction().begin();
            T entity = em.find(tClass,id);
            em.remove(entity);
            em.flush();
            em.getTransaction().commit();
            return entity;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println(e.getMessage());
        }
        em.close();
        return null;
    }

    public static <T> List<T> query(Class<T> tClass, String jpql, Object ...args) {
        try {
            List<T> list = null;
            em.getTransaction().begin();
            TypedQuery<T> query = em.createQuery(jpql, tClass);
            for(int i = 0;i<args.length;i++) {
                query.setParameter(i+1,args[i]);
            }
            list = query.getResultList();
            em.getTransaction().commit();
            return list;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println(e.getMessage());
        }
        em.close();
        return null;
    }

    public static <T> List<T> queryPage(Class<T> tClass, String jpql,int page,int size, Object ...args) {
        try {
            List<T> list = null;
            em.getTransaction().begin();
            TypedQuery<T> query = em.createQuery(jpql, tClass);
            for(int i = 0;i<args.length;i++) {
                query.setParameter(i+1,args[i]);
            }
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            list = query.getResultList();
            em.getTransaction().commit();
            return list;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println(e.getMessage());
        }
        em.close();
        return null;
    }

    public static <T,K> T findOne(Class<T> tClass,K id) {
        try {
            em.getTransaction().begin();
            T entity = em.find(tClass,id);
            em.getTransaction().commit();
            return entity;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println(e.getMessage());
        }
        em.close();
        return null;
    }

    public static <T> List<T> findPage(Class<T> tClass,String jpql,int page,int size) {
        try {
            List<T> list = null;
            em.getTransaction().begin();
            TypedQuery<T> query = em.createQuery(jpql, tClass);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            list = query.getResultList();
            em.getTransaction().commit();
            return list;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println(e.getMessage());
        }
        em.close();
        return null;
    }

}
