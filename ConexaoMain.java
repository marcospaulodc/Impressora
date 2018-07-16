package Trabalho3;
/**
 * @author Marcos Paulo de Castro
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ConexaoMain 
{
/*
 * Classe que deverá ser iniciada juntamente com o processo do Servidor de Impressão,
 * esta classe será responsável por verificar a quantidade de nodos presentes no
 * arquivo.txt, cria um socket do servidor com a porta definida estaticamente, realiza
 * a condição para inicilização dos nodos e starta a thread Conecta.
 */

	private ArrayList<Integer> listNodosInic;
	private Integer numTotalConexao;
	
	public ConexaoMain() 
	{
		this.listNodosInic = new ArrayList<Integer>();
	}
	
	public static void main(String[] args) 
	{
		ConexaoMain clienteFxControle = new ConexaoMain();
		ServerSocket servidorSocket = null;
		try 
		{
			System.out.println("*** Servidor de Conexoes iniciado com sucesso! ***");
			String path = new File("arquivo.txt").getAbsolutePath();
			Scanner Arquivo = new Scanner(new FileReader(path));
			int contNodos = 0;
			while (Arquivo.hasNext())
			{
				Arquivo.nextLine();
				contNodos++;
			}
			Arquivo.close();
			System.out.println("** Quantidade de nodos coletados do arquivo: " + contNodos + " **\n");
			
			//Instanciando o socket do servidor
			int porta = 9766;
			servidorSocket = new ServerSocket(porta);
			
			
			// Adiciona o número total de nodos vindo do arquivo.txt
			clienteFxControle.addConexao(contNodos);

			//Condição de inicialização dos nodos
			while (clienteFxControle.TodasConexoesIniciadas() == false)
			{
				//Instanciando o socket do cliente
				Socket clienteSocket = servidorSocket.accept();
				
				//Startando a thread de conexão
				new Conecta(clienteSocket, clienteFxControle).start();
				Thread.sleep(300);
				
				System.out.println("Nodo(s) do Arquivo.txt iniciado(s): " + clienteFxControle.getArray());
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
				servidorSocket.close();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public int getTotal() 
	{
		return this.listNodosInic.size();
	}

	public ArrayList<Integer> getArray() 
	{
		return this.listNodosInic;
	}

	public int getConexao() 
	{
		return this.numTotalConexao;
	}

	public void addArray(int nodo) 
	{
		this.listNodosInic.add(nodo);
	}

	public void addConexao(int total) 
	{
		this.numTotalConexao = total;
	}

	//Verifica se todas as conexões foram iniciadas
	public boolean TodasConexoesIniciadas() 
	{
		if (this.numTotalConexao == this.listNodosInic.size()) 
		{
			return true;
		} 
		else 
		{
			return false;
		}
	}
}
