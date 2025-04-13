package org.sid.maBanque.metier;

import java.util.Date;

import org.sid.maBanque.dao.ClientRepository;
import org.sid.maBanque.dao.CompteRepository;
import org.sid.maBanque.dao.OperationRepository;
import org.sid.maBanque.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BanqueMetierImpl implements IBanqueMetier {

	@Autowired
	private CompteRepository compteRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private OperationRepository operationRepository;
	
	@Override
	public Compte consulterCompte(String codeCpte) {
		Compte cp = compteRepository.findById(codeCpte).get();
		
		if(cp==null) throw new RuntimeException("Compte introuvable");
		return cp;
	}

	@Override
	public void verser(String codeCpte, double montant) {
		Compte cp=consulterCompte(codeCpte);
		Versement v = new Versement(new Date(), montant, cp);
		operationRepository.save(v);
		cp.setSolde(cp.getSolde()  + montant);
		compteRepository.save(cp);
	}

	@Override
	public void retirer(String codeCpte, double montant) {
		Compte cp=consulterCompte(codeCpte);
		double facilitesCaisse=0;
		
		if(cp instanceof CompteCourant)
			facilitesCaisse = ((CompteCourant) cp).getDecouvert();
		
		if((cp.getSolde()+facilitesCaisse)<montant)
			throw new RuntimeException("Solde insuffisant");
		
		Retrait r = new Retrait(new Date(), montant, cp);
		operationRepository.save(r);
		cp.setSolde(cp.getSolde()  - montant);
		compteRepository.save(cp);
	}

	@Override
	public void virement(String codeCpte1, String codeCpte2, double montant) {
		if (codeCpte1.equals(codeCpte2)) {
			throw new RuntimeException("Impossible d'effectuer un virement sur le même compte.");
		}

		Compte compteSource = compteRepository.findById(codeCpte1)
				.orElseThrow(() -> new RuntimeException("Compte source introuvable : " + codeCpte1));

		Compte compteDestination = compteRepository.findById(codeCpte2)
				.orElseThrow(() -> new RuntimeException("Compte destinataire introuvable : " + codeCpte2));

		// Vérification du solde suffisant
		double facilitesCaisse = 0;
		if (compteSource instanceof CompteCourant)
			facilitesCaisse = ((CompteCourant) compteSource).getDecouvert();

		if ((compteSource.getSolde() + facilitesCaisse) < montant)
			throw new RuntimeException("Solde insuffisant pour le virement.");

		// Retirer depuis source
		Virement retrait = new Virement(new Date(), montant, compteSource, codeCpte2);
		operationRepository.save(retrait);
		compteSource.setSolde(compteSource.getSolde() - montant);
		compteRepository.save(compteSource);

		// Verser vers destination
		Versement versement = new Versement(new Date(), montant, compteDestination);
		operationRepository.save(versement);
		compteDestination.setSolde(compteDestination.getSolde() + montant);
		compteRepository.save(compteDestination);
	}



	@Override
	public Page<Operation> listOperation(String codeCpte, int page, int size) {
		return operationRepository.listOperation(codeCpte, new PageRequest(page, size));
	}

//	@Override
//	public Compte createAccount(Long clientId, AccountType accountType, double initialBalance) {
//		// Find the client by ID
//		Client client = clientRepository.findById(clientId)
//				.orElseThrow(() -> new RuntimeException("Client not found"));
//
//		// Generate a unique account code
//		String code = generateAccountCode(); 
//
//		// Create a new Compte (account)
//		Compte compte = new Compte();
//		compte.setCodeCompte(code);
//		compte.setDateCreation(new Date());
//		compte.setSolde(initialBalance);
//		compte.setClient(client);  
//
//		// Save the account to the repository
//		compteRepository.save(compte);
//
//		// Add the account to the client's list of accounts
//		client.getComptes().add(compte);
//		clientRepository.save(client); 
//
//		return compte;
//	}
//
//
//	private String generateAccountCode() {
//		return "AC" + System.currentTimeMillis();
//	}



}
