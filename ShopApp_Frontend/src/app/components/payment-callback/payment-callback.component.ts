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
import { title } from "process";
import { error } from "console";
import { HttpErrorResponse } from "@angular/common/http";

@Component({
  selector: 'app-auth-callback',
  standalone: true,
  templateUrl: './payment-callback.component.html',
  styleUrl: './payment-callback.component.css',
  imports:[
    CommonModule
  ]
})

export class PaymentCallbackComponent extends BaseComponent{
    // loading:boolean=true;
    // paymentSuccess:boolean=false;

    // ngOnInit(): void {
    //   // Sử dụng this.activatedRoute từ BaseComponent
    //   this.activatedRoute.queryParams.subscribe(params=>{
    //       debugger
    //       const vnp_ResponseCode =params['vnp_ResponseCode']; // Ma phan hoi tu VNPay
    //       const orderId:number=Number(params['vnp_TxnRef']); // Ma don hang (neu ban truyen vao khi tao URL thanh toan)

    //       if(vnp_ResponseCode==='00'){
    //         // Thanh toan thanh cong
    //         this.handlePaymentSuccess(orderId);
    //       }
    //       else{
    //         // Thanh toan khong thanh cong
    //         this.handlePaymentFailure();
    //       }
    //   });
    // }

    // handlePaymentSuccess(orderId: number):void{
    //   // Su dung this.orderService tu BaseComponent
    //   this.donHangService.updateOrder(orderId,'shipped').subscribe({
    //     next:(response:ApiResponse)=>{
    //       this.loading=false;
    //       this.paymentSuccess=true;
    //       // Sử dụng this.toastService từ BaseComponent
    //       this.toastService.showToast({
    //         error: null,
    //         defaultMsg:'Thanh toán thành công',
    //         title:'Thành Công'
    //       });
    //       // Sử dụng this.router từ baseComponent để chuyển hướng
    //       setTimeout(() => {
    //         debugger
    //         this.cartService.clearCart();
    //         this.router.navigate(['/']);
    //       }, 3000);
    //     },
    //     error: (error:HttpErrorResponse)=>{
    //       this.loading=false;
    //       this.paymentSuccess=false;
    //       this.toastService.showToast({
    //         error: error,
    //         defaultMsg:'Lỗi khi cập nhật trạng thái đơn hàng',
    //         title:'Lỗi'
    //       })
    //     }
    //   })
    // }
}