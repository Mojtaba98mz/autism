import * as dayjs from 'dayjs';
import { HelpType } from 'app/entities/enumerations/help-type.model';
import { Account } from '../enumerations/account.model';

export interface IFromToList {
  username?: string | null;
  amount?: number | null;
  donationDate?: dayjs.Dayjs | null;
  stringDonationDate?: string | null;
}

export class FromToListModel implements IFromToList {
  constructor(
    public username?: string | null,
    public amount?: number | null,
    public donationDate?: dayjs.Dayjs | null,
    public stringDonationDate?: string | null
  ) {}
}
