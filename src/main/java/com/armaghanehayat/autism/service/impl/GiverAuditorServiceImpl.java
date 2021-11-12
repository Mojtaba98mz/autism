package com.armaghanehayat.autism.service.impl;

import com.armaghanehayat.autism.domain.Giver;
import com.armaghanehayat.autism.domain.GiverAuditor;
import com.armaghanehayat.autism.domain.User;
import com.armaghanehayat.autism.repository.GiverAuditorRepository;
import com.armaghanehayat.autism.service.GiverAuditorService;
import com.armaghanehayat.autism.service.UserService;
import java.time.Instant;
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

    private final UserService userService;

    public GiverAuditorServiceImpl(GiverAuditorRepository giverAuditorRepository, UserService userService) {
        this.giverAuditorRepository = giverAuditorRepository;
        this.userService = userService;
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
                    if (giverAuditor.getFieldName() != null) {
                        existingGiverAuditor.setFieldName(giverAuditor.getFieldName());
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

    @Override
    public void giverUpdated(Giver oldGiver, Giver newGiver) {
        User currentUser = null;
        if (userService.getUserWithAuthorities().isPresent()) {
            currentUser = userService.getUserWithAuthorities().get();
        }

        if (!oldGiver.getName().equals(newGiver.getName())) {
            GiverAuditor giverAuditor = new GiverAuditor();
            giverAuditor.setFieldName("نام");
            giverAuditor.setOldValue(oldGiver.getName());
            giverAuditor.setNewValue(newGiver.getName());
            giverAuditor.setChangeDate(Instant.now());
            giverAuditor.setGiver(newGiver);
            giverAuditor.setAuditor(currentUser);
            giverAuditorRepository.save(giverAuditor);
        }

        if (!oldGiver.getFamily().equals(newGiver.getFamily())) {
            GiverAuditor giverAuditor = new GiverAuditor();
            giverAuditor.setFieldName("نام خانوادگی");
            giverAuditor.setOldValue(oldGiver.getFamily());
            giverAuditor.setNewValue(newGiver.getFamily());
            giverAuditor.setChangeDate(Instant.now());
            giverAuditor.setGiver(newGiver);
            giverAuditor.setAuditor(currentUser);
            giverAuditorRepository.save(giverAuditor);
        }

        if (!oldGiver.getPhoneNumber().equals(newGiver.getPhoneNumber())) {
            GiverAuditor giverAuditor = new GiverAuditor();
            giverAuditor.setFieldName("شماره همراه");
            giverAuditor.setOldValue(oldGiver.getPhoneNumber());
            giverAuditor.setNewValue(newGiver.getPhoneNumber());
            giverAuditor.setChangeDate(Instant.now());
            giverAuditor.setGiver(newGiver);
            giverAuditor.setAuditor(currentUser);
            giverAuditorRepository.save(giverAuditor);
        }

        if (
            oldGiver.getHomeNumber() != null &&
            !oldGiver.getHomeNumber().equals(newGiver.getHomeNumber()) ||
            newGiver.getHomeNumber() != null &&
            !newGiver.getHomeNumber().equals(oldGiver.getHomeNumber())
        ) {
            GiverAuditor giverAuditor = new GiverAuditor();
            giverAuditor.setFieldName("تلفن ثابت");
            giverAuditor.setOldValue(oldGiver.getHomeNumber());
            giverAuditor.setNewValue(newGiver.getHomeNumber());
            giverAuditor.setChangeDate(Instant.now());
            giverAuditor.setGiver(newGiver);
            giverAuditor.setAuditor(currentUser);
            giverAuditorRepository.save(giverAuditor);
        }

        if (
            oldGiver.getAddress() != null &&
            !oldGiver.getAddress().equals(newGiver.getAddress()) ||
            newGiver.getAddress() != null &&
            !newGiver.getAddress().equals(oldGiver.getAddress())
        ) {
            GiverAuditor giverAuditor = new GiverAuditor();
            giverAuditor.setFieldName("نشانی");
            giverAuditor.setOldValue(oldGiver.getAddress());
            giverAuditor.setNewValue(newGiver.getAddress());
            giverAuditor.setChangeDate(Instant.now());
            giverAuditor.setGiver(newGiver);
            giverAuditor.setAuditor(currentUser);
            giverAuditorRepository.save(giverAuditor);
        }

        if (
            oldGiver.getProvince() != null &&
            !oldGiver.getProvince().equals(newGiver.getProvince()) ||
            newGiver.getProvince() != null &&
            !newGiver.getProvince().equals(oldGiver.getProvince())
        ) {
            GiverAuditor giverAuditor = new GiverAuditor();
            giverAuditor.setFieldName("استان");
            giverAuditor.setOldValue(oldGiver.getProvince() != null ? oldGiver.getProvince().getName() : "");
            giverAuditor.setNewValue(newGiver.getProvince() != null ? newGiver.getProvince().getName() : "");
            giverAuditor.setChangeDate(Instant.now());
            giverAuditor.setGiver(newGiver);
            giverAuditor.setAuditor(currentUser);
            giverAuditorRepository.save(giverAuditor);
        }

        if (
            oldGiver.getCity() != null &&
            !oldGiver.getCity().equals(newGiver.getCity()) ||
            newGiver.getCity() != null &&
            !newGiver.getCity().equals(oldGiver.getCity())
        ) {
            GiverAuditor giverAuditor = new GiverAuditor();
            giverAuditor.setFieldName("شهر");
            giverAuditor.setOldValue(oldGiver.getCity() != null ? oldGiver.getCity().getName() : "");
            giverAuditor.setNewValue(newGiver.getCity() != null ? newGiver.getCity().getName() : "");
            giverAuditor.setChangeDate(Instant.now());
            giverAuditor.setGiver(newGiver);
            giverAuditor.setAuditor(currentUser);
            giverAuditorRepository.save(giverAuditor);
        }
    }
}
