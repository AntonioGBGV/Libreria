package dao;

import database.HibernateUtil;
import model.Libreria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class LibreriaDAO {

    private Session session;

    public LibreriaDAO(){
    }

    public void insertarLibreria(Libreria libreria) {
        session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            session.persist(libreria);
            transaction.commit();
            System.out.println("Libreria insertada correctamente: " + libreria);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error al insertar la libreria: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void actualizarLibreria(Libreria libreria) {
        session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.merge(libreria);
            transaction.commit();
            System.out.println("Librería actualizada correctamente: " + libreria);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error al actualizar la librería: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<Libreria> obtenerTodasLasLibrerias() {
        List<Libreria> listaLibrerias = null; // Declaramos la variable fuera del try
        session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String querySTR = "select l from Libreria l";
            Query<Libreria> query = session.createQuery(querySTR, Libreria.class);
            listaLibrerias = query.list();

            if (listaLibrerias.isEmpty()) {
                System.out.println("No hay librerías en la base de datos.");
            } else {
                System.out.println("Lista de librerías:");
                for (Libreria l : listaLibrerias) {
                    System.out.println(l);
                }
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error al seleccionar las librerías: " + e.getMessage());
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        // Si la lista es null, retornamos una lista vacía
        return listaLibrerias;
    }
}
