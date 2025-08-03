import dayjs from 'dayjs';
import { IDepartment } from 'app/shared/model/department.model';

export interface IEmployee {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  hireDate?: dayjs.Dayjs | null;
  salary?: number | null;
  commissionPct?: number | null;
  manager?: IEmployee | null;
  department?: IDepartment | null;
}

export const defaultValue: Readonly<IEmployee> = {};
