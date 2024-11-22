package br.com.bibliaf.repository;

import br.com.bibliaf.model.AuthorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorModel, Long> {

    public List<AuthorModel> findByNameStartingWithIgnoreCaseOrderByName(String name);

    public List<AuthorModel> findAllByOrderByNameAsc();
}
