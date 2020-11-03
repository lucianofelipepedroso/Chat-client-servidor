package br.com.luciano.chat.client.ui;

import br.com.luciano.common.utlis.FXUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ChatClient extends Application {

	@Override
	public void start(Stage stage) throws Exception {

		stage.setMinWidth(380);
		stage.setMinHeight(250);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/luciano/chat/ui/MainLayout.fxml"));
		Pane root = loader.load();
		FXUtils.initLayout(loader, stage);
		
		stage.setResizable(false);
		Scene scene = new Scene(root,500,300);
		stage.setScene(scene);
		stage.show();


	}

	public static void main(String[] args) {
		launch(args);
	}

}
