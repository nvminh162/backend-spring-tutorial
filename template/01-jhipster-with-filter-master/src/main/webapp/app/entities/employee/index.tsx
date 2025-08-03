import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Employee from './employee';
import EmployeeDetail from './employee-detail';
import EmployeeUpdate from './employee-update';
import EmployeeDeleteDialog from './employee-delete-dialog';

const EmployeeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Employee />} />
    <Route path="new" element={<EmployeeUpdate />} />
    <Route path=":id">
      <Route index element={<EmployeeDetail />} />
      <Route path="edit" element={<EmployeeUpdate />} />
      <Route path="delete" element={<EmployeeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EmployeeRoutes;
