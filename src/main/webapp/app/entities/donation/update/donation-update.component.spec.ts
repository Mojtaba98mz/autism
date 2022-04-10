import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DonationService } from '../service/donation.service';
import { IDonation, Donation } from '../donation.model';
import { IGiver } from 'app/entities/giver/giver.model';
import { GiverService } from 'app/entities/giver/service/giver.service';

import { DonationUpdateComponent } from './donation-update.component';

describe('Donation Management Update Component', () => {
  let comp: DonationUpdateComponent;
  let fixture: ComponentFixture<DonationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let donationService: DonationService;
  let giverService: GiverService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DonationUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DonationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DonationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    donationService = TestBed.inject(DonationService);
    giverService = TestBed.inject(GiverService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Giver query and add missing value', () => {
      const donation: IDonation = { id: 456 };
      const giver: IGiver = { id: 61401 };
      donation.giver = giver;

      const giverCollection: IGiver[] = [{ id: 83914 }];
      jest.spyOn(giverService, 'query').mockReturnValue(of(new HttpResponse({ body: giverCollection })));
      const additionalGivers = [giver];
      const expectedCollection: IGiver[] = [...additionalGivers, ...giverCollection];
      jest.spyOn(giverService, 'addGiverToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ donation });
      comp.ngOnInit();

      expect(giverService.query).toHaveBeenCalled();
      expect(giverService.addGiverToCollectionIfMissing).toHaveBeenCalledWith(giverCollection, ...additionalGivers);
      expect(comp.giversSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const donation: IDonation = { id: 456 };
      const giver: IGiver = { id: 98742 };
      donation.giver = giver;

      activatedRoute.data = of({ donation });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(donation));
      expect(comp.giversSharedCollection).toContain(giver);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Donation>>();
      const donation = { id: 123 };
      jest.spyOn(donationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ donation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: donation }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(donationService.update).toHaveBeenCalledWith(donation);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Donation>>();
      const donation = new Donation();
      jest.spyOn(donationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ donation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: donation }));
      saveSubject.complete();

      // THEN
      expect(donationService.create).toHaveBeenCalledWith(donation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Donation>>();
      const donation = { id: 123 };
      jest.spyOn(donationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ donation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(donationService.update).toHaveBeenCalledWith(donation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackGiverById', () => {
      it('Should return tracked Giver primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackGiverById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
