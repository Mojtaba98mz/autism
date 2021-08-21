import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExcelImport, getExcelImportIdentifier } from '../excel-import.model';

export type EntityResponseType = HttpResponse<IExcelImport>;
export type EntityArrayResponseType = HttpResponse<IExcelImport[]>;

@Injectable({ providedIn: 'root' })
export class ExcelImportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/excel-imports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(excelImport: IExcelImport): Observable<EntityResponseType> {
    return this.http.post<IExcelImport>(this.resourceUrl, excelImport, { observe: 'response' });
  }

  update(excelImport: IExcelImport): Observable<EntityResponseType> {
    return this.http.put<IExcelImport>(`${this.resourceUrl}/${getExcelImportIdentifier(excelImport) as number}`, excelImport, {
      observe: 'response',
    });
  }

  partialUpdate(excelImport: IExcelImport): Observable<EntityResponseType> {
    return this.http.patch<IExcelImport>(`${this.resourceUrl}/${getExcelImportIdentifier(excelImport) as number}`, excelImport, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExcelImport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExcelImport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExcelImportToCollectionIfMissing(
    excelImportCollection: IExcelImport[],
    ...excelImportsToCheck: (IExcelImport | null | undefined)[]
  ): IExcelImport[] {
    const excelImports: IExcelImport[] = excelImportsToCheck.filter(isPresent);
    if (excelImports.length > 0) {
      const excelImportCollectionIdentifiers = excelImportCollection.map(excelImportItem => getExcelImportIdentifier(excelImportItem)!);
      const excelImportsToAdd = excelImports.filter(excelImportItem => {
        const excelImportIdentifier = getExcelImportIdentifier(excelImportItem);
        if (excelImportIdentifier == null || excelImportCollectionIdentifiers.includes(excelImportIdentifier)) {
          return false;
        }
        excelImportCollectionIdentifiers.push(excelImportIdentifier);
        return true;
      });
      return [...excelImportsToAdd, ...excelImportCollection];
    }
    return excelImportCollection;
  }
}
