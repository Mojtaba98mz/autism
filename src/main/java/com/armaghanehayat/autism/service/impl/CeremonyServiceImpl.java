package com.armaghanehayat.autism.service.impl;

import com.armaghanehayat.autism.domain.Ceremony;
import com.armaghanehayat.autism.repository.CeremonyRepository;
import com.armaghanehayat.autism.service.CeremonyService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ceremony}.
 */
@Service
@Transactional
public class CeremonyServiceImpl implements CeremonyService {

    private final Logger log = LoggerFactory.getLogger(CeremonyServiceImpl.class);

    private final CeremonyRepository ceremonyRepository;

    public CeremonyServiceImpl(CeremonyRepository ceremonyRepository) {
        this.ceremonyRepository = ceremonyRepository;
    }

    @Override
    public Ceremony save(Ceremony ceremony) {
        log.debug("Request to save Ceremony : {}", ceremony);
        return ceremonyRepository.save(ceremony);
    }

    @Override
    public Optional<Ceremony> partialUpdate(Ceremony ceremony) {
        log.debug("Request to partially update Ceremony : {}", ceremony);

        return ceremonyRepository
            .findById(ceremony.getId())
            .map(
                existingCeremony -> {
                    if (ceremony.getAmount() != null) {
                        existingCeremony.setAmount(ceremony.getAmount());
                    }
                    if (ceremony.getGivenDate() != null) {
                        existingCeremony.setGivenDate(ceremony.getGivenDate());
                    }
                    if (ceremony.getDescription() != null) {
                        existingCeremony.setDescription(ceremony.getDescription());
                    }
                    if (ceremony.getReceipt() != null) {
                        existingCeremony.setReceipt(ceremony.getReceipt());
                    }
                    if (ceremony.getReceiptContentType() != null) {
                        existingCeremony.setReceiptContentType(ceremony.getReceiptContentType());
                    }

                    return existingCeremony;
                }
            )
            .map(ceremonyRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Ceremony> findAll(Pageable pageable) {
        log.debug("Request to get all Ceremonies");
        return ceremonyRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ceremony> findOne(Long id) {
        log.debug("Request to get Ceremony : {}", id);
        return ceremonyRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ceremony : {}", id);
        ceremonyRepository.deleteById(id);
    }
}
