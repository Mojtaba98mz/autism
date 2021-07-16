import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGiver } from '../giver.model';

@Component({
  selector: 'jhi-giver-detail',
  templateUrl: './giver-detail.component.html',
})
export class GiverDetailComponent implements OnInit {
  giver: IGiver | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ giver }) => {
      this.giver = giver;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
