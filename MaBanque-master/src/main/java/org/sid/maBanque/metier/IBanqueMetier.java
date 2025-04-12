package org.sid.maBanque.metier;

import org.sid.maBanque.entities.AccountType;
import org.sid.maBanque.entities.Compte;
import org.sid.maBanque.entities.Operation;
import org.springframework.data.domain.Page;

public interface IBanqueMetier {

	public Compte consulterCompte(String codeCpte);
	public void verser(String codeCpte, double montant);
	public void retirer(String codeCpte, double montant);
	public void virement(String codeCpte1, String codeCpte2, double montant);
	public Page<Operation> listOperation(String codeCpte, int page, int size);

	//Compte createAccount(Long clientId, AccountType accountType, double initialBalance);
}
