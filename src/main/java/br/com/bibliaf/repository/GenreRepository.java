package br.com.bibliaf.repository;

import br.com.bibliaf.model.GenreModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<GenreModel, Long> {

    public List<GenreModel> findByNameStartingWithIgnoreCaseOrderByName(String name);

    public List<GenreModel> findAllByOrderByNameAsc();
}
