jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CeremonyService } from '../service/ceremony.service';
import { ICeremony, Ceremony } from '../ceremony.model';
import { ICeremonyUser } from 'app/entities/ceremony-user/ceremony-user.model';
import { CeremonyUserService } from 'app/entities/ceremony-user/service/ceremony-user.service';

import { CeremonyUpdateComponent } from './ceremony-update.component';

describe('Component Tests', () => {
  describe('Ceremony Management Update Component', () => {
    let comp: CeremonyUpdateComponent;
    let fixture: ComponentFixture<CeremonyUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ceremonyService: CeremonyService;
    let ceremonyUserService: CeremonyUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CeremonyUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CeremonyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CeremonyUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ceremonyService = TestBed.inject(CeremonyService);
      ceremonyUserService = TestBed.inject(CeremonyUserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CeremonyUser query and add missing value', () => {
        const ceremony: ICeremony = { id: 456 };
        const ceremonyUser: ICeremonyUser = { id: 83240 };
        ceremony.ceremonyUser = ceremonyUser;

        const ceremonyUserCollection: ICeremonyUser[] = [{ id: 43616 }];
        jest.spyOn(ceremonyUserService, 'query').mockReturnValue(of(new HttpResponse({ body: ceremonyUserCollection })));
        const additionalCeremonyUsers = [ceremonyUser];
        const expectedCollection: ICeremonyUser[] = [...additionalCeremonyUsers, ...ceremonyUserCollection];
        jest.spyOn(ceremonyUserService, 'addCeremonyUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ ceremony });
        comp.ngOnInit();

        expect(ceremonyUserService.query).toHaveBeenCalled();
        expect(ceremonyUserService.addCeremonyUserToCollectionIfMissing).toHaveBeenCalledWith(
          ceremonyUserCollection,
          ...additionalCeremonyUsers
        );
        expect(comp.ceremonyUsersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const ceremony: ICeremony = { id: 456 };
        const ceremonyUser: ICeremonyUser = { id: 41552 };
        ceremony.ceremonyUser = ceremonyUser;

        activatedRoute.data = of({ ceremony });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ceremony));
        expect(comp.ceremonyUsersSharedCollection).toContain(ceremonyUser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ceremony>>();
        const ceremony = { id: 123 };
        jest.spyOn(ceremonyService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ceremony });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ceremony }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ceremonyService.update).toHaveBeenCalledWith(ceremony);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ceremony>>();
        const ceremony = new Ceremony();
        jest.spyOn(ceremonyService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ceremony });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ceremony }));
        saveSubject.complete();

        // THEN
        expect(ceremonyService.create).toHaveBeenCalledWith(ceremony);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ceremony>>();
        const ceremony = { id: 123 };
        jest.spyOn(ceremonyService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ceremony });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ceremonyService.update).toHaveBeenCalledWith(ceremony);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCeremonyUserById', () => {
        it('Should return tracked CeremonyUser primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCeremonyUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
