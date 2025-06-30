import { Component } from '@angular/core';
import { SanPham } from '../../models/sanpham';
import { OrderDTO } from '../../dtos/account/order.dto';
import { CartService } from '../../services/cart.service';
import { SanPhamService } from '../../services/sanpham.service';

@Component({
  selector: 'app-order',
  standalone: false,
  templateUrl: './order.component.html',
  styleUrl: './order.component.css'
})
export class OrderComponent {
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
  }

  constructor(
    private cartService: CartService,
    private sanPhamService: SanPhamService,
    private orderService: OrderService
  ) { }

  ngOnInit(): void {
    debugger
    const cart = this.cartService.getCart();
    const maSanPhamList = Array.from(cart.keys()); // Truyền danh sách MASANPHAM từ Map giỏ hàng

    // Gọi service để lấy thông tin sản phẩm dựa trên danh sách MASANPHAM
    debugger
    this.sanPhamService.getSanPhamByMASANPHAMList(maSanPhamList).subscribe({
      next: (sanPhams: SanPham[]) => {
        debugger
        this.cartItems = maSanPhamList.map((masanpham) => {
          debugger
          const sanPham = sanPhams.find((p) => p.masanpham === masanpham);
          if (sanPham) {
            // sanPham.thumnail = xu ly lay thumbnail
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

  placeOrder(){
    this.orderService.placeOrder(this.orderData).subscribe({
      next:(response)=>{
        debugger
        console.log('Đặt hàng thành công');
      },
      complete:()=>{
        debugger;
        this.calculateTotal();
      },
      error:(error:any)=>{
        debugger
        console.error('Lỗi khi đặt hàng: ',error);
      }
    })
  }

  calculateTotal():void{
    this.totalAmount = this.cartItems.reduce(
      (total,item)=>total+item.sanPham.gia*item.quantity,
      0
    );
  }

  applyCoupon():void{
    // Xử lý áp dụng mã giảm giá
    // cập nhật giá trị totalAmount dựa trên mã giảm giá
  }
}
