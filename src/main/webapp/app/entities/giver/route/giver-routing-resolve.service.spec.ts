jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IGiver, Giver } from '../giver.model';
import { GiverService } from '../service/giver.service';

import { GiverRoutingResolveService } from './giver-routing-resolve.service';

describe('Service Tests', () => {
  describe('Giver routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: GiverRoutingResolveService;
    let service: GiverService;
    let resultGiver: IGiver | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(GiverRoutingResolveService);
      service = TestBed.inject(GiverService);
      resultGiver = undefined;
    });

    describe('resolve', () => {
      it('should return IGiver returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGiver = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultGiver).toEqual({ id: 123 });
      });

      it('should return new IGiver if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGiver = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultGiver).toEqual(new Giver());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Giver })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGiver = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultGiver).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
