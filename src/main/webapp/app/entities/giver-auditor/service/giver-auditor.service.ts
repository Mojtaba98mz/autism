import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGiverAuditor, getGiverAuditorIdentifier } from '../giver-auditor.model';

export type EntityResponseType = HttpResponse<IGiverAuditor>;
export type EntityArrayResponseType = HttpResponse<IGiverAuditor[]>;

@Injectable({ providedIn: 'root' })
export class GiverAuditorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/giver-auditors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(giverAuditor: IGiverAuditor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(giverAuditor);
    return this.http
      .post<IGiverAuditor>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(giverAuditor: IGiverAuditor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(giverAuditor);
    return this.http
      .put<IGiverAuditor>(`${this.resourceUrl}/${getGiverAuditorIdentifier(giverAuditor) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(giverAuditor: IGiverAuditor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(giverAuditor);
    return this.http
      .patch<IGiverAuditor>(`${this.resourceUrl}/${getGiverAuditorIdentifier(giverAuditor) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IGiverAuditor>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGiverAuditor[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGiverAuditorToCollectionIfMissing(
    giverAuditorCollection: IGiverAuditor[],
    ...giverAuditorsToCheck: (IGiverAuditor | null | undefined)[]
  ): IGiverAuditor[] {
    const giverAuditors: IGiverAuditor[] = giverAuditorsToCheck.filter(isPresent);
    if (giverAuditors.length > 0) {
      const giverAuditorCollectionIdentifiers = giverAuditorCollection.map(
        giverAuditorItem => getGiverAuditorIdentifier(giverAuditorItem)!
      );
      const giverAuditorsToAdd = giverAuditors.filter(giverAuditorItem => {
        const giverAuditorIdentifier = getGiverAuditorIdentifier(giverAuditorItem);
        if (giverAuditorIdentifier == null || giverAuditorCollectionIdentifiers.includes(giverAuditorIdentifier)) {
          return false;
        }
        giverAuditorCollectionIdentifiers.push(giverAuditorIdentifier);
        return true;
      });
      return [...giverAuditorsToAdd, ...giverAuditorCollection];
    }
    return giverAuditorCollection;
  }

  protected convertDateFromClient(giverAuditor: IGiverAuditor): IGiverAuditor {
    return Object.assign({}, giverAuditor, {
      changeDate: giverAuditor.changeDate?.isValid() ? giverAuditor.changeDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.changeDate = res.body.changeDate ? dayjs(res.body.changeDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((giverAuditor: IGiverAuditor) => {
        giverAuditor.changeDate = giverAuditor.changeDate ? dayjs(giverAuditor.changeDate) : undefined;
      });
    }
    return res;
  }
}
