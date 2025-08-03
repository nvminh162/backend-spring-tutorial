import { IJob } from 'app/shared/model/job.model';

export interface ITask {
  id?: number;
  title?: string | null;
  description?: string | null;
  jobs?: IJob[] | null;
}

export const defaultValue: Readonly<ITask> = {};
