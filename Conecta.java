package Trabalho3;
/**
 * @author Marcos Paulo de Castro
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Conecta extends Thread 
{
/*
 * Esta classe � capaz de adicionar no objeto compartilhado FluxoDeControle o ID do nodo iniciado,
 * fica aguardando todas a conex�es de todos os nodos do arquivo.txt, ou seja, o n�mero de nodos
 * do arquivo deve ser igual ao n�mero de processos levantados, o while se encarrega da condi��o.
 */

	private DataInputStream input;
	private DataOutputStream output;
	private Socket socketCliente;
	private ConexaoMain fxControle;

	public Conecta(Socket socketCliente, ConexaoMain fxControle) 
	{
		try 
		{
			this.socketCliente = socketCliente;
			this.input = new DataInputStream(socketCliente.getInputStream());
			this.output = new DataOutputStream(socketCliente.getOutputStream());
			this.fxControle = fxControle;
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Atrav�s do out ser� enviada a mensagem na rede
	public void mEnviarMsg(DataOutputStream out, String dataMsg) throws IOException 
	{
		out.writeUTF(dataMsg);
		out.flush();
	}

	//Atrav�s do in ser� recebida a mensagem na rede
	public String mReceberMsg(DataInputStream in) throws IOException 
	{
		String dataMsg = in.readUTF();
		return dataMsg;
	}

	public void run() 
	{
		try 
		{
			String dataMsg = this.mReceberMsg(input);
			int nodo = Integer.parseInt(dataMsg);
			this.fxControle.addArray(nodo);
			
			while (this.fxControle.getTotal() != this.fxControle.getConexao())
			{
				Thread.sleep(800);
			}
			this.mEnviarMsg(output, "msgPronta");
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				this.socketCliente.close();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}