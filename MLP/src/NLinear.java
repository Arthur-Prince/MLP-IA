

public class NLinear implements Funcao {

	@Override
	public double funcao(double x) {
		// tangente hiperbolica
		return Math.tanh(x);
	}

	@Override
	public double derivadaFuncao(double x) {
		// sec^2(x)
		return Math.pow(1/Math.cosh(x), 2);
	}

	@Override
	public double funcaoDeErro(double[] corretor, Camada atual, int k) {
		EntradaOuEscondida camada = (EntradaOuEscondida) atual;
		double[][] pesos = camada.getWeight();
		double rtn=0;
		for (int i = 0; i < corretor.length; i++) {
			rtn += corretor[i]*pesos[k][i];
		}
		return rtn;
	}

}
