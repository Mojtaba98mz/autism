import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICeremonyUser, CeremonyUser } from '../ceremony-user.model';
import { CeremonyUserService } from '../service/ceremony-user.service';

@Injectable({ providedIn: 'root' })
export class CeremonyUserRoutingResolveService implements Resolve<ICeremonyUser> {
  constructor(protected service: CeremonyUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICeremonyUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ceremonyUser: HttpResponse<CeremonyUser>) => {
          if (ceremonyUser.body) {
            return of(ceremonyUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CeremonyUser());
  }
}
