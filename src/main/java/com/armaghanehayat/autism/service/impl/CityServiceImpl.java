package com.armaghanehayat.autism.service.impl;

import com.armaghanehayat.autism.domain.City;
import com.armaghanehayat.autism.repository.CityRepository;
import com.armaghanehayat.autism.service.CityService;
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
 * Service Implementation for managing {@link City}.
 */
@Service
@Transactional
public class CityServiceImpl implements CityService {

    private final Logger log = LoggerFactory.getLogger(CityServiceImpl.class);

    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public City save(City city) {
        log.debug("Request to save City : {}", city);
        return cityRepository.save(city);
    }

    @Override
    public Optional<City> partialUpdate(City city) {
        log.debug("Request to partially update City : {}", city);

        return cityRepository
            .findById(city.getId())
            .map(
                existingCity -> {
                    if (city.getName() != null) {
                        existingCity.setName(city.getName());
                    }
                    if (city.getEnName() != null) {
                        existingCity.setEnName(city.getEnName());
                    }

                    return existingCity;
                }
            )
            .map(cityRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<City> findAll(Pageable pageable) {
        log.debug("Request to get all Cities");
        return cityRepository.findAll(pageable);
    }

    /**
     *  Get all the cities where Giver is {@code null}.
     *  @return the list of entities.
     */
    /* @Transactional(readOnly = true)
    public List<City> findAllWhereGiverIsNull() {
        log.debug("Request to get all cities where Giver is null");
        return StreamSupport
            .stream(cityRepository.findAll().spliterator(), false)
            .filter(city -> city.getGiver() == null)
            .collect(Collectors.toList());
    }*/

    @Override
    @Transactional(readOnly = true)
    public Optional<City> findOne(Long id) {
        log.debug("Request to get City : {}", id);
        return cityRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete City : {}", id);
        cityRepository.deleteById(id);
    }
}
