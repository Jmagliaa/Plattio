package com.plattio.plattio_backend.controller;

import com.plattio.plattio_backend.exceptions.*;
import com.plattio.plattio_backend.modelo.*;
import com.plattio.plattio_backend.service.*;
import com.plattio.plattio_backend.views.PedidoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
//@RequestMapping("/empleados")
public class controllerPlattio {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private ItemPedidoService itemPedidoService;

    @Autowired
    private MesaService mesaService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PlatoService platoService;

    @Autowired
    private SesionMesaService sesionMesaService;

    @GetMapping("/")
    public String mensaje() {
        return """
                recetas culinarias
                """;
    }

    // Endpoint para probar excepción
    @GetMapping("/probar-excepcion/{email}")
    public Empleado probarExcepcion(@PathVariable String email) throws EmpleadoException {
        return empleadoService.buscarPorEmail(email);
    }

//    ENDPOINTS MESA --------------------------------------------------------------
//    GET
    @GetMapping("/mesas")
    public ResponseEntity<List<Mesa>> obtenerTodasLasMesas() throws MesaException {
        return new ResponseEntity<>(mesaService.obtenerTodas(), HttpStatus.OK);
    }

    @GetMapping("/mesas/{id}")
    public ResponseEntity<Mesa> obtenerMesaPorId(@PathVariable Long id) throws MesaException {
        return new ResponseEntity<>(mesaService.obtenerPorId(id), HttpStatus.OK);
    }

    @GetMapping("/mesas/numero/{numero}")
    public ResponseEntity<Mesa> obtenerMesaPorNumero(@PathVariable Integer numero) throws MesaException {
        return new ResponseEntity<>(mesaService.obtenerPorNumero(numero), HttpStatus.OK);
    }

    @GetMapping("/mesas/qr/{qrToken}")
    public ResponseEntity<Mesa> obtenerMesaPorQr(@PathVariable String qrToken) throws MesaException {
        return new ResponseEntity<>(mesaService.obtenerPorQrToken(qrToken), HttpStatus.OK);
    }

    @GetMapping("/mesas/estado/{estado}")
    public ResponseEntity<List<Mesa>> obtenerMesasPorEstado(@PathVariable String estado) throws MesaException {
        return new ResponseEntity<>(mesaService.obtenerPorEstado(estado), HttpStatus.OK);
    }

    @GetMapping("/mesas/{id}/libre")
    public ResponseEntity<Boolean> estaLibre(@PathVariable Long id) throws MesaException {
        return new ResponseEntity<>(mesaService.estaLibre(id), HttpStatus.OK);
    }

    @GetMapping("/mesas/{id}/ocupada")
    public ResponseEntity<Boolean> estaOcupada(@PathVariable Long id) throws MesaException {
        return new ResponseEntity<>(mesaService.estaOcupada(id), HttpStatus.OK);
    }

    @GetMapping("/mesas/{id}/sesion-activa")
    public ResponseEntity<Boolean> tieneSesionActiva(@PathVariable Long id) throws MesaException {
        return new ResponseEntity<>(mesaService.tieneSesionActiva(id), HttpStatus.OK);
    }

    //POST
    @PostMapping("/mesas")
    public ResponseEntity<String> crearMesa(@RequestBody Mesa mesa) throws MesaException {
        mesaService.crearMesa(mesa);
        return new ResponseEntity<>("Mesa creada con éxito", HttpStatus.CREATED);
    }

    @PostMapping("/mesas/{id}/ocupar")
    public ResponseEntity<String> ocuparMesa(@PathVariable Long id) throws MesaException {
        mesaService.ocuparMesa(id);
        return new ResponseEntity<>("Mesa ocupada con éxito", HttpStatus.OK);
    }

    @PostMapping("/mesas/{id}/liberar")
    public ResponseEntity<String> liberarMesa(@PathVariable Long id) throws MesaException {
        mesaService.liberarMesa(id);
        return new ResponseEntity<>("Mesa liberada con éxito", HttpStatus.OK);
    }

    @PostMapping("/mesas/{id}/estado/{nuevoEstado}")
    public ResponseEntity<String> cambiarEstadoMesa(@PathVariable Long id, @PathVariable String nuevoEstado) throws MesaException {
        mesaService.cambiarEstado(id, nuevoEstado);
        return new ResponseEntity<>("Estado de la mesa actualizado con éxito", HttpStatus.OK);
    }

