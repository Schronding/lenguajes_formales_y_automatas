document.addEventListener('DOMContentLoaded', () => {
    actualizarStockUI();
});

const inventario = {
    'A1': { nombre: 'Nito', precio: 18, stock: 5 },
    'A2': { nombre: 'Chetos', precio: 17, stock: 4 },
    'A3': { nombre: 'Sabritas', precio: 16, stock: 6 },
    'B1': { nombre: 'Del Valle', precio: 20, stock: 3 },
    'B2': { nombre: 'Mantecadas', precio: 22, stock: 4 },
    'B3': { nombre: 'Ruffles', precio: 17, stock: 0 },
};

let saldoActual = 0;
const pantalla = document.getElementById('pantalla');
const inputCodigo = document.getElementById('input-codigo');
const bandejaProducto = document.getElementById('bandeja-producto');
const bandejaCambio = document.getElementById('bandeja-cambio');

document.getElementById('btn-seleccionar').addEventListener('click', seleccionarProducto);
document.getElementById('btn-cancelar').addEventListener('click', cancelarTransaccion);

function actualizarStockUI() {
    for (const codigo in inventario) {
        const producto = inventario[codigo];
        const elProducto = document.querySelector(`.producto[data-codigo="${codigo}"]`);
        
        if (elProducto) {
            const elStock = elProducto.querySelector('.stock-contador');
            elStock.textContent = `Disp: ${producto.stock}`;
            
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

function insertarDinero(monto) {
    saldoActual += monto;
    actualizarPantalla(`Saldo: $${saldoActual.toFixed(2)}`);
    limpiarSalidas();
}

function seleccionarProducto() {
    const codigo = inputCodigo.value.toUpperCase();
    limpiarSalidas();

    if (!inventario[codigo]) {
        actualizarPantalla(`Código "${codigo}" no existe.`);
        return;
    }

    const producto = inventario[codigo];

    if (producto.stock === 0) {
        actualizarPantalla("Producto agotado.");
        return;
    }

    if (saldoActual < producto.precio) {
        actualizarPantalla(`Saldo insuficiente. $${saldoActual.toFixed(2)} devueltos.`);
        entregarCambio(saldoActual);
        saldoActual = 0;
        return;
    }

    const cambio = saldoActual - producto.precio;
    producto.stock--;
    saldoActual = 0;

    entregarProducto(producto.nombre);
    entregarCambio(cambio);
    actualizarPantalla(`Gracias por su compra!`);
    actualizarStockUI();
    inputCodigo.value = "";
}

function cancelarTransaccion() {
    if (saldoActual > 0) {
        entregarCambio(saldoActual);
        saldoActual = 0;
        actualizarPantalla("Transacción cancelada. Dinero devuelto.");
    } else {
        actualizarPantalla("Bienvenido");
    }
    limpiarSalidas();
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
    if (monto > 0) {
        bandejaCambio.textContent = `$${monto.toFixed(2)}`;
    } else {
        bandejaCambio.textContent = "$0.00";
    }
}

function limpiarSalidas() {
    bandejaProducto.textContent = "";
    bandejaCambio.textContent = "";
}