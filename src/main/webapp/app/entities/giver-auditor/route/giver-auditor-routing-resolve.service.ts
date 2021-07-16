import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGiverAuditor, GiverAuditor } from '../giver-auditor.model';
import { GiverAuditorService } from '../service/giver-auditor.service';

@Injectable({ providedIn: 'root' })
export class GiverAuditorRoutingResolveService implements Resolve<IGiverAuditor> {
  constructor(protected service: GiverAuditorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGiverAuditor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((giverAuditor: HttpResponse<GiverAuditor>) => {
          if (giverAuditor.body) {
            return of(giverAuditor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new GiverAuditor());
  }
}
