package com.zyop.featureFlag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FlagRepository extends JpaRepository<Flag, Long> {
    Optional<Flag> findByKey(String key);
    List<Flag> findByArchivedFalse();
    boolean existsByKey(String key);
}
