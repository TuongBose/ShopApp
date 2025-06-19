import { Component } from '@angular/core';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  phone: string;
  password: string;
  retypePassword: string;
  fullName: string;
  address: string;
  isAccepted: boolean;
  dateOfBirth:Date;

  constructor() {
    this.phone = '';
    this.password = '';
    this.retypePassword = '';
    this.fullName = '';
    this.address = '';
    this.isAccepted = false;
    this.dateOfBirth= new Date();
    this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear()-18);
  }

  onPhoneChange() {
    console.log(`Phone typed: ${this.phone}`)
  }

  onDateOfBirthChange(){

  }

  register() {
    const message = `Phone: ${this.phone}\n` +
                  `Password: ${this.password}\n` +
                  `Retype Password: ${this.retypePassword}\n` +
                  `Full Name: ${this.fullName}\n` +
                  `Address: ${this.address}\n` +
                  `Is Accepted: ${this.isAccepted}\n`+
                  `Date of Birth: ${this.dateOfBirth}`
    alert(message)
  }
}
