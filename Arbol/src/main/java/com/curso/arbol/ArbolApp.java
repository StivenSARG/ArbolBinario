package com.curso.arbol;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Aplicación visual para manipular y dibujar un Árbol Binario en JavaFX
 */
public class ArbolApp extends Application {

    private ArbolBinario arbol = new ArbolBinario();
    private Pane panelDibujo;
    private TextArea areaTexto = new TextArea();

    private Label lblEstaVacio = new Label("Vacío: true");
    private Label lblPeso = new Label("Peso: 0");
    private Label lblAltura = new Label("Altura: 0");
    private Label lblHojas = new Label("Hojas: 0");
    private Label lblMenor = new Label("Menor: -");
    private Label lblMayor = new Label("Mayor: -");

    @Override
    public void start(Stage stage) {
        // ===== Campo y botones =====
        TextField txtValor = new TextField();
        txtValor.setPromptText("Ingrese valor (int)");

        Button btnAgregar = new Button("Agregar");
        Button btnEliminar = new Button("Eliminar");
        Button btnBorrar = new Button("Borrar Árbol");
        Button btnInorden = new Button("Inorden");
        Button btnPreorden = new Button("Preorden");
        Button btnPostorden = new Button("Postorden");
        Button btnAmplitud = new Button("Amplitud");
        Button btnExiste = new Button("¿Existe?");
        Button btnNivel = new Button("Nivel de...");
        Button btnActualizarInfo = new Button("Actualizar Info");

        HBox fila1 = new HBox(8, txtValor, btnAgregar, btnEliminar, btnBorrar);
        fila1.setAlignment(Pos.CENTER);

        HBox fila2 = new HBox(8, btnInorden, btnPreorden, btnPostorden, btnAmplitud);
        fila2.setAlignment(Pos.CENTER);

        HBox fila3 = new HBox(8, btnExiste, btnNivel, btnActualizarInfo);
        fila3.setAlignment(Pos.CENTER);

        VBox controles = new VBox(10, fila1, fila2, fila3);
        controles.setAlignment(Pos.CENTER);
        controles.setPadding(new Insets(10));
        controles.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #DDD;");

        // ===== Panel del árbol dentro de un ScrollPane =====
        panelDibujo = new Pane();
        panelDibujo.setMinSize(2000, 1200);
        panelDibujo.setStyle("-fx-background-color: white; -fx-border-color: #CCC;");

        Group group = new Group(panelDibujo);
        ScrollPane scrollArbol = new ScrollPane(group);
        scrollArbol.setPannable(true);
        scrollArbol.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollArbol.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        // ===== Panel lateral de información =====
        VBox panelInfo = new VBox(8, lblEstaVacio, lblPeso, lblAltura, lblHojas, lblMenor, lblMayor);
        panelInfo.setPadding(new Insets(10));
        panelInfo.setPrefWidth(160);

        // ===== Área inferior de texto =====
        areaTexto.setEditable(false);
        areaTexto.setPrefHeight(100);

        VBox panelInferior = new VBox(8, controles, areaTexto);
        panelInferior.setAlignment(Pos.CENTER);

        // ===== Layout principal =====
        BorderPane root = new BorderPane();
        root.setCenter(scrollArbol);
        root.setBottom(panelInferior);
        root.setRight(panelInfo);

        BorderPane.setMargin(panelInferior, new Insets(10));

        // ===== Habilitar arrastre con el mouse =====
        final double[] dragDelta = new double[2];
        group.setOnMousePressed(e -> {
            dragDelta[0] = e.getSceneX();
            dragDelta[1] = e.getSceneY();
        });
        group.setOnMouseDragged(e -> {
            scrollArbol.setHvalue(scrollArbol.getHvalue() - (e.getSceneX() - dragDelta[0]) / panelDibujo.getWidth());
            scrollArbol.setVvalue(scrollArbol.getVvalue() - (e.getSceneY() - dragDelta[1]) / panelDibujo.getHeight());
            dragDelta[0] = e.getSceneX();
            dragDelta[1] = e.getSceneY();
        });

        // ===== Eventos =====
        btnAgregar.setOnAction(e -> {
            try {
                int v = Integer.parseInt(txtValor.getText().trim());
                arbol.agregar(v);
                txtValor.clear();
                dibujarArbol();
                actualizarInfo();
            } catch (NumberFormatException ex) {
                mostrar("Ingrese un entero válido");
            }
        });

        btnEliminar.setOnAction(e -> {
            try {
                int v = Integer.parseInt(txtValor.getText().trim());
                arbol.eliminar(v);
                txtValor.clear();
                dibujarArbol();
                actualizarInfo();
            } catch (NumberFormatException ex) {
                mostrar("Ingrese un entero válido");
            }
        });

        btnBorrar.setOnAction(e -> {
            arbol.borrarArbol();
            panelDibujo.getChildren().clear();
            areaTexto.clear();
            actualizarInfo();
        });

        btnInorden.setOnAction(e -> areaTexto.setText("Inorden: " + arbol.inOrden()));
        btnPreorden.setOnAction(e -> areaTexto.setText("Preorden: " + arbol.preOrden()));
        btnPostorden.setOnAction(e -> areaTexto.setText("Postorden: " + arbol.postOrden()));
        btnAmplitud.setOnAction(e -> areaTexto.setText("Amplitud: " + arbol.imprimirAmplitud()));

        btnExiste.setOnAction(e -> {
            try {
                int v = Integer.parseInt(txtValor.getText().trim());
                mostrar("¿Existe " + v + "? " + arbol.existe(v));
            } catch (NumberFormatException ex) {
                mostrar("Ingrese un entero válido");
            }
        });

        btnNivel.setOnAction(e -> {
            try {
                int v = Integer.parseInt(txtValor.getText().trim());
                int nivel = arbol.obtenerNivel(v);
                mostrar(nivel == -1 ? "Valor no encontrado" : "Nivel de " + v + ": " + nivel);
            } catch (NumberFormatException ex) {
                mostrar("Ingrese un entero válido");
            }
        });

        btnActualizarInfo.setOnAction(e -> actualizarInfo());

        // ===== Mostrar escena =====
        Scene scene = new Scene(root, 1200, 750);
        stage.setTitle("Árbol Binario Visual con Métodos - JavaFX");
        stage.setScene(scene);
        stage.show();

        actualizarInfo();
    }

