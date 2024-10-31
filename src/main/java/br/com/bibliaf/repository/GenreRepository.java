package br.com.bibliaf.repository;

import br.com.bibliaf.model.GenreModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<GenreModel, Long> {

    public Page<GenreModel> findByNameStartingWithIgnoreCaseOrderByName(String name, Pageable pageable);

    public Page<GenreModel> findAll(Pageable pageable);
}
