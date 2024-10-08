module ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires junit;


    opens ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines to javafx.fxml;
    exports ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines;
}