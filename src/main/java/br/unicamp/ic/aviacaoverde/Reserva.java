package br.unicamp.ic.aviacaoverde;

import java.util.Observable;

public class Reserva extends Observable {

	private Passageiro passageiro;
	private boolean confirmada;

	public Passageiro getPassageiro() {
		return passageiro;
	}

	public void setPassageiro(Passageiro passageiro) {
		this.passageiro = passageiro;
	}

	public boolean isConfirmada() {
		return confirmada;
	}

	public void setConfirmada(boolean confirmada) {
	    this.confirmada = confirmada;

	    // Notifica observadores caso o status da reserva seja alterado.
	    // Neste exemplo o status está sendo atualizado pelo controlador mas
	    // ninguém está informando o usuário que a reserva passou a ser confirmada.
	    // Com o Pattern Oberver notificamos observadores interessados nessa alteração
	    // O que nesse caso fará o print da infromação da reserva que foi confirmada.
	    setChanged();
	    notifyObservers();
	}

    @Override public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null || getClass() != o.getClass())
	    return false;

	Reserva reserva = (Reserva)o;

	if (confirmada != reserva.confirmada)
	    return false;
        return passageiro != null ? passageiro.equals(reserva.passageiro) : reserva.passageiro == null;

    }

    @Override public int hashCode() {
	int result = passageiro != null ? passageiro.hashCode() : 0;
	result = 31 * result + (confirmada ? 1 : 0);
	return result;
    }
}
