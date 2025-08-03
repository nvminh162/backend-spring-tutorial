package vn.hoidanit.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import vn.hoidanit.domain.Department;
import vn.hoidanit.repository.DepartmentRepository;
import vn.hoidanit.service.DepartmentQueryService;
import vn.hoidanit.service.DepartmentService;
import vn.hoidanit.service.criteria.DepartmentCriteria;
import vn.hoidanit.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.hoidanit.domain.Department}.
 */
@RestController
@RequestMapping("/api/departments")
public class DepartmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(DepartmentResource.class);

    private static final String ENTITY_NAME = "department";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartmentService departmentService;

    private final DepartmentRepository departmentRepository;

    private final DepartmentQueryService departmentQueryService;

    public DepartmentResource(
        DepartmentService departmentService,
        DepartmentRepository departmentRepository,
        DepartmentQueryService departmentQueryService
    ) {
        this.departmentService = departmentService;
        this.departmentRepository = departmentRepository;
        this.departmentQueryService = departmentQueryService;
    }

    /**
     * {@code POST  /departments} : Create a new department.
     *
     * @param department the department to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new department, or with status {@code 400 (Bad Request)} if the department has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Department> createDepartment(@Valid @RequestBody Department department) throws URISyntaxException {
        LOG.debug("REST request to save Department : {}", department);
        if (department.getId() != null) {
            throw new BadRequestAlertException("A new department cannot already have an ID", ENTITY_NAME, "idexists");
        }
        department = departmentService.save(department);
        return ResponseEntity.created(new URI("/api/departments/" + department.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, department.getId().toString()))
            .body(department);
    }

    /**
     * {@code PUT  /departments/:id} : Updates an existing department.
     *
     * @param id the id of the department to save.
     * @param department the department to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated department,
     * or with status {@code 400 (Bad Request)} if the department is not valid,
     * or with status {@code 500 (Internal Server Error)} if the department couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Department department
    ) throws URISyntaxException {
        LOG.debug("REST request to update Department : {}, {}", id, department);
        if (department.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, department.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        department = departmentService.update(department);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, department.getId().toString()))
            .body(department);
    }

    /**
     * {@code PATCH  /departments/:id} : Partial updates given fields of an existing department, field will ignore if it is null
     *
     * @param id the id of the department to save.
     * @param department the department to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated department,
     * or with status {@code 400 (Bad Request)} if the department is not valid,
     * or with status {@code 404 (Not Found)} if the department is not found,
     * or with status {@code 500 (Internal Server Error)} if the department couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Department> partialUpdateDepartment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Department department
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Department partially : {}, {}", id, department);
        if (department.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, department.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Department> result = departmentService.partialUpdate(department);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, department.getId().toString())
        );
    }

    /**
     * {@code GET  /departments} : get all the departments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Department>> getAllDepartments(DepartmentCriteria criteria) {
        LOG.debug("REST request to get Departments by criteria: {}", criteria);

        List<Department> entityList = departmentQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /departments/count} : count all the departments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDepartments(DepartmentCriteria criteria) {
        LOG.debug("REST request to count Departments by criteria: {}", criteria);
        return ResponseEntity.ok().body(departmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /departments/:id} : get the "id" department.
     *
     * @param id the id of the department to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the department, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Department : {}", id);
        Optional<Department> department = departmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(department);
    }

    /**
     * {@code DELETE  /departments/:id} : delete the "id" department.
     *
     * @param id the id of the department to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Department : {}", id);
        departmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
