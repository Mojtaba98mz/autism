import { ICeremony } from 'app/entities/ceremony/ceremony.model';

export interface ICeremonyUser {
  id?: number;
  name?: string;
  family?: string;
  phoneNumber?: string;
  homeNumber?: string | null;
  address?: string | null;
  ceremonies?: ICeremony[] | null;
}

export class CeremonyUser implements ICeremonyUser {
  constructor(
    public id?: number,
    public name?: string,
    public family?: string,
    public phoneNumber?: string,
    public homeNumber?: string | null,
    public address?: string | null,
    public ceremonies?: ICeremony[] | null
  ) {}
}

export function getCeremonyUserIdentifier(ceremonyUser: ICeremonyUser): number | undefined {
  return ceremonyUser.id;
}
