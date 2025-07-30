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

  phoneNumber: string;
  password: string;
  rememberMe: boolean = false;
  accountResponse?: AccountResponse
  showPassword: boolean = false;

  constructor(
    private router: Router,
    private accountService: AccountService,
    private tokenService: TokenService,
    private cartService: CartService,
    private authService: AuthService
  ) {
    this.phoneNumber = '0904';
    this.password = '123';
  }

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

  onPhoneNumberChange() {
    console.log(`Phone typed: ${this.phoneNumber}`)
  }

  login() {
    const message = `Phone: ${this.phoneNumber}\n` +
      `Password: ${this.password}\n` +
      `Rememberme: ${this.rememberMe}`
    alert(message)

    const loginDTO: LoginDTO = {
      "password": this.password,
      "sodienthoai": this.phoneNumber,
    }
    this.accountService.login(loginDTO).subscribe({
      next: (response: LoginResponse) => {
        debugger
        const { token } = response;
        if (this.rememberMe) {
          this.tokenService.setToken(token, true);
        }
        this.tokenService.setToken(token);
        debugger
        this.accountService.getAccountDetails(token).subscribe({
          next: (response: any) => {
            debugger
            this.accountResponse = response;
            this.accountService.saveAccountToLocalStorage(this.accountResponse, this.rememberMe);
            this.router.navigate(['/']);
          },
          complete: () => {
            this.cartService.refreshCart();
            debugger
          },
          error: (error: any) => {
            debugger
            alert(error.error.message);
          }
        })
      },
      complete: () => { debugger },
      error: (error: any) => {
        alert(`Khong the dang nhap, loi: ${error?.error?.message}`);
      }
    });
  }
}
