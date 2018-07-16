package Trabalho3;
/**
 * @author Marcos Paulo de Castro
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorImpressaoMain extends Thread 
{
/*
 * Classe respons�vel por realizar a inicializa��o do Servidor de Impress�o,
 * dever� ser iniciada inicialmente para lavantar o processo do Servidor,
 * esta classe possui uma vari�vel est�tica que recebe um inteiro, sendo a
 * porta do Servidor, na qual os nodos ir�o se conectar. Esta classe tamb�m
 * instancia um objeto Socket e realizar� o start na thread de Conex�o
 * com a Impressora.
 */
	private Integer Osn;

	public ServidorImpressaoMain() 
	{
		this.Osn = -1;
	}

	public Integer getOsn() 
	{
		return this.Osn;
	}

	public void setOsn(Integer valorOsn) 
	{
		this.Osn = valorOsn;
	}
	
	public static void main(String[] args) 
	{
		ServerSocket servidorSocket = null;
		try 
		{
			System.out.println("*** Servidor de Impress�o iniciado com sucesso! ***\n");
			
			int portaDoServidor = 10010;
			servidorSocket = new ServerSocket(portaDoServidor);
			ServidorImpressaoMain fxControle = new ServidorImpressaoMain();
			Cliente cliente = new Cliente(null, null);

			while (true) 
			{
				Socket clienteSocket = servidorSocket.accept();
				
				//Startando a Thread Conex�oImpressora
				new ConexaoImpressora(clienteSocket, fxControle, cliente).start();
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
