import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICeremonyUser, getCeremonyUserIdentifier } from '../ceremony-user.model';

export type EntityResponseType = HttpResponse<ICeremonyUser>;
export type EntityArrayResponseType = HttpResponse<ICeremonyUser[]>;

@Injectable({ providedIn: 'root' })
export class CeremonyUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ceremony-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ceremonyUser: ICeremonyUser): Observable<EntityResponseType> {
    return this.http.post<ICeremonyUser>(this.resourceUrl, ceremonyUser, { observe: 'response' });
  }

  update(ceremonyUser: ICeremonyUser): Observable<EntityResponseType> {
    return this.http.put<ICeremonyUser>(`${this.resourceUrl}/${getCeremonyUserIdentifier(ceremonyUser) as number}`, ceremonyUser, {
      observe: 'response',
    });
  }

  partialUpdate(ceremonyUser: ICeremonyUser): Observable<EntityResponseType> {
    return this.http.patch<ICeremonyUser>(`${this.resourceUrl}/${getCeremonyUserIdentifier(ceremonyUser) as number}`, ceremonyUser, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICeremonyUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICeremonyUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCeremonyUserToCollectionIfMissing(
    ceremonyUserCollection: ICeremonyUser[],
    ...ceremonyUsersToCheck: (ICeremonyUser | null | undefined)[]
  ): ICeremonyUser[] {
    const ceremonyUsers: ICeremonyUser[] = ceremonyUsersToCheck.filter(isPresent);
    if (ceremonyUsers.length > 0) {
      const ceremonyUserCollectionIdentifiers = ceremonyUserCollection.map(
        ceremonyUserItem => getCeremonyUserIdentifier(ceremonyUserItem)!
      );
      const ceremonyUsersToAdd = ceremonyUsers.filter(ceremonyUserItem => {
        const ceremonyUserIdentifier = getCeremonyUserIdentifier(ceremonyUserItem);
        if (ceremonyUserIdentifier == null || ceremonyUserCollectionIdentifiers.includes(ceremonyUserIdentifier)) {
          return false;
        }
        ceremonyUserCollectionIdentifiers.push(ceremonyUserIdentifier);
        return true;
      });
      return [...ceremonyUsersToAdd, ...ceremonyUserCollection];
    }
    return ceremonyUserCollection;
  }
}
