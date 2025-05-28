package com.plattio.plattio_backend.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "plato")
public class Plato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column
    private String categoria;

    @Column(name = "tiempo_estimado")
    private Integer tiempoEstimado;

    // ---------- Constructores ----------

    public Plato() {}

    public Plato(String nombre, String descripcion, BigDecimal precio, String categoria, Integer tiempoEstimado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.tiempoEstimado = tiempoEstimado;
    }

    // ---------- Getters y Setters ----------

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(Integer tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    // ---------- METODOS ----------
    public boolean esCategoria(String cat) {
        return this.categoria != null && this.categoria.equalsIgnoreCase(cat);
    }

    public void cambiarPrecio(BigDecimal nuevoPrecio) {
        if (nuevoPrecio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        }
        this.precio = nuevoPrecio;
    }

    public boolean esPrecioValido() {
        return this.precio != null && this.precio.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean esRapido() {
        return tiempoEstimado != null && tiempoEstimado <= 10;
    }

    public void actualizarDatos(String nombre, String descripcion, BigDecimal precio, String categoria, Integer tiempoEstimado) {
        setNombre(nombre);
        setDescripcion(descripcion);
        setPrecio(precio);
        setCategoria(categoria);
        setTiempoEstimado(tiempoEstimado);
    }

//    PARA EL FUTURO SI ES QUE TRABAJAMOS CON OPCIONES
//    public boolean tieneOpciones() {
//        return !this.opciones.isEmpty();
//    }

    // ---------- toString ----------

    @Override
    public String toString() {
        return "Plato{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", categoria='" + categoria + '\'' +
                ", tiempoEstimado=" + tiempoEstimado +
                '}';
    }
}
