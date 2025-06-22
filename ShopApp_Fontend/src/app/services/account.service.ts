import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RegisterDTO } from '../dtos/account/register.dto';
import { LoginDTO } from '../dtos/account/login.dto';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private apiRegister = `${environment.apiBaseUrl}/accounts/register`;
  private apiLogin = `${environment.apiBaseUrl}/accounts/login`;
  
  private apiConfig = {
    headers: this.createHeads(),
  }
  constructor(private http: HttpClient,) { }

  private createHeads(): HttpHeaders {
    return new HttpHeaders({ 
      'Content-Type': 'application/json',
      'Accept-Language':'vi'
     })
  }

  register(registerDTO: RegisterDTO): Observable<any> {
    return this.http.post(this.apiRegister, registerDTO, this.apiConfig);
  }

  login(loginDTO: LoginDTO): Observable<any> {
    return this.http.post(this.apiLogin, loginDTO, this.apiConfig);
  }
}
