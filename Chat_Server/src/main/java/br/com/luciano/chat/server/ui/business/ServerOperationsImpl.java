package br.com.luciano.chat.server.ui.business;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.ReentrantLock;

import br.com.luciano.common.ClientInfo;
import br.com.luciano.common.DuplicateNameException;
import br.com.luciano.common.rmi.ClientCallback;
import br.com.luciano.common.rmi.ServerOperations;

public class ServerOperationsImpl extends UnicastRemoteObject implements ServerOperations {

	protected ServerOperationsImpl() throws RemoteException {

	}

	private static final long serialVersionUID = 1259959622331608984L;

	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	private Set<ClientInfo> clients = new HashSet<>();

	private ReentrantLock lock = new ReentrantLock();

	public Set<ClientInfo> getClients() {
		lock.lock();

		try {
			return clients;
		} finally {
			lock.unlock();
		}

	}

	@Override
	public void connect(ClientInfo clientInfo) throws RemoteException, DuplicateNameException {
		boolean added;
		lock.lock();

		try {

			added = clients.add(clientInfo);

		} finally {
			lock.unlock();
		}

		if (!added) {
			throw new DuplicateNameException("O nome " + clientInfo.getName() + " já existe no chat");
		}

		String message = clientInfo.getName() + " entrou no chat";

		String formatMessage = formatMessageFromServerToClients(message);

		broadcastMessage(formatMessage);
	}

	private void broadcastMessage(String message) {

		lock.lock();

		try {
			if (clients.size() > 0) {

				ExecutorService service = Executors.newFixedThreadPool(clients.size());

				try {

					clients.forEach(c -> {

						final ClientCallback callback = c.getCallback();

						service.execute(new FutureTask<>(() -> {

							callback.onIncomingMessage(message);
							return null;
						}));

					});

				} finally {
					service.shutdown();
				}
			}

		} finally {
			lock.unlock();
		}

	}

	private String formatMessageFromServerToClients(String message) {
		return String.format("(%s) %s", sdf.format(new Date()), message);
	}

	private String formatMessageFromClientToClients(String message, String clientName) {
		return String.format("(%s) [%s] %s", sdf.format(new Date()), clientName, message);
	}

	@Override
	public void disconnect(ClientInfo clientInfo) throws RemoteException {

		lock.lock();
		try {
			clients.remove(clientInfo);
		} finally {
			lock.unlock();
		}

		String message = clientInfo.getName() + " saiu do chat";

		String formatteMessage = formatMessageFromServerToClients(message);

		broadcastMessage(formatteMessage);

	}

	@Override
	public void sendMessage(ClientInfo clientInfo, String message) throws RemoteException {

		String fromattedMessage = formatMessageFromClientToClients(message, clientInfo.getName());

		broadcastMessage(fromattedMessage);

	}

}
