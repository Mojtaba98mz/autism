import * as dayjs from 'dayjs';
import { IGiver } from 'app/entities/giver/giver.model';
import { HelpType } from 'app/entities/enumerations/help-type.model';
import { Account } from '../enumerations/account.model';

export interface IDonation {
  id?: number;
  isCash?: boolean | null;
  amount?: number | null;
  donationDate?: dayjs.Dayjs | null;
  helpType?: HelpType | null;
  description?: string | null;
  receiptContentType?: string | null;
  receipt?: string | null;
  account?: Account | null;
  giver?: IGiver | null;
}

export class Donation implements IDonation {
  constructor(
    public id?: number,
    public isCash?: boolean | null,
    public amount?: number | null,
    public donationDate?: dayjs.Dayjs | null,
    public helpType?: HelpType | null,
    public description?: string | null,
    public receiptContentType?: string | null,
    public receipt?: string | null,
    public account?: Account | null,
    public giver?: IGiver | null
  ) {
    this.isCash = this.isCash ?? false;
  }
}

export function getDonationIdentifier(donation: IDonation): number | undefined {
  return donation.id;
}
