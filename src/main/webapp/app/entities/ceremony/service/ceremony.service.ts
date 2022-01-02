import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICeremony, getCeremonyIdentifier } from '../ceremony.model';

export type EntityResponseType = HttpResponse<ICeremony>;
export type EntityArrayResponseType = HttpResponse<ICeremony[]>;

@Injectable({ providedIn: 'root' })
export class CeremonyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ceremonies');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ceremony: ICeremony): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ceremony);
    return this.http
      .post<ICeremony>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(ceremony: ICeremony): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ceremony);
    return this.http
      .put<ICeremony>(`${this.resourceUrl}/${getCeremonyIdentifier(ceremony) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(ceremony: ICeremony): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ceremony);
    return this.http
      .patch<ICeremony>(`${this.resourceUrl}/${getCeremonyIdentifier(ceremony) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICeremony>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICeremony[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCeremonyToCollectionIfMissing(ceremonyCollection: ICeremony[], ...ceremoniesToCheck: (ICeremony | null | undefined)[]): ICeremony[] {
    const ceremonies: ICeremony[] = ceremoniesToCheck.filter(isPresent);
    if (ceremonies.length > 0) {
      const ceremonyCollectionIdentifiers = ceremonyCollection.map(ceremonyItem => getCeremonyIdentifier(ceremonyItem)!);
      const ceremoniesToAdd = ceremonies.filter(ceremonyItem => {
        const ceremonyIdentifier = getCeremonyIdentifier(ceremonyItem);
        if (ceremonyIdentifier == null || ceremonyCollectionIdentifiers.includes(ceremonyIdentifier)) {
          return false;
        }
        ceremonyCollectionIdentifiers.push(ceremonyIdentifier);
        return true;
      });
      return [...ceremoniesToAdd, ...ceremonyCollection];
    }
    return ceremonyCollection;
  }

  protected convertDateFromClient(ceremony: ICeremony): ICeremony {
    return Object.assign({}, ceremony, {
      givenDate: ceremony.givenDate?.isValid() ? ceremony.givenDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.givenDate = res.body.givenDate ? dayjs(res.body.givenDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((ceremony: ICeremony) => {
        ceremony.givenDate = ceremony.givenDate ? dayjs(ceremony.givenDate) : undefined;
      });
    }
    return res;
  }
}
