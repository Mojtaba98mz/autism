jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICeremonyUser, CeremonyUser } from '../ceremony-user.model';
import { CeremonyUserService } from '../service/ceremony-user.service';

import { CeremonyUserRoutingResolveService } from './ceremony-user-routing-resolve.service';

describe('Service Tests', () => {
  describe('CeremonyUser routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CeremonyUserRoutingResolveService;
    let service: CeremonyUserService;
    let resultCeremonyUser: ICeremonyUser | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CeremonyUserRoutingResolveService);
      service = TestBed.inject(CeremonyUserService);
      resultCeremonyUser = undefined;
    });

    describe('resolve', () => {
      it('should return ICeremonyUser returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCeremonyUser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCeremonyUser).toEqual({ id: 123 });
      });

      it('should return new ICeremonyUser if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCeremonyUser = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCeremonyUser).toEqual(new CeremonyUser());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CeremonyUser })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCeremonyUser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCeremonyUser).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