    // ===== Dibujar el árbol =====
    private void dibujarArbol() {
        panelDibujo.getChildren().clear();
        if (arbol.raiz != null) {
            double ancho = panelDibujo.getWidth();
            dibujarNodo(panelDibujo, arbol.raiz, ancho / 2, 40, ancho / 4);
        }
    }

    private void dibujarNodo(Pane panel, Nodo nodo, double x, double y, double offset) {
        if (nodo == null) return;

        // Izquierdo
        if (nodo.izquierdo != null) {
            double childX = x - offset;
            double childY = y + 80;
            Line line = new Line(x, y, childX, childY);
            line.setStrokeWidth(2);
            panel.getChildren().add(line);
            dibujarNodo(panel, nodo.izquierdo, childX, childY, offset / 1.6);
        }

        // Derecho
        if (nodo.derecho != null) {
            double childX = x + offset;
            double childY = y + 80;
            Line line = new Line(x, y, childX, childY);
            line.setStrokeWidth(2);
            panel.getChildren().add(line);
            dibujarNodo(panel, nodo.derecho, childX, childY, offset / 1.6);
        }

        // Nodo actual
        CircleNodeCirculo c = new CircleNodeCirculo(String.valueOf(nodo.valor), x, y);
        panel.getChildren().addAll(c.getCircle(), c.getText());
    }

    private void actualizarInfo() {
        lblEstaVacio.setText("Vacío: " + arbol.estaVacio());
        lblPeso.setText("Peso: " + arbol.obtenerPeso());
        lblAltura.setText("Altura: " + arbol.obtenerAltura());
        lblHojas.setText("Hojas: " + arbol.contarHojas());
        Integer menor = arbol.obtenerNodoMenor();
        Integer mayor = arbol.obtenerNodoMayor();
        lblMenor.setText("Menor: " + (menor == null ? "-" : menor));
        lblMayor.setText("Mayor: " + (mayor == null ? "-" : mayor));
    }

    private void mostrar(String mensaje) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(mensaje);
        a.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// ===== Clase auxiliar para representar visualmente un nodo =====
class CircleNodeCirculo {
    private javafx.scene.shape.Circle circle;
    private Text text;

    public CircleNodeCirculo(String valor, double x, double y) {
        circle = new javafx.scene.shape.Circle(x, y, 20);
        circle.setStyle("-fx-fill: lightblue; -fx-stroke: black;");
        text = new Text(x - (valor.length() > 1 ? 8 : 5), y + 5, valor);
    }

    public javafx.scene.shape.Circle getCircle() { return circle; }
    public Text getText() { return text; }
}
