import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class InteligenciaArtificial {

	public static void main(String[] args) throws FileNotFoundException {
		Dados comunicador = null;
		MLP mlp = null;
		// pesos que serao usado para o treinamento ou aplicacao da mlp
		double[][][] pesos = null;
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("digite o numero para escolher um arquivo o qual vai trabalhar");
			System.out.println("(qualquer outro numero encerra o programa)\n");
			System.out.println("0 - problema AND");
			System.out.println("1 - problema OR:");
			System.out.println("2 - problema XOR");
			System.out.println("3 - problema dos caracteres");

			int arquivo = scanner.nextInt();

			// escolha de arquivos
			switch (arquivo) {
			case 0: {
				comunicador = new Dados("arquivos" + File.separator+ "problemAND.csv", 4);

				break;

			}
			case 1: {
				comunicador = new Dados("arquivos" + File.separator+ "problemOR.csv", 4);

				break;

			}
			case 2: {
				comunicador = new Dados("arquivos" + File.separator+ "problemXOR.csv", 4);

				break;

			}
			case 3: {
				comunicador = new Dados("arquivos" + File.separator+ "caracteres-limpo.csv", 21);

				break;
			}
			default:
				System.out.println("numero ilegal " + arquivo);
				System.exit(0);
			}

			boolean podeTestar = true;
			// problema do "and" e do "or" e do "xor"
			if (arquivo == 0 || arquivo == 1 || arquivo == 2) {
				if (arquivo == 2) {
					// cria mlp de 3 camadas (problema do xor)
					mlp = new MLP(comunicador, 3);
				} else {
					// cria mlp de 2 camadas (problema do and e do or)
					mlp = new MLP(comunicador, 2);
				}
				// carrega os pesos aleatoriamente
				mlp.inicializaPesos();
				pesos = mlp.getPesos();
				System.out.println("\npesos inicializados com numeros aleatorias entre -1 e 1\n");
				// espera 2 segundo de delay
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// problema dos caracteres
			if (arquivo == 3) {
				// cria uma mlp para resolver os problemas dos caracteres(3 camadas)
				mlp = new MLP(comunicador, 3);

				System.out.println("\n\nescolha como carregar os pesos\n");
				System.out.println("0 - pesos aleatorios");
				System.out.println("1 - pesos perfeitos iniciais(que ao treinar se tornam pesos perfeitos)");
				System.out.println("2 - pesos perfeitos(que resolve o problema dos caracteres ruidos e limpos)");
				int escolha = scanner.nextInt();
				switch (escolha) {
				case 0: {
					mlp.inicializaPesos();
					pesos = mlp.getPesos();
					break;
				}
				case 1: {
					pesos = comunicador.lePesos(false);
					break;
				}
				case 2: {
					pesos = comunicador.lePesos(true);
					break;
				}
				default:
					podeTestar = false;
				}
			}
			if (podeTestar) {
				// fase de teste
				boolean repetidor = true;
				while (repetidor) {
					System.out.println("\nescolha uma opcao:\n");
					System.out.println("0 - treinamento(muda os pesos)");
					System.out.println("1 - aplicacao");
					System.out.println("2 - mostrar Pesos");
					int escolha = scanner.nextInt();
					switch (escolha) {
					case 0: {
						// treina a mlp com o numero de iteracoes especificado em parametros.txt
						// (pode nao encontrar a solucao)
						mlp.treinamento(pesos, comunicador);
						System.out.println("\n os erros de cada iteracao foram escritos em erros.txt");
						System.out.println("a saida de cada iteracao foi escrita no arquivo saida.txt");
						break;
					}
					case 1: {
						// carrega entrada
						System.out.println("escreva as entradas separado por \",\"");
						System.out.println("tipos de entradas:1,-1,0,-1.5(-1.5 e considerado igual a 0");
						String entradas = scanner.next();
						String[] entrada = entradas.split(",");
						// verifica se o tamanho da entrada corresponde ao tamanho da entrada da mlp
						if (entrada.length != comunicador.getEntradas(0).length) {
							System.out.println("o tamanho da entrada da mlp: " + comunicador.getEntradas(0).length);
							System.out.println("o tamanho da entrada passada: " + entrada.length);
							System.out.println("e nescessario que ambas tenham o mesmo tamanho");
						} else {
							// transforma a string em int
							int[] aplica = new int[entrada.length];
							for (int i = 0; i < aplica.length; i++) {
								double aux = Double.parseDouble(entrada[i]);
								// treanforma -1.5 em 0
								if (aux == -1.5)
									aux = 0;
								aplica[i] = (int) aux;
							}
							// imprime o resultado
							System.out.println("resultado:");
							comunicador.imprime(mlp.aplicacao(aplica));

						}
						break;
					}
					case 2: {
						// imprime os pesos
						double[][][] peso = mlp.getPesos();
						mlp.imprime(peso);
						break;
					}
					default:
						repetidor = false;
					}
				}
			}
		}
	}
}