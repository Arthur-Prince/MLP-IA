

public class Linear implements Funcao {

	@Override
	public double funcao(double x) {
		if(x>0) {
			return 1;
		}
		return -1;
	}

	@Override
	public double derivadaFuncao(double x) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public double funcaoDeErro(double[] corretor, Camada atual, int k) {
		double[] estados= atual.getStates();
		return (corretor[k]-estados[k])/2;
	}

}
