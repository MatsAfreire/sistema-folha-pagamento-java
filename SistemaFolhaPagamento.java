import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ============================================================
 *  SISTEMA DE FOLHA DE PAGAMENTO
 *  Gerencia o cadastro e listagem de funcionários com
 *  cálculo automático de salário conforme o tipo.
 * ============================================================
 */
public class SistemaFolhaPagamento {

    // -------------------------------------------------------
    // CLASSE INTERNA: Funcionario
    // Representa um funcionário com seus dados e salário final
    // -------------------------------------------------------
    static class Funcionario {
        private int    numeroRegistro;
        private String nome;
        private String tipo;
        private double salarioFinal;

        /**
         * Construtor completo do funcionário.
         *
         * @param numeroRegistro Número de registro informado pelo usuário
         * @param nome           Nome completo
         * @param tipo           Tipo: PADRÃO, COMISSIONADO ou PRODUÇÃO
         * @param salarioFinal   Salário calculado conforme o tipo
         */
        public Funcionario(int numeroRegistro, String nome,
                           String tipo, double salarioFinal) {
            this.numeroRegistro = numeroRegistro;
            this.nome           = nome;
            this.tipo           = tipo;
            this.salarioFinal   = salarioFinal;
        }

        // ----- Getters -----
        public int    getNumeroRegistro() { return numeroRegistro; }
        public String getNome()           { return nome; }
        public String getTipo()           { return tipo; }
        public double getSalarioFinal()   { return salarioFinal; }

        /**
         * Exibe os dados formatados do funcionário no console.
         */
        public void exibirDados() {
            System.out.println("┌─────────────────────────────────────────┐");
            System.out.printf( "│  Registro : %-28d │%n", numeroRegistro);
            System.out.printf( "│  Nome     : %-28s │%n", nome);
            System.out.printf( "│  Tipo     : %-28s │%n", tipo);
            System.out.printf( "│  Salário  : R$ %,-25.2f │%n", salarioFinal);
            System.out.println("└─────────────────────────────────────────┘");
        }
    }

    // -------------------------------------------------------
    // ATRIBUTOS DO SISTEMA
    // -------------------------------------------------------

    // Salário base constante para todos os tipos de funcionário
    private static final double SALARIO_BASE = 2000.00;

    // Lista que armazena todos os funcionários cadastrados
    private static ArrayList<Funcionario> listaFuncionarios = new ArrayList<>();

    // Scanner compartilhado para leitura de dados do console
    private static Scanner scanner = new Scanner(System.in);

    // -------------------------------------------------------
    // MÉTODO PRINCIPAL (ENTRY POINT)
    // -------------------------------------------------------
    public static void main(String[] args) throws Exception {
        // Garante saída em UTF-8 para exibir caracteres especiais corretamente
        System.setOut(new PrintStream(System.out, true, "UTF-8"));

        int opcao;

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║     SISTEMA DE FOLHA DE PAGAMENTO        ║");
        System.out.println("╚══════════════════════════════════════════╝");

        // Loop principal do menu — continua até o usuário escolher sair (0)
        do {
            exibirMenu();
            opcao = lerInteiroSeguro("Opção");

            switch (opcao) {
                case 1:
                    cadastrarFuncionario();
                    break;
                case 2:
                    listarFuncionarios();
                    break;
                case 0:
                    System.out.println("\n✔  Encerrando o sistema. Até logo!\n");
                    break;
                default:
                    System.out.println("\n⚠  Opção inválida. Digite 0, 1 ou 2.\n");
            }

        } while (opcao != 0);

        scanner.close();
    }

    // -------------------------------------------------------
    // EXIBIÇÃO DO MENU
    // -------------------------------------------------------
    /**
     * Imprime o menu de opções no console.
     */
    private static void exibirMenu() {
        System.out.println("\n──────────────────────────────────────────");
        System.out.println("  MENU PRINCIPAL");
        System.out.println("──────────────────────────────────────────");
        System.out.println("  1 - Cadastrar funcionário");
        System.out.println("  2 - Listar funcionários");
        System.out.println("  0 - Sair");
        System.out.println("──────────────────────────────────────────");
        System.out.print("  Digite sua opção: ");
    }

