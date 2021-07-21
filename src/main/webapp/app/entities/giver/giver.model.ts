import * as dayjs from 'dayjs';
import { IProvince } from 'app/entities/province/province.model';
import { ICity } from 'app/entities/city/city.model';
import { IDonation } from 'app/entities/donation/donation.model';
import { IGiverAuditor } from 'app/entities/giver-auditor/giver-auditor.model';
import { IUser } from 'app/entities/user/user.model';

export interface IGiver {
  id?: number;
  name?: string;
  family?: string;
  phoneNumber?: string;
  code?: string;
  address?: string | null;
  absorbDate?: dayjs.Dayjs | null;
  province?: IProvince | null;
  city?: ICity | null;
  donations?: IDonation[] | null;
  giverauditors?: IGiverAuditor[] | null;
  absorbant?: IUser | null;
  supporter?: IUser | null;
}

export class Giver implements IGiver {
  constructor(
    public id?: number,
    public name?: string,
    public family?: string,
    public phoneNumber?: string,
    public code?: string,
    public address?: string | null,
    public absorbDate?: dayjs.Dayjs | null,
    public province?: IProvince | null,
    public city?: ICity | null,
    public donations?: IDonation[] | null,
    public giverauditors?: IGiverAuditor[] | null,
    public absorbant?: IUser | null,
    public supporter?: IUser | null
  ) {}
}

export function getGiverIdentifier(giver: IGiver): number | undefined {
  return giver.id;
}
