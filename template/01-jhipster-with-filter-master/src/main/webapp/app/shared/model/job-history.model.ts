import dayjs from 'dayjs';
import { IJob } from 'app/shared/model/job.model';
import { IDepartment } from 'app/shared/model/department.model';
import { IEmployee } from 'app/shared/model/employee.model';
import { Language } from 'app/shared/model/enumerations/language.model';

export interface IJobHistory {
  id?: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  language?: keyof typeof Language | null;
  job?: IJob | null;
  department?: IDepartment | null;
  employee?: IEmployee | null;
}

export const defaultValue: Readonly<IJobHistory> = {};
