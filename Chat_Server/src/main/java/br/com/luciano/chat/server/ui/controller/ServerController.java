package br.com.luciano.chat.server.ui.controller;

import br.com.luciano.chat.server.ui.business.ServerHandler;
import br.com.luciano.common.ChatException;
import br.com.luciano.common.utlis.FXUtils;
import br.com.luciano.common.utlis.StageAwareController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ServerController implements StageAwareController {

	@FXML
	private Button btnStop;

	@FXML
	private Button btnStart;

	@FXML
	private Label lblStatus;

	private ServerHandler serverHandler;

	private BooleanProperty serverStarted = new SimpleBooleanProperty();

	@FXML
	private void initialize() {

		btnStart.disableProperty().bind(serverStarted);
		btnStop.disableProperty().bind(serverStarted.not());

		try {
			serverHandler = new ServerHandler();
		} catch (Exception e) {
			FXUtils.showExceptionDialog(e);
		}

	}

	@Override
	public void onStageDefined(Stage stage) {
		stage.setOnCloseRequest(event -> {

			if (serverHandler != null) {

				try {
					serverHandler.stopServer();
				} catch (Exception e) {

				}

			}

			Platform.exit();
			System.exit(0);
		});

	}

	@FXML
	public void onStart() {

		try {
			int port = serverHandler.startServer();
			lblStatus.setText("Servidor iniciado na porta " + port);
			serverStarted.set(true);
		} catch (ChatException e) {
			FXUtils.showExceptionDialog(e);
		}

	}

	@FXML
	public void onStop() {

		try {
			serverHandler.stopServer();
			lblStatus.setText("Servidor parado");
			serverStarted.set(false);
		} catch (ChatException e) {
			FXUtils.showExceptionDialog(e);
		}
	}

}
