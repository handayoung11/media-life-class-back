package com.jin.miracle.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {

    @Query("select r from RefreshToken r where HEX(r.value) = :value and r.id = :id")
    Optional<RefreshToken> findByValueAndId(String value, String id);

    @Modifying
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("delete from RefreshToken r where HEX(r.value) = :value")
    void deleteByValue(String value);
}
