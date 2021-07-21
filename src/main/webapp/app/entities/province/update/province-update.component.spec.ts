jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProvinceService } from '../service/province.service';
import { IProvince, Province } from '../province.model';

import { ProvinceUpdateComponent } from './province-update.component';

describe('Component Tests', () => {
  describe('Province Management Update Component', () => {
    let comp: ProvinceUpdateComponent;
    let fixture: ComponentFixture<ProvinceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let provinceService: ProvinceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProvinceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProvinceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProvinceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      provinceService = TestBed.inject(ProvinceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const province: IProvince = { id: 456 };

        activatedRoute.data = of({ province });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(province));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Province>>();
        const province = { id: 123 };
        jest.spyOn(provinceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ province });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: province }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(provinceService.update).toHaveBeenCalledWith(province);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Province>>();
        const province = new Province();
        jest.spyOn(provinceService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ province });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: province }));
        saveSubject.complete();

        // THEN
        expect(provinceService.create).toHaveBeenCalledWith(province);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Province>>();
        const province = { id: 123 };
        jest.spyOn(provinceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ province });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(provinceService.update).toHaveBeenCalledWith(province);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
