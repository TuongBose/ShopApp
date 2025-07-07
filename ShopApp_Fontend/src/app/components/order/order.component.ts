import { Component, OnInit } from '@angular/core';
import { SanPham } from '../../models/sanpham';
import { OrderDTO } from '../../dtos/order.dto';
import { CartService } from '../../services/cart.service';
import { SanPhamService } from '../../services/sanpham.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DonHangService } from '../../services/donhang.service';
import { TokenService } from '../../services/token.service';
import { environment } from '../../environments/environment';
import { Router } from '@angular/router';
import { DonHang } from '../../models/donhang';

@Component({
  selector: 'app-order',
  standalone: false,
  templateUrl: './order.component.html',
  styleUrl: './order.component.scss'
})
export class OrderComponent implements OnInit {
  orderForm: FormGroup;
  cartItems: { sanPham: SanPham, quantity: number }[] = [];
  couponCode: string = '';
  totalAmount: number = 0;
  orderData: OrderDTO = {
    userid: 1,
    fullname: '',
    email: '',
    sodienthoai: '',
    diachi: '',
    ghichu: '',
    tongtien: 0,
    phuongthucthanhtoan: 'cod',
    cartitems: []
  }

  constructor(
    private cartService: CartService,
    private sanPhamService: SanPhamService,
    private donHangService: DonHangService,
    private fb: FormBuilder,
    private tokenService: TokenService,
    private router: Router,
  ) {
    this.orderForm = this.fb.group({
      fullname: ['tuong', [Validators.required]],
      email: ['tuong@gmail.com', [Validators.email]],
      sodienthoai: ['090009848', [Validators.required, Validators.minLength(6)]],
      diachi: ['123 le trong tan, phuong 5', [Validators.required, Validators.minLength(5)]],
      ghichu: [''],
      phuongthucthanhtoan: ['cod']
    });
  }

  ngOnInit(): void {
    debugger
    this.orderData.userid = this.tokenService.getUserId();
    const cart = this.cartService.getCart();
    const maSanPhamList = Array.from(cart.keys()); // Truyền danh sách MASANPHAM từ Map giỏ hàng

    // Gọi service để lấy thông tin sản phẩm dựa trên danh sách MASANPHAM
    debugger
    if (maSanPhamList.length === 0) {
      return;
    }

    this.sanPhamService.getSanPhamByMASANPHAMList(maSanPhamList).subscribe({
      next: (sanPhams: SanPham[]) => {
        debugger
        this.cartItems = maSanPhamList.map((masanpham) => {
          debugger
          const sanPham = sanPhams.find((p) => p.masanpham === masanpham);
          if (sanPham) {
            // sanPham.thumnail = `${environment.apiBaseUrl}/sanphams/images/`xu ly lay thumbnail
          }
          return {
            sanPham: sanPham!,
            quantity: cart.get(masanpham)!
          };
        });
      },
      complete: () => {
        debugger
        this.calculateTotal();
      },
      error: (error: any) => {
        debugger
        console.error('Error fetching chi tiet: ', error)
      }
    })
  }

  placeOrder() {
    debugger
    if (this.orderForm.valid) {
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

      this.donHangService.placeOrder(this.orderData).subscribe({
        next: (response: DonHang) => {
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

  applyCoupon(): void {
    // Xử lý áp dụng mã giảm giá
    // cập nhật giá trị totalAmount dựa trên mã giảm giá
  }
}
