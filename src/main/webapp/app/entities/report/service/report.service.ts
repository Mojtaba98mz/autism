import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { getDonationIdentifier, IReport } from '../report.model';

export type EntityResponseType = HttpResponse<IReport>;
export type EntityArrayResponseType = HttpResponse<IReport[]>;

@Injectable({ providedIn: 'root' })
export class ReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(donation: IReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(donation);
    return this.http
      .post<IReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  getReport(report: IReport): Observable<EntityArrayResponseType> {
    const copy = this.convertDateFromClient(report);
    return this.http
      .post<IReport[]>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addDonationToCollectionIfMissing(donationCollection: IReport[], ...donationsToCheck: (IReport | null | undefined)[]): IReport[] {
    const donations: IReport[] = donationsToCheck.filter(isPresent);
    if (donations.length > 0) {
      const donationCollectionIdentifiers = donationCollection.map(donationItem => getDonationIdentifier(donationItem)!);
      const donationsToAdd = donations.filter(donationItem => {
        const donationIdentifier = getDonationIdentifier(donationItem);
        if (donationIdentifier == null || donationCollectionIdentifiers.includes(donationIdentifier)) {
          return false;
        }
        donationCollectionIdentifiers.push(donationIdentifier);
        return true;
      });
      return [...donationsToAdd, ...donationCollection];
    }
    return donationCollection;
  }

  protected convertDateFromClient(report: IReport): IReport {
    return Object.assign({}, report, {
      fromDate: report.fromDate?.isValid() ? report.fromDate.toJSON() : undefined,
      toDate: report.toDate?.isValid() ? report.toDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fromDate = res.body.fromDate ? dayjs(res.body.fromDate) : undefined;
      res.body.toDate = res.body.toDate ? dayjs(res.body.toDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((report: IReport) => {
        report.fromDate = report.fromDate ? dayjs(report.fromDate) : undefined;
        report.toDate = report.toDate ? dayjs(report.toDate) : undefined;
      });
    }
    return res;
  }
}
