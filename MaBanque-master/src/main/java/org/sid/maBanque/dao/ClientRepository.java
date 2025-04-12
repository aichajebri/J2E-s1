package org.sid.maBanque.dao;

import org.sid.maBanque.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository  extends JpaRepository<Client, Long>{

}
