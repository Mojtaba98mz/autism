package com.armaghanehayat.autism.service.impl;

import com.armaghanehayat.autism.domain.GiverAuditor;
import com.armaghanehayat.autism.repository.GiverAuditorRepository;
import com.armaghanehayat.autism.service.GiverAuditorService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GiverAuditor}.
 */
@Service
@Transactional
public class GiverAuditorServiceImpl implements GiverAuditorService {

    private final Logger log = LoggerFactory.getLogger(GiverAuditorServiceImpl.class);

    private final GiverAuditorRepository giverAuditorRepository;

    public GiverAuditorServiceImpl(GiverAuditorRepository giverAuditorRepository) {
        this.giverAuditorRepository = giverAuditorRepository;
    }

    @Override
    public GiverAuditor save(GiverAuditor giverAuditor) {
        log.debug("Request to save GiverAuditor : {}", giverAuditor);
        return giverAuditorRepository.save(giverAuditor);
    }

    @Override
    public Optional<GiverAuditor> partialUpdate(GiverAuditor giverAuditor) {
        log.debug("Request to partially update GiverAuditor : {}", giverAuditor);

        return giverAuditorRepository
            .findById(giverAuditor.getId())
            .map(
                existingGiverAuditor -> {
                    if (giverAuditor.getFiedlName() != null) {
                        existingGiverAuditor.setFiedlName(giverAuditor.getFiedlName());
                    }
                    if (giverAuditor.getOldValue() != null) {
                        existingGiverAuditor.setOldValue(giverAuditor.getOldValue());
                    }
                    if (giverAuditor.getNewValue() != null) {
                        existingGiverAuditor.setNewValue(giverAuditor.getNewValue());
                    }
                    if (giverAuditor.getChangeDate() != null) {
                        existingGiverAuditor.setChangeDate(giverAuditor.getChangeDate());
                    }

                    return existingGiverAuditor;
                }
            )
            .map(giverAuditorRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GiverAuditor> findAll(Pageable pageable) {
        log.debug("Request to get all GiverAuditors");
        return giverAuditorRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GiverAuditor> findOne(Long id) {
        log.debug("Request to get GiverAuditor : {}", id);
        return giverAuditorRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GiverAuditor : {}", id);
        giverAuditorRepository.deleteById(id);
    }
}
