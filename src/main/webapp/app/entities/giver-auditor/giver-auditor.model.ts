import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IGiver } from 'app/entities/giver/giver.model';

export interface IGiverAuditor {
  id?: number;
  fiedlName?: string;
  oldValue?: string;
  newValue?: string;
  changeDate?: dayjs.Dayjs;
  auditor?: IUser | null;
  giver?: IGiver | null;
}

export class GiverAuditor implements IGiverAuditor {
  constructor(
    public id?: number,
    public fiedlName?: string,
    public oldValue?: string,
    public newValue?: string,
    public changeDate?: dayjs.Dayjs,
    public auditor?: IUser | null,
    public giver?: IGiver | null
  ) {}
}

export function getGiverAuditorIdentifier(giverAuditor: IGiverAuditor): number | undefined {
  return giverAuditor.id;
}
