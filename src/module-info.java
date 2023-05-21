module ourGame {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.media;
	requires java.desktop;
	requires javafx.base;

	opens application to javafx.graphics, javafx.fxml, javafx.media;
}
