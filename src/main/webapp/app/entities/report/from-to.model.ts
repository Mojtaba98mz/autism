import * as dayjs from 'dayjs';

export interface IFromToModel {
  fromDate?: dayjs.Dayjs | null;
  toDate?: dayjs.Dayjs | null;
}

export class FromToModel implements IFromToModel {
  constructor(public fromDate?: dayjs.Dayjs | null, public toDate?: dayjs.Dayjs | null) {}
}
