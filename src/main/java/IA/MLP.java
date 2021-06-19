package IA;
import java.util.Iterator;
import java.util.LinkedList;

public class MLP {
	private int nCamada;
	private Camada[] camadas;
	/**
	 * numero de epocas usado no treinamento
	 */
	private int iteracoes;
	/**
	 * numero de epoca especificado no parametros.txt
	 */
	private int epocaDoTxt;
	/**
	 * acertos da MLP por epoca
	 */
	private LinkedList<Integer> acertos;
	
	private int[][] matrizDeConfusao;
	
	//construtor
	public MLP(Dados dados, int nCamadas) {
		int nEntrada = dados.getEntradas(0).length;
		int nSaida = dados.getResultados(0).length;
		this.matrizDeConfusao = new int[nSaida][nSaida];
		this.nCamada=nCamadas;
		this.camadas = new Camada[nCamadas];
		this.iteracoes = dados.getIteracoes();
		this.epocaDoTxt = dados.getIteracoes();
		this.acertos = new LinkedList<Integer>();
		
		this.camadas[nCamadas-1]= new Camada(nSaida);
		//a media e usado para definir quantos estados tem em cada camada escondida
		int media = (int)(nEntrada+nSaida)/(nCamadas-1)+1;
		//coloca estados nas camadas escondidadas de acordo com a media aritmetica
		
		int i= nCamadas-3;
		//faz a camada anterior a de saida ter uma funcao linear e as outras uma funcao
		//nao linear
		if(i>=0) {
			this.camadas[i+1] = new EntradaOuEscondida(media,this.camadas[i+2],true);
			while(i>0) {
				this.camadas[i] = new EntradaOuEscondida(media,this.camadas[i+1],false);
				i++;
			}
			
			this.camadas[0]=new EntradaOuEscondida(nEntrada,this.camadas[1],false);
		}
		else
			this.camadas[0]=new EntradaOuEscondida(nEntrada,this.camadas[1],true);
		
		
		
	}
	
	//treina a mlp
	public void treinamento(double[][][] pesos, Dados dados) {
		if(!carregaPesos(pesos))
			return;
		
		//variaveis
		int[][] entradas = dados.getEntradas();
		int[][] resultados = dados.getResultados();
		int contador = 0;
		int acertos=0;
		
		//so para se acertar todos os resultados ou iterar o numero limite de vezes
		while(contador<this.iteracoes && acertos!=resultados.length) {
			//reseta a matriz de confusao
			for(int i = 0; i<this.matrizDeConfusao.length;i++) {
				for (int j = 0; j < this.matrizDeConfusao.length; j++) {
					this.matrizDeConfusao[i][j] = 0;
				}
			}
			contador++;
			acertos=0;
			for (int i = 0; i < resultados.length; i++) {
				carregaEntrada(entradas[i]);
				ativandoEstados();
				dados.escreveSaidas(this.camadas[this.nCamada-1].getStates(), contador, false);
				double[][][] erros = calculaErros(resultados[i], 0.4);
				dados.escreveErro(erros, contador, false);
				
				for (int j = 0; j < erros.length; j++) {
					atualizaPesos(erros, j);
				}
				//confirma se a mlp acertou o resultado do teste
				if(acertouResultado(resultados,this.camadas[this.nCamada-1].getStates(),i))
					acertos++;
			}
			this.acertos.add(acertos);
		}
		
		dados.escreveErro(null, 0, true);
		dados.escreveSaidas(null, 0, true);
		
	}
	
	/* 
	 * usado apenas para conseguir os resultados perfeitos e escrever nos arquivos
	 * NAO USE!!!!!!!!!!!!
	 * caso contrario ira sobrescrever os arquivos de pesos
	 */
	public void treinamentoPerfeito(Dados cLimpo, Dados cRuido) {
		boolean testador = true;
		double[][][] pesos=getPesos();
		while(testador) {
			testador = false;
			//inicializa pesos aleatorio
			inicializaPesos();
			cLimpo.escrevepesos(pesos, false);
			
			//treina com esses pesos aleatorios com o arquivo cLimpo
			
			treinamento(pesos, cLimpo);

			//aplica o resultado do treinamento em cRuido ate que acerte tudo
			for (int linha = 0; linha < cRuido.getNumeroDeLinhas(); linha++) {
				double[] teste =aplicacao(cRuido.getEntradas(linha));
				
				if(!acertouResultado(cRuido.getResultados(), teste, linha)) {
					testador = true;
				}
			}
		}
		
		// escreve nos arquivos de pesos iniciais e finais
		cLimpo.escrevepesos(pesos, true);
	}
	
