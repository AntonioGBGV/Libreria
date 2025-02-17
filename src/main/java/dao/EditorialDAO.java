package dao;

import database.HibernateUtil;
import model.Editorial;
import model.Libreria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class EditorialDAO {
    private Session session;

    public EditorialDAO(){
    }

    public void insertarEditorial(Editorial editorial) {
        session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            session.persist(editorial);
            transaction.commit();
            System.out.println("Editorial insertada correctamente: " + editorial);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error al insertar la editorial: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Editorial obtenerEditorialPorId(int id) {
        session = null;
        Editorial editorial = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            editorial = session.get(Editorial.class, id);
        } catch (Exception e) {
            System.err.println("Error al obtener la editorial: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return editorial;
    }

    public void seleccionarTodasLasEditoriales() {
        session = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            String querySTR = "select e from Editorial e";
            Query<Editorial> query = session.createQuery(querySTR, Editorial.class);
            List<Editorial> listaEditoriales = query.list();

            if (listaEditoriales.isEmpty()) {
                System.out.println("No hay editoriales en la base de datos.");
            } else {
                System.out.println("Lista de editoriales:");
                for (Editorial e : listaEditoriales) {
                    System.out.println(e);
                }
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error al seleccionar las editoriales: " + e.getMessage());
            if (session.getTransaction() != null) session.getTransaction().rollback();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
