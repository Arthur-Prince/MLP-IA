package MLP.MLPAPI.api.model;

public class AtualizaDados {
    
    private String nome;
    

    private int nCamadas;
    private int[] nEstados;
    private double[][][] pesos;
    private int[] dadosDoGrafico;
    private int[][] matrizDeConfusao;

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome;
    }
    public int getnCamadas() {
        return nCamadas;
    }

    public void setnCamadas(int nCamadas) {
        this.nCamadas = nCamadas;
    }

    public int[] getnEstados() {
        return nEstados;
    }

    public void setnEstados(int[] nEstados) {
        this.nEstados = nEstados;
    }

    public int[] getDadosDoGrafico() {
        return dadosDoGrafico;
    }

    public void setDadosDoGrafico(int[] dadosDoGrafico) {
        this.dadosDoGrafico = dadosDoGrafico;
    }

    public int[][] getMatrizDeConfusao() {
        return matrizDeConfusao;
    }

    public void setMatrizDeConfusao(int[][] matrizDeConfusao) {
        this.matrizDeConfusao = matrizDeConfusao;
    }

    public double[][][] getPesos() {
        return pesos;
    }

    public void setPesos(double[][][] pesos) {
        this.pesos = pesos;
    }
    
    
    
    
    
   
}
