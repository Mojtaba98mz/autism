jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GiverService } from '../service/giver.service';
import { IGiver, Giver } from '../giver.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';
import { ICity } from 'app/entities/city/city.model';
import { CityService } from 'app/entities/city/service/city.service';

import { GiverUpdateComponent } from './giver-update.component';

describe('Component Tests', () => {
  describe('Giver Management Update Component', () => {
    let comp: GiverUpdateComponent;
    let fixture: ComponentFixture<GiverUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let giverService: GiverService;
    let userService: UserService;
    let provinceService: ProvinceService;
    let cityService: CityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GiverUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(GiverUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GiverUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      giverService = TestBed.inject(GiverService);
      userService = TestBed.inject(UserService);
      provinceService = TestBed.inject(ProvinceService);
      cityService = TestBed.inject(CityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const giver: IGiver = { id: 456 };
        const absorbant: IUser = { id: 81743 };
        giver.absorbant = absorbant;
        const supporter: IUser = { id: 99708 };
        giver.supporter = supporter;

        const userCollection: IUser[] = [{ id: 66628 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [absorbant, supporter];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ giver });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Province query and add missing value', () => {
        const giver: IGiver = { id: 456 };
        const province: IProvince = { id: 8976 };
        giver.province = province;

        const provinceCollection: IProvince[] = [{ id: 78403 }];
        jest.spyOn(provinceService, 'query').mockReturnValue(of(new HttpResponse({ body: provinceCollection })));
        const additionalProvinces = [province];
        const expectedCollection: IProvince[] = [...additionalProvinces, ...provinceCollection];
        jest.spyOn(provinceService, 'addProvinceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ giver });
        comp.ngOnInit();

        expect(provinceService.query).toHaveBeenCalled();
        expect(provinceService.addProvinceToCollectionIfMissing).toHaveBeenCalledWith(provinceCollection, ...additionalProvinces);
        expect(comp.provincesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call City query and add missing value', () => {
        const giver: IGiver = { id: 456 };
        const city: ICity = { id: 10533 };
        giver.city = city;

        const cityCollection: ICity[] = [{ id: 32683 }];
        jest.spyOn(cityService, 'query').mockReturnValue(of(new HttpResponse({ body: cityCollection })));
        const additionalCities = [city];
        const expectedCollection: ICity[] = [...additionalCities, ...cityCollection];
        jest.spyOn(cityService, 'addCityToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ giver });
        comp.ngOnInit();

        expect(cityService.query).toHaveBeenCalled();
        expect(cityService.addCityToCollectionIfMissing).toHaveBeenCalledWith(cityCollection, ...additionalCities);
        expect(comp.citiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const giver: IGiver = { id: 456 };
        const absorbant: IUser = { id: 73741 };
        giver.absorbant = absorbant;
        const supporter: IUser = { id: 58654 };
        giver.supporter = supporter;
        const province: IProvince = { id: 66115 };
        giver.province = province;
        const city: ICity = { id: 83483 };
        giver.city = city;

        activatedRoute.data = of({ giver });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(giver));
        expect(comp.usersSharedCollection).toContain(absorbant);
        expect(comp.usersSharedCollection).toContain(supporter);
        expect(comp.provincesSharedCollection).toContain(province);
        expect(comp.citiesSharedCollection).toContain(city);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Giver>>();
        const giver = { id: 123 };
        jest.spyOn(giverService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ giver });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: giver }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(giverService.update).toHaveBeenCalledWith(giver);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Giver>>();
        const giver = new Giver();
        jest.spyOn(giverService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ giver });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: giver }));
        saveSubject.complete();

        // THEN
        expect(giverService.create).toHaveBeenCalledWith(giver);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Giver>>();
        const giver = { id: 123 };
        jest.spyOn(giverService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ giver });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(giverService.update).toHaveBeenCalledWith(giver);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackProvinceById', () => {
        it('Should return tracked Province primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProvinceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCityById', () => {
        it('Should return tracked City primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCityById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
