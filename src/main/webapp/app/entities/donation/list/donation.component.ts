import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDonation } from '../donation.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { DonationService } from '../service/donation.service';
import { DonationDeleteDialogComponent } from '../delete/donation-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { IGiver } from '../../giver/giver.model';

@Component({
  selector: 'jhi-donation',
  templateUrl: './donation.component.html',
})
export class DonationComponent implements OnInit {
  donations?: IDonation[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  idFilter;
  isCashFilter;
  amountFilter;

  constructor(
    protected donationService: DonationService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const giverId = this.activatedRoute.snapshot.params['giverId'];
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.donationService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        'giverId.equals': giverId,
      })
      .subscribe(
        (res: HttpResponse<IDonation[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  loadPageWithReq(page?: number, dontNavigate?: boolean, req?: any): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.donationService.query(req).subscribe(
      (res: HttpResponse<IDonation[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
      },
      () => {
        this.isLoading = false;
        this.onError();
      }
    );
  }

  refreshPage(): void {
    this.idFilter = '';
    this.amountFilter = '';
    this.isCashFilter = false;
    this.loadPage();
  }

  ngOnInit(): void {
    this.handleNavigation();
  }
  private timer: any;
  onEnterPressed(event: any, fieldName: string): void {
    clearTimeout(this.timer);
    this.timer = setTimeout(() => {
      const pageToLoad: number = this.page ?? 1;
      const giverId = this.activatedRoute.snapshot.params['giverId'];
      const req = {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        'giverId.equals': giverId,
      };
      const searchValue = event.target.value;
      if (fieldName === 'id' || fieldName === 'amount') {
        req[fieldName + '.equals'] = searchValue;
      } else {
        const searchField = fieldName + '.contains';
        req[searchField] = searchValue;
      }
      if (fieldName === 'isCash') {
        req['isCash.equals'] = event.target.checked;
      }
      this.loadPageWithReq(undefined, undefined, req);
    }, 3000);
  }

  trackId(index: number, item: IDonation): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(donation: IDonation): void {
    const modalRef = this.modalService.open(DonationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.donation = donation;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  goToNewDonation(): void {
    const giverId = this.activatedRoute.snapshot.params['giverId'];
    this.router.navigate(['/donation', giverId, 'new']);
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IDonation[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    const giverId = this.activatedRoute.snapshot.params['giverId'];
    this.page = page;
    if (navigate) {
      this.router.navigate(['/donation', giverId, 'viewGiverDonate'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.donations = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
