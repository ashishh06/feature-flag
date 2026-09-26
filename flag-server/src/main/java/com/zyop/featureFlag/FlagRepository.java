package com.zyop.featureFlag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FlagRepository extends JpaRepository<Flag, Long> {
    // empty on purpose — save(), findAll(), findById() etc. come for free
}
