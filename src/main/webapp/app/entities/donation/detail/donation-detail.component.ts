import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDonation } from '../donation.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-donation-detail',
  templateUrl: './donation-detail.component.html',
})
export class DonationDetailComponent implements OnInit {
  donation: IDonation | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donation }) => {
      this.donation = donation;
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
