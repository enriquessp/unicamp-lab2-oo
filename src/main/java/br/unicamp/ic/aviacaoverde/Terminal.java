package br.unicamp.ic.aviacaoverde;

import java.util.Scanner;

public class Terminal {

    private final Scanner scanner;
    private final Controlador controlador;

    private int opcao;

    public Terminal() {
        super();
        scanner = new Scanner(System.in);
        controlador = new Controlador();
    }

    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        terminal.iniciarSistema();
    }

    private void iniciarSistema() {
        while (opcao != 4) {
            mostrarMenu();
            opcao = scanner.nextInt();
            executaOperacao();
        }
    }

    private void mostrarMenu() {
        System.out.println("Aviação Verde");
        System.out.println();
        System.out.println("Escolha uma opção");
        System.out.println("1. Reserva");
        System.out.println("2. Consulta de Reservas");
        System.out.println("3. Imprimir");
        System.out.println("4. Encerrar");
        System.out.println("5. Cancelar Reserva");
    }

    private void executaOperacao() {
        switch (opcao) {
            case 1:
                reservar();
                break;
            case 2:
                consultarReserva();
                break;
            case 3:
                imprimir();
                break;
            case 4:
                encerrar();
                break;
            case 5:
                cancelar();
                break;
            default:
                System.err.println("Operação Inválida!");
                break;
        }
    }

    private void cancelar() {
    	Integer idPassageiro = solicitarIdPassageiro();
        String numeroVoo = solicitarNumeroVoo();

        try {
			controlador.validarDadosParaCancelamento(idPassageiro, numeroVoo);
		} catch (ValidarDadosException e) {
			System.err.println(e.getMessage());
			return;
		}
        
        boolean cancelado = controlador.cancelarReserva(idPassageiro, numeroVoo);

        if (cancelado) {
        	System.out.println("Reserva cancelada com sucesso!");
        } else {
        	System.out.println("Reserva não pode ser cancelada!");
        }
	}

	private void reservar() {
        Integer idPassageiro = solicitarIdPassageiro();
        String numeroVoo = solicitarNumeroVoo();

        try {
            controlador.validarDados(idPassageiro, numeroVoo, false);
        } catch (ValidarDadosException e) {
            System.err.println(e.getMessage());
            return;
        }

        String nomePassageiro = solicitarNomePassageiro();

        boolean reservar = controlador.reservar(idPassageiro, nomePassageiro);

        if (reservar) {
            System.out.println("Reserva efetuada com sucesso!");
        } else {
            System.out.println("Este voo está lotado, a reserva foi adicionada à lista de espera.");
        }
    }

    private void consultarReserva() {
        Integer idPassageiro = solicitarIdPassageiro();
        String numeroVoo = solicitarNumeroVoo();

        try {
            controlador.validarDados(idPassageiro, numeroVoo, true);
        } catch (ValidarDadosException e) {
            System.err.println(e.getMessage());
            return;
        }

        Reserva reserva = controlador.buscarReserva(idPassageiro);
        Rota rota = controlador.getRotaVoo();

        System.out.println("Dados da reserva:");
        System.out.println("Voo: " + controlador.getNumeroVoo());
        System.out.println("Origem: " + rota.getOrigem().getNomeCidade());
        System.out.println("Destino: " + rota.getDestino().getNomeCidade());

        String escalas = rota.getEscalas().size() > 0 ? Integer.toString(rota.getEscalas().size()) : "Não";
        System.out.println("Escalas: " + escalas);

        Passageiro passageiro = reserva.getPassageiro();
        System.out.println("Id do Passageiro: " + passageiro.getId());
        System.out.println("Nome do Passageiro: " + passageiro.getNome());

        String statusReserva = reserva.isConfirmada() ? "Confirmada" : "Na Lista de Espera";
        System.out.println("Status da Reserva: " + statusReserva);
        System.out.println();
    }

    private void imprimir() {
        System.out.println("Passageiros do voo " + controlador.getNumeroVoo() + ":");

        controlador.getPassageirosReserva().forEach(p -> imprimirDadosPassageiro(p, "Confirmada"));
        controlador.getPassageirosListaEspera().forEach(p -> imprimirDadosPassageiro(p, "Na Lista de Espera"));
    }

    private void encerrar() {
        System.out.println("Pedidos encerrados.");
        System.out.println("O voo partiu com " + controlador.getTotalReservas() + " passageiros");

        boolean lotado = controlador.isVooLotado();
        System.out.println("A aeronave estava " + (lotado ? "loatada" : "com assentos desocupados"));
    }

    private Integer solicitarIdPassageiro() {
        System.out.println("Informe o número de identificação do passageiro");
        return scanner.nextInt();
    }

    private String solicitarNumeroVoo() {
        System.out.println("Informe o número do voo");
        return scanner.next();
    }

    private String solicitarNomePassageiro() {
        System.out.println("Informe o nome do passageiro");
        String nome = scanner.next();

        return nome;
    }

    private void imprimirDadosPassageiro(String nome, String statusReserva) {
        System.out.println("Passageiro: " + nome);
        System.out.println("Status da Reserva: " + statusReserva);
        System.out.println();
    }
}
