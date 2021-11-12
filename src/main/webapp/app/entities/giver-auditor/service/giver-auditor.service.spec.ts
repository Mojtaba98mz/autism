import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IGiverAuditor, GiverAuditor } from '../giver-auditor.model';

import { GiverAuditorService } from './giver-auditor.service';

describe('Service Tests', () => {
  describe('GiverAuditor Service', () => {
    let service: GiverAuditorService;
    let httpMock: HttpTestingController;
    let elemDefault: IGiverAuditor;
    let expectedResult: IGiverAuditor | IGiverAuditor[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(GiverAuditorService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        fieldName: 'AAAAAAA',
        oldValue: 'AAAAAAA',
        newValue: 'AAAAAAA',
        changeDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            changeDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a GiverAuditor', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            changeDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            changeDate: currentDate,
          },
          returnedFromService
        );

        service.create(new GiverAuditor()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a GiverAuditor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fieldName: 'BBBBBB',
            oldValue: 'BBBBBB',
            newValue: 'BBBBBB',
            changeDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            changeDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a GiverAuditor', () => {
        const patchObject = Object.assign(
          {
            oldValue: 'BBBBBB',
            newValue: 'BBBBBB',
            changeDate: currentDate.format(DATE_TIME_FORMAT),
          },
          new GiverAuditor()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            changeDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of GiverAuditor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fieldName: 'BBBBBB',
            oldValue: 'BBBBBB',
            newValue: 'BBBBBB',
            changeDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            changeDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a GiverAuditor', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addGiverAuditorToCollectionIfMissing', () => {
        it('should add a GiverAuditor to an empty array', () => {
          const giverAuditor: IGiverAuditor = { id: 123 };
          expectedResult = service.addGiverAuditorToCollectionIfMissing([], giverAuditor);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(giverAuditor);
        });

        it('should not add a GiverAuditor to an array that contains it', () => {
          const giverAuditor: IGiverAuditor = { id: 123 };
          const giverAuditorCollection: IGiverAuditor[] = [
            {
              ...giverAuditor,
            },
            { id: 456 },
          ];
          expectedResult = service.addGiverAuditorToCollectionIfMissing(giverAuditorCollection, giverAuditor);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a GiverAuditor to an array that doesn't contain it", () => {
          const giverAuditor: IGiverAuditor = { id: 123 };
          const giverAuditorCollection: IGiverAuditor[] = [{ id: 456 }];
          expectedResult = service.addGiverAuditorToCollectionIfMissing(giverAuditorCollection, giverAuditor);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(giverAuditor);
        });

        it('should add only unique GiverAuditor to an array', () => {
          const giverAuditorArray: IGiverAuditor[] = [{ id: 123 }, { id: 456 }, { id: 17953 }];
          const giverAuditorCollection: IGiverAuditor[] = [{ id: 123 }];
          expectedResult = service.addGiverAuditorToCollectionIfMissing(giverAuditorCollection, ...giverAuditorArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const giverAuditor: IGiverAuditor = { id: 123 };
          const giverAuditor2: IGiverAuditor = { id: 456 };
          expectedResult = service.addGiverAuditorToCollectionIfMissing([], giverAuditor, giverAuditor2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(giverAuditor);
          expect(expectedResult).toContain(giverAuditor2);
        });

        it('should accept null and undefined values', () => {
          const giverAuditor: IGiverAuditor = { id: 123 };
          expectedResult = service.addGiverAuditorToCollectionIfMissing([], null, giverAuditor, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(giverAuditor);
        });

        it('should return initial array if no GiverAuditor is added', () => {
          const giverAuditorCollection: IGiverAuditor[] = [{ id: 123 }];
          expectedResult = service.addGiverAuditorToCollectionIfMissing(giverAuditorCollection, undefined, null);
          expect(expectedResult).toEqual(giverAuditorCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
