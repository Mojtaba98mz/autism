import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IExcelImport, ExcelImport } from '../excel-import.model';

import { ExcelImportService } from './excel-import.service';

describe('Service Tests', () => {
  describe('ExcelImport Service', () => {
    let service: ExcelImportService;
    let httpMock: HttpTestingController;
    let elemDefault: IExcelImport;
    let expectedResult: IExcelImport | IExcelImport[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ExcelImportService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        excelContentType: 'image/png',
        excel: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ExcelImport', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ExcelImport()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ExcelImport', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            excel: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ExcelImport', () => {
        const patchObject = Object.assign(
          {
            excel: 'BBBBBB',
          },
          new ExcelImport()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ExcelImport', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            excel: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ExcelImport', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addExcelImportToCollectionIfMissing', () => {
        it('should add a ExcelImport to an empty array', () => {
          const excelImport: IExcelImport = { id: 123 };
          expectedResult = service.addExcelImportToCollectionIfMissing([], excelImport);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(excelImport);
        });

        it('should not add a ExcelImport to an array that contains it', () => {
          const excelImport: IExcelImport = { id: 123 };
          const excelImportCollection: IExcelImport[] = [
            {
              ...excelImport,
            },
            { id: 456 },
          ];
          expectedResult = service.addExcelImportToCollectionIfMissing(excelImportCollection, excelImport);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ExcelImport to an array that doesn't contain it", () => {
          const excelImport: IExcelImport = { id: 123 };
          const excelImportCollection: IExcelImport[] = [{ id: 456 }];
          expectedResult = service.addExcelImportToCollectionIfMissing(excelImportCollection, excelImport);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(excelImport);
        });

        it('should add only unique ExcelImport to an array', () => {
          const excelImportArray: IExcelImport[] = [{ id: 123 }, { id: 456 }, { id: 68039 }];
          const excelImportCollection: IExcelImport[] = [{ id: 123 }];
          expectedResult = service.addExcelImportToCollectionIfMissing(excelImportCollection, ...excelImportArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const excelImport: IExcelImport = { id: 123 };
          const excelImport2: IExcelImport = { id: 456 };
          expectedResult = service.addExcelImportToCollectionIfMissing([], excelImport, excelImport2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(excelImport);
          expect(expectedResult).toContain(excelImport2);
        });

        it('should accept null and undefined values', () => {
          const excelImport: IExcelImport = { id: 123 };
          expectedResult = service.addExcelImportToCollectionIfMissing([], null, excelImport, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(excelImport);
        });

        it('should return initial array if no ExcelImport is added', () => {
          const excelImportCollection: IExcelImport[] = [{ id: 123 }];
          expectedResult = service.addExcelImportToCollectionIfMissing(excelImportCollection, undefined, null);
          expect(expectedResult).toEqual(excelImportCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
