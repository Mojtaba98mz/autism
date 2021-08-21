jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExcelImportService } from '../service/excel-import.service';
import { IExcelImport, ExcelImport } from '../excel-import.model';

import { ExcelImportUpdateComponent } from './excel-import-update.component';

describe('Component Tests', () => {
  describe('ExcelImport Management Update Component', () => {
    let comp: ExcelImportUpdateComponent;
    let fixture: ComponentFixture<ExcelImportUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let excelImportService: ExcelImportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExcelImportUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ExcelImportUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExcelImportUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      excelImportService = TestBed.inject(ExcelImportService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const excelImport: IExcelImport = { id: 456 };

        activatedRoute.data = of({ excelImport });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(excelImport));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ExcelImport>>();
        const excelImport = { id: 123 };
        jest.spyOn(excelImportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ excelImport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: excelImport }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(excelImportService.update).toHaveBeenCalledWith(excelImport);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ExcelImport>>();
        const excelImport = new ExcelImport();
        jest.spyOn(excelImportService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ excelImport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: excelImport }));
        saveSubject.complete();

        // THEN
        expect(excelImportService.create).toHaveBeenCalledWith(excelImport);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ExcelImport>>();
        const excelImport = { id: 123 };
        jest.spyOn(excelImportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ excelImport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(excelImportService.update).toHaveBeenCalledWith(excelImport);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
