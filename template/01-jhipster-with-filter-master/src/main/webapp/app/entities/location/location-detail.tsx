import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './location.reducer';

export const LocationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const locationEntity = useAppSelector(state => state.location.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="locationDetailsHeading">
          <Translate contentKey="springrestApp.location.detail.title">Location</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{locationEntity.id}</dd>
          <dt>
            <span id="streetAddress">
              <Translate contentKey="springrestApp.location.streetAddress">Street Address</Translate>
            </span>
          </dt>
          <dd>{locationEntity.streetAddress}</dd>
          <dt>
            <span id="postalCode">
              <Translate contentKey="springrestApp.location.postalCode">Postal Code</Translate>
            </span>
          </dt>
          <dd>{locationEntity.postalCode}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="springrestApp.location.city">City</Translate>
            </span>
          </dt>
          <dd>{locationEntity.city}</dd>
          <dt>
            <span id="stateProvince">
              <Translate contentKey="springrestApp.location.stateProvince">State Province</Translate>
            </span>
          </dt>
          <dd>{locationEntity.stateProvince}</dd>
          <dt>
            <Translate contentKey="springrestApp.location.country">Country</Translate>
          </dt>
          <dd>{locationEntity.country ? locationEntity.country.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/location" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/location/${locationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LocationDetail;
