import React from 'react';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Country from './country';
import Department from './department';
import Employee from './employee';
import Job from './job';
import JobHistory from './job-history';
import Location from './location';
import Region from './region';
import Task from './task';
import { Route } from 'react-router';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="country/*" element={<Country />} />
        <Route path="department/*" element={<Department />} />
        <Route path="employee/*" element={<Employee />} />
        <Route path="job/*" element={<Job />} />
        <Route path="job-history/*" element={<JobHistory />} />
        <Route path="location/*" element={<Location />} />
        <Route path="region/*" element={<Region />} />
        <Route path="task/*" element={<Task />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
