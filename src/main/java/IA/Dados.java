package IA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Dados {

	private String nome;
	private int numeroDeLinhas;
	private int[][] entradas;
	private int[][] resultados;
	private int iteracoes;
	private double taxaDeAprendizado;
	private String erros;
	private String saidas;

	// contrutor: carrega as variaveis
	public Dados(String nome, int numeroDeLinhas) {
		this.nome = nome;
		this.numeroDeLinhas = numeroDeLinhas;
		this.entradas = new int[numeroDeLinhas][];
		this.resultados = new int[numeroDeLinhas][];
		this.saidas = "";
		this.erros = "";
		leParametrosIniciais();
		carregaEntrada();
		carregaResultado();

	}

	/*
	 * escreve a matrix de peso de cada camada em um txt se o finalOuInicial for
	 * true escreve no arquivo:pesosfinais.txt se finalOuInicial for false escreve
	 * no arquivo: pesosiniciais.txt
	 */
	public void escrevepesos(double[][][] weight, boolean finalOuInicial) {
		String nome = "";
		if (finalOuInicial)
			nome = "arquivos" + File.separator + "pesosfinais.txt";
		else
			nome = "arquivos" + File.separator + "pesosiniciais.txt";
		String pesos = preparaString(weight, false);
		escreve(nome, pesos);
	}

	/*
	 * escre a matrix de pesos de cada camada em pesos.txt
	 */
	public void escrevepesos(double[][][] weight) {
		String pesos = preparaString(weight, false);
		String nome = "arquivos" + File.separator + "pesos.txt";
		escreve(nome, pesos);
	}

	/*
	 * le a matrix de peso de cada camada se o resultadoFinal for true le no
	 * arquivo:pesosfinais.txt se resultadoFinal for false le no arquivo:
	 * pesosiniciais.txt
	 */
	public double[][][] lePesos(boolean finalOuInicial) {
		String nome = "";
		if (finalOuInicial)
			nome = "arquivos" + File.separator + "pesosfinais.txt";
		else
			nome = "arquivos" + File.separator + "pesosiniciais.txt";

		File f = new File(nome);
		Scanner s = null;
		try {
			s = new Scanner(f);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		String aux = "";

		while (s.hasNext()) {

			String linha = s.nextLine();

			aux = aux.concat(linha + "\n");
		}
		// camada
		String[] aux2 = aux.split("camada");
		double[][][] rtn = new double[aux2.length - 1][][];
		for (int i = 1; i < aux2.length; i++) {
			// coluna
			String[] aux3 = aux2[i].split("\n");
			rtn[i - 1] = new double[aux3.length - 1][];

			for (int j = 1; j < aux3.length; j++) {

				// linha
				String[] aux4 = aux3[j].split(", ");
				rtn[i - 1][j - 1] = new double[aux4.length];

				for (int k = 0; k < aux4.length; k++) {
					// carrega o numero
					rtn[i - 1][j - 1][k] = Double.parseDouble(aux4[k]);
				}
			}
		}

		return rtn;
	}

	/*
	 * escreve no arquivo erros.txt: 1- a iteracao em que esta 2- as matrizes de
	 * erros
	 */
	public void escreveErro(double[][][] erros, int iteracao, boolean ultimaIteracao) {
		if (ultimaIteracao)
			escreve("arquivos" + File.separator + "erros.txt", this.erros);
		else {
			String erroNessaIteracao = "iteracao" + iteracao + ":\n";
			erroNessaIteracao = erroNessaIteracao.concat(preparaString(erros, true));
			this.erros = this.erros.concat(erroNessaIteracao + "\n");
		}
	}

	/*
	 * escreve no arquivo saidas.txt: 1- a iteracao 2- o que estiver em "saida"
	 */
	public void escreveSaidas(double[] saida, int iteracao, boolean ultimaIteracao) {
		if (ultimaIteracao)
			escreve("arquivos" + File.separator + "saidas.txt", this.saidas);
		else {
			String saidaNessaIteracao = "iteracao" + iteracao + ": ";
			for (int i = 0; i < saida.length; i++) {

				saidaNessaIteracao = saidaNessaIteracao.concat(saida[i] + ", ");
			}
			this.saidas = this.saidas.concat(saidaNessaIteracao + "\n");
		}

	}

	/**************************
	 * metodos auxiliares
	 *************************************/

	/*
	 * carrega os parametros iniciais do arquivo parametros.txt parametros: taxa de
	 * aprendizado numero de camadas numero de iteracoes
	 */
	private void leParametrosIniciais() {
		Scanner scan = null;
		try {
			scan = new Scanner(new File("arquivos" + File.separator + "parametros.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.iteracoes = scan.nextInt();
		this.taxaDeAprendizado = scan.nextDouble();
	}

	/*
	 * carrega o "nome" contendo a entrada e adiciona em entradas
	 */
	private void carregaEntrada() {
		FileInputStream fis = null;
		InputStreamReader inRead = null;
		BufferedReader buffRead = null;
		String line;
		try {
			fis = new FileInputStream(nome);
			inRead = new InputStreamReader(fis);
			buffRead = new BufferedReader(inRead);
			line = buffRead.readLine();
			line = line.replaceAll("﻿", "");
			int x = 0;
			while (x < this.numeroDeLinhas && line != null) {

				String[] lineSep = line.split(",");
				double[] entradouble = new double[lineSep.length - 1];

				for (int i = 0; i < entradouble.length; i++) {
					entradouble[i] = Double.parseDouble(lineSep[i]);
					if (entradouble[i] == -1.5)
						entradouble[i] = 0;
				}
				int[] entrada = new int[entradouble.length];
				this.entradas[x] = new int[entrada.length];
				for (int i = 0; i < entradouble.length; i++) {
					entrada[i] = (int) entradouble[i];
				}
				this.entradas[x] = entrada;
				line = buffRead.readLine();
				x++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * vai ler o csv com o nome "nome" e vai retornar apenas o resultado no seguinte
	 * formato: contem 1 na posicao certa e -1 na posicao errada
	 * 
	 * por exemplo: No exemplo de and, xor e or: retorna {1 -1} se for 1 o resultado
	 * e {-1,1} caso contrario
	 * 
	 * Ja nos caracteres tera q fazer uma transformacao nesse sentido
	 * 
	 * A ocupa a primeira posicao , B a segunda ... e K a setima posicao
	 * 
	 * se na linha i estiver C como resultado entao retornara {-1,-1,1,-1,-1,-1,-1}
	 * 
	 */
	private void carregaResultado() {
		FileInputStream fis = null;
		InputStreamReader inRead = null;
		BufferedReader buffread = null;
		String line = "";
		try {
			fis = new FileInputStream(nome);
			inRead = new InputStreamReader(fis);
			buffread = new BufferedReader(inRead);
			for (int x = 0; x < this.numeroDeLinhas && line != null; x++) {
				line = buffread.readLine();
				String[] lineSep = line.split(",");
				char result = lineSep[lineSep.length - 1].charAt(0);

				int[] resultado;

				if (nome.equals("arquivos" + File.separator + "caracteres-ruido.csv")
						|| nome.equals("arquivos" + File.separator + "caracteres-limpo.csv")) {
					resultado = new int[7];
					for (int i = 0; i < resultado.length; i++)
						resultado[i] = -1;
					if (result == 'A')
						resultado[0] = 1;
					if (result == 'B')
						resultado[1] = 1;
					if (result == 'C')
						resultado[2] = 1;
					if (result == 'D')
						resultado[3] = 1;
					if (result == 'E')
						resultado[4] = 1;
					if (result == 'J')
						resultado[5] = 1;
					if (result == 'K')
						resultado[6] = 1;

				} else {
					resultado = new int[2];
					if (result == '1') {
						resultado[0] = -1;
						resultado[1] = 1;

					} else {
						resultado[0] = 1;
						resultado[1] = -1;

					}
				}
				this.resultados[x] = resultado;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void escreve(String path, String s) {
		BufferedWriter buffWrite;
		try {
			buffWrite = new BufferedWriter(new FileWriter(path));
			buffWrite.append(s);
			buffWrite.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public String preparaString(double[][][] d, boolean format) {
		String s = "";
		for (int i = 0; i < d.length; i++) {
			s = s.concat("camada" + i + "\n");
			for (int j = 0; j < d[i].length; j++) {
				for (int k = 0; k < d[i][j].length; k++) {
					String num = "";
					if (format)
						num = String.format("%.2f", d[i][j][k]);
					else
						num = d[i][j][k] + "";
					num = num.replace(',', '.');

					s = s.concat(num + ", ");

				}
				s = s.concat("\n");
			}

		}

		return s;
	}

	public void imprime(double[] a) {
		String s = "";
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + " ");
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
		System.out.print(s);
	}

	public void imprime(int[][] a) {
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				System.out.print(a[i][j] + "  ");
			}
			System.out.println("\n\n");
		}
	}

	/*********************** getters e setters **********************************/
	public String getNome() {
		return nome;
	}

	public int getNumeroDeLinhas() {
		return numeroDeLinhas;
	}

	public int[] getResultados(int linha) {
		return resultados[linha];
	}

	public int[] getEntradas(int linha) {
		return entradas[linha];
	}

	public int[][] getResultados() {
		return resultados;
	}

	public int[][] getEntradas() {
		return entradas;
	}

	public double getTaxaDeAprendizado() {
		return taxaDeAprendizado;
	}

	public int getIteracoes() {
		return iteracoes;
	}

}
