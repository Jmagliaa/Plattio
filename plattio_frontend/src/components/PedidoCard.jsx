import React from 'react';
import '../styles/PedidoCard.css';

const PedidoCard = ({ pedido, onMarcarPlato, onAbrirModal, onCambiarEstado }) => {
    const handleEstadoClick = () => {
        const nuevoEstado = pedido.estadoReal === "pendiente" ? "en_preparacion" : "finalizado";
        onCambiarEstado(pedido.id, nuevoEstado);
    };

    return (
        <div className="pedido-card">
            <div className="pedido-header">
                <div className="icon-expandir" onClick={() => onAbrirModal(pedido)}>
                    🔍
                </div>
                <div className="mesa-numero">{pedido.mesa}</div>

                {/* Botón de estado */}
                <button
                    className="estado-boton"
                    onClick={() => onCambiarEstado(pedido.id, pedido.estado === "Comenzar" ? "en_preparacion" : "pendiente")}
                >
                    {pedido.estado}
                </button>


                <div className="hora-pedido">{pedido.tiempo}</div>
            </div>

            <div className="pedido-items-scroll">
                {pedido.items.map((item, i) => (
                    <div className="plato-item" key={i}>
                        <div className="plato-info">
                            <div className="plato-nombre">{item.nombre}</div>
                            <div className="plato-detalle">{item.detalle}</div>
                        </div>
                        <div className="plato-tiempo">{item.tiempo}′</div>
                        <input
                            type="checkbox"
                            checked={item.finalizado}
                            onChange={() => onMarcarPlato(pedido.id, item.id)}
                        />
                    </div>
                ))}
            </div>

            <div className="pedido-footer">
                <div className="btn-input-disabled" title="Input aún no disponible">📥</div>
                <div className="btn-comentarios" title="Ver comentarios del pedido">💬</div>
            </div>
        </div>
    );
};

export default PedidoCard;
