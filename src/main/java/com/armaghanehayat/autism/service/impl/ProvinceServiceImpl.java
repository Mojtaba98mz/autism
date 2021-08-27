package com.armaghanehayat.autism.service.impl;

import com.armaghanehayat.autism.domain.Province;
import com.armaghanehayat.autism.repository.ProvinceRepository;
import com.armaghanehayat.autism.service.ProvinceService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Province}.
 */
@Service
@Transactional
public class ProvinceServiceImpl implements ProvinceService {

    private final Logger log = LoggerFactory.getLogger(ProvinceServiceImpl.class);

    private final ProvinceRepository provinceRepository;

    public ProvinceServiceImpl(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    @Override
    public Province save(Province province) {
        log.debug("Request to save Province : {}", province);
        return provinceRepository.save(province);
    }

    @Override
    public Optional<Province> partialUpdate(Province province) {
        log.debug("Request to partially update Province : {}", province);

        return provinceRepository
            .findById(province.getId())
            .map(
                existingProvince -> {
                    if (province.getName() != null) {
                        existingProvince.setName(province.getName());
                    }
                    if (province.getEnName() != null) {
                        existingProvince.setEnName(province.getEnName());
                    }

                    return existingProvince;
                }
            )
            .map(provinceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Province> findAll(Pageable pageable) {
        log.debug("Request to get all Provinces");
        return provinceRepository.findAll(pageable);
    }

    /**
     *  Get all the provinces where Giver is {@code null}.
     *  @return the list of entities.
     */
    /* @Transactional(readOnly = true)
    public List<Province> findAllWhereGiverIsNull() {
        log.debug("Request to get all provinces where Giver is null");
        return StreamSupport
            .stream(provinceRepository.findAll().spliterator(), false)
            .filter(province -> province.getGiver() == null)
            .collect(Collectors.toList());
    }*/

    @Override
    @Transactional(readOnly = true)
    public Optional<Province> findOne(Long id) {
        log.debug("Request to get Province : {}", id);
        return provinceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Province : {}", id);
        provinceRepository.deleteById(id);
    }
}
