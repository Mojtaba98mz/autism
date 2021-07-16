import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GiverAuditorDetailComponent } from './giver-auditor-detail.component';

describe('Component Tests', () => {
  describe('GiverAuditor Management Detail Component', () => {
    let comp: GiverAuditorDetailComponent;
    let fixture: ComponentFixture<GiverAuditorDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [GiverAuditorDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ giverAuditor: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(GiverAuditorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GiverAuditorDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load giverAuditor on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.giverAuditor).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
