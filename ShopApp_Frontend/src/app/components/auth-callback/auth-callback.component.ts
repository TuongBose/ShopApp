import { Component, OnInit } from "@angular/core";
import { AccountResponse } from "../../responses/account/account.response";
import { ActivatedRoute, Router } from "@angular/router";
import { CommonModule } from "@angular/common";
import { subscribe } from "diagnostics_channel";
import { BaseComponent } from "../base/base.component";
import { AuthService } from "../../services/auth.service";
import { response } from "express";
import { TokenService } from "../../services/token.service";
import { switchMap } from "rxjs";
import { AccountService } from "../../services/account.service";

@Component({
  selector: 'app-auth-callback',
  standalone: true,
  templateUrl: './auth-callback.component.html',
  styleUrl: './auth-callback.component.css',
  imports:[
    CommonModule
  ]
})

export class AuthCallbackComponent extends BaseComponent implements OnInit{
    accountResponse?:AccountResponse

    constructor(
    private router: Router,
    private activatedRoute:ActivatedRoute,
    private authService: AuthService,
    private tokenService:TokenService,
    private accountService: AccountService
  ) { }

    ngOnInit(): void {
      // Config: OAuth consent screen in Google Console
      // Config: OAuth Client ID in Google Console
        const url=this.router.url;
        let loginType:'google'|'facebook';
        if(url.includes('/auth/google/callback')){
            loginType='google';
        }
        else if(url.includes('/auth/facebook/callback')){
            loginType='facebook';
        }
        else
        {
            console.error('Không xác định được nhà cung cấp xác thực.');
            return;
        }

        // Lấy mã xác nhận từ URL
        this.activatedRoute.queryParams.subscribe(params=>{
          debugger
          const code =params['code'];
          if(code){
            // Gửi mã này đến server để lấy token
            this.authService.exchangeCodeForToken(code,loginType).pipe(
              tap((response:ApiResponse)=>{
                debugger
                // Gia su API tra ve token trong response.data
                const token =  response.data.token;
                // Luu token
                this.tokenService.setToken(token);
              }),
              switchMap((response)=>{
                debugger
                const token = response.data.token;
                // Gọi hàm getAccountDetails với token
                return this.accountService.getAccountDetails(token);
              }),
            ).subscribe({
              next:(apiResponse:ApiResponse)=>{
                // Xử lý thông tin người dùng
              }
            })
          }
        })
    }
}