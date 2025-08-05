import { Component, inject, OnInit } from '@angular/core';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AccountResponse } from '../../responses/account/account.response';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { TokenService } from '../../services/token.service';
import { BaseComponent } from '../base/base.component';
import { UpdateUserDTO } from '../../dtos/account/update.user.dto';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-user-profile',
  imports: [HeaderComponent, FooterComponent, ReactiveFormsModule, FormsModule, CommonModule],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.scss',
  standalone: true
})
export class UserProfileComponent extends BaseComponent implements OnInit {
  accountResponse?: AccountResponse;
  token: string = '';
  userProfileForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
  ) {
    super();
    this.userProfileForm = this.fb.group({
      fullname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      sodienthoai: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      diachi: [''],
      ngaysinh: [''],
      facebook_account_id: [''],
      google_account_id: [''],
      ghichu: [''],
      password: [''],
      retypePassword: ['']
    });
  }

  ngOnInit(): void {
    this.token = this.tokenService.getToken() ?? '';
    if (!this.token) {
      alert('Bạn chưa đăng nhập!');
      this.router.navigate(['/login']);
      return;
    }

    const accountStr = localStorage.getItem('account') || sessionStorage.getItem('account');
    if (!accountStr) {
      alert('Không tìm thấy thông tin tài khoản trong localStorage!');
      this.router.navigate(['/login']);
      return;
    }

    try {
      this.accountResponse = JSON.parse(accountStr) as AccountResponse;
      this.userProfileForm.patchValue({
        fullname: this.accountResponse.fullname || '',
        email: this.accountResponse.email || '',
        sodienthoai: this.accountResponse.sodienthoai || '',
        diachi: this.accountResponse.diachi || '',
        ngaysinh: this.accountResponse.ngaysinh || '',
        facebook_account_id: this.accountResponse.facebook_account_id || '',
        google_account_id: this.accountResponse.google_account_id || ''
      });
    } catch (error) {
      console.error('Lỗi khi parse account từ localStorage:', error);
      alert('Không thể xử lý thông tin tài khoản.');
    }

    // Kiểm tra mật khẩu khớp
    this.userProfileForm.get('retypePassword')?.valueChanges.subscribe(value => {
      const password = this.userProfileForm.get('password')?.value;
      if (password && value && password !== value) {
        this.userProfileForm.get('retypePassword')?.setErrors({ mismatch: true });
      } else {
        this.userProfileForm.get('retypePassword')?.setErrors(null);
      }
    });
  }

  save(): void {
    if (this.userProfileForm.valid) {
      const updateUserDTO: UpdateUserDTO = {
        fullname: this.userProfileForm.get('fullname')?.value,
        address: this.userProfileForm.get('diachi')?.value, // Sử dụng diachi thay vì address
        password: this.userProfileForm.get('password')?.value,
        retype_password: this.userProfileForm.get('retypePassword')?.value,
        date_of_birth: this.userProfileForm.get('ngaysinh')?.value // Sử dụng ngaysinh thay vì date_of_birth
      };

      this.accountService.updateUserDetail(this.token, updateUserDTO)
        .subscribe({
          next: (response: any) => {
            this.accountService.removeAccountFromLocalStorage();
            this.tokenService.removeToken();
            this.router.navigate(['/login']);
          },
          error: (error: HttpErrorResponse) => {
            console.error(error?.error?.message ?? '');
          }
        });
    } else {
      if (this.userProfileForm.get('retypePassword')?.hasError('mismatch')) {
        console.error('Mật khẩu và mật khẩu gõ lại chưa chính xác');
      }
      console.log('Form invalid');
    }
  }
}