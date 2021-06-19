package IA;


public interface Funcao {
	public double funcao(double x);
	
	public double derivadaFuncao(double x);
	
	public double funcaoDeErro(double[]corretor , Camada atual , int k);
}
