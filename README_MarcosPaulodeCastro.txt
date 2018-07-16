Marcos Paulo de Castro

1- Execute o trabalho da seguinte forma:
   1.1 - O arquivo.txt permite a inserção de um ou mais nodos, o mesmo está anexado juntamente com as classes e deverá ficar no mesmo diretório do pacote, conforme Figura1.
   1.2 - Execute a classe ServidorImpressaoMain para levantar o processo do Servidor de Impressão.
   1.3 - Execute a classe ConexaoMain para iniciar o processo de leitura dos dados do arquivo e inicialização da coleta da quantidade de nodos.
   1.4 - Por fim, execute a classe NodoMain, proporcionalmente à quantidade de nodos informada no arquivo.txt, ou seja, se no arquivo.txt conter 3 nodos, essa classe deverá ser executada 3 vezes, informando o ID de cada nodo. 
	
2- As classes e métodos mais complexos estão devidamente comentados, a fim de obter um melhor entendimento do funcionamento do programa.


Descrição sucinta do programa

Para o funcionamento correto da Impressora Distribuída é necessário levantar os processos de forma adequada, conforme as instruções iniciais.
O programa funciona com a lógica de segurança em que apenas um determinado processo, tenha acesso a um recurso compartilhado em um intervalo de tempo.
A classe ServidorImpressaoMain inicia o processo da impressora, atribuindo uma porta de acesso ao servidor, e starta a thread de ConexaoImpressora, esta classe por sua vez, aguarda até que um processo ganhe o acesso ao recurso compartilhado e comece a imprimir.
A classe ConectaMain contém o segundo processo para ser iniciado, e se responsabiliza por verificar a quantidade de nodos presentes no arquivo.txt, criar um objeto do tipo socket e contém o método que verifica se todos os nodos foram iniciados.
A classe NodoMain é o último processo a ser inicializado, essa classe instancia o objeto compartilhado FluxoDeControle que é responsável por parametrizar o acesso à Seção Crítica, além da coleta de informações do arquivo, a classe NodoMain, pode iniciar as threads servidor e cliente, caso as condições forem satisfeitas. 
A classe FluxoDeControle provê o acesso ao recurso compartilhando, e realiza as manipulações nas variáveis do algortimo proposto por Ricart e Agrawala, de modo que haja uma ordenação total de todos os eventos do programa.


Por fim, realizei os testes manipulando o arquivo.txt com 1, 2, 3, 4, 5 e 6 nodos, sendo percebido que o propósito da EMD foi alcançada com sucesso. Ressalto que o foi muito difícil obter o comportamento correto do programa, pois debugar as várias threads que o programa possui foi bem moroso, porém o resultado final foi gratificante.






