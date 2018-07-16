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
 * Classe de conex�o cuja fun��o � imprimir a mensagem do nodo que acessou
 * a se��o cr�tica, imprimindo uma sequ�ncia num�rica com um delay de 0.5
 * segundo entre cada n�mero, do total de 10 n�meros contando a partir do
 * timestamp da �ltima mensagem recebida.
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

	///Atrav�s do in ser� recebida a mensagem
	public String recebeMensagem(DataInputStream in) throws IOException 
	{
		String dataMsg = in.readUTF();
		return dataMsg;
	}

	public void run() 
	{
		try 
		{
			System.out.println("Impress�o da sequ�ncia n�merica: ");
			System.out.println("Nodo atrelado � porta: " + socketCliente.getPort() + " imprimindo...");
			
			//Imprimindo a sequ�ncia num�rica que deve ter o tamanho 10
			for (int i = 0; i < 10; i++) 
			{
				String numImpresso = this.recebeMensagem(this.input);
				Integer num = this.fxControle.getOsn() + Integer.parseInt(numImpresso);
				System.out.print(num + ",");
				this.fxControle.setOsn(num);
				Thread.sleep(500);
			}
			System.out.println("\n" + "Impress�o finalizada com sucesso!" + "\n");
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
