import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICeremony, Ceremony } from '../ceremony.model';

import { CeremonyService } from './ceremony.service';

describe('Service Tests', () => {
  describe('Ceremony Service', () => {
    let service: CeremonyService;
    let httpMock: HttpTestingController;
    let elemDefault: ICeremony;
    let expectedResult: ICeremony | ICeremony[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CeremonyService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        amount: 0,
        givenDate: currentDate,
        description: 'AAAAAAA',
        receiptContentType: 'image/png',
        receipt: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            givenDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Ceremony', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            givenDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            givenDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Ceremony()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Ceremony', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            amount: 1,
            givenDate: currentDate.format(DATE_TIME_FORMAT),
            description: 'BBBBBB',
            receipt: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            givenDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Ceremony', () => {
        const patchObject = Object.assign(
          {
            amount: 1,
            givenDate: currentDate.format(DATE_TIME_FORMAT),
            description: 'BBBBBB',
            receipt: 'BBBBBB',
          },
          new Ceremony()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            givenDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Ceremony', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            amount: 1,
            givenDate: currentDate.format(DATE_TIME_FORMAT),
            description: 'BBBBBB',
            receipt: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            givenDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Ceremony', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCeremonyToCollectionIfMissing', () => {
        it('should add a Ceremony to an empty array', () => {
          const ceremony: ICeremony = { id: 123 };
          expectedResult = service.addCeremonyToCollectionIfMissing([], ceremony);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ceremony);
        });

        it('should not add a Ceremony to an array that contains it', () => {
          const ceremony: ICeremony = { id: 123 };
          const ceremonyCollection: ICeremony[] = [
            {
              ...ceremony,
            },
            { id: 456 },
          ];
          expectedResult = service.addCeremonyToCollectionIfMissing(ceremonyCollection, ceremony);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Ceremony to an array that doesn't contain it", () => {
          const ceremony: ICeremony = { id: 123 };
          const ceremonyCollection: ICeremony[] = [{ id: 456 }];
          expectedResult = service.addCeremonyToCollectionIfMissing(ceremonyCollection, ceremony);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ceremony);
        });

        it('should add only unique Ceremony to an array', () => {
          const ceremonyArray: ICeremony[] = [{ id: 123 }, { id: 456 }, { id: 64073 }];
          const ceremonyCollection: ICeremony[] = [{ id: 123 }];
          expectedResult = service.addCeremonyToCollectionIfMissing(ceremonyCollection, ...ceremonyArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ceremony: ICeremony = { id: 123 };
          const ceremony2: ICeremony = { id: 456 };
          expectedResult = service.addCeremonyToCollectionIfMissing([], ceremony, ceremony2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ceremony);
          expect(expectedResult).toContain(ceremony2);
        });

        it('should accept null and undefined values', () => {
          const ceremony: ICeremony = { id: 123 };
          expectedResult = service.addCeremonyToCollectionIfMissing([], null, ceremony, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ceremony);
        });

        it('should return initial array if no Ceremony is added', () => {
          const ceremonyCollection: ICeremony[] = [{ id: 123 }];
          expectedResult = service.addCeremonyToCollectionIfMissing(ceremonyCollection, undefined, null);
          expect(expectedResult).toEqual(ceremonyCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
