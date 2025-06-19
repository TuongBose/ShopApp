import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  @ViewChild('registerForm') registerForm!: NgForm;
  phone: string;
  password: string;
  retypePassword: string;
  fullName: string;
  email: string;
  address: string;
  isAccepted: boolean;
  dateOfBirth: Date;

  constructor(private http: HttpClient, private router: Router) {
    this.phone = '123';
    this.password = '123456';
    this.retypePassword = '123456';
    this.fullName = 'tuong';
    this.email = 'tuong@123gmail.com';
    this.address = '123 ltt';
    this.isAccepted = true;
    this.dateOfBirth = new Date();
    this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear() - 18);

    //inject

  }

  onPhoneChange() {
    console.log(`Phone typed: ${this.phone}`)
  }

  register() {
    const message = `Phone: ${this.phone}\n` +
      `Password: ${this.password}\n` +
      `Retype Password: ${this.retypePassword}\n` +
      `Full Name: ${this.fullName}\n` +
      `Email: ${this.email}\n` +
      `Address: ${this.address}\n` +
      `Is Accepted: ${this.isAccepted}\n` +
      `Date of Birth: ${this.dateOfBirth}`
    //alert(message)

    const apiUrl = "http://localhost:8080/api/v1/accounts/register";
    const registerData = {
      "password": this.password,
      "retypepassword": this.retypePassword,
      "email": this.email,
      "fullname": this.fullName,
      "diachi": this.address,
      "sodienthoai": this.phone,
      "ngaysinh": this.dateOfBirth,
      "FACEBOOK_ACCOUNT_ID": 0,
      "GOOGLE_ACCOUNT_ID": 0
    }
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    this.http.post(apiUrl, registerData, { headers }).subscribe({
      next: (response: any) => {
        debugger
        this.router.navigate(['/login']);
      },
      complete: () => { debugger },
      error: (error: any) => {
        alert(`Khong the dang ky, loi: ${error.error}`);
      }
    })
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
