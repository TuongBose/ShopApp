import { Injectable } from "@angular/core";
import { JwtHelperService } from "@auth0/angular-jwt"

@Injectable({
    providedIn: 'root',
})

export class TokenService {
    private readonly TOKEN_KEY = 'access_token';
    private jwtHelperService = new JwtHelperService();

    constructor() { }

    // getter and setter
    getToken(): string | null {
        return localStorage.getItem(this.TOKEN_KEY) || sessionStorage.getItem(this.TOKEN_KEY);
    }

    setToken(token: string, rememberMe: boolean=false): void {
        if (rememberMe) {
            localStorage.setItem(this.TOKEN_KEY, token);
        }
        else {
            sessionStorage.setItem(this.TOKEN_KEY, token);
        }
    }

    removeToken(): void {
        localStorage.removeItem(this.TOKEN_KEY);
        sessionStorage.removeItem(this.TOKEN_KEY);
    }

    getUserId(): number {
        debugger
        let accountObject = this.jwtHelperService.decodeToken(this.getToken() ?? '');
        return 'USERID' in accountObject ? parseInt(accountObject['USERID']) : 0;
    }

    isTokenExpired(): boolean {
        debugger
        if (this.getToken() == null) {
            return false;
        }
        return this.jwtHelperService.isTokenExpired(this.getToken());
    }
}