	/*
	 * SO USE APOS CARREGAR OS PESOS
	 * esse metodo testa a entrada na mlp ja pronta
	 */
	public double[] aplicacao(int[] entrada) {
		carregaEntrada(entrada);
		ativandoEstados();
		return this.camadas[this.nCamada-1].getStates();
	}
	
	/***************************usado em testes**********************************************/
	
	
	//apenas para testes e inicializar os pesos
	public void inicializaPesos() {
		this.acertos.clear();
		for (int i = 0; i < camadas.length-1; i++) {
			EntradaOuEscondida a = (EntradaOuEscondida) this.camadas[i];
			a.inicializaPeso();
		}
	}
	
	public double[][][] getPesos(){
		double[][][] rtn = new double[this.nCamada-1][][];
		for (int i = 0; i < rtn.length; i++) {
			EntradaOuEscondida a = (EntradaOuEscondida)this.camadas[i];
			rtn[i]=a.getWeight();
		}
		return rtn;
	}
	
	//carrega pesos ja inicializados
	public boolean carregaPesos(double[][][] pesos) {
		for (int i = 0; i < pesos.length; i++) {
			if(i>this.nCamada-1) {
				System.out.println("erro com o indice da camada");
				return false;
			}
			if(pesos[i].length!=this.camadas[i].getNumeroDeStates()+1) {
				System.out.println("erro ao carregar os pesos da camada anterior devido ao seu tamanho");
				return false;
			}
			for (int j = 0; j < pesos.length; j++) {
				if(pesos[i][j].length!=this.camadas[i+1].getNumeroDeStates()) {
					System.out.println("erro ao carregar os pesos devido ao tamanho da camada posterior");
					return false;
				}
						
			}
			EntradaOuEscondida atual = (EntradaOuEscondida) this.camadas[i];
			atual.carregaPeso(pesos[i]);
		}		
		
		return true;
	}
	
	/**
	 * muda o numero de epocas que a mlp treina para 1 ou para o numero especificado no parametro.txt.
	 * 
	 */
	public void setNEpoca() {
	  if(this.iteracoes == 1) {
	    this.iteracoes = this.epocaDoTxt;
	  }else {
	    this.iteracoes = 1;
	  }
	}
	
	/**
	 * 
	 * @return uma lista ordenado por epoca dos acertos da mlp
	 */
	public int[] dadosDoGrafico() {
		Integer[] dados = acertos.toArray(new Integer[0]);
		int[] rtn = new int[dados.length];
		for (int i = 0; i < dados.length; i++) {
			rtn[i] = dados[i];
		}
		return rtn;
		
	}
	
	public int[][] getMatrizDeConfusao(){
		return this.matrizDeConfusao;
	}
	
	public int getNCamadas() {
	    return this.nCamada;
	}
	
	public int[] gerNEstados() {
	    int[] rtn = new int[this.nCamada];
	    for (int i = 0; i < rtn.length; i++) {
		rtn[i] = this.camadas[i].getNStates();
	    }
	    return rtn;
	}
	
	
	/**************************auxiliares**********************************************/
	
	
	private void carregaEntrada(int[] estados) {
		double[] entrada = new double[estados.length];
		for (int i = 0; i < entrada.length; i++) {
			entrada[i]=(double) estados[i];
		}
		this.camadas[0].setStates(entrada);
	}
	
	private void ativandoEstados() {
		EntradaOuEscondida c;
		Camada prox;
		double[] novosEstados;
		
		for(int i = 0 ; i<this.nCamada-1;i++) {
			c = (EntradaOuEscondida)this.camadas[i];
			prox = this.camadas[i+1];
			novosEstados = new double[prox.getNumeroDeStates()];
			
			for(int j = 0; j<prox.getNumeroDeStates();j++) {
				novosEstados[j] = c.funcao.funcao(c.combinacaoLinear(j));
			}
			
			prox.setStates(novosEstados);
		}
	}
	
