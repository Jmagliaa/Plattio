
package com.plattio.plattio_backend.views;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoView {

    private Long id;
    private String fechaInicio;
    private String fechaFin;
    private String estado;
    private String categoria;
    private int numMesa;
    private List<ItemPedidoView> items;
    private int tiempo;

    public PedidoView() {
    }

    public PedidoView(Long id, String fechaInicio, String fechaFin, String estado, String categoria, int numMesa, List<ItemPedidoView> items) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.categoria = categoria;
        this.numMesa = numMesa;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getNumMesa() {
        return numMesa;
    }

    public void setMesaId(int numMesa) {
        this.numMesa = numMesa;
    }

    public List<ItemPedidoView> getItems() {
        return items;
    }

    public void setItems(List<ItemPedidoView> items) {
        this.items = items;
    }
}
