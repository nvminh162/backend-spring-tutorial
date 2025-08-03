import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Task from './task';
import TaskDetail from './task-detail';
import TaskUpdate from './task-update';
import TaskDeleteDialog from './task-delete-dialog';

const TaskRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Task />} />
    <Route path="new" element={<TaskUpdate />} />
    <Route path=":id">
      <Route index element={<TaskDetail />} />
      <Route path="edit" element={<TaskUpdate />} />
      <Route path="delete" element={<TaskDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TaskRoutes;
