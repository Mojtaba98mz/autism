jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IExcelImport, ExcelImport } from '../excel-import.model';
import { ExcelImportService } from '../service/excel-import.service';

import { ExcelImportRoutingResolveService } from './excel-import-routing-resolve.service';

describe('Service Tests', () => {
  describe('ExcelImport routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ExcelImportRoutingResolveService;
    let service: ExcelImportService;
    let resultExcelImport: IExcelImport | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ExcelImportRoutingResolveService);
      service = TestBed.inject(ExcelImportService);
      resultExcelImport = undefined;
    });

    describe('resolve', () => {
      it('should return IExcelImport returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExcelImport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultExcelImport).toEqual({ id: 123 });
      });

      it('should return new IExcelImport if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExcelImport = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultExcelImport).toEqual(new ExcelImport());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ExcelImport })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExcelImport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultExcelImport).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
