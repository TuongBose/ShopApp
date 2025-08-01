import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})

export class AuthService {
    private apiBaseUrl = environment.apiBaseUrl;

    constructor(private http: HttpClient) { }

    // Corrected function name and parameter usage
    authenticate(loginType: 'facebook' | 'google'): Observable<string> {
        debugger
        return this.http.get(
            `${this.apiBaseUrl}/accounts/auth/social-login?login_type=${loginType}`,
            { responseType: 'text' }
        );
    }

    exchangeCodeForToken(code: string, loginType: 'facebook' | 'google'): Observable<any> {
        const params = new HttpParams()
            .set('code', code)
            .set('login-type', loginType);

        return this.http.get<any>(`${this.apiBaseUrl}/account/auth/social/callback`, { params });
    }
}