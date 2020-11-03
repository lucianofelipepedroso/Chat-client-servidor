module chatServer {
	exports br.com.luciano.chat.server.ui.controller;
	exports br.com.luciano.chat.server.ui;
	
	requires transitive javafx.controls;
	requires javafx.fxml;
	requires chatcommon;
	requires javafx.base;
	requires javafx.graphics;
	requires java.rmi;
	
	opens br.com.luciano.chat.server.ui.controller to javafx.fxml;
	
	
}