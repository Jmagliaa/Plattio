import React, { useState, useEffect } from 'react';
import PedidoColumn from './PedidoColumn';
import OrdenamientoBar from './OrdenamientoBar';
import '../styles/CocinaBoard.css';

const CocinaBoard = () => {
    const [pendientes, setPendientes] = useState([]);
    const [enProceso, setEnProceso] = useState([]);

    const [ordenPendientes, setOrdenPendientes] = useState("llegada"); // llegada o timer
    const [ordenProceso, setOrdenProceso] = useState("llegada");

    useEffect(() => {
        loadPedidos("pendiente", ordenPendientes);
        loadPedidos("en_preparacion", ordenProceso);
    }, []);

    const onMarcarPlato = (pedidoId, itemId) => {
        console.log(`Marcar como listo el item ${itemId} del pedido ${pedidoId}`);
    };

    const onAbrirModal = (pedido) => {
        console.log("Abrir detalles del pedido:", pedido);
    };

    const ordenarPor = (criterio) => {
        if (criterio === 'llegada-pendientes') {
            setOrdenPendientes("llegada");
            setPendientes(prev => [...prev].sort((a, b) => a.fechaInicio - b.fechaInicio));
        }

        if (criterio === 'llegada-proceso') {
            setOrdenProceso("llegada");
            setEnProceso(prev => [...prev].sort((a, b) => a.fechaInicio - b.fechaInicio));
        }

        if (criterio === 'timer-pendientes') {
            setOrdenPendientes("timer");
            setPendientes(prev => [...prev].sort((a, b) => tiempoASegundos(b.tiempo) - tiempoASegundos(a.tiempo)));
        }

        if (criterio === 'timer-proceso') {
            setOrdenProceso("timer");
            setEnProceso(prev => [...prev].sort((a, b) => tiempoASegundos(a.tiempo) - tiempoASegundos(b.tiempo)));
        }
    };

    const onCambiarEstado = (pedidoId, nuevoEstado) => {
        fetch(`http://localhost:8080/pedidos/${pedidoId}/cambiarEstado/${nuevoEstado}`, {
            method: "POST"
        })
            .then(response => {
                if (!response.ok) throw new Error("Error al cambiar el estado");
                return response.text();
            })
            .then(data => {
                console.log("Respuesta del servidor:", data);
                loadPedidos("pendiente", ordenPendientes);
                loadPedidos("en_preparacion", ordenProceso);
            })
            .catch(error => {
                console.error("Error:", error);
            });
    };

    const loadPedidos = (estado, criterioOrden) => {
        fetch(`http://localhost:8080/pedidos/estado/${estado}`)
            .then(response => {
                if (!response.ok) throw new Error("Error al obtener los pedidos");
                return response.json();
            })
            .then(data => {
                const pedidosFormateados = data.map(p => {
                    const maxTiempo = Math.max(...p.items.map(i => i.tiempo));
                    const tiempoAjustado = Math.ceil(maxTiempo * 1.3);
                    const tiempoFormateado = `${String(tiempoAjustado).padStart(2, '0')}:00`;

                    let tituloBoton = p.estado === "pendiente" ? "Comenzar" : "Detener";

                    return {
                        id: p.id,
                        mesa: p.numMesa,
                        estado: tituloBoton,
                        fechaInicio: parseFechaStr(p.fechaInicio),
                        tiempo: tiempoFormateado,
                        items: p.items.map(i => ({
                            id: i.id,
                            nombre: i.nombre,
                            detalle: i.detalle,
                            tiempo: i.tiempo,
                            finalizado: i.finalizado
                        }))
                    };
                });

                // aplicar orden
                let pedidosOrdenados = pedidosFormateados;
                if (criterioOrden === "llegada") {
                    pedidosOrdenados = pedidosOrdenados.sort((a, b) => a.fechaInicio - b.fechaInicio);
                } else if (criterioOrden === "timer") {
                    pedidosOrdenados = pedidosOrdenados.sort((a, b) => {
                        return estado === "pendiente"
                            ? tiempoASegundos(b.tiempo) - tiempoASegundos(a.tiempo) // descendente
                            : tiempoASegundos(a.tiempo) - tiempoASegundos(b.tiempo); // ascendente
                    });
                }

                if (estado === "pendiente") {
                    setPendientes(pedidosOrdenados);
                } else if (estado === "en_preparacion") {
                    setEnProceso(pedidosOrdenados);
                }
            })
            .catch(error => console.error("Error cargando pedidos:", error));
    };

    const parseFechaStr = (fechaStr) => {
        if (typeof fechaStr !== "string") return new Date("Invalid");
        const [fecha, hora] = fechaStr.split(" ");
        if (!fecha || !hora) return new Date("Invalid");
        const [dd, mm, yyyy] = fecha.split("/");
        return new Date(`${yyyy}-${mm}-${dd}T${hora}`);
    };

    const tiempoASegundos = (tiempoStr) => {
        if (typeof tiempoStr !== "string") return 0;
        const [min, seg] = tiempoStr.split(":").map(Number);
        return (min * 60) + seg;
    };

    return (
        <div className="cocina-container">
            <OrdenamientoBar ordenarPor={ordenarPor} />
            <div className="cocina-tablero">
                <PedidoColumn
                    titulo="Pendientes"
                    pedidos={pendientes}
                    onMarcarPlato={onMarcarPlato}
                    onAbrirModal={onAbrirModal}
                    onCambiarEstado={onCambiarEstado}
                />
                <PedidoColumn
                    titulo="En proceso"
                    pedidos={enProceso}
                    onMarcarPlato={onMarcarPlato}
                    onAbrirModal={onAbrirModal}
                    onCambiarEstado={onCambiarEstado}
                />
            </div>
        </div>
    );
};

export default CocinaBoard;
