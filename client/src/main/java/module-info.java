module kz.timka {
    requires javafx.controls;
    requires javafx.fxml;

    opens kz.timka.client to javafx.fxml;
    exports kz.timka.client;
}