    // -------------------------------------------------------
    // CADASTRO DE FUNCIONÁRIO
    // -------------------------------------------------------
    /**
     * Solicita os dados do funcionário, valida as entradas
     * e adiciona à lista. Entradas inválidas repetem a pergunta
     * sem cancelar o cadastro.
     */
    private static void cadastrarFuncionario() {
        System.out.println("\n── CADASTRO DE FUNCIONÁRIO ───────────────");

        // Limpa o buffer antes de ler texto
        scanner.nextLine();

        // ── Número de registro: loop até valor válido e único ──
        int numeroRegistro;
        System.out.print("  Número de registro: ");
        while (true) {
            try {
                numeroRegistro = Integer.parseInt(scanner.nextLine().trim());
                if (numeroRegistro <= 0) {
                    System.out.print("\n✖  Deve ser maior que zero! Digite novamente: ");
                    continue;
                }
                // Verifica duplicata
                boolean duplicado = false;
                for (Funcionario f : listaFuncionarios) {
                    if (f.getNumeroRegistro() == numeroRegistro) {
                        duplicado = true;
                        break;
                    }
                }
                if (duplicado) {
                    System.out.print("\n✖  Registro " + numeroRegistro
                            + " já cadastrado! Digite outro: ");
                    continue;
                }
                break; // valor válido e único
            } catch (NumberFormatException e) {
                System.out.print("\n✖  Entrada inválida! Digite um número inteiro: ");
            }
        }

        // ── Nome: loop até não ser vazio ──
        String nome;
        System.out.print("  Nome do funcionário: ");
        while (true) {
            nome = scanner.nextLine().trim();
            if (!nome.isEmpty()) break;
            System.out.print("\n✖  Nome não pode ser vazio! Digite novamente: ");
        }

        // ── Tipo de funcionário: loop até opção válida ──
        System.out.println("\n  Tipo de funcionário:");
        System.out.println("    1 - Padrão       (salário fixo R$ 2.000,00)");
        System.out.println("    2 - Comissionado (salário + comissão sobre vendas)");
        System.out.println("    3 - Produção     (salário + bônus por peças)");

        int tipoEscolhido;
        System.out.print("    Tipo: ");
        while (true) {
            try {
                tipoEscolhido = Integer.parseInt(scanner.nextLine().trim());
                if (tipoEscolhido >= 1 && tipoEscolhido <= 3) break;
                System.out.print("\n✖  Opção inválida! Digite 1, 2 ou 3: ");
            } catch (NumberFormatException e) {
                System.out.print("\n✖  Entrada inválida! Digite 1, 2 ou 3: ");
            }
        }

        String tipoNome;
        double salarioFinal;

        switch (tipoEscolhido) {

            // ── FUNCIONÁRIO PADRÃO ──────────────────────
            case 1:
                tipoNome     = "PADRÃO";
                salarioFinal = SALARIO_BASE;
                break;

            // ── FUNCIONÁRIO COMISSIONADO ─────────────────
            case 2:
                tipoNome = "COMISSIONADO";

                double totalVendas = lerDoublePositivo("  Total de vendas (R$)");
                double percentualComissao = lerDoublePositivo("  Percentual de comissão (%)");

                double comissao = totalVendas * percentualComissao / 100.0;
                salarioFinal    = SALARIO_BASE + comissao;

                System.out.printf("  → Comissão calculada: R$ %.2f%n", comissao);
                break;

            // ── FUNCIONÁRIO DE PRODUÇÃO ──────────────────
            case 3:
                tipoNome = "PRODUÇÃO";

                double valorPorPeca  = lerDoublePositivo("  Valor por peça (R$)");
                int quantidadePecas  = lerInteiroPositivo("  Quantidade de peças produzidas");

                double bonus = valorPorPeca * quantidadePecas;
                salarioFinal = SALARIO_BASE + bonus;

                System.out.printf("  → Bônus calculado: R$ %.2f%n", bonus);
                break;

            // Nunca alcançado (loop acima já garante 1-3), mas exigido pelo compilador
            default:
                return;
        }

        // Cria e armazena o funcionário com o registro informado pelo usuário
        Funcionario novoFuncionario = new Funcionario(
                numeroRegistro, nome, tipoNome, salarioFinal);
        listaFuncionarios.add(novoFuncionario);

        System.out.printf("%n✔  Funcionário \"%s\" cadastrado com sucesso!%n", nome);
        System.out.printf("   Registro nº %d | Salário final: R$ %.2f%n%n",
                novoFuncionario.getNumeroRegistro(), salarioFinal);
    }

