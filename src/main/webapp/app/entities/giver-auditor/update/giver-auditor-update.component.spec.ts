jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GiverAuditorService } from '../service/giver-auditor.service';
import { IGiverAuditor, GiverAuditor } from '../giver-auditor.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IGiver } from 'app/entities/giver/giver.model';
import { GiverService } from 'app/entities/giver/service/giver.service';

import { GiverAuditorUpdateComponent } from './giver-auditor-update.component';

describe('Component Tests', () => {
  describe('GiverAuditor Management Update Component', () => {
    let comp: GiverAuditorUpdateComponent;
    let fixture: ComponentFixture<GiverAuditorUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let giverAuditorService: GiverAuditorService;
    let userService: UserService;
    let giverService: GiverService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GiverAuditorUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(GiverAuditorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GiverAuditorUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      giverAuditorService = TestBed.inject(GiverAuditorService);
      userService = TestBed.inject(UserService);
      giverService = TestBed.inject(GiverService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const giverAuditor: IGiverAuditor = { id: 456 };
        const auditor: IUser = { id: 85650 };
        giverAuditor.auditor = auditor;

        const userCollection: IUser[] = [{ id: 42883 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [auditor];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ giverAuditor });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Giver query and add missing value', () => {
        const giverAuditor: IGiverAuditor = { id: 456 };
        const giver: IGiver = { id: 61972 };
        giverAuditor.giver = giver;

        const giverCollection: IGiver[] = [{ id: 80398 }];
        jest.spyOn(giverService, 'query').mockReturnValue(of(new HttpResponse({ body: giverCollection })));
        const additionalGivers = [giver];
        const expectedCollection: IGiver[] = [...additionalGivers, ...giverCollection];
        jest.spyOn(giverService, 'addGiverToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ giverAuditor });
        comp.ngOnInit();

        expect(giverService.query).toHaveBeenCalled();
        expect(giverService.addGiverToCollectionIfMissing).toHaveBeenCalledWith(giverCollection, ...additionalGivers);
        expect(comp.giversSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const giverAuditor: IGiverAuditor = { id: 456 };
        const auditor: IUser = { id: 75769 };
        giverAuditor.auditor = auditor;
        const giver: IGiver = { id: 21397 };
        giverAuditor.giver = giver;

        activatedRoute.data = of({ giverAuditor });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(giverAuditor));
        expect(comp.usersSharedCollection).toContain(auditor);
        expect(comp.giversSharedCollection).toContain(giver);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<GiverAuditor>>();
        const giverAuditor = { id: 123 };
        jest.spyOn(giverAuditorService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ giverAuditor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: giverAuditor }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(giverAuditorService.update).toHaveBeenCalledWith(giverAuditor);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<GiverAuditor>>();
        const giverAuditor = new GiverAuditor();
        jest.spyOn(giverAuditorService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ giverAuditor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: giverAuditor }));
        saveSubject.complete();

        // THEN
        expect(giverAuditorService.create).toHaveBeenCalledWith(giverAuditor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<GiverAuditor>>();
        const giverAuditor = { id: 123 };
        jest.spyOn(giverAuditorService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ giverAuditor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(giverAuditorService.update).toHaveBeenCalledWith(giverAuditor);
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

      describe('trackGiverById', () => {
        it('Should return tracked Giver primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackGiverById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
