module chatcommon {
	exports br.com.luciano.common.utlis;
	exports br.com.luciano.common;
	exports br.com.luciano.common.rmi;

	requires java.rmi;
	requires transitive javafx.controls;
	requires javafx.fxml;
	
}