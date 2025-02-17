import model.Autor;
import model.Editorial;
import model.Libro;
import model.Libreria;
import dao.AutorDAO;
import dao.EditorialDAO;
import dao.LibroDAO;
import dao.LibreriaDAO;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class GestorLibreria {

    private AutorDAO autorDAO = new AutorDAO();
    private EditorialDAO editorialDAO = new EditorialDAO();
    private LibroDAO libroDAO = new LibroDAO();
    private LibreriaDAO libreriaDAO = new LibreriaDAO();
    private Scanner scanner = new Scanner(System.in);

    public void ejecutar() {
        int opcion;

        do {
            System.out.println("\n--- Menú ---");
            System.out.println("1. Dar de alta un autor");
            System.out.println("2. Dar de alta una editorial");
            System.out.println("3. Dar de alta un libro");
            System.out.println("4. Dar de alta una librería");
            System.out.println("5. Mostrar todos los libros con su editorial y autor");
            System.out.println("6. Mostrar todos los autores con sus libros asociados");
            System.out.println("7. Mostrar todas las librerías con sus libros asociados");
            System.out.println("8. Mostrar todos los libros y en qué librería están");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    darDeAltaAutor();
                    break;
                case 2:
                    darDeAltaEditorial();
                    break;
                case 3:
                    darDeAltaLibro();
                    break;
                case 4:
                    darDeAltaLibreria();
                    break;
                case 5:
                    mostrarLibrosConEditorialYAutor();
                    break;
                case 6:
                    mostrarAutoresConLibros();
                    break;
                case 7:
                    mostrarLibreriasConLibros();
                    break;
                case 8:
                    mostrarLibrosYLibrerias();
                    break;
                case 9:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 9);

        scanner.close();
    }

    private void darDeAltaAutor() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el nombre del autor: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese los apellidos del autor: ");
        String apellidos = scanner.nextLine();

        System.out.print("Fecha de nacimiento (yyyy-MM-dd): ");
        String fechaStr = scanner.nextLine();
        Date fechaNac = java.sql.Date.valueOf(fechaStr);

        Autor autor = new Autor(nombre, apellidos, fechaNac);
        autorDAO.insertarAutor(autor);

    }

    private void darDeAltaEditorial() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nIngrese los datos de la editorial:");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();

        Editorial editorial = new Editorial(nombre, direccion);
        editorialDAO.insertarEditorial(editorial);
    }

    private void darDeAltaLibro() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nIngrese los datos del libro:");
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Precio: ");
        Double precio = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("ID del autor: ");
        int idAutor = scanner.nextInt();
        scanner.nextLine();

        Autor autor = autorDAO.obtenerAutorPorId(idAutor);
        if (autor == null) {
            System.out.println("No se encontró un autor con el ID: " + idAutor);
            return;
        }

        System.out.print("ID de la editorial: ");
        int idEditorial = scanner.nextInt();
        scanner.nextLine();
        Editorial editorial = editorialDAO.obtenerEditorialPorId(idEditorial);
        if (editorial == null) {
            System.out.println("No se encontró una editorial con el ID: " + idEditorial);
            return;
        }

        Libro libro = new Libro(titulo, precio);
        libro.setAutor(autor);
        libro.setEditorial(editorial);

        libroDAO.insertarLibro(libro);
    }

    private void darDeAltaLibreria() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nIngrese los datos de la librería:");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Dueño: ");
        String duenio = scanner.nextLine();
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();

        Libreria libreria = new Libreria(nombre, duenio, direccion);
        libreriaDAO.insertarLibreria(libreria);

        for (int i = 1; i <= 1; i++) {
            System.out.print("ID del libro " + i + " para la librería: ");
            int idLibro = scanner.nextInt();
            scanner.nextLine();

            Libro libro = libroDAO.obtenerLibroPorId(idLibro);
            if (libro != null) {
                libreria.getListaLibros().add(libro);
                libro.getListaLibrerias().add(libreria);
                libroDAO.actualizarLibro(libro);
            } else {
                System.out.println("No se encontró un libro con el ID: " + idLibro);
            }
        }
        libreriaDAO.actualizarLibreria(libreria);
    }

    private void mostrarLibrosConEditorialYAutor() {
        List<Libro> libros = libroDAO.obtenerTodosLosLibros();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            for (Libro libro : libros) {
                System.out.println("Libro: " + libro.getTitulo() + ", Precio: " + libro.getPrecio());
                if (libro.getEditorial() != null) {
                    System.out.println("  Editorial: " + libro.getEditorial().getNombre());
                } else {
                    System.out.println("  Editorial: N/A");
                }
                if (libro.getAutor() != null) {
                    System.out.println("  Autor: " + libro.getAutor().getNombre() + " " + libro.getAutor().getApellidos());
                } else {
                    System.out.println("  Autor: N/A");
                }
            }
        }
    }

    private void mostrarAutoresConLibros() {
        List<Autor> autores = autorDAO.obtenerTodosLosAutores();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            for (Autor autor : autores) {
                System.out.println("Autor: " + autor.getNombre() + " " + autor.getApellidos());
                if (autor.getListaLibros() != null && !autor.getListaLibros().isEmpty()) {
                    System.out.println("  Libros asociados:");
                    for (Libro libro : autor.getListaLibros()) {
                        System.out.println("    - " + libro.getTitulo());
                    }
                } else {
                    System.out.println("  No tiene libros asociados.");
                }
            }
        }
    }

    private void mostrarLibreriasConLibros() {
        List<Libreria> librerias = libreriaDAO.obtenerTodasLasLibrerias();
        if (librerias.isEmpty()) {
            System.out.println("No hay librerías registradas.");
        } else {
            for (Libreria libreria : librerias) {
                System.out.println("Librería: " + libreria.getNombre());
                if (libreria.getListaLibros() != null && !libreria.getListaLibros().isEmpty()) {
                    System.out.println("  Libros asociados:");
                    for (Libro libro : libreria.getListaLibros()) {
                        System.out.println("    - " + libro.getTitulo());
                    }
                } else {
                    System.out.println("  No tiene libros asociados.");
                }

            }
        }
    }
    private void mostrarLibrosYLibrerias() {
        List<Libro> libros = libroDAO.obtenerTodosLosLibros();
            if (libros.isEmpty()) {
                System.out.println("No hay libros registrados.");
            } else {
                for (Libro libro : libros) {
                    System.out.println("Libro: " + libro.getTitulo());
                    if (libro.getListaLibrerias() != null && !libro.getListaLibrerias().isEmpty()) {
                        System.out.println("  Se encuentra en las siguientes librerías:");
                        for (Libreria libreria : libro.getListaLibrerias()) {
                            System.out.println("    - " + libreria.getNombre());
                        }
                    } else {
                        System.out.println("  No está asociado a ninguna librería.");
                    }
                }
            }
    }
}