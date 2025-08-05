import { Component, OnInit } from "@angular/core";
import { AccountResponse } from "../../responses/account/account.response";
import { CommonModule } from "@angular/common";
import { BaseComponent } from "../base/base.component";
import { switchMap, tap } from "rxjs";
import { HttpErrorResponse } from "@angular/common/http";
import { ApiResponse } from "../../responses/api.response";

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

  ngOnInit(): void {
    // Config: OAuth consent screen in Google Console
    // Config: OAuth Client ID in Google Console
    const url = this.router.url;
    let loginType: 'google' | 'facebook';
    if (url.includes('/auth/google/callback')) {
      loginType = 'google';
    }
    else if (url.includes('/auth/facebook/callback')) {
      loginType = 'facebook';
    }
    else {
      console.error('Không xác định được nhà cung cấp xác thực.');
      return;
    }

    // Lấy mã xác nhận từ URL
    this.activatedRoute.queryParams.subscribe(params => {
      debugger
      const code = params['code'];
      if (code) {
        // Gửi mã này đến server để lấy token
        this.authService.exchangeCodeForToken(code, loginType).pipe(
          tap((response: ApiResponse) => {
            debugger
            // Gia su API tra ve token trong response.data
            const token = response.data.token;
            // Luu token
            this.tokenService.setToken(token);
          }),
          switchMap((response) => {
            debugger
            const token = response.data.token;
            // Gọi hàm getAccountDetails với token
            return this.accountService.getAccountDetails(token);
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
            // Thực hiện các tác vụ khác nếu cần
            //this.cartService.refreshCart();
          }
        });
      } else {
        console.error('Không tìm thấy mã xác thực hoặc loại đăng nhập trong URL.');
      }
    })
  }
}