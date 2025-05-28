
package com.plattio.plattio_backend.views;

public class ItemPedidoView {

    private Long id;
    private String nombre;
    private String detalle;
    private int tiempo;
    private boolean finalizado;

    public ItemPedidoView() {
    }

    public ItemPedidoView(Long id, String nombre, String detalle, int tiempo, boolean finalizado) {
        this.id = id;
        this.nombre = nombre;
        this.detalle = detalle;
        this.tiempo = tiempo;
        this.finalizado = finalizado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }
}
