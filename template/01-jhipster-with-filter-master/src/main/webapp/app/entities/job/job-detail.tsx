import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './job.reducer';

export const JobDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const jobEntity = useAppSelector(state => state.job.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="jobDetailsHeading">
          <Translate contentKey="springrestApp.job.detail.title">Job</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{jobEntity.id}</dd>
          <dt>
            <span id="jobTitle">
              <Translate contentKey="springrestApp.job.jobTitle">Job Title</Translate>
            </span>
          </dt>
          <dd>{jobEntity.jobTitle}</dd>
          <dt>
            <span id="minSalary">
              <Translate contentKey="springrestApp.job.minSalary">Min Salary</Translate>
            </span>
          </dt>
          <dd>{jobEntity.minSalary}</dd>
          <dt>
            <span id="maxSalary">
              <Translate contentKey="springrestApp.job.maxSalary">Max Salary</Translate>
            </span>
          </dt>
          <dd>{jobEntity.maxSalary}</dd>
          <dt>
            <Translate contentKey="springrestApp.job.task">Task</Translate>
          </dt>
          <dd>
            {jobEntity.tasks
              ? jobEntity.tasks.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.title}</a>
                    {jobEntity.tasks && i === jobEntity.tasks.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="springrestApp.job.employee">Employee</Translate>
          </dt>
          <dd>{jobEntity.employee ? jobEntity.employee.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/job" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/job/${jobEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default JobDetail;
