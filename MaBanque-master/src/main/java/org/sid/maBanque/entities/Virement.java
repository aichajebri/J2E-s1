package org.sid.maBanque.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue("VIR")
public class Virement extends Operation {
    private String compteDestination;

    public Virement() {
        super();
    }

    public Virement(Date dateOperation, double montant, Compte compte, String compteDestination) {
        super(dateOperation, montant, compte);
        this.compteDestination = compteDestination;
    }

    public String getCompteDestination() {
        return compteDestination;
    }

    public void setCompteDestination(String compteDestination) {
        this.compteDestination = compteDestination;
    }
}
