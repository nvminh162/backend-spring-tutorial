import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Country from './country';
import CountryDetail from './country-detail';
import CountryUpdate from './country-update';
import CountryDeleteDialog from './country-delete-dialog';

const CountryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Country />} />
    <Route path="new" element={<CountryUpdate />} />
    <Route path=":id">
      <Route index element={<CountryDetail />} />
      <Route path="edit" element={<CountryUpdate />} />
      <Route path="delete" element={<CountryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CountryRoutes;
