
public class Camada {
	private double[] states;
	private int nstates;
	
	//construtor que carrega as variaveis
	public Camada(int numeroDeEstados) {
		this.nstates=numeroDeEstados;
		this.states = new double[numeroDeEstados];
	}
	
	
	
	public double[] getStates() {
		return states;
	}
	public void setStates(double[] states) {
		this.states = states;
	}
	
	//retorna o numero de estados
	public int getNumeroDeStates() {
		return this.nstates;
	}
}
