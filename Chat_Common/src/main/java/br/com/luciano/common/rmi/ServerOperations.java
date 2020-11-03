package br.com.luciano.common.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import br.com.luciano.common.ClientInfo;
import br.com.luciano.common.DuplicateNameException;

public interface ServerOperations extends Remote{
	
	public static final String SERVER_OBJ_NAME ="chatServer";
	
	public void connect(ClientInfo clientInfo) throws RemoteException,DuplicateNameException;
	
	public void disconnect(ClientInfo clientInfo) throws RemoteException;
	
	public void sendMessage(ClientInfo clientInfo,String message) throws RemoteException;

}
