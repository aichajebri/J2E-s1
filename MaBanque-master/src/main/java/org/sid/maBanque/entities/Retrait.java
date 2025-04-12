package org.sid.maBanque.entities;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("R")  
public class Retrait extends Operation {

	public Retrait() {
		super();
	}

	public Retrait(Date dateOperation, double montantOperation, Compte compte) {
		super(dateOperation, montantOperation, compte);
	}
	
	public Retrait(Long numeroOperation, Date dateOperation, double montantOperation, Compte compte) {
		super(numeroOperation, dateOperation, montantOperation, compte);
	}

}
