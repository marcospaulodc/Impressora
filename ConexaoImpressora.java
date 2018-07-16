package Trabalho3;
/**
 * @author Marcos Paulo de Castro
 */

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ConexaoImpressora extends Thread 
{
/*
 * Classe de conexão cuja função é imprimir a mensagem do nodo que acessou
 * a seção crítica, imprimindo uma sequência numérica com um delay de 0.5
 * segundo entre cada número, do total de 10 números contando a partir do
 * timestamp da última mensagem recebida.
 */
	private DataInputStream input;
	private Socket socketCliente;
	private ServidorImpressaoMain fxControle;
	private Cliente cliente;

	public ConexaoImpressora(Socket socketCliente, ServidorImpressaoMain Osn, Cliente cliente) 
	{
		try 
		{
			this.fxControle = Osn;
			this.socketCliente = socketCliente;
			this.input = new DataInputStream(socketCliente.getInputStream());
			this.cliente = cliente;
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	///Através do in será recebida a mensagem
	public String recebeMensagem(DataInputStream in) throws IOException 
	{
		String dataMsg = in.readUTF();
		return dataMsg;
	}

	public void run() 
	{
		try 
		{
			System.out.println("Impressão da sequência númerica: ");
			System.out.println("Nodo atrelado à porta: " + socketCliente.getPort() + " imprimindo...");
			
			//Imprimindo a sequência numérica que deve ter o tamanho 10
			for (int i = 0; i < 10; i++) 
			{
				String numImpresso = this.recebeMensagem(this.input);
				Integer num = this.fxControle.getOsn() + Integer.parseInt(numImpresso);
				System.out.print(num + ",");
				this.fxControle.setOsn(num);
				Thread.sleep(500);
			}
			System.out.println("\n" + "Impressão finalizada com sucesso!" + "\n");
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
