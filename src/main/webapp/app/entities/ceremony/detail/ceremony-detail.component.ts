import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICeremony } from '../ceremony.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-ceremony-detail',
  templateUrl: './ceremony-detail.component.html',
})
export class CeremonyDetailComponent implements OnInit {
  ceremony: ICeremony | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ceremony }) => {
      this.ceremony = ceremony;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