    //    PEDIDO-------------------------
    //    GET ----------------------------------
    @GetMapping("/pedidos")
    public ResponseEntity<List<Pedido>> obtenerTodosLosPedidos() throws PedidoException {
        return new ResponseEntity<>(pedidoService.obtenerTodos(), HttpStatus.OK);
    }

    @GetMapping("/pedidos/{id}")
    public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable Long id) throws PedidoException {
        return new ResponseEntity<>(pedidoService.obtenerPorId(id), HttpStatus.OK);
    }

    @GetMapping("/pedidos/sesion/{sesionId}")
    public ResponseEntity<List<Pedido>> obtenerPedidosPorSesion(@PathVariable Long sesionId) throws PedidoException {
        return new ResponseEntity<>(pedidoService.obtenerPorSesion(sesionId), HttpStatus.OK);
    }

//    @GetMapping("/pedidos/estado/{estado}")
//    public ResponseEntity<List<Pedido>> obtenerPedidosPorEstado(@PathVariable String estado) throws PedidoException {
//        return new ResponseEntity<>(pedidoService.obtenerPorEstado(estado), HttpStatus.OK);
//    }

    @GetMapping("/pedidos/estado/{estado}")
    public ResponseEntity<List<PedidoView>> obtenerPedidosPorEstado(@PathVariable String estado) throws PedidoException {
        List<Pedido> pedidos = pedidoService.obtenerPorEstado(estado);
        List<PedidoView> views = pedidos.stream()
                .map(Pedido::toView)
                .toList();
        return new ResponseEntity<>(views, HttpStatus.OK);
    }

    @GetMapping("/pedidos/{id}/total")
    public ResponseEntity<Double> calcularTotalDelPedido(@PathVariable Long id) throws PedidoException {
        return new ResponseEntity<>(pedidoService.calcularTotalPedido(id), HttpStatus.OK);
    }
    
    //    POST-------------------------
    @PostMapping("/pedidos/{sesionId}/{categoria}")
    public ResponseEntity<String> crearPedido(@PathVariable Long sesionId, @PathVariable String categoria) throws PedidoException {
        pedidoService.crearPedido(sesionId, categoria);
        return new ResponseEntity<>("Pedido creado con éxito", HttpStatus.CREATED);
    }

    @PostMapping("/pedidos/{id}/iniciar")
    public ResponseEntity<String> iniciarPreparacion(@PathVariable Long id) throws PedidoException {
        pedidoService.iniciarPreparacion(id);
        return new ResponseEntity<>("Preparación del pedido iniciada", HttpStatus.OK);
    }

    @PostMapping("/pedidos/{id}/cambiarEstado/{estado}")
    public ResponseEntity<String> CambiarEstado(@PathVariable Long id, @PathVariable String estado) throws PedidoException {
        pedidoService.cambiarEstado(id, estado);
        return new ResponseEntity<>("Estado cambiado correctamente", HttpStatus.OK);
    }

    @PostMapping("/pedidos/{id}/finalizar")
    public ResponseEntity<String> finalizarPedido(@PathVariable Long id) throws PedidoException {
        pedidoService.finalizarPedido(id);
        return new ResponseEntity<>("Pedido finalizado con éxito", HttpStatus.OK);
    }

    @PostMapping("/pedidos/{id}/cancelar")
    public ResponseEntity<String> cancelarPedido(@PathVariable Long id) throws PedidoException {
        pedidoService.cancelarPedido(id);
        return new ResponseEntity<>("Pedido cancelado con éxito", HttpStatus.OK);
    }

    //    TIEM PEDIDO---------------------------------
    //    GET --------------------------------------------------------
    @GetMapping("/items/pedido/{pedidoId}")
    public ResponseEntity<List<ItemPedido>> obtenerItemsPorPedido(@PathVariable Long pedidoId) throws ItemPedidoException {
        return new ResponseEntity<>(itemPedidoService.obtenerPorPedido(pedidoId), HttpStatus.OK);
    }

    @GetMapping("/items/estado/{estado}")
    public ResponseEntity<List<ItemPedido>> obtenerItemsPorEstado(@PathVariable String estado) {
        return new ResponseEntity<>(itemPedidoService.obtenerPorEstado(estado), HttpStatus.OK);
    }

    @GetMapping("/items/activos")
    public ResponseEntity<List<ItemPedido>> obtenerItemsActivos() {
        return new ResponseEntity<>(itemPedidoService.obtenerActivos(), HttpStatus.OK);
    }

    @GetMapping("/items/{itemId}/subtotal")
    public ResponseEntity<Double> calcularSubtotalItem(@PathVariable Long itemId) throws ItemPedidoException {
        return new ResponseEntity<>(itemPedidoService.calcularSubtotal(itemId), HttpStatus.OK);
    }

    //    POST
    @PostMapping("/items/{pedidoId}/{platoId}/{cantidad}/{nota}")
    public ResponseEntity<String> agregarItemAPedido(
            @PathVariable Long pedidoId,
            @PathVariable Long platoId,
            @PathVariable int cantidad,
            @PathVariable String nota) throws ItemPedidoException {

        itemPedidoService.agregarItem(pedidoId, platoId, cantidad, nota);
        return new ResponseEntity<>("Ítem agregado al pedido con éxito", HttpStatus.CREATED);
    }

    @PostMapping("/items/{itemId}/iniciar")
    public ResponseEntity<String> iniciarPreparacionItem(@PathVariable Long itemId) throws ItemPedidoException {
        itemPedidoService.iniciarPreparacion(itemId);
        return new ResponseEntity<>("Preparación del ítem iniciada", HttpStatus.OK);
    }

    @PostMapping("/items/{itemId}/listo")
    public ResponseEntity<String> marcarItemListo(@PathVariable Long itemId) throws ItemPedidoException {
        itemPedidoService.marcarListo(itemId);
        return new ResponseEntity<>("Ítem marcado como listo", HttpStatus.OK);
    }

    @PostMapping("/items/{itemId}/entregar")
    public ResponseEntity<String> entregarItem(@PathVariable Long itemId) throws ItemPedidoException {
        itemPedidoService.entregarItem(itemId);
        return new ResponseEntity<>("Ítem entregado", HttpStatus.OK);
    }

    @PostMapping("/items/{itemId}/cancelar")
    public ResponseEntity<String> cancelarItem(@PathVariable Long itemId) throws ItemPedidoException {
        itemPedidoService.cancelarItem(itemId);
        return new ResponseEntity<>("Ítem cancelado", HttpStatus.OK);
    }

    //SESION MESA:
    @GetMapping("/sesiones")
    public ResponseEntity<List<SesionMesa>> obtenerTodasLasSesiones() throws SesionMesaException {
        return new ResponseEntity<>(sesionMesaService.obtenerTodas(), HttpStatus.OK);
    }

    @GetMapping("/sesiones/{id}")
    public ResponseEntity<SesionMesa> obtenerSesionPorId(@PathVariable Long id) throws SesionMesaException {
        return new ResponseEntity<>(sesionMesaService.buscarPorId(id), HttpStatus.OK);
    }

    @GetMapping("/sesiones/mesa/{mesaId}/activa")
    public ResponseEntity<SesionMesa> obtenerSesionActivaPorMesa(@PathVariable Long mesaId) throws SesionMesaException {
        return new ResponseEntity<>(sesionMesaService.obtenerSesionActivaPorMesa(mesaId), HttpStatus.OK);
    }

    @GetMapping("/sesiones/mozo/{mozoId}/activas")
    public ResponseEntity<List<SesionMesa>> obtenerSesionesActivasPorMozo(@PathVariable Long mozoId) throws SesionMesaException {
        return new ResponseEntity<>(sesionMesaService.obtenerSesionesActivasPorMozo(mozoId), HttpStatus.OK);
    }

    @PostMapping("/sesiones/iniciar/{mesaId}/{tipoComensal}")
    public ResponseEntity<String> iniciarSesionSinMozo(@PathVariable Long mesaId, @PathVariable String tipoComensal) throws SesionMesaException, MesaException {
        sesionMesaService.iniciarSesion(mesaId, null, tipoComensal);
        return new ResponseEntity<>("Sesión iniciada sin mozo", HttpStatus.CREATED);
    }

    @PostMapping("/sesiones/iniciar/{mesaId}/{mozoId}/{tipoComensal}")
    public ResponseEntity<String> iniciarSesionConMozo(@PathVariable Long mesaId, @PathVariable Long mozoId, @PathVariable String tipoComensal) throws SesionMesaException, MesaException {
        sesionMesaService.iniciarSesion(mesaId, mozoId, tipoComensal);
        return new ResponseEntity<>("Sesión iniciada con mozo", HttpStatus.CREATED);
    }

    @PostMapping("/sesiones/{sesionId}/finalizar")
    public ResponseEntity<String> finalizarSesion(@PathVariable Long sesionId) throws SesionMesaException {
        sesionMesaService.finalizarSesion(sesionId);
        return new ResponseEntity<>("Sesión finalizada con éxito", HttpStatus.OK);
    }

    @PostMapping("/sesiones/{sesionId}/cerrarSiSinPedidos")
    public ResponseEntity<String> cerrarSesionSiNoHayPedidos(@PathVariable Long sesionId) throws SesionMesaException {
        sesionMesaService.cerrarSesionSiNoHayPedidos(sesionId);
        return new ResponseEntity<>("Sesión cerrada si no tenía pedidos", HttpStatus.OK);
    }

    @PostMapping("/sesiones/{sesionId}/reasignarMozo/{nuevoMozoId}")
    public ResponseEntity<String> reasignarMozoASesion(@PathVariable Long sesionId, @PathVariable Long nuevoMozoId) throws SesionMesaException {
        sesionMesaService.reasignarMozo(sesionId, nuevoMozoId);
        return new ResponseEntity<>("Mozo reasignado a la sesión con éxito", HttpStatus.OK);
    }

    //    PLATO SERVICE

    @GetMapping("/platos")
    public ResponseEntity<List<Plato>> obtenerTodosLosPlatos() throws PlatoException {
        return new ResponseEntity<>(platoService.obtenerTodos(), HttpStatus.OK);
    }

    @GetMapping("/platos/{id}")
    public ResponseEntity<Plato> obtenerPlatoPorId(@PathVariable Long id) throws PlatoException {
        return new ResponseEntity<>(platoService.buscarPorId(id), HttpStatus.OK);
    }

    @GetMapping("/platos/categoria/{categoria}")
    public ResponseEntity<List<Plato>> obtenerPlatosPorCategoria(@PathVariable String categoria) throws PlatoException {
        return new ResponseEntity<>(platoService.buscarPorCategoria(categoria), HttpStatus.OK);
    }

    @GetMapping("/platos/nombre/{nombreParcial}")
    public ResponseEntity<List<Plato>> buscarPlatosPorNombre(@PathVariable String nombreParcial) throws PlatoException {
        return new ResponseEntity<>(platoService.buscarPorNombre(nombreParcial), HttpStatus.OK);
    }

    @GetMapping("/platos/precioMenorA/{max}")
    public ResponseEntity<List<Plato>> buscarPlatosPorPrecio(@PathVariable
                                                                 BigDecimal max) throws PlatoException {
        return new ResponseEntity<>(platoService.buscarPorPrecioMenorA(max), HttpStatus.OK);
    }

    @GetMapping("/platos/rapidos/{minutosMax}")
    public ResponseEntity<List<Plato>> buscarPlatosRapidos(@PathVariable int minutosMax) throws PlatoException {
        return new ResponseEntity<>(platoService.buscarPlatosRapidos(minutosMax), HttpStatus.OK);
    }

    @PostMapping("/platos")
    public ResponseEntity<String> crearPlato(@RequestBody Plato plato) throws PlatoException {
        platoService.crearPlato(plato);
        return new ResponseEntity<>("Plato creado con éxito", HttpStatus.CREATED);
    }

    @PostMapping("/platos/{id}/actualizar")
    public ResponseEntity<String> actualizarPlato(
            @PathVariable Long id,
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam BigDecimal precio,
            @RequestParam String categoria,
            @RequestParam Integer tiempoEstimado) throws PlatoException {

        platoService.actualizarPlato(id, nombre, descripcion, precio, categoria, tiempoEstimado);
        return new ResponseEntity<>("Plato actualizado con éxito", HttpStatus.OK);
    }

