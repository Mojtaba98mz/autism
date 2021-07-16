import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGiverAuditor } from '../giver-auditor.model';

@Component({
  selector: 'jhi-giver-auditor-detail',
  templateUrl: './giver-auditor-detail.component.html',
})
export class GiverAuditorDetailComponent implements OnInit {
  giverAuditor: IGiverAuditor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ giverAuditor }) => {
      this.giverAuditor = giverAuditor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
