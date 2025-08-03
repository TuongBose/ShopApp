import { Component, inject, OnInit } from '@angular/core';
import { SanPham } from '../../models/sanpham';
import { OrderDTO } from '../../dtos/order.dto';
import { CartService } from '../../services/cart.service';
import { SanPhamService } from '../../services/sanpham.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { DonHangService } from '../../services/donhang.service';
import { TokenService } from '../../services/token.service';
import { environment } from '../../environments/environment';
import { Router, RouterModule } from '@angular/router';
import { DonHang } from '../../models/donhang';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { CommonModule } from '@angular/common';
import { PaymentService } from '../../services/payment.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ToastService } from '../../services/toast.service';
import { error } from 'console';
import { BaseComponent } from '../base/base.component';
import { ApiResponse } from '../../responses/api.response';

@Component({
  selector: 'app-order',
  standalone: true,
  templateUrl: './order.component.html',
  styleUrl: './order.component.scss',
  imports: [
    HeaderComponent,
    FooterComponent,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule
  ]
})
export class OrderComponent extends BaseComponent implements OnInit {
  private formBuilder = inject(FormBuilder);

  cart: Map<number, number> = new Map();
  orderForm: FormGroup;
  cartItems: { sanPham: SanPham, quantity: number }[] = [];
  couponDiscount: number = 0; //số tiền được discount từ coupon
  couponApplied: boolean = false;
  totalAmount: number = 0;
  orderData: OrderDTO = {
    userid: 0,
    fullname: '',
    email: '',
    sodienthoai: '',
    diachi: '',
    ghichu: '',
    tongtien: 0,
    phuongthucthanhtoan: 'cod',
    cartitems: []
  }

  constructor() {
    super();

    this.orderForm = this.formBuilder.group({
      fullname: ['tuong', [Validators.required]],
      email: ['tuong@gmail.com', [Validators.email]],
      sodienthoai: ['090009848', [Validators.required, Validators.minLength(6)]],
      diachi: ['123 le trong tan, phuong 5', [Validators.required, Validators.minLength(5)]],
      ghichu: [''],
      couponCode: [''],
      phuongthucthanhtoan: ['cod']
    });
  }

  ngOnInit(): void {
    debugger
    this.orderData.userid = this.tokenService.getUserId();
    this.cart = this.cartService.getCart();
    const maSanPhamList = Array.from(this.cart.keys()); // Truyền danh sách MASANPHAM từ Map giỏ hàng

    // Gọi service để lấy thông tin sản phẩm dựa trên danh sách MASANPHAM
    debugger
    if (maSanPhamList.length === 0) {
      return;
    }

    this.sanPhamService.getSanPhamByMASANPHAMList(maSanPhamList).subscribe({
      next: (apiResponse: ApiResponse) => {
        const sanPhams: SanPham[] = apiResponse.data.sanPhamResponseList;
        debugger
        this.cartItems = maSanPhamList.map((masanpham) => {
          debugger
          const sanPham = sanPhams.find((p) => p.masanpham === masanpham);
          if (sanPham) {
            sanPham.thumbnail = `${environment.apiBaseUrl}/sanphams/images/${sanPham.thumbnail}`
          }
          return {
            sanPham: sanPham!,
            quantity: this.cart.get(masanpham)!
          };
        });
      },
      complete: () => {
        debugger
        this.calculateTotal();
      },
      error: (error: HttpErrorResponse) => {
        debugger
        console.error(error?.error?.message ?? '')
      }
    })
  }

