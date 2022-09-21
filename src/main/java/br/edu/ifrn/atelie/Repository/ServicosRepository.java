package br.edu.ifrn.atelie.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifrn.atelie.Modelo.Servicos;

@Repository
public interface ServicosRepository extends JpaRepository<Servicos, Integer>{

}
