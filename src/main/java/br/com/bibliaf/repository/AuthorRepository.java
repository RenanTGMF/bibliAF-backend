package br.com.bibliaf.repository;

import br.com.bibliaf.model.AuthorModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorModel, Long> {

    public Page<AuthorModel> findByNameStartingWithIgnoreCaseOrderByName(String name, Pageable pageable);

    public Page<AuthorModel> findAll(Pageable pageable);
}
