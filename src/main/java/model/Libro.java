package model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table (name="libros")

public class Libro implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String titulo;
    @Column
    private Double precio;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id_autor")
    private Autor autor;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id_editorial")
    private Editorial editorial;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "colecciones",
            joinColumns = @JoinColumn(name = "id_libro"),
            inverseJoinColumns = @JoinColumn(name = "id_libreria"))
    private List<Libreria> listaLibrerias = new ArrayList<>();



    public Libro(String titulo) {
        this.titulo = titulo;
    }

    public Libro(String titulo, Double precio) {
        this.titulo = titulo;
        this.precio = precio;
    }
}
