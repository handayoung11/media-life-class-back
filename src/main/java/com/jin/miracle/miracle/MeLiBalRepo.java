package com.jin.miracle.miracle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeLiBalRepo extends JpaRepository<MeLiBal, Long> {
}
