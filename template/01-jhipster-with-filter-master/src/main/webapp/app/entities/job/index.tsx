import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Job from './job';
import JobDetail from './job-detail';
import JobUpdate from './job-update';
import JobDeleteDialog from './job-delete-dialog';

const JobRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Job />} />
    <Route path="new" element={<JobUpdate />} />
    <Route path=":id">
      <Route index element={<JobDetail />} />
      <Route path="edit" element={<JobUpdate />} />
      <Route path="delete" element={<JobDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default JobRoutes;
