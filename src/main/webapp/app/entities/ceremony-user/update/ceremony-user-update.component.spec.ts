jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CeremonyUserService } from '../service/ceremony-user.service';
import { ICeremonyUser, CeremonyUser } from '../ceremony-user.model';

import { CeremonyUserUpdateComponent } from './ceremony-user-update.component';

describe('Component Tests', () => {
  describe('CeremonyUser Management Update Component', () => {
    let comp: CeremonyUserUpdateComponent;
    let fixture: ComponentFixture<CeremonyUserUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ceremonyUserService: CeremonyUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CeremonyUserUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CeremonyUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CeremonyUserUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ceremonyUserService = TestBed.inject(CeremonyUserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const ceremonyUser: ICeremonyUser = { id: 456 };

        activatedRoute.data = of({ ceremonyUser });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ceremonyUser));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CeremonyUser>>();
        const ceremonyUser = { id: 123 };
        jest.spyOn(ceremonyUserService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ceremonyUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ceremonyUser }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ceremonyUserService.update).toHaveBeenCalledWith(ceremonyUser);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CeremonyUser>>();
        const ceremonyUser = new CeremonyUser();
        jest.spyOn(ceremonyUserService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ceremonyUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ceremonyUser }));
        saveSubject.complete();

        // THEN
        expect(ceremonyUserService.create).toHaveBeenCalledWith(ceremonyUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CeremonyUser>>();
        const ceremonyUser = { id: 123 };
        jest.spyOn(ceremonyUserService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ceremonyUser });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ceremonyUserService.update).toHaveBeenCalledWith(ceremonyUser);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
