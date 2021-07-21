import { ICity } from 'app/entities/city/city.model';
import { IGiver } from 'app/entities/giver/giver.model';

export interface IProvince {
  id?: number;
  name?: string;
  enName?: string;
  cities?: ICity[] | null;
  giver?: IGiver | null;
}

export class Province implements IProvince {
  constructor(
    public id?: number,
    public name?: string,
    public enName?: string,
    public cities?: ICity[] | null,
    public giver?: IGiver | null
  ) {}
}

export function getProvinceIdentifier(province: IProvince): number | undefined {
  return province.id;
}
