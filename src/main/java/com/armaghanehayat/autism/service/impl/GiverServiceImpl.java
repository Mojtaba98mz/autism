package com.armaghanehayat.autism.service.impl;

import com.armaghanehayat.autism.config.Constants;
import com.armaghanehayat.autism.domain.Giver;
import com.armaghanehayat.autism.domain.User;
import com.armaghanehayat.autism.repository.GiverRepository;
import com.armaghanehayat.autism.repository.UserRepository;
import com.armaghanehayat.autism.security.AuthoritiesConstants;
import com.armaghanehayat.autism.service.GiverService;
import com.armaghanehayat.autism.service.UserService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.xml.crypto.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Giver}.
 */
@Service
@Transactional
public class GiverServiceImpl implements GiverService {

    private final UserService userService;

    private final Logger log = LoggerFactory.getLogger(GiverServiceImpl.class);

    private final GiverRepository giverRepository;

    private final UserRepository userRepository;

    public GiverServiceImpl(GiverRepository giverRepository, UserService userService, UserRepository userRepository) {
        this.giverRepository = giverRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public Giver save(Giver giver) {
        if (userService.getUserWithAuthorities().isPresent()) {
            User currentUser = userService.getUserWithAuthorities().get();
            giver.setAbsorbant(currentUser);
            giver.setSupporter(currentUser);
        }
        giver.setAbsorbDate(Instant.now());
        log.debug("Request to save Giver : {}", giver);
        return giverRepository.save(giver);
    }

    @Override
    public Optional<Giver> partialUpdate(Giver giver) {
        log.debug("Request to partially update Giver : {}", giver);

        return giverRepository
            .findById(giver.getId())
            .map(
                existingGiver -> {
                    if (giver.getName() != null) {
                        existingGiver.setName(giver.getName());
                    }
                    if (giver.getFamily() != null) {
                        existingGiver.setFamily(giver.getFamily());
                    }
                    if (giver.getPhoneNumber() != null) {
                        existingGiver.setPhoneNumber(giver.getPhoneNumber());
                    }
                    if (giver.getAddress() != null) {
                        existingGiver.setAddress(giver.getAddress());
                    }
                    if (giver.getAbsorbDate() != null) {
                        existingGiver.setAbsorbDate(giver.getAbsorbDate());
                    }

                    return existingGiver;
                }
            )
            .map(giverRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Giver> findAll(Pageable pageable) {
        log.debug("Request to get all Givers");
        return giverRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Giver> findOne(Long id) {
        log.debug("Request to get Giver : {}", id);
        return giverRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Giver : {}", id);
        giverRepository.deleteById(id);
    }

    @Override
    public List<User> findAllGiversSupporters() {
        List<User> allUsers = userRepository.findAllByIdNotNullAndActivatedIsTrue();
        return allUsers.stream().filter(item -> item.hasOnlyRole(AuthoritiesConstants.USER)).collect(Collectors.toList());
    }
}
