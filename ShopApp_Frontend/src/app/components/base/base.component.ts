import { Component, DOCUMENT, inject, OnInit } from "@angular/core";
import { AccountResponse } from "../../responses/account/account.response";
import { ActivatedRoute, Router } from "@angular/router";
import { CommonModule } from "@angular/common";
import { subscribe } from "diagnostics_channel";
import { ToastService } from "../../services/toast.service";
import { LoaiSanPhamService } from "../../services/loaisanpham.service";
import { SanPhamService } from "../../services/sanpham.service";
import { TokenService } from "../../services/token.service";
import { AccountService } from "../../services/account.service";
import { CartService } from "../../services/cart.service";
import { CouponService } from "../../services/coupon.service";
import { DonHangService } from "../../services/donhang.service";
import { PaymentService } from "../../services/payment.service";

@Component({
  selector: 'app-base',
  standalone: true,
  templateUrl: './base.component.html',
  styleUrl: './base.component.css',
  imports: [
    CommonModule
  ]
})

export class BaseComponent {
  toastService = inject(ToastService);
  router: Router = inject(Router);
  loaiSanPhamService: LoaiSanPhamService = inject(LoaiSanPhamService);
  sanPhamService: SanPhamService = inject(SanPhamService);
  tokenService: TokenService = inject(TokenService);
  activatedRoute: ActivatedRoute = inject(ActivatedRoute);
  accountService: AccountService = inject(AccountService);
  cartService: CartService = inject(CartService);
  couponService: CouponService = inject(CouponService);
  donHangService: DonHangService = inject(DonHangService);
  paymentService: PaymentService = inject(PaymentService);

  generateVisiblePageArray(currentPage: number, totalPages: number): number[] {
    const maxVisiblePages = 5;
    const halfVisiblePages = Math.floor(maxVisiblePages / 2);
    if (currentPage < 1 || totalPages < 1 || currentPage > totalPages) {
      return []; // Trả về mảng rỗng nếu giá trị không hợp lệ
    }
    let startPage = Math.max(currentPage - halfVisiblePages, 1);
    let endPage = Math.min(startPage + maxVisiblePages - 1, totalPages);

    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(endPage - maxVisiblePages + 1, 1);
    }
    return new Array(endPage - startPage + 1).fill(0)
      .map((_, index) => startPage + index);
  }
}