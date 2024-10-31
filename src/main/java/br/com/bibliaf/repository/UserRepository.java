package br.com.bibliaf.repository;

import br.com.bibliaf.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    public Page<UserModel> findByUsernameStartingWithIgnoreCaseOrderByUsername(String username, Pageable pageable);

    public Page<UserModel> findAll(Pageable pageable);

    public Page<UserModel> findByEmail(String email, Pageable pageable);

    UserDetails findByEmail(String email);
}
