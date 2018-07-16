Marcos Paulo de Castro

1- Execute o trabalho da seguinte forma:
   1.1 - O arquivo.txt permite a inser��o de um ou mais nodos, o mesmo est� anexado juntamente com as classes e dever� ficar no mesmo diret�rio do pacote, conforme Figura1.
   1.2 - Execute a classe ServidorImpressaoMain para levantar o processo do Servidor de Impress�o.
   1.3 - Execute a classe ConexaoMain para iniciar o processo de leitura dos dados do arquivo e inicializa��o da coleta da quantidade de nodos.
   1.4 - Por fim, execute a classe NodoMain, proporcionalmente � quantidade de nodos informada no arquivo.txt, ou seja, se no arquivo.txt conter 3 nodos, essa classe dever� ser executada 3 vezes, informando o ID de cada nodo. 
	
2- As classes e m�todos mais complexos est�o devidamente comentados, a fim de obter um melhor entendimento do funcionamento do programa.


Descri��o sucinta do programa

Para o funcionamento correto da Impressora Distribu�da � necess�rio levantar os processos de forma adequada, conforme as instru��es iniciais.
O programa funciona com a l�gica de seguran�a em que apenas um determinado processo, tenha acesso a um recurso compartilhado em um intervalo de tempo.
A classe ServidorImpressaoMain inicia o processo da impressora, atribuindo uma porta de acesso ao servidor, e starta a thread de ConexaoImpressora, esta classe por sua vez, aguarda at� que um processo ganhe o acesso ao recurso compartilhado e comece a imprimir.
A classe ConectaMain cont�m o segundo processo para ser iniciado, e se responsabiliza por verificar a quantidade de nodos presentes no arquivo.txt, criar um objeto do tipo socket e cont�m o m�todo que verifica se todos os nodos foram iniciados.
A classe NodoMain � o �ltimo processo a ser inicializado, essa classe instancia o objeto compartilhado FluxoDeControle que � respons�vel por parametrizar o acesso � Se��o Cr�tica, al�m da coleta de informa��es do arquivo, a classe NodoMain, pode iniciar as threads servidor e cliente, caso as condi��es forem satisfeitas. 
A classe FluxoDeControle prov� o acesso ao recurso compartilhando, e realiza as manipula��es nas vari�veis do algortimo proposto por Ricart e Agrawala, de modo que haja uma ordena��o total de todos os eventos do programa.


Por fim, realizei os testes manipulando o arquivo.txt com 1, 2, 3, 4, 5 e 6 nodos, sendo percebido que o prop�sito da EMD foi alcan�ada com sucesso. Ressalto que o foi muito dif�cil obter o comportamento correto do programa, pois debugar as v�rias threads que o programa possui foi bem moroso, por�m o resultado final foi gratificante.






