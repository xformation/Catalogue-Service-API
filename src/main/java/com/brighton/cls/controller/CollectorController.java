package com.brighton.cls.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brighton.cls.config.Constants;
import com.brighton.cls.domain.Catalog;
import com.brighton.cls.domain.CatalogDetail;
import com.brighton.cls.domain.Collector;
import com.brighton.cls.domain.Dashboard;
import com.brighton.cls.domain.Library;
import com.brighton.cls.repository.CollectorRepository;
import com.brighton.cls.repository.DashboardRepository;
import com.brighton.cls.repository.LibraryRepository;

import io.github.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link com.brighton.cls.domain.Collector}.
 */
@RestController
@RequestMapping("/api")
public class CollectorController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String ENTITY_NAME = "collector";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	@Autowired
	private CollectorRepository collectorRepository;

	@Autowired
	private DashboardRepository dashboardRepository;

	@Autowired
	private LibraryRepository libraryRepository;

	/**
	 * {@code POST  /addCollector} : Create a new collector.
	 *
	 * @param name: collector's name.
	 * @param type: collector's type like AWS, GCS, AZURE, SYNECTIKS
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the list of collectors, or with status {@code 400 (Bad Request)}
	 *         if the name or type not provided.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/addCollector")
	public ResponseEntity<List<Catalog>> addCollector(@RequestParam String name, @RequestParam String type,
			@RequestParam(required = false) String description,
			@RequestParam(required = false) String subType,
			@RequestParam(name = "userName", required = false) String userName) throws URISyntaxException {
		logger.info(String.format("Request to create a Collector. Collector name : %s, type : %s", name, type));
		Collector collector = new Collector();
		collector.setName(name);
		collector.setType(type);
		collector.setSubType(subType);
		collector.setDescription(description);

		if (!StringUtils.isBlank(userName)) {
			collector.setCreatedBy(userName);
			collector.setUpdatedBy(userName);
		} else {
			collector.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			collector.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}
		Instant now = Instant.now();
		collector.setCreatedOn(now);
		collector.setUpdatedOn(now);

		collector = collectorRepository.save(collector);
		List<Catalog> list = getAllCollectors();
		return ResponseEntity
				.created(new URI("/api/addCollector/" + collector.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, false, ENTITY_NAME, collector.getId().toString()))
				.body(list);

	}

	/**
	 * {@code PUT  /updateCollector} : Updates an existing collector.
	 *
	 * @param id:         collector's id.
	 * @param dataSource: collector's dataSource.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated collector, or with status {@code 400 (Bad Request)} if
	 *         the id is null, or with status {@code 500 (Internal Server Error)} if
	 *         the collector couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/updateCollector")
	public ResponseEntity<Collector> updateCollector(@RequestParam Long id, @RequestParam String name,
			@RequestParam String type, @RequestParam(required = false) String description,
			@RequestParam(name = "userName", required = false) String userName) throws URISyntaxException {
		logger.info(String.format("Request to update a Collector. Collector id : %d, type : %s", id, type));

		Collector collector = new Collector();
		collector.setId(id);
		collector.setName(name);
		collector.setDescription(description);
		collector.setType(type);
		if (!StringUtils.isBlank(userName)) {
			collector.setCreatedBy(userName);
			collector.setUpdatedBy(userName);
		} else {
			collector.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			collector.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}
		Instant now = Instant.now();
		collector.setUpdatedOn(now);

		collector = collectorRepository.save(collector);

		return ResponseEntity
				.created(new URI("/api/updateCollector/" + collector.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, false, ENTITY_NAME, collector.getId().toString()))
				.body(collector);
	}

	/**
	 * {@code DELETE  /deleteCollector/:id} : delete a collector.
	 *
	 * @param id the id of the collector to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/deleteCollector/{id}")
	public ResponseEntity<Void> deleteCollector(@PathVariable Long id) {
		logger.info(String.format("Request to delete a Collector. Collector id : %d", id));
		Optional<Collector> oc = collectorRepository.findById(id);
		if (oc.isPresent()) {
			logger.debug("Deleting dashboards related to collector id: ", id);
			Dashboard dashboard = new Dashboard();
			dashboard.setCollector(oc.get());
			dashboardRepository.deleteAll(dashboardRepository.findAll(Example.of(dashboard)));

			logger.debug("Deleting library related to collector id: ", id);
			Library library = new Library();
			library.setCollector(oc.get());
			libraryRepository.deleteAll(libraryRepository.findAll(Example.of(library)));

			collectorRepository.deleteById(id);
		}
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
				.build();
	}

	/**
	 * {@code GET  /listCollector} : get all the collectors.
	 *
	 * @return the {@link List<Collector>} with status {@code 200 (OK)} and the list
	 *         of collectors in body.
	 */
	@GetMapping("/listCollector")
	public List<Catalog> getAllCollectors() {
		logger.info("Request to get all Collectors");
		List<Collector> listCollector = collectorRepository.findAll();
		List<Catalog> catalogList = new ArrayList<>();
		for (Collector collector : listCollector) {
			Catalog catalog = createCatalog(collector);
			catalogList.add(catalog);
		}
		logger.info("Request to get all Collectors completed. Response returned");
		return catalogList;
	}

	private Catalog createCatalog(Collector collector) {
		Catalog catalog = new Catalog();
		catalog.setId(collector.getId());
		catalog.setCatalogName(collector.getName());
		catalog.setType(collector.getType());
		catalog.setCatalogDescription(collector.getDescription());

		Dashboard dashboard = new Dashboard();
		dashboard.setCollector(collector);
		List<Dashboard> dashboardList = dashboardRepository.findAll(Example.of(dashboard));
		List<CatalogDetail> catalogDetailList = new ArrayList<>();
		for (Dashboard db : dashboardList) {
			CatalogDetail catalogDetail = new CatalogDetail();
			catalogDetail.setTitle(db.getName());
			catalogDetail.setDescription(db.getDescription());
			catalogDetail.setDashboardJson(new String(db.getDashboard()));
			catalogDetailList.add(catalogDetail);
		}
		catalog.setCatalogDetail(catalogDetailList);
		return catalog;
	}

	/**
	 * {@code GET  /listCollector/:id} : get a collector for given id.
	 *
	 * @param id the id of the collector to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the collector, or with status {@code 404 (Not Found)}.
	 * @throws URISyntaxException
	 */
	@GetMapping("/getCollector/{id}")
	public ResponseEntity<Catalog> getCollector(@PathVariable Long id) throws URISyntaxException {
		logger.debug("Request to get a Collector. Collector id : ", id);
		Collector collector = collectorRepository.findById(id).get();
		Catalog catalog = createCatalog(collector);
		return ResponseEntity
				.created(new URI("/api/listCollector/" + collector.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, false, ENTITY_NAME, collector.getId().toString()))
				.body(catalog);
	}

	/**
	 * {@code GET  /searchCollector} : get collectors based on filter criteria.
	 *
	 * @return the {@link List<Collector>} with status {@code 200 (OK)} and the list
	 *         of collectors in body.
	 */
	@GetMapping("/searchCollector")
	public List<Collector> searchCollector(@RequestParam Map<String, String> criteriaMap) {
		logger.debug("Request to get Collectors on given filter criteria");
		Collector obj = new Collector();
		boolean isFilter = false;

		if (criteriaMap.get("id") != null) {
			obj.setId(Long.parseLong(criteriaMap.get("id")));
			isFilter = true;
		}
		if (criteriaMap.get("name") != null) {
			obj.setName(criteriaMap.get("name"));
			isFilter = true;
		}
		if (criteriaMap.get("type") != null) {
			obj.setType(criteriaMap.get("type"));
			isFilter = true;
		}
		if (criteriaMap.get("datasource") != null) {
			obj.setDatasource(criteriaMap.get("datasource"));
			isFilter = true;
		}

		List<Collector> list = null;
		if (isFilter) {
			list = this.collectorRepository.findAll(Example.of(obj), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.collectorRepository.findAll(Sort.by(Direction.DESC, "id"));
		}

		return list;
	}

}