	private double[][][] calculaErros(int[] resultados,double taxaDeAprendizado){
		int camada = this.nCamada-1;
		double[][][] erros = new double[camada][][];
		
		double[] correcao = new double[resultados.length];
		for(int i =0;i<resultados.length;i++)
			correcao[i]=(double)resultados[i];
		
		
		
		
		//para cada camada de traz para frente
		
		while(camada>0) {
			//salva a camada atual e a anterior
			Camada atual = this.camadas[camada];
			EntradaOuEscondida anterior = (EntradaOuEscondida) this.camadas[camada-1];
			//arruma espaco na matriz de erro para os erros dos pesos com o bias
			erros[camada-1]= new double[anterior.getNumeroDeStates()+1][atual.getNumeroDeStates()];
			//usado para salvar o lambidaK
			double[] atualizador = new double[atual.getNumeroDeStates()];
			double lambidaK;
			int k=0;
			
			double[] estadosAnterior = anterior.getStates();
			//calcula o erro para cada estado de "atual"
			
			while(k<atual.getNumeroDeStates()) {
				
				//(Tk-Yk)*f'(Yk_IN) ou somaj(Wkj*correcaoj)*f'(Zk_IN) caso nao esteja a penultima camada
				lambidaK = anterior.funcao.funcaoDeErro(correcao, atual, k)*anterior.funcao.derivadaFuncao(anterior.combinacaoLinear(k)); 
				//salva o lambidaK para ajudar na correcao da proxima camada
				atualizador[k]= lambidaK;
				
				//carrega os erros dos pesos
				
				for (int j = 0; j < anterior.getNumeroDeStates(); j++) {
					//dentaW= alfa*lambidaK*(estado anterior j)
					
					erros[camada-1][j+1][k]=taxaDeAprendizado*lambidaK*estadosAnterior[j];
					
				}
				//bias lambidaK*alfa
				erros[camada-1][0][k]=taxaDeAprendizado*lambidaK;
				k++;
			}
			//atualiza o corretor e volta uma camada
			correcao = atualizador;
			camada--;
			
		}
		
		return erros;
	}

	//atualiza os pesos da camada "camada"
	private void atualizaPesos(double[][][] erros, int camada) {
		EntradaOuEscondida atual = (EntradaOuEscondida) this.camadas[camada];
		double[][] pesos = atual.getWeight();
		double[][] rtn = new double [erros[camada].length][erros[camada][0].length];
		
		for (int i = 0; i < pesos.length; i++) {
			for (int j = 0; j < pesos[i].length; j++) {
				rtn[i][j]=pesos[i][j]+erros[camada][i][j];
			}
			
			
			
		}
		
		atual.carregaPeso(rtn);
	}
	
	//confirma se a mlp acertou o resultado do teste
	private boolean acertouResultado(int[][] resultados, double[] teste, int linha) {
		boolean rtn = true;
		int tipo=0;
		int[] aux = new int[teste.length];
		for (int i = 0; i < teste.length; i++) {
			if(teste[i]>0) {
				aux[i]=1;
			}else {
				aux[i]=0;
			}
			if(resultados[linha][i]==1) 
				tipo = i;
			
			if(resultados[linha][i]!=teste[i])
				rtn = false;
		}
		for (int i = 0; i < aux.length; i++) {
			this.matrizDeConfusao[tipo][i] += aux[i];
			
		}
		
		
		return rtn;
	}
	
	
	
	
	
	/***************************************impressora**********************************************/
	
	public void imprime(int[] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]+"  ");
		}
		System.out.println();
	}
	public void imprime(double[] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]+"  ");
		}
		System.out.println("\n");
	}
	public void imprime(double[][] a) {
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				System.out.print(a[i][j]+"  ");
			}
			System.out.println();
		}
		System.out.println("\n");
	}
	
	public void imprime(double[][][] a) {
		
		
		for (int x = 0; x < a.length; x++) {
			
			for (int i = 0; i < a[x].length; i++) {
				
				for (int j = 0; j < a[x][i].length; j++) {
					String num =String.format("%.2f", a[x][i][j]);
					System.out.print(num + "  ");
				}
				System.out.println();
				
			}
			System.out.println();
		}
		
	}
	
	public void imprimeEstados() {
		for (int i = 0; i < this.nCamada; i++) {
			imprime(this.camadas[i].getStates());
		}
	} 
	
	
}
