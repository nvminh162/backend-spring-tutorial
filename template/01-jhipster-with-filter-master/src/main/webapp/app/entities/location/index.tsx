import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Location from './location';
import LocationDetail from './location-detail';
import LocationUpdate from './location-update';
import LocationDeleteDialog from './location-delete-dialog';

const LocationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Location />} />
    <Route path="new" element={<LocationUpdate />} />
    <Route path=":id">
      <Route index element={<LocationDetail />} />
      <Route path="edit" element={<LocationUpdate />} />
      <Route path="delete" element={<LocationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LocationRoutes;
