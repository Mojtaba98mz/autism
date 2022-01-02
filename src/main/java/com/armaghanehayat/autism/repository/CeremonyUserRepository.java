package com.armaghanehayat.autism.repository;

import com.armaghanehayat.autism.domain.CeremonyUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CeremonyUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CeremonyUserRepository extends JpaRepository<CeremonyUser, Long>, JpaSpecificationExecutor<CeremonyUser> {}
