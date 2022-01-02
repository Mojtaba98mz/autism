package com.armaghanehayat.autism.repository;

import com.armaghanehayat.autism.domain.Ceremony;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ceremony entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CeremonyRepository extends JpaRepository<Ceremony, Long>, JpaSpecificationExecutor<Ceremony> {}
