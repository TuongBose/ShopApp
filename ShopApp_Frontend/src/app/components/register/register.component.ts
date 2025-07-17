import { Component, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { RegisterDTO } from '../../dtos/account/register.dto';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';

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
export class RegisterComponent {
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

  constructor(private router: Router, private accountService: AccountService) {
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

  onPhoneNumberChange() {
    console.log(`Phone typed: ${this.phoneNumber}`)
  }

  register() {
    const message = `Phone: ${this.phoneNumber}\n` +
      `Password: ${this.password}\n` +
      `Retype Password: ${this.retypePassword}\n` +
      `Full Name: ${this.fullName}\n` +
      `Email: ${this.email}\n` +
      `Address: ${this.address}\n` +
      `Is Accepted: ${this.isAccepted}\n` +
      `Date of Birth: ${this.dateOfBirth}`
    alert(message)


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
      next: (response: any) => {
        debugger
        this.router.navigate(['/login']);
      },
      complete: () => { debugger },
      error: (error: any) => {
        debugger
        alert(`Khong the dang ky, loi: ${error.error}`);
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
