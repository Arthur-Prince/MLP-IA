package IA;


public class Linear implements Funcao {

	public double funcao(double x) {
		if(x>0) {
			return 1;
		}
		return -1;
	}

	public double derivadaFuncao(double x) {
		return 1;
	}

	public double funcaoDeErro(double[] corretor, Camada atual, int k) {
		double[] estados= atual.getStates();
		return (corretor[k]-estados[k])/2;
	}

}