  placeOrder() {
    debugger
    if (this.orderForm.errors == null) {
      // Gán giá trị từ form vào đối tuọng orderData
      /*
      this.orderData.fullname = this.orderForm.get('fullname')!.value;
      this.orderData.email=this.orderForm.get('email')!.value;
      this.orderData.sodienthoai=this.orderForm.get('sodienthoai')!.value;
      this.orderData.diachi=this.orderForm.get('diachi')!.value;
      this.orderData.ghichu=this.orderForm.get('ghichu')!.value;
      this.orderData.phuongthucthanhtoan=this.orderForm.get('phuongthucthanhtoan')!.value;
      */
      // Sử dụng toán tử spread (...) để sao chép giá trị từ form vào orderData
      this.orderData = {
        ...this.orderData,
        ...this.orderForm.value
      };
      this.orderData.cartitems = this.cartItems.map(cartItem => ({
        masanpham: cartItem.sanPham.masanpham,
        quantity: cartItem.quantity
      }));

      this.orderData.tongtien = this.totalAmount;

      if (this.orderData.phuongthucthanhtoan === 'vnpay') {
        debugger
        const amount = this.orderData.tongtien || 0;

        // Bước 1: gọi API tạo link thanh toán
        this.paymentService.createPaymentUrl({ amount, language: 'vn' })
          .subscribe({
            next: (res: ApiResponse) => {
              // res.data la URL thanh toan
              const paymentUrl = res.data;
              console.log('URL thanh toán: ', paymentUrl);

              // Bước 2: Tách vnp_TxnRef từ URL vừa trả về
              const vnp_TxnRef = new URL(paymentUrl).searchParams.get('vnp_TxnRef') || '';

              // Bước 3: gọi palceOrder kèm theo vnp_TxnRef
              this.donHangService.placeOrder({...this.orderData}).subscribe({
                next: (placeOrderResponse: ApiResponse) => {
                  // Bước 4: Nếu đặt hàng thành công, điều hướng sang trang thanh toán
                  debugger
                  window.location.href = paymentUrl;
                },
                error: (err: HttpErrorResponse) => {
                  debugger
                  this.toastService.showToast({
                    error: err,
                    defaultMsg: 'Lỗi trong quá trình đặt hàng',
                    title: 'Lỗi Đặt Hàng'
                  });

                }
              })
            },
            error: (err: HttpErrorResponse) => {
              this.toastService.showToast({
                error: err,
                defaultMsg: 'Lỗi kết nối đến cổng thanh toán',
                title: 'Lỗi thanh toán',
              });
            }
          })
      }

      this.donHangService.placeOrder(this.orderData).subscribe({
        next: (apiResponse: ApiResponse) => {
          const response = apiResponse.data;
          debugger
          alert('Đặt hàng thành công');
          this.cartService.clearCart();
          this.router.navigate(['/'])
        },
        complete: () => {
          debugger;
          this.calculateTotal();
        },
        error: (error: any) => {
          debugger
          alert(`Lỗi khi đặt hàng: ${error}`);
        }
      });
    } else {
      alert('Dữ liệu không hợp lệ. Vui lòng kiểm tra lại.');
    }
  }


  calculateTotal(): void {
    this.totalAmount = this.cartItems.reduce(
      (total, item) => total + item.sanPham.gia * item.quantity,
      0
    );
  }

  decreaseQuantity(index: number): void {
    if (this.cartItems[index].quantity > 1) {
      this.cartItems[index].quantity--;
      // Cập nhật lại this.cart từ this.cartItems
      this.updateCartFromCartItems();
      this.calculateTotal();
    }
  }

  increaseQuantity(index: number): void {
    this.cartItems[index].quantity++;
    debugger;
    // Cập nhật lại this.cart từ this.cartItems
    this.updateCartFromCartItems();
    this.calculateTotal();
  }

  confirmDelete(index: number): void {
    if (confirm('Bạn có chắc muốn xóa sản phẩm này?')) {
      // Xoa san pham khoi danh sach cartItems
      this.cartItems.splice(index, 1);
      // Cập nhật lại this.cart từ this.cartItems
      this.updateCartFromCartItems();
      // Tinh toan lai tong tien
      this.calculateTotal();
    }

  }

  private updateCartFromCartItems(): void {
    this.cart.clear();
    this.cartItems.forEach((item) => {
      this.cart.set(item.sanPham.masanpham, item.quantity);
    });
    this.cartService.setCart(this.cart);
  }

  applyCoupon(): void {
    // Xử lý áp dụng mã giảm giá
    // cập nhật giá trị totalAmount dựa trên mã giảm giá
    debugger
    const couponCode = this.orderForm.get('couponCode')!.value;
    if (!this.couponApplied && couponCode) {
      this.calculateTotal();
      this.couponService.calculateCouponValue(couponCode, this.totalAmount)
        .subscribe({
          next: (apiResponse: ApiResponse) => {
            this.totalAmount = apiResponse.data;
            this.couponApplied = true;
          }
        });
    }
  }
}
