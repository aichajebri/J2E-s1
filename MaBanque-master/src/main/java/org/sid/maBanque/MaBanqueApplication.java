package org.sid.maBanque;

import java.util.Date;

import org.sid.maBanque.dao.ClientRepository;
import org.sid.maBanque.dao.CompteRepository;
import org.sid.maBanque.dao.OperationRepository;
import org.sid.maBanque.entities.Client;
import org.sid.maBanque.entities.Compte;
import org.sid.maBanque.entities.CompteCourant;
import org.sid.maBanque.entities.CompteEpargne;
import org.sid.maBanque.entities.Retrait;
import org.sid.maBanque.entities.Versement;
import org.sid.maBanque.metier.IBanqueMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MaBanqueApplication implements CommandLineRunner{
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private CompteRepository compteRepository;
	
	@Autowired
	private OperationRepository operationRepository;
	
	@Autowired
	private IBanqueMetier banqueMetier;
	
	public static void main(String[] args) {
		SpringApplication.run(MaBanqueApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	

		 Client c1 = clientRepository.save(new Client("Diane", "diane@gmail.com"));
		 Client c2 = clientRepository.save(new Client("Dom", "dom@gmail.com"));

		  Compte cp1 = compteRepository.save(new CompteCourant("c1", new Date(), 90000,
		  c1, 6000)); Compte cp2 = compteRepository.save(new CompteEpargne("c2", new
		  Date(), 6000, c2, 5.5));

		  operationRepository.save(new Versement(new Date(),9000,cp1));
		  operationRepository.save(new Versement(new Date(),6000,cp1));
		  operationRepository.save(new Versement(new Date(),2000,cp1));
		  operationRepository.save(new Retrait(new Date(),2000,cp1));

		  operationRepository.save(new Versement(new Date(),2000,cp2));
		  operationRepository.save(new Versement(new Date(),3000,cp2));
		  operationRepository.save(new Versement(new Date(),1000,cp2));
		  operationRepository.save(new Retrait(new Date(),1000,cp2));

		  banqueMetier.verser("c1",111111);

	}
	
	

}
