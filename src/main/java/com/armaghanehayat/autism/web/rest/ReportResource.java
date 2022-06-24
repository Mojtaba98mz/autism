package com.armaghanehayat.autism.web.rest;

import com.armaghanehayat.autism.domain.Donation;
import com.armaghanehayat.autism.repository.DonationRepository;
import com.armaghanehayat.autism.service.DonationQueryService;
import com.armaghanehayat.autism.service.DonationService;
import com.armaghanehayat.autism.service.SMSService;
import com.armaghanehayat.autism.service.criteria.DonationCriteria;
import com.armaghanehayat.autism.web.rest.errors.BadRequestAlertException;
import com.armaghanehayat.autism.web.rest.vm.ReportVM;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link Donation}.
 */
@RestController
@RequestMapping("/api")
public class ReportResource {

    public ReportResource() {}

    @PostMapping("/reports")
    public ResponseEntity<List<ReportVM>> getReport(@RequestBody ReportVM reportVM) {
        return ResponseEntity.ok().body(new ArrayList<ReportVM>());
    }
}
