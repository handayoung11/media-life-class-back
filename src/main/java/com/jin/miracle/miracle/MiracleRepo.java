package com.jin.miracle.miracle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MiracleRepo extends JpaRepository<Miracle, Long> {
}
