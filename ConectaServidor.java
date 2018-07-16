package Trabalho3;
/**
 * @author Marcos Paulo de Castro
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConectaServidor extends Thread 
{
/*
* Esta classe recebe a mensagem do nodo do cliente, se o valor do Osn recebido 
* for maior que o meu Hsn, realiza a atualiza��o, se o receptor n�o estiver acessando 
* o recurso e n�o quiser acess�-lo devolve uma mensagem de "ok" para o remetente.
* Se o receptor j� tiver acesso ao recurso, simplemente n�o responde, coloca a requisi��o 
* na Fila de Espera.
*/
	private DataInputStream entrada;
	private DataOutputStream saida;
	private Socket socketCliente;
	private FluxoDeControle clienteFxControle;

	public ConectaServidor(Socket socketCliente, FluxoDeControle fxControle) 
	{
		try 
		{
			this.socketCliente = socketCliente;
			entrada = new DataInputStream(socketCliente.getInputStream());
			saida = new DataOutputStream(socketCliente.getOutputStream());
			this.clienteFxControle = fxControle;
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Atrav�s do out ser� enviada a mensagem na rede
	public void mEnviarMsg(DataOutputStream out, String dataMsg) 
	{
		try 
		{
			out.writeUTF(dataMsg);
			out.flush();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Atrav�s do in ser� recebida a mensagem na rede
	public String mReceberMsg(DataInputStream in) throws IOException 
	{
		String dataMsg = in.readUTF();
		return dataMsg;
	}

	public void run() 
	{
		while (this.socketCliente.isClosed() == false) 
		{
			try 
			{
				String msgRecebida = this.mReceberMsg(this.entrada);
				String dataMsg[] = msgRecebida.split(";");
				Integer Osn = Integer.parseInt(dataMsg[0]);
				Integer ID = Integer.parseInt(dataMsg[1]);
				if (this.clienteFxControle.getHsn() < Osn) 
				{
					this.clienteFxControle.setHsn(Osn);
				}

				if (this.clienteFxControle.getEstado().contentEquals("ESTADO_LIVRE")) 
				{
					//Se o receptor n�o estiver acessando o recurso e n�o quiser acess�-lo
					//devolve uma mensagem de "ok" para o remetente.
					String msgEnvio = "ok";
					mEnviarMsg(this.saida, msgEnvio);
				}

				if (this.clienteFxControle.getEstado().contentEquals("ESTADO_AGUARDANDO")) 
				{
					if (this.clienteFxControle.getOsn() < Osn) 
					{
						//Se o receptor j� tiver acesso ao recurso, simplemente n�o responde...
						//Coloca a requisi��o na Fila de Espera
						this.clienteFxControle.setFila(ID);
						mEnviarMsg(this.saida, "aguarde");
					} 
					else 
					{
						//Se o receptor n�o estiver acessando o recurso e n�o quiser acess�-lo
						//devolve uma mensagem de "ok" para o remetente.
						mEnviarMsg(this.saida, "ok");
					}
				}

				if (this.clienteFxControle.getEstado().contentEquals("ESTADO_OCUPADO")) 
				{
					//Se o receptor j� tiver acesso ao recurso, simplemente n�o responde...
					//Coloca a requisi��o na Fila de Espera
					this.clienteFxControle.setFila(ID);
					while (this.clienteFxControle.mSecaoCriticaLiberada()) 
					{
						Thread.sleep(500);
					}
					//Se o receptor n�o estiver acessando o recurso e n�o quiser acess�-lo
					//devolve uma mensagem de "ok" para o remetente.
					mEnviarMsg(this.saida, "ok");
				}
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
}