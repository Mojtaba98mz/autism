import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { IUser } from '../../entities/user/user.model';

@Injectable({ providedIn: 'root' })
export class UserSelectionService {
  public resourceUrl = SERVER_API_URL + '/api/admin/users';
  private _selected: IUser | undefined;
  constructor(private http: HttpClient) {}

  getAll(nameFamily: string): Observable<HttpResponse<IUser[]>> {
    return this.http.get<IUser[]>(this.resourceUrl + '/filter-by-name?filter=' + nameFamily, { observe: 'response' });
  }

  // GETTER AND SETTER
  get selected(): IUser | undefined {
    return this._selected;
  }

  set selected(value: IUser | undefined) {
    this._selected = value;
  }
}
