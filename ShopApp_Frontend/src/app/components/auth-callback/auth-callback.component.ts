import { Component, OnInit } from "@angular/core";
import { AccountResponse } from "../../responses/account/account.response";
import { CommonModule } from "@angular/common";
import { BaseComponent } from "../base/base.component";
import { finalize, switchMap, tap } from "rxjs";
import { HttpErrorResponse } from "@angular/common/http";
import { ApiResponse } from "../../responses/api.response";
import { ActivatedRoute, Router } from "@angular/router";
import { AuthService } from "../../services/auth.service";
import { TokenService } from "../../services/token.service";
import { AccountService } from "../../services/account.service";
import { CartService } from "../../services/cart.service";
import { ToastService } from "../../services/toast.service";
import { error } from "console";

@Component({
  selector: 'app-auth-callback',
  standalone: true,
  templateUrl: './auth-callback.component.html',
  styleUrl: './auth-callback.component.css',
  imports: [
    CommonModule
  ]
})

export class AuthCallbackComponent extends BaseComponent implements OnInit {
  accountResponse?: AccountResponse;
  isLoading: boolean = true;

  ngOnInit(): void {
    debugger
    const code = this.activatedRoute.snapshot.queryParamMap.get('code');
    if (!code) {
      console.error('Không tìm thấy code trong URL');
      this.isLoading=false;
      this.toastService.showToast({
          defaultMsg: 'Đăng nhập thất bại',
          title: 'Thông báo',
          delay: 3000,
          type: 'danger'
        });
      this.router.navigate(['/login']);
      return;
    }

    const fullUrl = window.location.href;
    let loginType: 'google' | 'facebook' | undefined;

    if (fullUrl.includes('/auth/google/callback')) {
      loginType = 'google';
    } else if (fullUrl.includes('/auth/facebook/callback')) {
      loginType = 'facebook';
    } else {
      console.error('Không xác định được nhà cung cấp xác thực.');
      return;
    }

    this.authService.exchangeCodeForToken(code, loginType).pipe(
      tap((response: ApiResponse) => {
        debugger
        // Gia su API tra ve token trong response.data
        const token = response.data.token;
        // Luu token
        this.tokenService.setToken(token);
      }),
      switchMap(response => {
        const token = response.data.token;
        return this.accountService.getAccountDetails(token);
      }),
      finalize(() => {
        this.isLoading = false;
      }),
    ).subscribe({
      next: (response: any) => {
        const apiResponse = response as ApiResponse;
        // Xử lý thông tin người dùng
        debugger
        this.accountResponse = {
          ...apiResponse.data,
          ngaysinh: new Date(apiResponse.data.ngaysinh),
        };
        this.accountService.saveAccountToLocalStorage(this.accountResponse);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Lỗi khi lấy thông tin người dùng hoặc trao đổi mã xác thực:', error);
      },
      complete: () => {
        this.cartService.refreshCart();
        this.toastService.showToast({
          defaultMsg: 'Đăng nhập thành công',
          title: 'Thông báo',
          delay: 3000,
          type: 'success'
        });
        this.router.navigate(['/']);
      }
    });
  }
}