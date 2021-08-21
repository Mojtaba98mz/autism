import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExcelImport, ExcelImport } from '../excel-import.model';
import { ExcelImportService } from '../service/excel-import.service';

@Injectable({ providedIn: 'root' })
export class ExcelImportRoutingResolveService implements Resolve<IExcelImport> {
  constructor(protected service: ExcelImportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExcelImport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((excelImport: HttpResponse<ExcelImport>) => {
          if (excelImport.body) {
            return of(excelImport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExcelImport());
  }
}
