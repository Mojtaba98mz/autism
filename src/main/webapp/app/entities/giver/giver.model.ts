import * as dayjs from 'dayjs';
import { IDonation } from 'app/entities/donation/donation.model';
import { IGiverAuditor } from 'app/entities/giver-auditor/giver-auditor.model';
import { IUser } from 'app/entities/user/user.model';
import { IProvince } from 'app/entities/province/province.model';
import { ICity } from 'app/entities/city/city.model';

export interface IGiver {
  id?: number;
  name?: string;
  family?: string;
  phoneNumber?: string;
  address?: string | null;
  absorbDate?: dayjs.Dayjs | null;
  donations?: IDonation[] | null;
  giverauditors?: IGiverAuditor[] | null;
  absorbant?: IUser | null;
  supporter?: IUser | null;
  province?: IProvince | null;
  city?: ICity | null;
}

export class Giver implements IGiver {
  constructor(
    public id?: number,
    public name?: string,
    public family?: string,
    public phoneNumber?: string,
    public address?: string | null,
    public absorbDate?: dayjs.Dayjs | null,
    public donations?: IDonation[] | null,
    public giverauditors?: IGiverAuditor[] | null,
    public absorbant?: IUser | null,
    public supporter?: IUser | null,
    public province?: IProvince | null,
    public city?: ICity | null
  ) {}
}

export function getGiverIdentifier(giver: IGiver): number | undefined {
  return giver.id;
}
