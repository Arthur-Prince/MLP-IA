package IA;


public class EntradaOuEscondida extends Camada {

	private double[][] weight;
	public Funcao funcao;
	
	/*
	 * essa camada tem peso e tipo de funcoes para calcular a ativacao dos estados e os erros
	 */
	public EntradaOuEscondida(int numeroDeEstados, Camada vizinho, boolean penultimaCamada) {
		super(numeroDeEstados);

		this.weight= new double[numeroDeEstados+1][vizinho.getNumeroDeStates()];
		if(penultimaCamada) 
			this.funcao= new Linear();
		else
			this.funcao=new NLinear();
	}
	
	//atualiza os pesos
	public void carregaPeso(double[][] pesos) {
		for (int i = 0; i < pesos.length; i++) {
			for (int j = 0; j < pesos[i].length; j++) {
				
				this.weight[i][j]=pesos[i][j];
				
			}
			
		}
		
	}

	
	//inicializa os valores do peso com valores aleatorios entre 1 -1
	public void inicializaPeso() {
		for(int i=0; i<this.weight.length;i++) {
			for(int j=0; j<this.weight[i].length;j++) {
				this.weight[i][j]=Math.random()*2 - 1;
				}			
			}
			
	}
	
	//multiplica os estados com seus pesos destinados para "saida" e soma
	public double combinacaoLinear(int saida) {
		double retorno=0;
		double[] states =super.getStates();
		for(int i = 0; i<states.length;i++) {
			retorno += states[i]*this.weight[i+1][saida];
		}
			
		retorno+=this.weight[0][saida];
			
		return retorno;
	}	
		
		
	//getters e setters
		
	public double[][] getWeight() {
		return weight;
	}
	public void setWeight(double[][] weight) {
		this.weight = weight;
	}
}
