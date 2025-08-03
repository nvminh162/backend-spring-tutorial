import country from 'app/entities/country/country.reducer';
import department from 'app/entities/department/department.reducer';
import employee from 'app/entities/employee/employee.reducer';
import job from 'app/entities/job/job.reducer';
import jobHistory from 'app/entities/job-history/job-history.reducer';
import location from 'app/entities/location/location.reducer';
import region from 'app/entities/region/region.reducer';
import task from 'app/entities/task/task.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  country,
  department,
  employee,
  job,
  jobHistory,
  location,
  region,
  task,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
