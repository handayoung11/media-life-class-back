package com.jin.miracle.miracle_like;

import com.jin.miracle.miracle.Miracle;
import com.jin.miracle.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MiracleLikeRepo extends JpaRepository<MiracleLike, Long> {
    Optional<MiracleLike> findByMiracleAndUser(Miracle m, Users u);
}
