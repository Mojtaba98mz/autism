import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { Giver, IGiver } from '../giver-assigner.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { GiverAssignerService } from '../service/giver-assigner.service';
import { IUser } from '../../user/user.model';

@Component({
  selector: 'jhi-giver',
  templateUrl: './giver-assigner.component.html',
  styleUrls: ['./giver-assigner.scss'],
})
export class GiverAssignerComponent implements OnInit {
  assignedGivers?: IGiver[];
  notAssignedGivers?: IGiver[];
  supporters?: IUser[];
  isLoading = false;
  assignTotalItems = 0;
  notAssignTotalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  assignedPage?: number;
  notAssignedPage?: number;
  assignedPredicate!: string;
  notAssignedPredicate!: string;
  assignedAscending!: boolean;
  notAssignedAscending!: boolean;
  assignedNgbPaginationPage = 1;
  notAssignedNgbPaginationPage = 1;
  selectedSupporter?: number;

  rNameFilter;
  lNameFilter;
  rFamilyFilter;
  lFamilyFilter;
  rPhoneNumberFilter;
  lPhoneNumberFilter;
  supporterNameFilter;
  supporterFamilyFilter;

  constructor(protected giverAssignerService: GiverAssignerService, protected activatedRoute: ActivatedRoute, protected router: Router) {}

  loadAssignedPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.assignedPage ?? 1;

