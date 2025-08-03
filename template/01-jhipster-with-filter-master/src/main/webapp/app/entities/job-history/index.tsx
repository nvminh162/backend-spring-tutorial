import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import JobHistory from './job-history';
import JobHistoryDetail from './job-history-detail';
import JobHistoryUpdate from './job-history-update';
import JobHistoryDeleteDialog from './job-history-delete-dialog';

const JobHistoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<JobHistory />} />
    <Route path="new" element={<JobHistoryUpdate />} />
    <Route path=":id">
      <Route index element={<JobHistoryDetail />} />
      <Route path="edit" element={<JobHistoryUpdate />} />
      <Route path="delete" element={<JobHistoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default JobHistoryRoutes;
