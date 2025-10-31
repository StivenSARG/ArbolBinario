module com.curso.arbol {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.curso.arbol to javafx.fxml;
    exports com.curso.arbol;
}