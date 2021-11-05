import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ExcelImportService } from '../service/excel-import.service';

import { ExcelImportComponent } from './excel-import.component';

describe('Component Tests', () => {
  describe('ExcelImport Management Component', () => {
    let comp: ExcelImportComponent;
    let fixture: ComponentFixture<ExcelImportComponent>;
    let service: ExcelImportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExcelImportComponent],
      })
        .overrideTemplate(ExcelImportComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExcelImportComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ExcelImportService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.excelImports?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
