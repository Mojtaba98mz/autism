import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IGiver, Giver } from '../giver.model';

import { GiverService } from './giver.service';

describe('Service Tests', () => {
  describe('Giver Service', () => {
    let service: GiverService;
    let httpMock: HttpTestingController;
    let elemDefault: IGiver;
    let expectedResult: IGiver | IGiver[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(GiverService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        family: 'AAAAAAA',
        phoneNumber: 'AAAAAAA',
        homeNumber: 'AAAAAAA',
        address: 'AAAAAAA',
        absorbDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            absorbDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Giver', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            absorbDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            absorbDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Giver()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Giver', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            family: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            homeNumber: 'BBBBBB',
            address: 'BBBBBB',
            absorbDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            absorbDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Giver', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new Giver()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            absorbDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Giver', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            family: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            homeNumber: 'BBBBBB',
            address: 'BBBBBB',
            absorbDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            absorbDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Giver', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addGiverToCollectionIfMissing', () => {
        it('should add a Giver to an empty array', () => {
          const giver: IGiver = { id: 123 };
          expectedResult = service.addGiverToCollectionIfMissing([], giver);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(giver);
        });

        it('should not add a Giver to an array that contains it', () => {
          const giver: IGiver = { id: 123 };
          const giverCollection: IGiver[] = [
            {
              ...giver,
            },
            { id: 456 },
          ];
          expectedResult = service.addGiverToCollectionIfMissing(giverCollection, giver);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Giver to an array that doesn't contain it", () => {
          const giver: IGiver = { id: 123 };
          const giverCollection: IGiver[] = [{ id: 456 }];
          expectedResult = service.addGiverToCollectionIfMissing(giverCollection, giver);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(giver);
        });

        it('should add only unique Giver to an array', () => {
          const giverArray: IGiver[] = [{ id: 123 }, { id: 456 }, { id: 39849 }];
          const giverCollection: IGiver[] = [{ id: 123 }];
          expectedResult = service.addGiverToCollectionIfMissing(giverCollection, ...giverArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const giver: IGiver = { id: 123 };
          const giver2: IGiver = { id: 456 };
          expectedResult = service.addGiverToCollectionIfMissing([], giver, giver2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(giver);
          expect(expectedResult).toContain(giver2);
        });

        it('should accept null and undefined values', () => {
          const giver: IGiver = { id: 123 };
          expectedResult = service.addGiverToCollectionIfMissing([], null, giver, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(giver);
        });

        it('should return initial array if no Giver is added', () => {
          const giverCollection: IGiver[] = [{ id: 123 }];
          expectedResult = service.addGiverToCollectionIfMissing(giverCollection, undefined, null);
          expect(expectedResult).toEqual(giverCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
