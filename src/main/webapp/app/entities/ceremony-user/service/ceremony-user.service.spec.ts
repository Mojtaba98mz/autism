import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICeremonyUser, CeremonyUser } from '../ceremony-user.model';

import { CeremonyUserService } from './ceremony-user.service';

describe('Service Tests', () => {
  describe('CeremonyUser Service', () => {
    let service: CeremonyUserService;
    let httpMock: HttpTestingController;
    let elemDefault: ICeremonyUser;
    let expectedResult: ICeremonyUser | ICeremonyUser[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CeremonyUserService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        family: 'AAAAAAA',
        phoneNumber: 'AAAAAAA',
        homeNumber: 'AAAAAAA',
        address: 'AAAAAAA',
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

      it('should create a CeremonyUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CeremonyUser()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CeremonyUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            family: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            homeNumber: 'BBBBBB',
            address: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CeremonyUser', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            homeNumber: 'BBBBBB',
            address: 'BBBBBB',
          },
          new CeremonyUser()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CeremonyUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            family: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            homeNumber: 'BBBBBB',
            address: 'BBBBBB',
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

      it('should delete a CeremonyUser', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCeremonyUserToCollectionIfMissing', () => {
        it('should add a CeremonyUser to an empty array', () => {
          const ceremonyUser: ICeremonyUser = { id: 123 };
          expectedResult = service.addCeremonyUserToCollectionIfMissing([], ceremonyUser);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ceremonyUser);
        });

        it('should not add a CeremonyUser to an array that contains it', () => {
          const ceremonyUser: ICeremonyUser = { id: 123 };
          const ceremonyUserCollection: ICeremonyUser[] = [
            {
              ...ceremonyUser,
            },
            { id: 456 },
          ];
          expectedResult = service.addCeremonyUserToCollectionIfMissing(ceremonyUserCollection, ceremonyUser);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CeremonyUser to an array that doesn't contain it", () => {
          const ceremonyUser: ICeremonyUser = { id: 123 };
          const ceremonyUserCollection: ICeremonyUser[] = [{ id: 456 }];
          expectedResult = service.addCeremonyUserToCollectionIfMissing(ceremonyUserCollection, ceremonyUser);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ceremonyUser);
        });

        it('should add only unique CeremonyUser to an array', () => {
          const ceremonyUserArray: ICeremonyUser[] = [{ id: 123 }, { id: 456 }, { id: 80178 }];
          const ceremonyUserCollection: ICeremonyUser[] = [{ id: 123 }];
          expectedResult = service.addCeremonyUserToCollectionIfMissing(ceremonyUserCollection, ...ceremonyUserArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ceremonyUser: ICeremonyUser = { id: 123 };
          const ceremonyUser2: ICeremonyUser = { id: 456 };
          expectedResult = service.addCeremonyUserToCollectionIfMissing([], ceremonyUser, ceremonyUser2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ceremonyUser);
          expect(expectedResult).toContain(ceremonyUser2);
        });

        it('should accept null and undefined values', () => {
          const ceremonyUser: ICeremonyUser = { id: 123 };
          expectedResult = service.addCeremonyUserToCollectionIfMissing([], null, ceremonyUser, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ceremonyUser);
        });

        it('should return initial array if no CeremonyUser is added', () => {
          const ceremonyUserCollection: ICeremonyUser[] = [{ id: 123 }];
          expectedResult = service.addCeremonyUserToCollectionIfMissing(ceremonyUserCollection, undefined, null);
          expect(expectedResult).toEqual(ceremonyUserCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