    this.giverAssignerService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.assignedSort(),
      })
      .subscribe(
        (res: HttpResponse<IGiver[]>) => {
          this.isLoading = false;
          this.onAssignedSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onAssignedError();
        }
      );
  }

  loadNotAssignedPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.notAssignedPage ?? 1;

    this.giverAssignerService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.notAssignedSort(),
      })
      .subscribe(
        (res: HttpResponse<IGiver[]>) => {
          this.isLoading = false;
          this.onNotAssignedSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onNotAssignedError();
        }
      );
  }

  loadAssignedPageWithReq(page?: number, dontNavigate?: boolean, req?: any): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.assignedPage ?? 1;

    this.giverAssignerService.query(req).subscribe(
      (res: HttpResponse<IGiver[]>) => {
        this.isLoading = false;
        this.onAssignedSuccess(res.body, res.headers, pageToLoad, false);
      },
      () => {
        this.isLoading = false;
        this.onAssignedError();
      }
    );
  }

  loadNotAssignedPageWithReq(page?: number, dontNavigate?: boolean, req?: any): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.notAssignedPage ?? 1;

    this.giverAssignerService.query(req).subscribe(
      (res: HttpResponse<IGiver[]>) => {
        this.isLoading = false;
        this.onNotAssignedSuccess(res.body, res.headers, pageToLoad, false);
      },
      () => {
        this.isLoading = false;
        this.onNotAssignedError();
      }
    );
  }

  assign(giver: Giver): void {
    if (this.selectedSupporter && giver.id) {
      this.giverAssignerService.assign(this.selectedSupporter, giver.id).subscribe((res: HttpResponse<{}>) => {
        this.onSupporterChange();
      });
    }
  }

  unAssign(giver: Giver): void {
    if (this.selectedSupporter && giver.id) {
      this.giverAssignerService.unAssign(this.selectedSupporter, giver.id).subscribe((res: HttpResponse<{}>) => {
        this.onSupporterChange();
      });
    }
  }

  onSupporterChange(): void {
    const assignedPageToLoad: number = this.assignedPage ?? 1;
    const notAssignedPageToLoad: number = this.notAssignedPage ?? 1;
    const assignedReq = {
      page: assignedPageToLoad - 1,
      size: this.itemsPerPage,
    };

    const NotAssignedReq = {
      page: notAssignedPageToLoad - 1,
      size: this.itemsPerPage,
    };

    assignedReq['supporterId.equals'] = this.selectedSupporter;
    NotAssignedReq['supporterId.notEquals'] = this.selectedSupporter;
    this.loadAssignedPageWithReq(undefined, undefined, assignedReq);
    this.loadNotAssignedPageWithReq(undefined, undefined, NotAssignedReq);
  }

  ngOnInit(): void {
    this.giverAssignerService.findAllSupporters().subscribe(
      (res: HttpResponse<IGiver[]>) => {
        this.isLoading = false;
        // this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        this.supporters = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );

    //this.handleNavigation();
  }

  trackId(index: number, item: IGiver): number {
    return item.id!;
  }

  onRightEnterPressed(event: any, fieldName: string): void {
    if (event.keyCode === 13) {
      const searchValue = event.target.value;
      const searchField = fieldName + '.contains';
      const notAssignedPageToLoad: number = this.notAssignedPage ?? 1;
      const NotAssignedReq = {
        page: notAssignedPageToLoad - 1,
        size: this.itemsPerPage,
      };
      NotAssignedReq['supporterId.notEquals'] = this.selectedSupporter;
      NotAssignedReq[searchField] = searchValue;
      this.loadNotAssignedPageWithReq(undefined, undefined, NotAssignedReq);
    }
  }

  onLeftEnterPressed(event: any, fieldName: string): void {
    if (event.keyCode === 13) {
      const searchValue = event.target.value;
      const searchField = fieldName + '.contains';
      const notAssignedPageToLoad: number = this.notAssignedPage ?? 1;
      const NotAssignedReq = {
        page: notAssignedPageToLoad - 1,
        size: this.itemsPerPage,
      };
      NotAssignedReq['supporterId.equals'] = this.selectedSupporter;
      NotAssignedReq[searchField] = searchValue;
      this.loadAssignedPageWithReq(undefined, undefined, NotAssignedReq);
    }
  }

  protected assignedSort(): string[] {
    const result = [this.assignedPredicate + ',' + (this.assignedAscending ? ASC : DESC)];
    if (this.assignedPredicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected notAssignedSort(): string[] {
    const result = [this.notAssignedPredicate + ',' + (this.notAssignedAscending ? ASC : DESC)];
    if (this.notAssignedPredicate !== 'id') {
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
      if (pageNumber !== this.assignedPage || predicate !== this.assignedPredicate || ascending !== this.assignedAscending) {
        this.assignedPredicate = predicate;
        this.assignedAscending = ascending;
        this.loadAssignedPage(pageNumber, true);
      }

      if (pageNumber !== this.notAssignedPage || predicate !== this.notAssignedPredicate || ascending !== this.notAssignedAscending) {
        this.notAssignedPredicate = predicate;
        this.notAssignedAscending = ascending;
        this.loadNotAssignedPage(pageNumber, true);
      }
    });
  }

  protected onAssignedSuccess(data: IGiver[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.assignTotalItems = Number(headers.get('X-Total-Count'));
    this.assignedPage = page;
    if (navigate) {
      this.router.navigate(['/giver'], {
        queryParams: {
          page: this.assignedPage,
          size: this.itemsPerPage,
          sort: this.assignedPredicate + ',' + (this.assignedAscending ? ASC : DESC),
        },
      });
    }
    this.assignedGivers = data ?? [];
    this.assignedNgbPaginationPage = this.assignedPage;
  }

  protected onNotAssignedSuccess(data: IGiver[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.notAssignTotalItems = Number(headers.get('X-Total-Count'));
    this.notAssignedPage = page;
    if (navigate) {
      this.router.navigate(['/giver'], {
        queryParams: {
          page: this.notAssignedPage,
          size: this.itemsPerPage,
          sort: this.notAssignedPredicate + ',' + (this.notAssignedAscending ? ASC : DESC),
        },
      });
    }
    this.notAssignedGivers = data ?? [];
    this.notAssignedNgbPaginationPage = this.notAssignedPage;
  }

  protected onAssignedError(): void {
    this.assignedNgbPaginationPage = this.assignedPage ?? 1;
  }

  protected onNotAssignedError(): void {
    this.notAssignedNgbPaginationPage = this.notAssignedPage ?? 1;
  }
}
