import { ITask } from 'app/shared/model/task.model';
import { IEmployee } from 'app/shared/model/employee.model';

export interface IJob {
  id?: number;
  jobTitle?: string | null;
  minSalary?: number | null;
  maxSalary?: number | null;
  tasks?: ITask[] | null;
  employee?: IEmployee | null;
}

export const defaultValue: Readonly<IJob> = {};
