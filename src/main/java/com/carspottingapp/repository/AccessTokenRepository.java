package com.carspottingapp.repository;

import com.carspottingapp.model.token.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    @Query(value = """
            select t from AccessToken t inner join User u\s
            on t.user.id = u.id\s
            where u.id = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<AccessToken> findAllValidTokensByUser(Long id);

    Optional<AccessToken> findByToken(String token);
}
