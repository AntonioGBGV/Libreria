package model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "listaLibros")

@Entity
@Table(name="librerias")

public class Libreria implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String nombre;
    @Column
    private String duenio;
    @Column
    private String direccion;

    @ManyToMany(mappedBy = "listaLibrerias", fetch = FetchType.EAGER)
    private List<Libro> listaLibros = new ArrayList<>();;

    public Libreria(String nombre, String duenio, String direccion) {
        this.nombre = nombre;
        this.duenio = duenio;
        this.direccion = direccion;
    }
}
