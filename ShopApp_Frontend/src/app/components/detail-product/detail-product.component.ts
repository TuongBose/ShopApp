import { Component, OnInit } from '@angular/core';
import { SanPham } from '../../models/sanpham';
import { SanPhamService } from '../../services/sanpham.service';
import { LoaiSanPhamService } from '../../services/loaisanpham.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HinhAnh } from '../../models/hinhanh.image';
import { environment } from '../../environments/environment';
import { CartService } from '../../services/cart.service';
import { AccountResponse } from '../../responses/account/account.response';
import { AccountService } from '../../services/account.service';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-detail-product',
  standalone: true,
  templateUrl: './detail-product.component.html',
  styleUrl: './detail-product.component.css',
  imports:[
    CommonModule,
    HeaderComponent,
    FooterComponent,
    NgbModule
  ]
})
export class DetailProductComponent implements OnInit {
  sanPham?: SanPham;
  maSanPham: number = 0;
  currentImageIndex: number = 0;
  quantity: number = 1;
  account?: AccountResponse | null;
  isPressAddToCart:boolean=false;

  constructor(
    private sanPhamService: SanPhamService,
    private cartService: CartService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private accountService: AccountService
  ) { }

  ngOnInit(): void {
    debugger
    this.activatedRoute.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam !== null) {
        this.maSanPham = +idParam; // Chuyển đổi sang số
      } else {
        console.error('maSanPham không được tìm thấy trong route');
        this.router.navigate(['/']); // Điều hướng về trang chủ nếu không có tham số
        return;
      }

      if (!isNaN(this.maSanPham)) {
        this.sanPhamService.getSanPham(this.maSanPham).subscribe({
          next: (response: any) => {
            debugger
            if (response.hinhAnhUrls && response.hinhAnhUrls.length > 0) {
              response.hinhAnhUrls.forEach((product_image: HinhAnh) => {
                product_image.image_url = `${environment.apiBaseUrl}/sanphams/images/${product_image.tenhinhanh}`;
              });
            }
            this.sanPham = response
            this.showImage(0);
          },
          complete: () => { debugger },
          error: (error: any) => {
            debugger;
            console.error('Error fetching detail: ', error);
          }
        });
      }
      else {
        console.error('Invalid masanpham: ', idParam)
      }
    })
  }

  showImage(index: number): void {
    debugger
    if (this.sanPham && this.sanPham.hinhAnhUrls && this.sanPham.hinhAnhUrls.length > 0) {
      if (index < 0) {
        index = 0;
      } else if (index >= this.sanPham.hinhAnhUrls.length) {
        index = this.sanPham.hinhAnhUrls.length - 1;
      }

      //Gán index hiện tại và cập nhật ảnh hiển thị
      this.currentImageIndex = index;
    }
  }

  thumbnailClick(index: number) {
    debugger
    this.currentImageIndex = index;
  }

  nextImage(): void {
    debugger
    this.showImage(this.currentImageIndex + 1);
  }

  previousImage(): void {
    debugger
    this.showImage(this.currentImageIndex - 1)
  }

  addToCart(): void {
    debugger
    this.isPressAddToCart=true;
    if (this.sanPham) {
      this.cartService.addToCart(this.sanPham.masanpham, this.quantity);
    }
    else {
      console.error("Không thể thêm sản phẩm vào giỏ hàng vì San Phẩm là Null.");
    }
  }

  increaseQuantity(): void {
    debugger;
    this.quantity++;
  }

  decreaseQuantity(): void {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  buyNow(): void {
    this.account = this.accountService.getAccountFromLocalStorage();
    if (this.account == null) {
      this.router.navigate(['/login']);
    }
    else {
      if(this.isPressAddToCart==false){
        this.addToCart();
      }
      this.router.navigate(['/orders']);
    }
  }

  getTotalPrice(): number {
    if (this.sanPham) {
      return this.sanPham.gia * this.quantity;
    }
    return 0;
  }
}
