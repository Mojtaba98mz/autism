import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GiverDetailComponent } from './giver-detail.component';

describe('Component Tests', () => {
  describe('Giver Management Detail Component', () => {
    let comp: GiverDetailComponent;
    let fixture: ComponentFixture<GiverDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [GiverDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ giver: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(GiverDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GiverDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load giver on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.giver).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
