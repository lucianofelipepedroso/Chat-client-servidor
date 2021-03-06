package br.com.luciano.chat.server.ui;

import br.com.luciano.common.utlis.FXUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ChatServer extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/luciano/chat/server/ui/ServerWindown.fxml"));
        Pane root = loader.load();
		FXUtils.initLayout(loader, primaryStage);

		primaryStage.setTitle("Servidor de Chat");
		primaryStage.setResizable(false);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
