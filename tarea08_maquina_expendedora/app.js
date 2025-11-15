document.addEventListener('DOMContentLoaded', () => {
    actualizarStockUI();
    actualizarPantallaSaldo(); 
});

const inventario = {
    'A1': { nombre: 'Nito', precio: 18, stock: 5 },
    'A2': { nombre: 'Chetos', precio: 17, stock: 4 },
    'A3': { nombre: 'Sabritas', precio: 16, stock: 6 },
    'B1': { nombre: 'Del Valle', precio: 20, stock: 3 },
    'B2': { nombre: 'Mantecadas', precio: 22, stock: 4 },
    'B3': { nombre: 'Ruffles', precio: 17, stock: 1 },
};

let saldoActual = 0;
const pantalla = document.getElementById('pantalla');
const inputCodigo = document.getElementById('input-codigo');
const bandejaProducto = document.getElementById('bandeja-producto');
const bandejaCambio = document.getElementById('bandeja-cambio');

document.getElementById('btn-seleccionar').addEventListener('click', seleccionarProducto);
document.getElementById('btn-cancelar').addEventListener('click', pedirCambio); 

function actualizarStockUI() {
    for (const codigo in inventario) {
        const producto = inventario[codigo];
        const elProducto = document.querySelector(`.producto[data-codigo="${codigo}"]`);
        
        if (elProducto) {
            const elStock = elProducto.querySelector('.stock-contador');
            elStock.textContent = `Disponibles: ${producto.stock}`;
            
            if (producto.stock === 0) {
                elProducto.classList.add('agotado');
            } else {
                elProducto.classList.remove('agotado');
            }
        }
    }
}

function actualizarPantalla(mensaje) {
    pantalla.textContent = mensaje;
}

function actualizarPantallaSaldo() {
    pantalla.textContent = `Saldo: $${saldoActual.toFixed(2)}`;
}

function insertarDinero(monto) {
    saldoActual += monto;
    actualizarPantallaSaldo();
    limpiarSalidas();
}

function seleccionarProducto() {
    const codigo = inputCodigo.value.toUpperCase();
    limpiarSalidas();

    if (!inventario[codigo]) {
        actualizarPantalla(`Código "${codigo}" no válido`);
        setTimeout(actualizarPantallaSaldo, 2000); 
        return;
    }

    const producto = inventario[codigo];

    if (producto.stock === 0) {
        actualizarPantalla("Producto Agotado");
        setTimeout(actualizarPantallaSaldo, 2000);
        return;
    }

    if (saldoActual < producto.precio) {
        const faltante = producto.precio - saldoActual;
        actualizarPantalla(`Faltan $${faltante.toFixed(2)}`);
        setTimeout(actualizarPantallaSaldo, 2000);
        return;
    }

    saldoActual -= producto.precio;
    producto.stock--;

    entregarProducto(producto.nombre);
    
    actualizarPantalla(`Entregando: ${producto.nombre}`);
    
    setTimeout(() => {
        actualizarPantallaSaldo();
    }, 2000);

    actualizarStockUI();
    inputCodigo.value = "";
}

function pedirCambio() {
    if (saldoActual > 0) {
        entregarCambio(saldoActual);
        saldoActual = 0;
        actualizarPantalla("Cambio devuelto");
        setTimeout(actualizarPantallaSaldo, 2000);
    } else {
        actualizarPantalla("No hay saldo");
        setTimeout(actualizarPantallaSaldo, 2000);
    }
    inputCodigo.value = "";
}

function entregarProducto(nombreProducto) {
    bandejaProducto.textContent = nombreProducto;
    bandejaProducto.classList.add('producto-entregado');
    
    setTimeout(() => {
        bandejaProducto.classList.remove('producto-entregado');
    }, 500);
}

function entregarCambio(monto) {
    bandejaCambio.textContent = `$${monto.toFixed(2)}`;
    bandejaCambio.classList.add('producto-entregado');
    setTimeout(() => {
        bandejaCambio.classList.remove('producto-entregado');
    }, 500);
}

function limpiarSalidas() {
    bandejaProducto.textContent = "";
    bandejaCambio.textContent = "";
}