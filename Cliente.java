package Trabalho3;
/**
 * @author Marcos Paulo de Castro
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Cliente extends Thread 
{
/*
 * Esta classe atrav�s nos m�todos mEnviarMsg() e mReceberMsg() montar� a mensagem,
 * o nodo que solicitar acesso ao recurso compartilhado, ter� seu o estado setado
 * para aguardando (setEstado == 2), assim que solicitar tal acesso ao recurso compartilhado,
 * a mensagem ser� montada desta forma <Osn, id>, enviando a mensagem � todos os processos.
 */
	private DataInputStream clienteInput;
	private DataOutputStream clienteOutput;
	private FluxoDeControle clienteFxControle;
	private Socket socket;

	public Cliente(Socket socket, FluxoDeControle fxControle) 
	{
		this.clienteFxControle = fxControle;
		this.socket = socket;
	}

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

	public String mReceberMsg(DataInputStream in) throws IOException 
	{
		String dataMsg = in.readUTF();
		return dataMsg;
	}

	public void run() 
	{
		try 
		{
			this.clienteInput = new DataInputStream(socket.getInputStream());
			this.clienteOutput = new DataOutputStream(socket.getOutputStream());

			this.clienteFxControle.setEstado(2);
			String msgAEnviar = this.clienteFxControle.mExibirMsg();
			this.mEnviarMsg(this.clienteOutput, msgAEnviar);
			String msgAReceber = mReceberMsg(clienteInput);
			
			//Se o receptor n�o estiver acessando o recurso e n�o quiser acess�-lo
			//devolve uma mensagem de "ok" para o remetente.
			if (msgAReceber.contentEquals("ok"))
			{
				this.clienteFxControle.setReposta(msgAReceber);
			}
			
			if (msgAReceber.contentEquals("aguardando"))
			{
				this.clienteFxControle.setEstado(1);
			}
			
			if (msgAReceber.contentEquals("ocupado"))
			{
				this.clienteFxControle.setEstado(1);			
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
				socket.close();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}