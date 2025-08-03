import { ILocation } from 'app/shared/model/location.model';

export interface IDepartment {
  id?: number;
  departmentName?: string;
  location?: ILocation | null;
}

export const defaultValue: Readonly<IDepartment> = {};
