package com.armaghanehayat.autism.repository;

import com.armaghanehayat.autism.domain.City;
import com.armaghanehayat.autism.domain.Giver;
import com.armaghanehayat.autism.domain.Province;
import com.armaghanehayat.autism.domain.enumeration.Account;
import com.armaghanehayat.autism.domain.enumeration.HelpType;
import com.armaghanehayat.autism.web.rest.vm.ReportListVM;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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
    List<Giver> filterByGiverNameForAdmin(@Param("filter") String filter);

    @Query(
        "select giver from Giver giver where giver.supporter.login = ?#{principal.username} and lower(concat(giver.name, ' ', giver.family)) like lower(concat('%', :filter,'%'))"
    )
    List<Giver> filterByGiverName(@Param("filter") String filter);

    @Query(
        "select new com.armaghanehayat.autism.web.rest.vm.ReportListVM(g.id,g.name,g.family,g.phoneNumber,d.isCash,d.amount,d.donationDate,d.helpType,p.name,c.name,d.account)" +
        "from Giver g left join fetch Donation d on g.id = d.giver.id inner join fetch Province p on g.province.id = p.id " +
        "inner join fetch City c on c.id = g.city.id where d.isCash = :isCash and d.amount between :amountFrom and :amountTo and d.donationDate between :fromDate and :toDate " +
        "and d.helpType in(:helpType) and p.id in(:pId) and c.id in(:cId) and d.account in (:account)"
    )
    List<ReportListVM> getReport(
        @Param("isCash") Boolean isCash,
        @Param("amountFrom") Long amountFrom,
        @Param("amountTo") Long amountTo,
        @Param("fromDate") Instant fromDate,
        @Param("toDate") Instant toDate,
        @Param("helpType") List<HelpType> helpType,
        @Param("pId") List<Long> pId,
        @Param("cId") List<Long> cId,
        @Param("account") List<Account> account
    );

    @Query("select g.phoneNumber from Giver g")
    List<String> findAllPhoneNumbers();
}
