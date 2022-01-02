import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICeremonyUser } from '../ceremony-user.model';

@Component({
  selector: 'jhi-ceremony-user-detail',
  templateUrl: './ceremony-user-detail.component.html',
})
export class CeremonyUserDetailComponent implements OnInit {
  ceremonyUser: ICeremonyUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ceremonyUser }) => {
      this.ceremonyUser = ceremonyUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