/*    @PostMapping("/platos/{id}/cambiarPrecio")
    public ResponseEntity<String> cambiarPrecioPlato(
            @PathVariable Long id,
            @RequestParam BigDecimal nuevoPrecio) throws PlatoException {

        platoService.cambiarPrecio(id, nuevoPrecio);
        return new ResponseEntity<>("Precio del plato actualizado con éxito", HttpStatus.OK);
    }*/

    @PostMapping("/platos/{id}/cambiarPrecio/{nuevoPrecio}")
    public ResponseEntity<String> cambiarPrecioPlato(
            @PathVariable Long id,
            @PathVariable BigDecimal nuevoPrecio) throws PlatoException {

        platoService.cambiarPrecio(id, nuevoPrecio);
        return new ResponseEntity<>("Precio del plato actualizado con éxito", HttpStatus.OK);
    }

    @DeleteMapping("/platos/{id}")
    public ResponseEntity<String> eliminarPlato(@PathVariable Long id) throws PlatoException {
        platoService.eliminar(id);
        return new ResponseEntity<>("Plato eliminado con éxito", HttpStatus.OK);
    }

    //EMPLEADO SERVICE
    @GetMapping("/empleados")
    public ResponseEntity<List<Empleado>> obtenerTodosLosEmpleados() throws EmpleadoException {
        return new ResponseEntity<>(empleadoService.obtenerTodos(), HttpStatus.OK);
    }

    @GetMapping("/empleados/{id}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorId(@PathVariable Long id) throws EmpleadoException {
        return new ResponseEntity<>(empleadoService.buscarPorId(id), HttpStatus.OK);
    }

    @GetMapping("/empleados/email/{email}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorEmail(@PathVariable String email) throws EmpleadoException {
        return new ResponseEntity<>(empleadoService.buscarPorEmail(email), HttpStatus.OK);
    }

    @GetMapping("/empleados/nombre/{nombreParcial}")
    public ResponseEntity<List<Empleado>> buscarEmpleadosPorNombre(@PathVariable String nombreParcial) throws EmpleadoException {
        return new ResponseEntity<>(empleadoService.buscarPorNombre(nombreParcial), HttpStatus.OK);
    }

    @GetMapping("/empleados/rol/{rol}")
    public ResponseEntity<List<Empleado>> buscarEmpleadosPorRol(@PathVariable String rol) throws EmpleadoException {
        return new ResponseEntity<>(empleadoService.buscarPorRol(rol), HttpStatus.OK);
    }

    @PostMapping("/empleados")
    public ResponseEntity<String> registrarEmpleado(@RequestBody Empleado nuevo) throws EmpleadoException {
        empleadoService.registrarEmpleado(nuevo);
        return new ResponseEntity<>("Empleado registrado con éxito", HttpStatus.CREATED);
    }

    @PostMapping("/empleados/login/{email}/{password}")
    public ResponseEntity<Empleado> loginEmpleado(@PathVariable String email, @PathVariable String password) throws EmpleadoException {
        Empleado empleado = empleadoService.login(email, password);
        return new ResponseEntity<>(empleado, HttpStatus.OK);
    }

