import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGiver, Giver } from '../giver.model';
import { GiverService } from '../service/giver.service';

@Injectable({ providedIn: 'root' })
export class GiverRoutingResolveService implements Resolve<IGiver> {
  constructor(protected service: GiverService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGiver> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((giver: HttpResponse<Giver>) => {
          if (giver.body) {
            return of(giver.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Giver());
  }
}
