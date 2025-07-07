import { Component, ViewChild } from '@angular/core';
import { LoginDTO } from '../../dtos/account/login.dto';
import { Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { NgForm } from '@angular/forms';
import { LoginResponse } from '../../responses/account/login.response';
import { AccountResponse } from '../../responses/account/account.response';
import { TokenService } from '../../services/token.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  @ViewChild('loginForm') loginForm!: NgForm;

  phoneNumber: string;
  password: string;
  rememberMe: boolean = false;
  accountResponse?: AccountResponse

  constructor(
    private router: Router,
    private accountService: AccountService,
    private tokenService: TokenService
  ) {
    this.phoneNumber = '0904';
    this.password = '123';
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
          this.tokenService.setToken(token);
          debugger
          this.accountService.getAccountDetails(token).subscribe({
            next: (response: any) => {
              debugger
              this.accountResponse = response;
              this.accountService.saveAccountToLocalStorage(this.accountResponse);
              this.router.navigate(['/']);
            },
            complete: () => { debugger },
            error: (error: any) => {
              debugger
              alert(error.error.message);
            }
          })
        }
      },
      complete: () => { debugger },
      error: (error: any) => {
        alert(`Khong the dang nhap, loi: ${error?.error?.message}`);
      }
    });
  }
}
