package com.jin.miracle.miracle;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MiracleRepo extends JpaRepository<Miracle, Long> {
    Long countByUser_LoginId(String userLoginId);

    @Query("SELECT m FROM Miracle m WHERE m.user.loginId = :loginId ORDER BY m.miracleDate DESC limit 5")
    List<Miracle> findLatestRecords(String loginId);

    @Query("SELECT m FROM Miracle m join fetch m.user ORDER BY m.miracleDate DESC")
    Slice<Miracle> findWithUser(Pageable pageable);
}
