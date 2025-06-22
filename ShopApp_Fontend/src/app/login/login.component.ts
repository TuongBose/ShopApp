import { Component, ViewChild } from '@angular/core';
import { LoginDTO } from '../dtos/account/login.dto';
import { Router } from '@angular/router';
import { AccountService } from '../services/account.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  @ViewChild('loginForm') loginForm!: NgForm;

  phoneNumber: string;
  password: string;

  constructor(private router: Router, private accountService: AccountService) {
    this.phoneNumber = '123';
    this.password = '555';
  }

  onPhoneNumberChange() {
    console.log(`Phone typed: ${this.phoneNumber}`)
  }

  login() {
    const message = `Phone: ${this.phoneNumber}\n` +
      `Password: ${this.password}\n`
    alert(message)


    const loginDTO: LoginDTO = {
      "password": this.password,
      "sodienthoai": this.phoneNumber,
    }
    this.accountService.login(loginDTO).subscribe({
      next: (response: any) => {
        debugger
        //this.router.navigate(['/login']);
      },
      complete: () => { debugger },
      error: (error: any) => {
        alert(`Khong the dang nhap, loi: ${error.error}`);
      }
    });
  }
}
