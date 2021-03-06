import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGiver } from '../giver.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { GiverService } from '../service/giver.service';
import { GiverDeleteDialogComponent } from '../delete/giver-delete-dialog.component';
import { AlertService } from '../../../core/util/alert.service';

@Component({
  selector: 'jhi-giver',
  templateUrl: './giver.component.html',
})
export class GiverComponent implements OnInit {
  givers?: IGiver[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  idFilter;
  nameFilter;
  familyFilter;
  phoneNumberFilter;
  homeNumberFilter;
  provinceFilter;
  cityFilter;

  constructor(
    protected giverService: GiverService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected alertService: AlertService
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.giverService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IGiver[]>) => {
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
    this.nameFilter = '';
    this.familyFilter = '';
    this.phoneNumberFilter = '';
    this.provinceFilter = '';
    this.cityFilter = '';
    this.loadPage();
  }

  loadPageWithReq(page?: number, dontNavigate?: boolean, req?: any): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.giverService.query(req).subscribe(
      (res: HttpResponse<IGiver[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
      },
      () => {
        this.isLoading = false;
        this.onError();
      }
    );
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(index: number, item: IGiver): number {
    return item.id!;
  }

  private timer: any;
  onEnterPressed(event: any, fieldName: string): void {
    clearTimeout(this.timer);
    this.timer = setTimeout(() => {
      const searchValue = event.target.value;
      let searchField = fieldName + '.contains';
      const pageToLoad: number = this.page ?? 1;
      const req = {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      };
      if (fieldName === 'id') {
        searchField = fieldName + '.equals';
      }
      req[searchField] = searchValue;
      this.loadPageWithReq(undefined, undefined, req);
    }, 1000);
  }

  delete(giver: IGiver): void {
    const modalRef = this.modalService.open(GiverDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.giver = giver;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  disable(giver: IGiver): void {
    if (giver.id) {
      this.giverService.disableEnable(giver.id).subscribe(() => {
        giver.disabled = !giver.disabled;
      });
    }
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

  protected onSuccess(data: IGiver[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/giver'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.givers = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
