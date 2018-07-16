package Trabalho3;
/**
 * @author Marcos Paulo de Castro
 */

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class FluxoDeControle 
{
/*
 * Classe cujo papel é importantíssimo no programa, pois dela provê o acesso ao recurso
 * compartilhando, e realiza as manipulações nas variáveis do algortimo proposto por 
 * Ricart e Agrawala, de modo que haja uma ordenação total de todos os eventos do programa.
 * 
 */
	private Integer Osn;
	private Integer Hsn;
	private String ESTADO;
	private ArrayList<String> ListResp;
	private ArrayList<Integer> ListEsperaDosNodos;
	private boolean nodoFim;
	private Integer nodoID;
	private String nodoIP;
	private Integer nodoPorta;
	private boolean SecaoCritica;
	private int nTotImpr;
	private Integer OperacoesNodos;
	private ArrayList<NodoMain> ListNodos;
	
	public FluxoDeControle() 
	{
		this.Osn = 0;
		this.Hsn = 0;
		this.ESTADO = "ESTADO_LIVRE";
		this.ListResp = new ArrayList<String>();
		this.ListEsperaDosNodos = new ArrayList<Integer>();
		this.nodoFim = false;
		this.SecaoCritica = false;
		this.nTotImpr = 0;
		this.OperacoesNodos = 1;
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

	public void mAcessarImpressaoSecaoCritica() 
	{
		this.ESTADO = "ESTADO_OCUPADO";
		this.SecaoCritica = true;
		DataOutputStream saida = null;
		Socket socket = null;
		
		try 
		{
			String initHost = "localhost";
			Integer initPort = 10010;
			socket = new Socket(initHost, initPort);
			saida = new DataOutputStream(socket.getOutputStream());

			for (int i = 1; i < 11; i++) 
			{
				this.mEnviarMsg(saida, String.valueOf(1));
				Thread.sleep(500);
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
				socket.close();
				this.SecaoCritica = false;
				this.nTotImpr++;
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void mSelecionarArquivo() 
	{
		try 
		{
			String path = new File("arquivo.txt").getCanonicalPath();
			Scanner Arquivo = new Scanner(new FileReader(path));
			this.ListNodos = new ArrayList<NodoMain>();

			while (Arquivo.hasNext()) 
			{
				String linha = Arquivo.nextLine();
				String hosts[] = linha.split("/");
				Integer idHost = Integer.parseInt(hosts[0]);
				String endHost = hosts[1];
				Integer portaHost = Integer.parseInt(hosts[2]);

				if (this.nodoID == idHost) 
				{
					this.nodoIP = endHost;
					this.nodoPorta = portaHost;
				} 
				else 
				{
					NodoMain novoNodo = new NodoMain(idHost, endHost, portaHost);
					ListNodos.add(novoNodo);
				}
			}
			Arquivo.close();
		} 
		catch (FileNotFoundException e) 
		{
			System.err.println("Erro na abertura do arquivo:");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startServidor() 
	{
		Servidor novoServidor = new Servidor(this);
		novoServidor.start();
	}

	public void startClientes() 
	{
		try 
		{
			for (int i = 0; i < this.ListNodos.size(); i++) 
			{
				String host = this.ListNodos.get(i).getHost();
				int port = this.ListNodos.get(i).getPorta();
				Socket socket = new Socket(host, port);
				Cliente novoCliente = new Cliente(socket, this);
				novoCliente.start();
			}
		} 
		catch (UnknownHostException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String mExibirMsg() 
	{
		this.Osn = this.Hsn + 1;
		return new String(this.Osn + ";" + this.nodoID);
	}

	public void mImprimirEstadoNodo() 
	{
		System.out.println("Estado do Nodo:" + "\nOsn: " + this.Osn + "\nHSN: " + this.Hsn + "\nESTADO: " + this.ESTADO);
	}

	public boolean mFinalizarImpressao() 
	{
		return this.nodoFim;
	}

	public boolean mSecaoCriticaLiberada() 
	{
		return this.SecaoCritica;
	}

	public void mRemoverEnfileirado() 
	{
		this.ListResp.clear();
		this.ListEsperaDosNodos.clear();
	}

	public Integer getID() 
	{
		return nodoID;
	}

	public String getIP() 
	{
		return nodoIP;
	}

	public Integer getPorta() 
	{
		return nodoPorta;
	}

	public Integer getOperacao() 
	{
		return OperacoesNodos;
	}

	public ArrayList<NodoMain> getNodos() 
	{
		return ListNodos;
	}

	public String getEstado() 
	{
		return ESTADO;
	}

	public int getOsn() 
	{
		return this.Osn;
	}

	public int getHsn() 
	{
		return this.Hsn;
	}

	public int getTotalImpressao() 
	{
		return this.nTotImpr;
	}

	public int getTotalReposta() 
	{
		return this.ListResp.size();
	}

	public void setID(Integer nodoID) 
	{
		this.nodoID = nodoID;
	}

	public void setIP(String nodoIP) 
	{
		this.nodoIP = nodoIP;
	}

	public void setPorta(Integer nodoPorta) 
	{
		this.nodoPorta = nodoPorta;
	}

	public void setOperacao(Integer OperacoesNodos) 
	{
		this.OperacoesNodos = OperacoesNodos;
	}

	public void setReposta(String ID_ESTADO) 
	{
		this.ListResp.add(ID_ESTADO);
	}

	public void setEstado(int estado) 
	{
		if (estado == 1) 
		{
			this.ESTADO = "ESTADO_LIVRE";
		}

		if (estado == 2) 
		{
			this.ESTADO = "ESTADO_AGUARDANDO";
		}

		if (estado == 3) 
		{
			this.ESTADO = "ESTADO_OCUPADO";
		}
	}
	
	public void setFinalizar() 
	{
		if (this.nTotImpr == 100) 
		{
			this.nodoFim = true;
		}
	}
	
	public void setOsn(int valorOsn) 
	{
		this.Osn = valorOsn;
	}

	public void setHsn(int valorHsn) 
	{
		this.Hsn = valorHsn;
	}

	public void setFila(int valor) 
	{
		this.ListEsperaDosNodos.add(valor);
	}
}