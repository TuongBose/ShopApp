import { Component, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { RegisterDTO } from '../../dtos/account/register.dto';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { ApiResponse } from '../../responses/api.response';
import { HttpErrorResponse } from '@angular/common/http';
import { BaseComponent } from '../base/base.component';

@Component({
  selector: 'app-register',
  standalone: true,
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
  imports: [
    CommonModule,
    FormsModule,
    HeaderComponent,
    FooterComponent
  ]
})
export class RegisterComponent extends BaseComponent{
  @ViewChild('registerForm') registerForm!: NgForm;
  phoneNumber: string;
  password: string;
  retypePassword: string;
  fullName: string;
  email: string;
  address: string;
  isAccepted: boolean;
  dateOfBirth: Date;
  showPassword: boolean = false;
  showRetypePassword: boolean = false;

  constructor() {
    super();
    
    this.phoneNumber = '';
    this.password = '';
    this.retypePassword = '';
    this.fullName = '';
    this.email = '';
    this.address = '';
    this.isAccepted = true;
    this.dateOfBirth = new Date();
    this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear() - 18);

    //inject
  }

  register() {
    const registerDTO: RegisterDTO = {
      "password": this.password,
      "retypepassword": this.retypePassword,
      "email": this.email,
      "fullname": this.fullName,
      "diachi": this.address,
      "sodienthoai": this.phoneNumber,
      "ngaysinh": this.dateOfBirth,
      "FACEBOOK_ACCOUNT_ID": 0,
      "GOOGLE_ACCOUNT_ID": 0
    }
    debugger
    this.accountService.register(registerDTO).subscribe({
      next: (apiResponse: ApiResponse) => {
        const confirmation = window.confirm('Đăng ký thành công, mời bạn đăng nhập. Bấm "OK" để chuyển đến trang đăng nhập.');
        debugger
        if (confirmation) {
          this.router.navigate(['/login']);
        }
      },
      complete: () => { debugger },
      error: (error: HttpErrorResponse) => {
        this.toastService.showToast({
            error: error,
            defaultMsg: 'Lỗi không xác định',
            title: 'Lỗi Đăng Ký'
          });
      }
    });
  }

  checkPasswordsMatch() {
    if (this.password !== this.retypePassword) {
      this.registerForm.form.controls['retypePassword'].setErrors({ 'passwordMismatch': true })
    } else {
      this.registerForm.form.controls['retypePassword'].setErrors(null);
    }
  }

  checkAge() {
    if (this.dateOfBirth) {
      const today = new Date();
      const birthDate = new Date(this.dateOfBirth);
      let age = today.getFullYear() - birthDate.getFullYear();
      const monthDiff = today.getMonth() - birthDate.getMonth();

      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
      }

      if (age < 18) {
        this.registerForm.form.controls['dateOfBirth'].setErrors({ 'invalidAge': true });
      }
      else {
        this.registerForm.form.controls['dateOfBirth'].setErrors(null);
      }
    }
  }
}
