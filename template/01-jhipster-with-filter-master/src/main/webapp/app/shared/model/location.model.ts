import { ICountry } from 'app/shared/model/country.model';

export interface ILocation {
  id?: number;
  streetAddress?: string | null;
  postalCode?: string | null;
  city?: string | null;
  stateProvince?: string | null;
  country?: ICountry | null;
}

export const defaultValue: Readonly<ILocation> = {};
