package com.armaghanehayat.autism.service.impl;

import com.armaghanehayat.autism.domain.Donation;
import com.armaghanehayat.autism.repository.DonationRepository;
import com.armaghanehayat.autism.service.DonationService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Donation}.
 */
@Service
@Transactional
public class DonationServiceImpl implements DonationService {

    private final Logger log = LoggerFactory.getLogger(DonationServiceImpl.class);

    private final DonationRepository donationRepository;

    public DonationServiceImpl(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    @Override
    public Donation save(Donation donation) {
        log.debug("Request to save Donation : {}", donation);
        return donationRepository.save(donation);
    }

    @Override
    public Optional<Donation> partialUpdate(Donation donation) {
        log.debug("Request to partially update Donation : {}", donation);

        return donationRepository
            .findById(donation.getId())
            .map(
                existingDonation -> {
                    if (donation.getIsCash() != null) {
                        existingDonation.setIsCash(donation.getIsCash());
                    }
                    if (donation.getAmount() != null) {
                        existingDonation.setAmount(donation.getAmount());
                    }
                    if (donation.getDonationDate() != null) {
                        existingDonation.setDonationDate(donation.getDonationDate());
                    }
                    if (donation.getHelpType() != null) {
                        existingDonation.setHelpType(donation.getHelpType());
                    }
                    if (donation.getDescription() != null) {
                        existingDonation.setDescription(donation.getDescription());
                    }
                    if (donation.getReceipt() != null) {
                        existingDonation.setReceipt(donation.getReceipt());
                    }
                    if (donation.getReceiptContentType() != null) {
                        existingDonation.setReceiptContentType(donation.getReceiptContentType());
                    }
                    if (donation.getAccount() != null) {
                        existingDonation.setAccount(donation.getAccount());
                    }

                    return existingDonation;
                }
            )
            .map(donationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Donation> findAll(Pageable pageable) {
        log.debug("Request to get all Donations");
        return donationRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Donation> findOne(Long id) {
        log.debug("Request to get Donation : {}", id);
        return donationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Donation : {}", id);
        donationRepository.deleteById(id);
    }
}
