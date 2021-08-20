package com.armaghanehayat.autism.repository;

import com.armaghanehayat.autism.domain.Giver;
import com.armaghanehayat.autism.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Giver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GiverRepository extends JpaRepository<Giver, Long>, JpaSpecificationExecutor<Giver> {
    @Query("select giver from Giver giver where giver.absorbant.login = ?#{principal.username}")
    List<Giver> findByAbsorbantIsCurrentUser();

    @Query("select giver from Giver giver where giver.supporter.login = ?#{principal.username}")
    List<Giver> findBySupporterIsCurrentUser();
}
