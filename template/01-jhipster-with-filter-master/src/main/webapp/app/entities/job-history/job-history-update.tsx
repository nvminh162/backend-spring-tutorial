import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getJobs } from 'app/entities/job/job.reducer';
import { getEntities as getDepartments } from 'app/entities/department/department.reducer';
import { getEntities as getEmployees } from 'app/entities/employee/employee.reducer';
import { Language } from 'app/shared/model/enumerations/language.model';
import { createEntity, getEntity, updateEntity } from './job-history.reducer';

export const JobHistoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const jobs = useAppSelector(state => state.job.entities);
  const departments = useAppSelector(state => state.department.entities);
  const employees = useAppSelector(state => state.employee.entities);
  const jobHistoryEntity = useAppSelector(state => state.jobHistory.entity);
  const loading = useAppSelector(state => state.jobHistory.loading);
  const updating = useAppSelector(state => state.jobHistory.updating);
  const updateSuccess = useAppSelector(state => state.jobHistory.updateSuccess);
  const languageValues = Object.keys(Language);

  const handleClose = () => {
    navigate('/job-history');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getJobs({}));
    dispatch(getDepartments({}));
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
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);

    const entity = {
      ...jobHistoryEntity,
      ...values,
      job: jobs.find(it => it.id.toString() === values.job?.toString()),
      department: departments.find(it => it.id.toString() === values.department?.toString()),
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
      ? {
          startDate: displayDefaultDateTime(),
          endDate: displayDefaultDateTime(),
        }
      : {
          language: 'FRENCH',
          ...jobHistoryEntity,
          startDate: convertDateTimeFromServer(jobHistoryEntity.startDate),
          endDate: convertDateTimeFromServer(jobHistoryEntity.endDate),
          job: jobHistoryEntity?.job?.id,
          department: jobHistoryEntity?.department?.id,
          employee: jobHistoryEntity?.employee?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="springrestApp.jobHistory.home.createOrEditLabel" data-cy="JobHistoryCreateUpdateHeading">
            <Translate contentKey="springrestApp.jobHistory.home.createOrEditLabel">Create or edit a JobHistory</Translate>
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
                  id="job-history-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('springrestApp.jobHistory.startDate')}
                id="job-history-startDate"
                name="startDate"
                data-cy="startDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('springrestApp.jobHistory.endDate')}
                id="job-history-endDate"
                name="endDate"
                data-cy="endDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('springrestApp.jobHistory.language')}
                id="job-history-language"
                name="language"
                data-cy="language"
                type="select"
              >
                {languageValues.map(language => (
                  <option value={language} key={language}>
                    {translate(`springrestApp.Language.${language}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="job-history-job" name="job" data-cy="job" label={translate('springrestApp.jobHistory.job')} type="select">
                <option value="" key="0" />
                {jobs
                  ? jobs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="job-history-department"
                name="department"
                data-cy="department"
                label={translate('springrestApp.jobHistory.department')}
                type="select"
              >
                <option value="" key="0" />
                {departments
                  ? departments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="job-history-employee"
                name="employee"
                data-cy="employee"
                label={translate('springrestApp.jobHistory.employee')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/job-history" replace color="info">
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

export default JobHistoryUpdate;
