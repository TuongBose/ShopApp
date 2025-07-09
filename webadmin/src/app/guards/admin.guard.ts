import { inject, Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, CanActivateFn } from '@angular/router';
import { TokenService } from '../services/token.service';
import { AccountService } from '../services/account.service';
import { AccountResponse } from '../responses/account/account.response';

@Injectable({
    providedIn: 'root'
})
export class AdminGuard {
    accountResponse?: AccountResponse | null

    constructor(
        private router: Router,
        private tokenService: TokenService,
        private accountService: AccountService
    ) { }

    canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        const isTokenExpired = this.tokenService.isTokenExpired();
        const isUserIdValid = this.tokenService.getUserId() > 0;
        this.accountResponse = this.accountService.getAccountFromLocalStorage();
        const isAdmin = this.accountResponse?.rolename == true;

        debugger
        if (!isTokenExpired && isUserIdValid && isAdmin) {
            return true;
        } else {
            // Nếu không authenticated, trả về trang login
            this.router.navigate(['/login']);
            return false;
        }
    }
}

export const AdminGuardFn: CanActivateFn = (next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
    debugger
    return inject(AdminGuard).canActivate(next, state);
}