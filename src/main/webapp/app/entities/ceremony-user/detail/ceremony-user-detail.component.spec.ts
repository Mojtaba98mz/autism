import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CeremonyUserDetailComponent } from './ceremony-user-detail.component';

describe('Component Tests', () => {
  describe('CeremonyUser Management Detail Component', () => {
    let comp: CeremonyUserDetailComponent;
    let fixture: ComponentFixture<CeremonyUserDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CeremonyUserDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ceremonyUser: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CeremonyUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CeremonyUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ceremonyUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ceremonyUser).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