/*    @PostMapping("/empleados/{id}/actualizar")
    public ResponseEntity<String> actualizarDatosEmpleado(
            @PathVariable Long id,
            @RequestParam String nuevoNombre,
            @RequestParam String nuevoEmail) throws EmpleadoException {

        empleadoService.actualizarDatos(id, nuevoNombre, nuevoEmail);
        return new ResponseEntity<>("Datos del empleado actualizados con éxito", HttpStatus.OK);
    }*/

    @PostMapping("/empleados/{id}/actualizar/{nuevoNombre}/{nuevoEmail}")
    public ResponseEntity<String> actualizarDatosEmpleado(
            @PathVariable Long id,
            @PathVariable String nuevoNombre,
            @PathVariable String nuevoEmail) throws EmpleadoException {

        empleadoService.actualizarDatos(id, nuevoNombre, nuevoEmail);
        return new ResponseEntity<>("Datos del empleado actualizados con éxito", HttpStatus.OK);
    }

    @PostMapping("/empleados/{id}/cambiarRol/{nuevoRol}")
    public ResponseEntity<String> cambiarRolEmpleado(
            @PathVariable Long id,
            @PathVariable String nuevoRol) throws EmpleadoException {

        empleadoService.cambiarRol(id, nuevoRol);
        return new ResponseEntity<>("Rol del empleado actualizado con éxito", HttpStatus.OK);
    }

    @DeleteMapping("/empleados/{id}")
    public ResponseEntity<String> eliminarEmpleado(@PathVariable Long id) throws EmpleadoException {
        empleadoService.eliminar(id);
        return new ResponseEntity<>("Empleado eliminado con éxito", HttpStatus.OK);
    }











}
