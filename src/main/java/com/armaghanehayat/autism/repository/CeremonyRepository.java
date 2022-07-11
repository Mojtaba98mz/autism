package com.armaghanehayat.autism.repository;

import com.armaghanehayat.autism.domain.Ceremony;
import com.armaghanehayat.autism.domain.User;
import com.armaghanehayat.autism.web.rest.vm.ReportMonthListVM;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ceremony entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CeremonyRepository extends JpaRepository<Ceremony, Long>, JpaSpecificationExecutor<Ceremony> {
    @Query(
        "select new com.armaghanehayat.autism.web.rest.vm.ReportMonthListVM((concat(cu.name, ' ', cu.family)),c.amount,c.givenDate) " +
        "from Ceremony c inner join fetch CeremonyUser cu on c.ceremonyUser.id = cu.id where c.givenDate between :fromDate and :toDate"
    )
    List<ReportMonthListVM> findAllCeremonyInDate(@Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate);
}
