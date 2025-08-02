import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RegisterDTO } from '../dtos/account/register.dto';
import { LoginDTO } from '../dtos/account/login.dto';
import { environment } from '../environments/environment';
import { AccountResponse } from '../responses/account/account.response';
import { ApiResponse } from '../responses/api.response';
import { UpdateUserDTO } from '../dtos/account/update.user.dto';

@Injectable({
  providedIn: 'root'
})

export class AccountService {
  private apiRegister = `${environment.apiBaseUrl}/accounts/register`;
  private apiLogin = `${environment.apiBaseUrl}/accounts/login`;
  private apiAccountDetails = `${environment.apiBaseUrl}/accounts/details`;

  private apiConfig = {
    headers: this.createHeads(),
  }
  constructor(private http: HttpClient,) { }

  private createHeads(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept-Language': 'vi'
    })
  }

  register(registerDTO: RegisterDTO): Observable<ApiResponse> {
    debugger
    return this.http.post<ApiResponse>(this.apiRegister, registerDTO, this.apiConfig);
  }

  login(loginDTO: LoginDTO): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(this.apiLogin, loginDTO, this.apiConfig);
  }

  getAccountDetails(token: string): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(this.apiAccountDetails, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      })
    });
  }

  saveAccountToLocalStorage(accountResponse?: AccountResponse, rememberMe: boolean = false) {
    try {
      debugger
      if (accountResponse == null || !accountResponse) { return; }
      // Convert the accountResponse object to a JSON string
      const accountResponseJSON = JSON.stringify(accountResponse);

      // Save the JSON string to local storage with a key (e.g.,"accountResponse")
      if (rememberMe) {
        localStorage.setItem('account', accountResponseJSON);
      }
      else {
        sessionStorage.setItem('account', accountResponseJSON)
      }
      console.log('Account response saved to storage.');
    } catch (error) {
      console.error('Error saving account response to storage: ', error);
    }
  }

  getAccountFromLocalStorage(): AccountResponse | null {
    try {
      debugger
      // Retrieve the JSON string from local storage using the key
      const accountResponseJSON = localStorage.getItem('account') || sessionStorage.getItem('account');
      if (accountResponseJSON == null || accountResponseJSON == undefined) { return null; }

      // Parse the JSON string back to an object
      const accountResponse = JSON.parse(accountResponseJSON!);
      console.log('Account retrieved from storage.');
      return accountResponse;
    } catch (error) {
      console.error('Error retrieving account from storage: ', error);
      return null;
    }
  }

  removeAccountFromLocalStorage(): void {
    try {
      localStorage.removeItem('account');
      sessionStorage.removeItem('account');
      console.log('Account data removed from storage.');
    } catch (error) {
      console.error('Error revoming account from storage: ', error)
    }
  }

  updateUserDetail(token: string, updateUserDTO: UpdateUserDTO): Observable<ApiResponse> {
    let userResponse = this.getAccountFromLocalStorage();
    return this.http.put<ApiResponse>(`${this.apiAccountDetails}/${userResponse?.userid}`, updateUserDTO, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      })
    })
  }
}
