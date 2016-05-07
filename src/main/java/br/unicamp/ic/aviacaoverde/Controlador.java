package br.unicamp.ic.aviacaoverde;

import java.util.List;
import java.util.stream.Collectors;

public class Controlador {

    private Voo voo;

    public Controlador() {
        super();
        criarVoo();
    }

    public void validarDados(Integer idPassageiro, String numeroVoo, boolean passageiroExistente) throws ValidarDadosException {
        if (!numeroVoo.equals(getNumeroVoo())) {
            throw new ValidarDadosException("Número de voo inválido");
        }

        if (passageiroExistente) {
            if (!existeReserva(idPassageiro)) {
                throw new ValidarDadosException("Não existe uma reserva para esse passageiro neste voo");
            }
        } else {
            if (existeReserva(idPassageiro)) {
                throw new ValidarDadosException("Já existe uma reserva para esse passageiro neste voo");
            }
        }
    }

    public boolean reservar(Integer idPassageiro, String nomePassageiro) {
        Passageiro passageiro = criarPassageiro(idPassageiro, nomePassageiro);
        Reserva reserva = new Reserva();
        reserva.setPassageiro(passageiro);
        int capacidadeTotal = voo.getAeronave().getCapacidadeTotal();

        if (voo.getReservas().size() < capacidadeTotal) {
            reserva.setConfirmada(true);
            adicionarParaReservas(reserva);
            return true;
        }
        adicionarParaListaEspera(reserva);
        return false;
    }

    public Reserva buscarReserva(Integer idPassageiro) {
        Reserva reserva = null;

        if (voo.getIdsReservados().containsKey(idPassageiro)) {
            int index = voo.getIdsReservados().get(idPassageiro);
            reserva = voo.getReservas().get(index);
        } else {
            int index = voo.getIdsListaEspera().get(idPassageiro);
            reserva = voo.getListaDeEspera().get(index);
        }

        return reserva;
    }

    public String getNumeroVoo() {
        return voo.getNumero();
    }

    public Rota getRotaVoo() {
        return voo.getRota();
    }

    public List<String> getPassageirosReserva() {
        return voo.getReservas().stream().map(Reserva::getPassageiro).map(Passageiro::getNome).collect(Collectors.toList());
    }

    public List<String> getPassageirosListaEspera() {
        return voo.getListaDeEspera().stream().map(Reserva::getPassageiro).map(Passageiro::getNome).collect(Collectors.toList());
    }

    public int getTotalReservas() {
        return voo.getReservas().size();
    }

    public boolean isVooLotado() {
        return voo.getReservas().size() == voo.getAeronave().getCapacidadeTotal();
    }

    private void criarVoo() {
        voo = new Voo();
        voo.setNumero("AD4032");
        voo.setAeronave(criarAeronave());
        voo.setRota(criarRota());
    }

    private Aeronave criarAeronave() {
        Aeronave aeronave = new Aeronave();
        aeronave.setCapacidadeTotal(30);
        return aeronave;
    }

    private Rota criarRota() {
        Rota rota = new Rota();
        rota.setOrigem(criarAeroporto("Campinas"));
        rota.setDestino(criarAeroporto("Campo Grande"));
        return rota;
    }

    private Aeroporto criarAeroporto(String nomeCidade) {
        Aeroporto aeroporto = new Aeroporto();
        aeroporto.setNomeCidade(nomeCidade);
        return aeroporto;
    }

    private Passageiro criarPassageiro(Integer idPassageiro, String nomePassageiro) {
        Passageiro passageiro = new Passageiro();
        passageiro.setId(idPassageiro);
        passageiro.setNome(nomePassageiro);

        return passageiro;
    }

    private boolean existeReserva(Integer idPassageiro) {
        return voo.getIdsReservados().containsKey(idPassageiro) || voo.getIdsListaEspera().containsKey(idPassageiro);
    }

    private void adicionarParaReservas(Reserva reserva) {
        voo.getIdsReservados().put(reserva.getPassageiro().getId(), voo.getReservas().size());
        voo.getReservas().add(reserva);
    }

    private void adicionarParaListaEspera(Reserva reserva) {
        voo.getIdsListaEspera().put(reserva.getPassageiro().getId(), voo.getListaDeEspera().size());
        voo.getListaDeEspera().push(reserva);
    }

}
