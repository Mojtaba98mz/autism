import { IGiver } from 'app/entities/giver/giver.model';
import { IProvince } from 'app/entities/province/province.model';

export interface ICity {
  id?: number;
  name?: string;
  enName?: string;
  giver?: IGiver | null;
  province?: IProvince | null;
}

export class City implements ICity {
  constructor(
    public id?: number,
    public name?: string,
    public enName?: string,
    public giver?: IGiver | null,
    public province?: IProvince | null
  ) {}
}

export function getCityIdentifier(city: ICity): number | undefined {
  return city.id;
}
