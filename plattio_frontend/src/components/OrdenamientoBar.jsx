// src/components/OrdenamientoBar.js
import React from 'react';
import '../styles/OrdenamientoBar.css';

const OrdenamientoBar = ({ ordenarPor }) => {
    return (
        <div className="ordenamiento-bar-container">
            <div className="ordenamiento-grupo izquierda">
                <button className="menu-btn">☰</button>
                <button onClick={() => ordenarPor('inteligente')}>🧠 Inteligente</button>
                <button onClick={() => ordenarPor('llegada-pendientes')}>🍽️ Por llegada</button>
                <button onClick={() => ordenarPor('timer-pendientes')}>⏱️ Por timer</button>
                <button onClick={() => ordenarPor('personalizado')}>🛠️ Personalizado</button>
            </div>
            <div className="ordenamiento-grupo derecha">
                <button onClick={() => ordenarPor('llegada-proceso')}>🍽️ Por llegada</button>
                <button onClick={() => ordenarPor('timer-proceso')}>⏱️ Por timer</button>
            </div>
        </div>
    );
};

export default OrdenamientoBar;