    // -------------------------------------------------------
    // LISTAGEM DE FUNCIONÁRIOS
    // -------------------------------------------------------
    /**
     * Percorre a lista de funcionários e exibe os dados de cada um.
     * Se a lista estiver vazia, informa o usuário.
     */
    private static void listarFuncionarios() {
        System.out.println("\n── LISTA DE FUNCIONÁRIOS ─────────────────");

        if (listaFuncionarios.isEmpty()) {
            System.out.println("  ⚠  Nenhum funcionário cadastrado ainda.\n");
            return;
        }

        System.out.printf("  Total de funcionários: %d%n%n",
                listaFuncionarios.size());

        // Percorre e exibe cada funcionário da lista
        for (Funcionario funcionario : listaFuncionarios) {
            funcionario.exibirDados();
        }

        // Exibe resumo do total da folha ao final
        exibirResumoFolha();
    }

    /**
     * Calcula e exibe o total da folha de pagamento.
     */
    private static void exibirResumoFolha() {
        double totalFolha = 0;
        for (Funcionario f : listaFuncionarios) {
            totalFolha += f.getSalarioFinal();
        }

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.printf( "║  TOTAL DA FOLHA: R$ %,-20.2f ║%n", totalFolha);
        System.out.println("╚══════════════════════════════════════════╝\n");
    }

    // -------------------------------------------------------
    // MÉTODOS AUXILIARES DE VALIDAÇÃO COM LOOP
    // -------------------------------------------------------

    /**
     * Lê um número inteiro do console com loop de reentrada.
     * Repete a pergunta até receber um valor numérico válido.
     *
     * @param campo Nome do campo exibido no prompt
     * @return Valor inteiro válido
     */
    private static int lerInteiroSeguro(String campo) {
        System.out.print("  " + campo + ": ");
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("\n✖  Entrada inválida! Digite um número inteiro: ");
            }
        }
    }

    /**
     * Lê um número inteiro >= 0 com loop de reentrada.
     * Repete a pergunta enquanto o valor for negativo ou não numérico.
     *
     * @param campo Nome do campo exibido no prompt
     * @return Valor inteiro positivo ou zero
     */
    private static int lerInteiroPositivo(String campo) {
        System.out.print("  " + campo + ": ");
        while (true) {
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor >= 0) return valor;
                System.out.print("\n✖  Valor inválido! Digite novamente (>= 0): ");
            } catch (NumberFormatException e) {
                System.out.print("\n✖  Entrada inválida! Digite um número inteiro: ");
            }
        }
    }

    /**
     * Lê um número decimal (double) >= 0 com loop de reentrada.
     * Aceita vírgula ou ponto como separador decimal.
     * Repete a pergunta enquanto o valor for negativo ou não numérico.
     *
     * @param campo Nome do campo exibido no prompt
     * @return Valor double positivo ou zero
     */
    private static double lerDoublePositivo(String campo) {
        System.out.print("  " + campo + ": ");
        while (true) {
            try {
                // Substitui vírgula por ponto para aceitar formato brasileiro
                String entrada = scanner.nextLine().trim().replace(",", ".");
                double valor = Double.parseDouble(entrada);
                if (valor >= 0) return valor;
                System.out.print("\n✖  Valor inválido! Digite novamente (>= 0): ");
            } catch (NumberFormatException e) {
                System.out.print("\n✖  Entrada inválida! Digite um número (ex: 1500,00): ");
            }
        }
    }
}