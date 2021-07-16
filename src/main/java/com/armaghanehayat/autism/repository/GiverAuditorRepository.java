package com.armaghanehayat.autism.repository;

import com.armaghanehayat.autism.domain.GiverAuditor;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GiverAuditor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GiverAuditorRepository extends JpaRepository<GiverAuditor, Long>, JpaSpecificationExecutor<GiverAuditor> {
    @Query("select giverAuditor from GiverAuditor giverAuditor where giverAuditor.auditor.login = ?#{principal.username}")
    List<GiverAuditor> findByAuditorIsCurrentUser();
}
