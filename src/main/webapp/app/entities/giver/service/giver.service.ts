import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGiver, getGiverIdentifier } from '../giver.model';

export type EntityResponseType = HttpResponse<IGiver>;
export type EntityArrayResponseType = HttpResponse<IGiver[]>;

@Injectable({ providedIn: 'root' })
export class GiverService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/givers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(giver: IGiver): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(giver);
    return this.http
      .post<IGiver>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(giver: IGiver): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(giver);
    return this.http
      .put<IGiver>(`${this.resourceUrl}/${getGiverIdentifier(giver) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(giver: IGiver): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(giver);
    return this.http
      .patch<IGiver>(`${this.resourceUrl}/${getGiverIdentifier(giver) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IGiver>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGiver[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  disableEnable(id: number): Observable<HttpResponse<{}>> {
    return this.http.get(`${this.resourceUrl}/disableEnable/${id}`, { observe: 'response' });
  }

  addGiverToCollectionIfMissing(giverCollection: IGiver[], ...giversToCheck: (IGiver | null | undefined)[]): IGiver[] {
    const givers: IGiver[] = giversToCheck.filter(isPresent);
    if (givers.length > 0) {
      const giverCollectionIdentifiers = giverCollection.map(giverItem => getGiverIdentifier(giverItem)!);
      const giversToAdd = givers.filter(giverItem => {
        const giverIdentifier = getGiverIdentifier(giverItem);
        if (giverIdentifier == null || giverCollectionIdentifiers.includes(giverIdentifier)) {
          return false;
        }
        giverCollectionIdentifiers.push(giverIdentifier);
        return true;
      });
      return [...giversToAdd, ...giverCollection];
    }
    return giverCollection;
  }

  protected convertDateFromClient(giver: IGiver): IGiver {
    return Object.assign({}, giver, {
      absorbDate: giver.absorbDate?.isValid() ? giver.absorbDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.absorbDate = res.body.absorbDate ? dayjs(res.body.absorbDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((giver: IGiver) => {
        giver.absorbDate = giver.absorbDate ? dayjs(giver.absorbDate) : undefined;
      });
    }
    return res;
  }
}
