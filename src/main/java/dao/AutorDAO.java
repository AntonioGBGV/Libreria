package dao;

import database.HibernateUtil;
import model.Autor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class AutorDAO {

    private Session session;

    public AutorDAO(){
    }

    public void insertarAutor(Autor autor) {
        session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            session.persist(autor);
            transaction.commit();
            System.out.println("Autor insertado correctamente: " + autor);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error al insertar el autor. " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Autor obtenerAutorPorId(int id) {
        session = null;
        Autor autor = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            autor = session.get(Autor.class, id);
        } catch (Exception e) {
            System.err.println("Error al obtener el autor: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return autor;
    }


    public List<Autor> obtenerTodosLosAutores() {
        List<Autor> listaAutores = null;
        session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String querySTR = "select a from Autor a";
            Query<Autor> query = session.createQuery(querySTR, Autor.class);
            listaAutores = query.list();

            if (listaAutores.isEmpty()) {
                System.out.println("No hay autores en la base de datos.");
            } else {
                System.out.println("Lista de autores:");
                for (Autor a : listaAutores) {
                    System.out.println(a);
                }
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error al seleccionar los autores: " + e.getMessage());
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return listaAutores;
    }
}
