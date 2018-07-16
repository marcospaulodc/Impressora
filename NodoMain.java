package Trabalho3;
/**
 * @author Marcos Paulo de Castro
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class NodoMain 
{
/*
 * Classe NodoMain, é o último processo a ser levantado, os processos do Servidor de Impressão e das
 * Conexões já devem estar inicializados. Este processo levantará os processos de cada Nodo existente
 * no arquivo.txt, essa classe deverá ser executada proporcionalmente ao número de nodos informados
 * no respectivo arquivo. A classe instancia o objeto compartilhado FluxoDeControle que é responsável
 * por parametrizar o acesso à Seção Crítica. Além da coleta de informações do arquivo, a classe
 * NodoMain, pode iniciar as threads servidor e cliente, caso as condições forem satisfeitas.
 */
	private Integer nodoID;
	private String nHost;
	private Integer nPorta;
	
	public NodoMain(Integer nodoID, String nHost, Integer nPorta) 
	{
		this.nodoID = nodoID;
		this.nHost = nHost;
		this.nPorta = nPorta;
	}

	public static void mEnviarMsg(DataOutputStream out, String dataMsg)
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
	
	public Integer getID() 
	{
		return nodoID;
	}

	public String getHost() 
	{
		return nHost;
	}

	public Integer getPorta() 
	{
		return nPorta;
	}

	public void setID(Integer nodoID) 
	{
		this.nodoID = nodoID;
	}

	public void setHost(String nHost) 
	{
		this.nHost = nHost;
	}

	public void setPorta(Integer nPorta) 
	{
		this.nPorta = nPorta;
	}

	public static String mReceberMsg(DataInputStream in) throws IOException
	{
			String dataMsg;
			dataMsg = in.readUTF();
			return dataMsg;
	}

	public static void main(String[] args) 
	{
		DataInputStream entrada = null;
		DataOutputStream saida = null;
		Socket nSocket = null;
		FluxoDeControle nodoFxControle = new FluxoDeControle();
		Scanner sTeclado = new Scanner(System.in);

		try 
		{
			System.out.print("Atenção!!! Esse main deverá ser executado proporcionalmente ao número de Nodos do Arquivo.txt \n");
			System.out.print("Digite o ID do Nodo do Arquivo.txt: ");
			nodoFxControle.setID(sTeclado.nextInt());
			sTeclado.close();
			nodoFxControle.mSelecionarArquivo();
			String set_Host = "localhost";
			Integer set_Port = 9766;
			nSocket = new Socket(set_Host, set_Port);
			entrada = new DataInputStream(nSocket.getInputStream());
			saida = new DataOutputStream(nSocket.getOutputStream());
			mEnviarMsg(saida, String.valueOf(nodoFxControle.getID()));
			String msg = mReceberMsg(entrada);
			
			if (msg.contentEquals("msgPronta")) 
			{
				nodoFxControle.startServidor();
			}

			while (nodoFxControle.mFinalizarImpressao() == false) 
			{
				try 
				{
					while (nodoFxControle.getTotalImpressao() != 30) 
					{
						Random rand = new Random();
						int operacao = rand.nextInt(51);
						if ((operacao > 20) && (operacao < 30)) 
						{
							nodoFxControle.setEstado(2);
							nodoFxControle.startClientes();
							while (nodoFxControle.getTotalReposta() != nodoFxControle.getNodos().size()) 
							{
								int cont = 0;
								Thread.sleep(1000);
								if (cont == 10) 
								{
									nodoFxControle.setEstado(1);
									break;
								}
								cont++;
							}
							if (nodoFxControle.getEstado().contains("ESTADO_AGUARDANDO")) 
							{
								nodoFxControle.setEstado(3);
								nodoFxControle.mAcessarImpressaoSecaoCritica();
							}
							nodoFxControle.setEstado(1);
							nodoFxControle.mRemoverEnfileirado();
						} 
						Thread.sleep(1000);
					}
					nodoFxControle.setFinalizar();
				} 
				catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				nSocket.close();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}