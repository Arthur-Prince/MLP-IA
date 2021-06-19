package IA;

import java.io.File;

public class Protocolos {
    /**
     * MLPs e dados que resolvem seus respectivos problemas
     */
    private MLP AND;
    private MLP OR;
    private MLP XOR;
    private MLP caracteres;
    private Dados dAnd;
    private Dados dOr;
    private Dados dXor;
    private Dados dCaracteres;

    /**
     * MLP, dados e pesos com o qual estamos trabalhando
     */
    private String problema = "";
    private MLP atual;
    private Dados dados;
    private double[][][] pesos;

    public Protocolos() {
	// problema do and
	this.dAnd = new Dados("arquivos" + File.separator + "problemAND.csv", 4);
	// cria mlp de 2 camadas
	this.AND = new MLP(dAnd, 2);
	this.AND.inicializaPesos();

	// problema do or
	this.dOr = new Dados("arquivos" + File.separator + "problemOR.csv", 4);
	// cria mlp de 2 camadas
	this.OR = new MLP(dOr, 2);

	// problema do xor
	this.dXor = new Dados("arquivos" + File.separator + "problemXOR.csv", 4);
	// cria mlp de 3 camadas (problema do xor)
	this.XOR = new MLP(dXor, 3);

	// problema dos caracteres
	this.dCaracteres = new Dados("arquivos" + File.separator + "caracteres-limpo.csv", 21);
	// cria uma mlp para resolver os problemas dos caracteres(3 camadas)
	this.caracteres = new MLP(dCaracteres, 3);

	// comecamos trabalhando com o and no inicio
	this.problema = "AND";
	this.atual = this.AND;
	this.dados = this.dAnd;
	this.pesos = atual.getPesos();
    }

    /**
     * muda o problema que trabalhamos que trabalhamos
     * 
     * @param nomeDoProblema pode ser: AND,OR,XOR e caracteres
     * @return true se a mudança ocorreu com sucesso e false se não
     */
    public boolean setMLP(String nomeDoProblema) {
	if (nomeDoProblema.equals("AND")) {
	    this.problema = "AND";
	    this.atual = this.AND;
	    this.dados = this.dAnd;
	    atual.inicializaPesos();
	    this.pesos = atual.getPesos();
	    return true;
	}
	if (nomeDoProblema.equals("OR")) {
	    this.problema = "OR";
	    this.atual = this.OR;
	    this.dados = this.dOr;
	    atual.inicializaPesos();
	    this.pesos = atual.getPesos();
	    return true;
	}
	if (nomeDoProblema.equals("XOR")) {
	    this.problema = "XOR";
	    this.atual = this.XOR;
	    this.dados = this.dXor;
	    atual.inicializaPesos();
	    this.pesos = atual.getPesos();
	    return true;
	}
	if (nomeDoProblema.equals("caracteres")) {
	    this.problema = "caracteres";
	    this.atual = this.caracteres;
	    this.dados = this.dCaracteres;
	    atual.inicializaPesos();
	    this.pesos = atual.getPesos();
	    return true;
	}

	return false;

    }

    /**
     * esse metodo só serve para o problema dos caracteres. ele inicializa os pesos
     * de acordo com o tipo escolhido
     * 
     * @param tipo tipo = 0 - pesos aleatorios tipo = 1 - pesos perfeitos iniciais
     *             tipo = 2 - pesos perfeitos finais
     * @return true se a mudança ocorreu com sucesso e false se não
     */
    public boolean inicializaPesos(int tipo) {
	if (this.atual != this.caracteres)
	    return false;
	if (tipo == 0) {
	    this.atual.inicializaPesos();
	    this.pesos = atual.getPesos();
	    return true;
	}
	if (tipo == 1) {
	    this.pesos = this.dCaracteres.lePesos(false);
	    return true;
	}
	if (tipo == 2) {
	    this.pesos = this.dCaracteres.lePesos(false);
	    return true;
	}

	return false;

    }

