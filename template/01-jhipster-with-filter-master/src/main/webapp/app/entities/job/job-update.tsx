import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getTasks } from 'app/entities/task/task.reducer';
import { getEntities as getEmployees } from 'app/entities/employee/employee.reducer';
import { createEntity, getEntity, reset, updateEntity } from './job.reducer';

export const JobUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const tasks = useAppSelector(state => state.task.entities);
  const employees = useAppSelector(state => state.employee.entities);
  const jobEntity = useAppSelector(state => state.job.entity);
  const loading = useAppSelector(state => state.job.loading);
  const updating = useAppSelector(state => state.job.updating);
  const updateSuccess = useAppSelector(state => state.job.updateSuccess);

  const handleClose = () => {
    navigate(`/job${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTasks({}));
    dispatch(getEmployees({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.minSalary !== undefined && typeof values.minSalary !== 'number') {
      values.minSalary = Number(values.minSalary);
    }
    if (values.maxSalary !== undefined && typeof values.maxSalary !== 'number') {
      values.maxSalary = Number(values.maxSalary);
    }

    const entity = {
      ...jobEntity,
      ...values,
      tasks: mapIdList(values.tasks),
      employee: employees.find(it => it.id.toString() === values.employee?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...jobEntity,
          tasks: jobEntity?.tasks?.map(e => e.id.toString()),
          employee: jobEntity?.employee?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="springrestApp.job.home.createOrEditLabel" data-cy="JobCreateUpdateHeading">
            <Translate contentKey="springrestApp.job.home.createOrEditLabel">Create or edit a Job</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="job-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('springrestApp.job.jobTitle')}
                id="job-jobTitle"
                name="jobTitle"
                data-cy="jobTitle"
                type="text"
              />
              <ValidatedField
                label={translate('springrestApp.job.minSalary')}
                id="job-minSalary"
                name="minSalary"
                data-cy="minSalary"
                type="text"
              />
              <ValidatedField
                label={translate('springrestApp.job.maxSalary')}
                id="job-maxSalary"
                name="maxSalary"
                data-cy="maxSalary"
                type="text"
              />
              <ValidatedField label={translate('springrestApp.job.task')} id="job-task" data-cy="task" type="select" multiple name="tasks">
                <option value="" key="0" />
                {tasks
                  ? tasks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="job-employee"
                name="employee"
                data-cy="employee"
                label={translate('springrestApp.job.employee')}
                type="select"
              >
                <option value="" key="0" />
                {employees
                  ? employees.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/job" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default JobUpdate;
