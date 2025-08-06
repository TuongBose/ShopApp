import { Component, OnInit } from "@angular/core";
import { AccountResponse } from "../../responses/account/account.response";
import { CommonModule } from "@angular/common";
import { BaseComponent } from "../base/base.component";
import { switchMap, tap } from "rxjs";
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

export class AuthCallbackComponent implements OnInit {
  accountResponse?: AccountResponse;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private authService: AuthService,
    private tokenService: TokenService,
    private accountService: AccountService,
    private cartService: CartService,
    private toastService: ToastService
  ) { }


  ngOnInit(): void {
    // Config: OAuth consent screen in Google Console
    // Config: OAuth Client ID in Google Console
    debugger
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

    //Lấy mã xác nhận từ URL
    this.activatedRoute.queryParams.subscribe(params => {
      debugger
      const code = params['code'];
      if (code) {
        this.authService.exchangeCodeForToken(code, loginType).subscribe({
          next: (apiResponse: ApiResponse) => {
            debugger
            const token = apiResponse.data.token;
            this.tokenService.setToken(token);

            this.accountService.getAccountDetails(token).subscribe({
              next: (apiResponse: ApiResponse) => {
                debugger
                this.accountResponse = {
                  ...apiResponse.data,
                  ngaysinh: new Date(apiResponse.data.ngaysinh),
                };
                this.accountService.saveAccountToLocalStorage(this.accountResponse, false);
              },
              error: (error: HttpErrorResponse) => {
                console.error('Lỗi gọi accoun detail', error);
              },
            });
          },
          complete: () => {
            debugger
            this.cartService.refreshCart();
            this.router.navigate(['/']);
          },
          error: (error: HttpErrorResponse) => {
            console.error('Lỗi gọi callback', error);
          }
        });
      } else {
        console.error('Không tìm thấy mã xác thực hoặc loại đăng nhập trong URL.');
      }


      //   // Gửi mã này đến server để lấy token
      //   this.authService.exchangeCodeForToken(code, loginType).pipe(
      //     tap((response: ApiResponse) => {
      //       debugger
      //       // Gia su API tra ve token trong response.data
      //       const token = response.data.token;
      //       // Luu token
      //       this.tokenService.setToken(token);
      //     }),
      //     switchMap((response) => {
      //       debugger
      //       const token = response.data.token;
      //       // Gọi hàm getAccountDetails với token
      //       return this.accountService.getAccountDetails(token);
      //     }),
      //   ).subscribe({
      //     next: (response: any) => {
      //       const apiResponse = response as ApiResponse;
      //       // Xử lý thông tin người dùng
      //       debugger
      //       this.accountResponse = {
      //         ...apiResponse.data,
      //         ngaysinh: new Date(apiResponse.data.ngaysinh),
      //       };
      //       this.accountService.saveAccountToLocalStorage(this.accountResponse);
      //     },
      //     error: (error: HttpErrorResponse) => {
      //       console.error('Lỗi khi lấy thông tin người dùng hoặc trao đổi mã xác thực:', error);
      //     },
      //     complete: () => {
      //       this.cartService.refreshCart();
      //       this.toastService.showToast({
      //         defaultMsg: 'Đăng nhập thành công',
      //         title: 'Thông báo',
      //         delay: 3000,
      //         type: 'success'
      //       });
      //       this.router.navigate(['/']);
      //     }
      //   });
      // } else {
      //   console.error('Không tìm thấy mã xác thực hoặc loại đăng nhập trong URL.');
      // }
    })
  }
}