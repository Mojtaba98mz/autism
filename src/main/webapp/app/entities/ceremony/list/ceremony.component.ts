import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICeremony } from '../ceremony.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { CeremonyService } from '../service/ceremony.service';
import { CeremonyDeleteDialogComponent } from '../delete/ceremony-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { IDonation } from '../../donation/donation.model';

@Component({
  selector: 'jhi-ceremony',
  templateUrl: './ceremony.component.html',
})
export class CeremonyComponent implements OnInit {
  ceremonies?: ICeremony[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  idFilter;
  amountFilter;

  constructor(
    protected ceremonyService: CeremonyService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const ceremonyUserId = this.activatedRoute.snapshot.params['ceremonyUserId'];
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.ceremonyService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        'ceremonyUserId.equals': ceremonyUserId,
      })
      .subscribe(
        (res: HttpResponse<ICeremony[]>) => {
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

    this.ceremonyService.query(req).subscribe(
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
    this.loadPage();
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  onEnterPressed(event: any, fieldName: string): void {
    if (event.keyCode === 13 || fieldName === 'isCash') {
      const pageToLoad: number = this.page ?? 1;
      const ceremonyUserId = this.activatedRoute.snapshot.params['ceremonyUserId'];
      const req = {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        'ceremonyUserId.equals': ceremonyUserId,
      };
      if (event.keyCode === 13) {
        const searchValue = event.target.value;
        if (fieldName === 'id' || fieldName === 'amount') {
          req[fieldName + '.equals'] = searchValue;
        }
      }
      this.loadPageWithReq(undefined, undefined, req);
    }
  }

  trackId(index: number, item: ICeremony): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(ceremony: ICeremony): void {
    const modalRef = this.modalService.open(CeremonyDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ceremony = ceremony;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  goToNew(): void {
    const ceremonyUserId = this.activatedRoute.snapshot.params['ceremonyUserId'];
    this.router.navigate(['/ceremony', ceremonyUserId, 'new']);
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

  protected onSuccess(data: ICeremony[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    const ceremonyUserId = this.activatedRoute.snapshot.params['ceremonyUserId'];
    this.page = page;
    if (navigate) {
      this.router.navigate(['/ceremony', ceremonyUserId, 'viewCeremony'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.ceremonies = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
