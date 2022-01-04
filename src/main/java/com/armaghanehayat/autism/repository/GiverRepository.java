package com.armaghanehayat.autism.repository;

import com.armaghanehayat.autism.domain.Giver;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
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

    Optional<Giver> findFirstByPhoneNumber(String phoneNumber);

    @Query("select giver from Giver giver where lower(concat(giver.name, ' ', giver.family)) like lower(concat('%', :filter,'%'))")
    List<Giver> filterByGiverName(@Param("filter") String filter);
}
