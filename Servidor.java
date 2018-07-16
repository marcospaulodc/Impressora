package Trabalho3;
/**
 * @author Marcos Paulo de Castro
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends Thread 
{
/*
 * Classe que informa a conexão no console de cada nodo, apresentando
 * o ID do nodo, o servidor e a porta de conexão. Através de um loop,
 * starta a thread de conexão com o servidor de impressão.
 */
	private FluxoDeControle servidorFxControle;

	public Servidor(FluxoDeControle fxControle) 
	{
		this.servidorFxControle = fxControle;
	}

	public void run() 
	{
		ServerSocket servidorSocket = null;
		try 
		{
			System.err.println("Nodo conectado: " + this.servidorFxControle.getID() + "/" + this.servidorFxControle.getIP() + "/" + this.servidorFxControle.getPorta());
			servidorSocket = new ServerSocket(this.servidorFxControle.getPorta());

			while (true) 
			{
				Socket clienteSocket = servidorSocket.accept();
				//Startando a thread de conexão com o servidor de impressão
				new ConectaServidor(clienteSocket, servidorFxControle).start();
			}

		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				servidorSocket.close();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}