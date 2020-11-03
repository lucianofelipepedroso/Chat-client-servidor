module chatclient {
	exports br.com.luciano.chat.client.ui.controller;
	exports br.com.luciano.chat.client.ui;

	
	requires transitive javafx.controls;
	requires javafx.fxml;
	requires chatcommon;
	requires javafx.base;
	requires javafx.graphics;
	requires java.rmi;
	
	opens br.com.luciano.chat.client.ui.controller to javafx.fxml;
}