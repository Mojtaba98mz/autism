package com.armaghanehayat.autism.repository;

import com.armaghanehayat.autism.domain.CeremonyUser;
import com.armaghanehayat.autism.domain.Giver;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CeremonyUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CeremonyUserRepository extends JpaRepository<CeremonyUser, Long>, JpaSpecificationExecutor<CeremonyUser> {
    @Query(
        "select ceremonyUser from CeremonyUser ceremonyUser where lower(concat(ceremonyUser.name, ' ', ceremonyUser.family)) like lower(concat('%', :filter,'%'))"
    )
    List<CeremonyUser> filterByCeremonyUserName(@Param("filter") String filter);
}
