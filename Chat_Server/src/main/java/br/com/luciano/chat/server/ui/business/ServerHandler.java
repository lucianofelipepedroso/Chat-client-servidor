package br.com.luciano.chat.server.ui.business;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import br.com.luciano.common.ChatException;
import br.com.luciano.common.ClientInfo;
import br.com.luciano.common.rmi.ClientCallback;
import br.com.luciano.common.rmi.ServerOperations;

public class ServerHandler {

	private static final String CONFIG_FILE = "server_config.txt";

	private static final String PROP_PORT = "port";

	private static final int DEFAULT_PORT = 1909;

	private ServerOperationsImpl serverOperations;

	private Registry registry;

	private Properties properties;

	private boolean started;

	public ServerHandler() throws ChatException, IOException {
		properties = new Properties();
		Path file = Paths.get(CONFIG_FILE);

		if (Files.exists(file)) {
			FileInputStream fileInputStream = null;
			try (InputStream in = Files.newInputStream(file)) {
				properties.load(fileInputStream);
			}
		} else {
			properties.setProperty(PROP_PORT, String.valueOf(DEFAULT_PORT));
		}

		try {
			registry = LocateRegistry.createRegistry(getServerPort());
		} catch (RemoteException e) {
			throw new ChatException("Erro ao criar Registry", e);
		}

	}

	private int getServerPort() throws ChatException {

		String portStr = properties.getProperty(PROP_PORT);

		if (portStr == null) {
			throw new ChatException("Porta servidor não definida");
		}

		try {

			int port = Integer.parseInt(portStr);

			if (port < 1 || port > 65635) {
				throw new ChatException("A porta não está em um intervalo válido");
			}

			return port;

		} catch (NumberFormatException e) {
			throw new ChatException("A porta não é um número válido");
		}

	}

	public int startServer() throws ChatException {

		try {
			serverOperations = new ServerOperationsImpl();
			registry.bind(ServerOperations.SERVER_OBJ_NAME, serverOperations);
			started = true;
			return getServerPort();
		} catch (Exception e) {
			throw new ChatException("Erro ao iniciar o servidor", e);
		}

	}

	public void stopServer() throws ChatException {

		try {

			if (!started) {
				return;
			}

			Set<ClientInfo> clients = serverOperations.getClients();

			if (clients.size() > 0) {
				ExecutorService service = Executors.newFixedThreadPool(clients.size());

				try {
					clients.forEach(clientInfo -> {

						ClientCallback callback = clientInfo.getCallback();

						service.execute(new FutureTask<>(() -> {
							callback.onServerShutdown();

							return null;

						}));

					});

					clients.clear();

				} finally {
					
					service.shutdown();
				}

			}
			
			registry.unbind(ServerOperations.SERVER_OBJ_NAME);
			started=false;

		} catch (Exception e) {
			throw new ChatException("Erro ao parar o servidor", e);
		}

	}

}
