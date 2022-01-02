package com.armaghanehayat.autism.service.impl;

import com.armaghanehayat.autism.domain.CeremonyUser;
import com.armaghanehayat.autism.repository.CeremonyUserRepository;
import com.armaghanehayat.autism.service.CeremonyUserService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CeremonyUser}.
 */
@Service
@Transactional
public class CeremonyUserServiceImpl implements CeremonyUserService {

    private final Logger log = LoggerFactory.getLogger(CeremonyUserServiceImpl.class);

    private final CeremonyUserRepository ceremonyUserRepository;

    public CeremonyUserServiceImpl(CeremonyUserRepository ceremonyUserRepository) {
        this.ceremonyUserRepository = ceremonyUserRepository;
    }

    @Override
    public CeremonyUser save(CeremonyUser ceremonyUser) {
        log.debug("Request to save CeremonyUser : {}", ceremonyUser);
        return ceremonyUserRepository.save(ceremonyUser);
    }

    @Override
    public Optional<CeremonyUser> partialUpdate(CeremonyUser ceremonyUser) {
        log.debug("Request to partially update CeremonyUser : {}", ceremonyUser);

        return ceremonyUserRepository
            .findById(ceremonyUser.getId())
            .map(
                existingCeremonyUser -> {
                    if (ceremonyUser.getName() != null) {
                        existingCeremonyUser.setName(ceremonyUser.getName());
                    }
                    if (ceremonyUser.getFamily() != null) {
                        existingCeremonyUser.setFamily(ceremonyUser.getFamily());
                    }
                    if (ceremonyUser.getPhoneNumber() != null) {
                        existingCeremonyUser.setPhoneNumber(ceremonyUser.getPhoneNumber());
                    }
                    if (ceremonyUser.getHomeNumber() != null) {
                        existingCeremonyUser.setHomeNumber(ceremonyUser.getHomeNumber());
                    }
                    if (ceremonyUser.getAddress() != null) {
                        existingCeremonyUser.setAddress(ceremonyUser.getAddress());
                    }

                    return existingCeremonyUser;
                }
            )
            .map(ceremonyUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CeremonyUser> findAll(Pageable pageable) {
        log.debug("Request to get all CeremonyUsers");
        return ceremonyUserRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CeremonyUser> findOne(Long id) {
        log.debug("Request to get CeremonyUser : {}", id);
        return ceremonyUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CeremonyUser : {}", id);
        ceremonyUserRepository.deleteById(id);
    }
}
