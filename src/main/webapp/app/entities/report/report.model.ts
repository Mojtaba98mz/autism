import * as dayjs from 'dayjs';
import { IGiver } from 'app/entities/giver/giver.model';
import { HelpType } from 'app/entities/enumerations/help-type.model';
import { Account } from '../enumerations/account.model';
import { IProvince } from '../province/province.model';
import { ICity } from '../city/city.model';

export interface IReport {
  id?: number;
  isCash?: boolean | null;
  amountFrom?: number | null;
  amountTo?: number | null;
  fromDate?: dayjs.Dayjs | null;
  toDate?: dayjs.Dayjs | null;
  helpType?: HelpType | null;
  description?: string | null;
  receiptContentType?: string | null;
  receipt?: string | null;
  account?: Account | null;
  giver?: IGiver | null;
  province?: IProvince | null;
  city?: ICity | null;
}

export class Report implements IReport {
  constructor(
    public id?: number,
    public isCash?: boolean | null,
    public amountFrom?: number | null,
    public amountTo?: number | null,
    public donationDate?: dayjs.Dayjs | null,
    public fromDate?: dayjs.Dayjs | null,
    public toDate?: dayjs.Dayjs | null,
    public helpType?: HelpType | null,
    public description?: string | null,
    public receiptContentType?: string | null,
    public receipt?: string | null,
    public account?: Account | null,
    public giver?: IGiver | null,
    public province?: IProvince | null,
    public city?: ICity | null
  ) {
    this.isCash = this.isCash ?? false;
  }
}

export function getDonationIdentifier(report: IReport): number | undefined {
  return report.id;
}
