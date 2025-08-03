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
  formBuilder: FormBuilder = inject(FormBuilder);
  userProfileForm: FormGroup = this.formBuilder.group({
    fullname: [''],
    email: [''],
    sodienthoai: [''],
    diachi: [''],
    ngaysinh: [''],
    facebook_account_id: [''],
    google_account_id: [''],
    ghichu: ['']
  });

  ngOnInit(): void {
    debugger
    this.token = this.tokenService.getToken() ?? '';
    if (!this.token) {
      alert('Bạn chưa đăng nhập!');
      this.router.navigate(['/login']);
      return;
    }

    // Lấy dữ liệu tài khoản từ localStorage
    const accountStr = localStorage.getItem('account') || sessionStorage.getItem('account');
    if (!accountStr) {
      alert('Không tìm thấy thông tin tài khoản trong localStorage!');
      this.router.navigate(['/login']);
      return;
    }

    try {
      this.accountResponse = JSON.parse(accountStr) as AccountResponse;

      // Khởi tạo form với dữ liệu từ localStorage
      this.userProfileForm = this.formBuilder.group({
        fullname: [this.accountResponse.fullname, Validators.required],
        email: [this.accountResponse.email || '', [Validators.email]],
        sodienthoai: [this.accountResponse.sodienthoai, Validators.required],
        diachi: [this.accountResponse.diachi || '', Validators.required],
        ngaysinh: [this.accountResponse.ngaysinh ? this.accountResponse.ngaysinh : ''],
        facebook_account_id: [this.accountResponse.facebook_account_id || ''],
        google_account_id: [this.accountResponse.google_account_id || ''],
        ghichu: ['']
      });
    } catch (error) {
      console.error('Lỗi khi parse account từ localStorage:', error);
      alert('Không thể xử lý thông tin tài khoản.');
    }
  }

  save(): void {
    debugger
    if (this.userProfileForm.valid) {
      const updateUserDTO: UpdateUserDTO = {
        fullname: this.userProfileForm.get('fullname')?.value,
        address: this.userProfileForm.get('address')?.value,
        password: this.userProfileForm.get('password')?.value,
        retype_password: this.userProfileForm.get('retype_password')?.value,
        date_of_birth: this.userProfileForm.get('date_of_birth')?.value
      };

      this.accountService.updateUserDetail(this.token, updateUserDTO)
        .subscribe({
          next: (response: any) => {
            this.accountService.removeAccountFromLocalStorage();
            this.tokenService.removeToken();
            this.router.navigate(['/login']);
          },
          error: (error: HttpErrorResponse) => {
            debugger
            console.error(error?.error?.message ?? '');
          }
        });
    } else {
      if (this.userProfileForm.hasError('passwordMismatch')) {
        console.error('Mật khẩu và mật khẩu gõ lại chưa chính xác')
      }
    }
  }
}
