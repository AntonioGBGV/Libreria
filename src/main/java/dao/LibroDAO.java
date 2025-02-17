package dao;

import database.HibernateUtil;
import model.Autor;
import model.Editorial;
import model.Libro;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.Transaction;

import java.util.List;

public class LibroDAO {

    private Session session;

    public LibroDAO() {
    }


    public void insertarLibro(Libro libro) {
        session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();

            // Recuperar el autor desde la base de datos
            Autor autor = session.get(Autor.class, libro.getAutor().getId());
            libro.setAutor(autor);
            // Recuperar la editorial desde la base de datos
            Editorial editorial = session.get(Editorial.class, libro.getEditorial().getId());
            libro.setEditorial(editorial);

            session.persist(libro);
            transaction.commit();
            System.out.println("Libro insertado correctamente: " + libro);
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            System.err.println("Error al insertar el libro: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Libro obtenerLibroPorId(int id) {
        Session session = null;
        Libro libro = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            libro = session.get(Libro.class, id);
        } catch (Exception e) {
            System.err.println("Error al obtener el libro: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return libro;
    }

    public void actualizarLibro(Libro libro) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.merge(libro);
            transaction.commit();
            System.out.println("Libro actualizado correctamente: " + libro);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error al actualizar el libro: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<Libro> obtenerTodosLosLibros() {
        List<Libro> listaLibros = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String querySTR = "select l from Libro l";
            Query<Libro> query = session.createQuery(querySTR, Libro.class);
            listaLibros = query.list();

            if (listaLibros == null || listaLibros.isEmpty()) {
                System.out.println("No hay libros en la base de datos.");
            } else {
                System.out.println("Lista de libros:");
                for (Libro l : listaLibros) {
                    System.out.println(l);
                }
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error al seleccionar los libros: " + e.getMessage());
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return listaLibros;
    }
}