    /**
     * treina a MLP com um total de epocas especificado nos parametros.txt
     */
    public void treinaMLP() {
	this.atual.treinamento(this.pesos, this.dados);
	this.pesos = this.atual.getPesos();
    }

    public void treinaUmaEpoca() {
	this.atual.setNEpoca();
	this.atual.treinamento(this.pesos, this.dados);
	this.atual.setNEpoca();
	this.pesos = this.atual.getPesos();
    }

    /**
     * 
     * @return os pesos da MLP ex: camada0\n1, 1, 1, camada1\n1, 1, 1, 1, \n
     */
    public double[][][] getPesos() {
	return this.atual.getPesos();
    }

    /**
     * 
     * @return dados do grafico de acertos por epocas
     */
    public int[] getDadosDoGrafico() {

	return this.atual.dadosDoGrafico();
    }

    /**
     * 
     * @return a matriz de confusao
     */
    public int[][] getMatrizConfusao() {

	return this.atual.getMatrizDeConfusao();
    }

    /**
     * 
     * @return o numero de camadas
     */
    public int getNCamadas() {
	return atual.getNCamadas();
    }

    /**
     * 
     * @return o numero de estados de cada camada
     */
    public int[] getNEstados() {
	return atual.gerNEstados();
    }
    
    /**
     * 
     * @return o nome do problema
     */
    public String getProblema() {
	return this.problema;
    }

    /**
     * 
     * @param entrada entrada da mlp escreva as entradas separado por "," tipos de
     *                entradas:1,-1,0,-1.5(-1.5 e considerado igual a 0) ex: -1.5,1
     * @return saida da mlp ex: 1 -1 -1 -1 -1 -1 -1 A
     * @throws AplicacaoExcepition excecao caso o tamanho da entrada não bata ou nao
     *                             e possivel ler os dados
     */
    public String aplicacao(String entradas) throws AplicacaoExcepition {
	String[] entrada = entradas.split(",");
	// verifica se o tamanho da entrada corresponde ao tamanho da entrada da mlp
	if (entrada.length != dados.getEntradas(0).length) {
	    String msg = "o tamanho da entrada da mlp: " + dados.getEntradas(0).length + "\n"
		    + "o tamanho da entrada passada:" + entrada.length + "\n"
		    + "e nescessario que ambas tenham o mesmo tamanho";
	    throw new AplicacaoExcepition(msg);
	} else {
	    // transforma a string em int
	    int[] aplica = new int[entrada.length];
	    for (int i = 0; i < aplica.length; i++) {
		double aux;
		try {
		    aux = Double.parseDouble(entrada[i]);
		} catch (NumberFormatException e) {
		    throw new AplicacaoExcepition("erro ao ler dados");
		}
		// verifica se a entrada e igual a 1,-1,0,-1.5
		if (aux != -1 && aux != 1 && aux != 0 && aux != -1.5)
		    throw new AplicacaoExcepition("erro ao ler dados");
		// treanforma -1.5 em 0
		if (aux == -1.5)
		    aux = 0;
		aplica[i] = (int) aux;
	    }
	    return retornaAplicacao(this.atual.aplicacao(aplica));
	}

    }

    private String retornaAplicacao(double[] a) {
	String s = "";
	for (int i = 0; i < a.length; i++) {
	    s.concat(a[i] + " ");
	}
	if (a.length == 7) {
	    if (a[0] == 1)
		s = s.concat("A ");
	    if (a[1] == 1)
		s = s.concat("B ");
	    if (a[2] == 1)
		s = s.concat("C ");
	    if (a[3] == 1)
		s = s.concat("D ");
	    if (a[4] == 1)
		s = s.concat("E ");
	    if (a[5] == 1)
		s = s.concat("J ");
	    if (a[6] == 1)
		s = s.concat("K ");

	}
	if (a.length == 2) {
	    if (a[0] == 1)
		s = s.concat("false ");
	    if (a[1] == 1)
		s = s.concat("true ");
	}
	return s;
    }

}
