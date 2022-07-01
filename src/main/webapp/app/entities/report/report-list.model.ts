import * as dayjs from 'dayjs';
import { HelpType } from 'app/entities/enumerations/help-type.model';
import { Account } from '../enumerations/account.model';

export interface IReportList {
  giverId?: number;
  giverName?: string | null;
  giverFamily?: string | null;
  giverPhoneNumber?: string | null;
  isCash?: boolean | null;
  amount?: number | null;
  donationDate?: dayjs.Dayjs | null;
  stringDonationDate?: string | null;
  helpType?: HelpType | null;
  account?: Account | null;
  province?: string | null;
  city?: string | null;
}

export class ReportListModel implements IReportList {
  constructor(
    public giverId?: number,
    public giverName?: string | null,
    public giverFamily?: string | null,
    public giverPhoneNumber?: string | null,
    public isCash?: boolean | null,
    public amount?: number | null,
    public donationDate?: dayjs.Dayjs | null,
    public stringDonationDate?: string | null,
    public helpType?: HelpType | null,
    public account?: Account | null,
    public province?: string | null,
    public city?: string | null
  ) {
    this.isCash = this.isCash ?? false;
  }
}

export function getDonationIdentifier(reportList: IReportList): number | undefined {
  return reportList.giverId;
}
