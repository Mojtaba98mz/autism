import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICeremony, Ceremony } from '../register-ceremony.model';
import { RegisterCeremonyService } from '../service/register-ceremony.service';

@Injectable({ providedIn: 'root' })
export class RegisterCeremonyRoutingResolveService implements Resolve<ICeremony> {
  constructor(protected service: RegisterCeremonyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICeremony> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ceremony: HttpResponse<Ceremony>) => {
          if (ceremony.body) {
            return of(ceremony.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ceremony());
  }
}
