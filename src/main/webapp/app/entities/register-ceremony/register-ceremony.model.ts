import * as dayjs from 'dayjs';
import { ICeremonyUser } from 'app/entities/ceremony-user/ceremony-user.model';

export interface ICeremony {
  id?: number;
  amount?: number | null;
  givenDate?: dayjs.Dayjs | null;
  description?: string | null;
  receiptContentType?: string | null;
  receipt?: string | null;
  ceremonyUser?: ICeremonyUser | null;
}

export class Ceremony implements ICeremony {
  constructor(
    public id?: number,
    public amount?: number | null,
    public givenDate?: dayjs.Dayjs | null,
    public description?: string | null,
    public receiptContentType?: string | null,
    public receipt?: string | null,
    public ceremonyUser?: ICeremonyUser | null
  ) {}
}

export function getCeremonyIdentifier(ceremony: ICeremony): number | undefined {
  return ceremony.id;
}
