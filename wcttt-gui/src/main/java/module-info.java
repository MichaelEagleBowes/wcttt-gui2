module wcttt.gui {
	requires javafx.fxml;
	requires javafx.controls;
	requires wcttt.lib;
	requires javafx.base;
	requires javafx.graphics;
	requires java.desktop;

	exports wcttt.gui;

	opens wcttt.gui.controller to javafx.fxml;
}
