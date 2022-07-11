package com.armaghanehayat.autism.repository;

import com.armaghanehayat.autism.domain.Donation;
import com.armaghanehayat.autism.domain.User;
import com.armaghanehayat.autism.web.rest.vm.ReportMonthListVM;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Donation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DonationRepository extends JpaRepository<Donation, Long>, JpaSpecificationExecutor<Donation> {
    List<Donation> findAllByGiverId(Long giverId);

    @Query(
        "select new com.armaghanehayat.autism.web.rest.vm.ReportMonthListVM(d.amount,d.donationDate) " +
        "from Donation d inner join fetch Giver g on d.giver.id = g.id where d.donationDate between :fromDate and :toDate and g.supporter = :supporter"
    )
    List<ReportMonthListVM> findAllDonationsOfUserInDate(
        @Param("fromDate") Instant fromDate,
        @Param("toDate") Instant toDate,
        @Param("supporter") User supporter
    );
}
