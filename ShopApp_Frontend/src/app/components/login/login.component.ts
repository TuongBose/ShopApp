import { Component, OnInit, ViewChild } from '@angular/core';
import { LoginDTO } from '../../dtos/account/login.dto';
import { Router, RouterModule } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { FormsModule, NgForm } from '@angular/forms';
import { LoginResponse } from '../../responses/account/login.response';
import { AccountResponse } from '../../responses/account/account.response';
import { TokenService } from '../../services/token.service';
import { CartService } from '../../services/cart.service';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { CommonModule } from '@angular/common';
import { BaseComponent } from '../base/base.component';
import { nextTick } from 'process';
import { error } from 'console';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { catchError, finalize, of, switchMap, tap } from 'rxjs';
import { ApiResponse } from '../../responses/api.response';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  imports: [
    HeaderComponent,
    FooterComponent,
    CommonModule,
    FormsModule,
    RouterModule
  ]
})
export class LoginComponent extends BaseComponent {
  @ViewChild('loginForm') loginForm!: NgForm;

  phoneNumber: string = '';
  password: string = '';
  rememberMe: boolean = false;
  accountResponse?: AccountResponse
  showPassword: boolean = false;

  createAccount() {
    debugger
    // Chuyển hướng người dùng đến trang đăng ký (hoặc trang tạo tài khoản)
    this.router.navigate(['/register']);
  }

  loginWithGoogle() {
    debugger
    this.authService.authenticate('google').subscribe({
      next: (url: string) => {
        debugger
        // Chuyển hướng người dùng đến URL đăng nhập Google
        window.location.href = url;
      },
      error: (error: HttpErrorResponse) => {
        debugger
        console.error('Lỗi khi xác thực với Google: ', error?.error?.message ?? '');
      },
    });
  }

  loginWithFacebook() {
    debugger
    this.authService.authenticate('facebook').subscribe({
      next: (url: string) => {
        debugger
        // Chuyển hướng người dùng đến URL đăng nhập Facebook
        window.location.href = url;
      },
      error: (error: HttpErrorResponse) => {
        debugger
        console.error('Lỗi khi xác thực với Facebook: ', error?.error?.message ?? '');
      },
    });
  }

  login() {
    const loginDTO: LoginDTO = {
      password: this.password,
      sodienthoai: this.phoneNumber,
    }
    this.accountService.login(loginDTO).pipe(
      tap((apiResponse: ApiResponse) => {
        const { token } = apiResponse.data;
        this.tokenService.setToken(token);
      }),
      switchMap((apiResponse: ApiResponse) => {
        const { token } = apiResponse.data;
        return this.accountService.getAccountDetails(token).pipe(
          tap((apiResponse2: ApiResponse) => {
            this.accountResponse = {
              ...apiResponse2.data,
              ngaysinh: new Date(apiResponse2.data.ngaysinh),
            };
            this.accountService.saveAccountToLocalStorage(this.accountResponse, this.rememberMe);
          }),
          catchError((error: HttpErrorResponse) => {
            console.error('Lỗi khi lấy thông tin người dùng:', error?.error?.message ?? '');
            return of(null); // Tiếp tục chuỗi Observable
          })
        );
      }),
      finalize(() => {
        this.cartService.refreshCart();
      })
    ).subscribe({
      next: () => {
        this.toastService.showToast({
          defaultMsg: 'Đăng nhập thành công',
          title: 'Thông báo',
          delay: 3000
        });
        this.router.navigate(['/']);
      },
      error: (error: HttpErrorResponse) => {
        this.toastService.showToast({
          error,
          defaultMsg: 'Đăng nhập thất bại!',
          title: 'Lỗi đăng nhập'
        });
        console.error('Lỗi đăng nhập:', error?.error?.message ?? '');
      }
    });
  }
}
