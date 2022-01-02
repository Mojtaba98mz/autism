jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICeremony, Ceremony } from '../ceremony.model';
import { CeremonyService } from '../service/ceremony.service';

import { CeremonyRoutingResolveService } from './ceremony-routing-resolve.service';

describe('Service Tests', () => {
  describe('Ceremony routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CeremonyRoutingResolveService;
    let service: CeremonyService;
    let resultCeremony: ICeremony | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CeremonyRoutingResolveService);
      service = TestBed.inject(CeremonyService);
      resultCeremony = undefined;
    });

    describe('resolve', () => {
      it('should return ICeremony returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCeremony = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCeremony).toEqual({ id: 123 });
      });

      it('should return new ICeremony if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCeremony = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCeremony).toEqual(new Ceremony());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Ceremony })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCeremony = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCeremony).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
