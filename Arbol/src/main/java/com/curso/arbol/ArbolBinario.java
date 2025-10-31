package com.curso.arbol;

import java.util.*;

public class ArbolBinario {
    public Nodo raiz;

    // -------------------- Estado básico --------------------
    public boolean estaVacio() {
        return raiz == null;
    }

    // -------------------- Insertar / Eliminar --------------------
    public void agregar(int valor) {
        raiz = agregarRecursivo(raiz, valor);
    }

    private Nodo agregarRecursivo(Nodo actual, int valor) {
        if (actual == null) return new Nodo(valor);
        if (valor < actual.valor) actual.izquierdo = agregarRecursivo(actual.izquierdo, valor);
        else if (valor > actual.valor) actual.derecho = agregarRecursivo(actual.derecho, valor);
        // si es igual, no se agrega (evita duplicados)
        return actual;
    }

    public void eliminar(int valor) {
        raiz = eliminarRecursivo(raiz, valor);
    }

    private Nodo eliminarRecursivo(Nodo actual, int valor) {
        if (actual == null) return null;

        if (valor < actual.valor) {
            actual.izquierdo = eliminarRecursivo(actual.izquierdo, valor);
        } else if (valor > actual.valor) {
            actual.derecho = eliminarRecursivo(actual.derecho, valor);
        } else {
            // encontrado
            // caso 1: sin hijos
            if (actual.izquierdo == null && actual.derecho == null) {
                return null;
            }
            // caso 2: un hijo
            if (actual.izquierdo == null) return actual.derecho;
            if (actual.derecho == null) return actual.izquierdo;
            // caso 3: dos hijos -> reemplazar por sucesor (menor del subárbol derecho)
            int menor = obtenerValorMenor(actual.derecho);
            actual.valor = menor;
            actual.derecho = eliminarRecursivo(actual.derecho, menor);
        }
        return actual;
    }

    private int obtenerValorMenor(Nodo nodo) {
        Nodo actual = nodo;
        while (actual.izquierdo != null) actual = actual.izquierdo;
        return actual.valor;
    }

    public void borrarArbol() {
        raiz = null;
    }

    // -------------------- Búsqueda / existencia --------------------
    public boolean existe(int valor) {
        return existeRecursivo(raiz, valor);
    }

    private boolean existeRecursivo(Nodo nodo, int valor) {
        if (nodo == null) return false;
        if (nodo.valor == valor) return true;
        return valor < nodo.valor ? existeRecursivo(nodo.izquierdo, valor) : existeRecursivo(nodo.derecho, valor);
    }

    // -------------------- Peso / Altura / Nivel / Hojas --------------------
    public int obtenerPeso() {
        return contarNodos(raiz);
    }

    private int contarNodos(Nodo nodo) {
        if (nodo == null) return 0;
        return 1 + contarNodos(nodo.izquierdo) + contarNodos(nodo.derecho);
    }

    public int obtenerAltura() {
        return alturaRecursiva(raiz);
    }

    private int alturaRecursiva(Nodo nodo) {
        if (nodo == null) return 0; // altura en nodos; si prefieres en aristas devuelve -1 para nodo null
        return 1 + Math.max(alturaRecursiva(nodo.izquierdo), alturaRecursiva(nodo.derecho));
    }

    public int contarHojas() {
        return contarHojasRecursivo(raiz);
    }

    private int contarHojasRecursivo(Nodo nodo) {
        if (nodo == null) return 0;
        if (nodo.izquierdo == null && nodo.derecho == null) return 1;
        return contarHojasRecursivo(nodo.izquierdo) + contarHojasRecursivo(nodo.derecho);
    }

    public int obtenerNivel(int valor) {
        return obtenerNivelRecursivo(raiz, valor, 1); // nivel de la raíz = 1
    }

    private int obtenerNivelRecursivo(Nodo nodo, int valor, int nivel) {
        if (nodo == null) return -1;
        if (nodo.valor == valor) return nivel;
        int izq = obtenerNivelRecursivo(nodo.izquierdo, valor, nivel + 1);
        if (izq != -1) return izq;
        return obtenerNivelRecursivo(nodo.derecho, valor, nivel + 1);
    }

    // -------------------- Menor / Mayor --------------------
    public Integer obtenerNodoMenor() {
        if (raiz == null) return null;
        Nodo actual = raiz;
        while (actual.izquierdo != null) actual = actual.izquierdo;
        return actual.valor;
    }

    public Integer obtenerNodoMayor() {
        if (raiz == null) return null;
        Nodo actual = raiz;
        while (actual.derecho != null) actual = actual.derecho;
        return actual.valor;
    }

    // -------------------- Recorridos --------------------
    public List<Integer> inOrden() {
        List<Integer> lista = new ArrayList<>();
        inOrdenRecursivo(raiz, lista);
        return lista;
    }

    private void inOrdenRecursivo(Nodo nodo, List<Integer> lista) {
        if (nodo != null) {
            inOrdenRecursivo(nodo.izquierdo, lista);
            lista.add(nodo.valor);
            inOrdenRecursivo(nodo.derecho, lista);
        }
    }

    public List<Integer> preOrden() {
        List<Integer> lista = new ArrayList<>();
        preOrdenRecursivo(raiz, lista);
        return lista;
    }

    private void preOrdenRecursivo(Nodo nodo, List<Integer> lista) {
        if (nodo != null) {
            lista.add(nodo.valor);
            preOrdenRecursivo(nodo.izquierdo, lista);
            preOrdenRecursivo(nodo.derecho, lista);
        }
    }

    public List<Integer> postOrden() {
        List<Integer> lista = new ArrayList<>();
        postOrdenRecursivo(raiz, lista);
        return lista;
    }

    private void postOrdenRecursivo(Nodo nodo, List<Integer> lista) {
        if (nodo != null) {
            postOrdenRecursivo(nodo.izquierdo, lista);
            postOrdenRecursivo(nodo.derecho, lista);
            lista.add(nodo.valor);
        }
    }

    // -------------------- Amplitud / Nivel específico --------------------
    public List<Integer> imprimirAmplitud() {
        List<Integer> res = new ArrayList<>();
        if (raiz == null) return res;
        Queue<Nodo> cola = new LinkedList<>();
        cola.add(raiz);
        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();
            res.add(actual.valor);
            if (actual.izquierdo != null) cola.add(actual.izquierdo);
            if (actual.derecho != null) cola.add(actual.derecho);
        }
        return res;
    }
}
