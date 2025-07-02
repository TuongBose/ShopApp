import { Component, OnInit } from '@angular/core';
import { SanPham } from '../../models/sanpham';
import { CartService } from '../../services/cart.service';
import { SanPhamService } from '../../services/sanpham.service';
import { error } from 'console';
import { DonHangService } from '../../services/donhang.service';
import { environment } from '../../environments/environment';
import { DonHangResponse } from '../../responses/donhang.response';
import { CTDH } from '../../models/ctdh';

@Component({
  selector: 'app-order-confirm',
  standalone: false,
  templateUrl: './order-confirm.component.html',
  styleUrl: './order-confirm.component.css'
})
export class OrderConfirmComponent implements OnInit {
  donHangResponse: DonHangResponse = {
    madonhang: 0,
    diachi: '',
    userid: 1,
    ghichu: '',
    email: '',
    tongtien: 0,
    sodienthoai: '',
    ngaydathang: new Date(),
    fullname: '',
    trangthai: '',
    phuongthucthanhtoan: '',
    ctdhList: [],
  }

  constructor(private donHangService: DonHangService) { }

  ngOnInit(): void {
    this.getCTDH()
  }

  getCTDH(): void {
    debugger
    const maDonHang = 8;
    this.donHangService.getDonHangById(maDonHang).subscribe({
      next: (response: any) => {
        debugger
        this.donHangResponse.madonhang = response.madonhang;
        this.donHangResponse.userid = response.userid;
        this.donHangResponse.fullname = response.fullname;
        this.donHangResponse.email = response.email;
        this.donHangResponse.sodienthoai = response.sodienthoai;
        this.donHangResponse.diachi = response.diachi;
        this.donHangResponse.ghichu = response.ghichu;
        this.donHangResponse.ngaydathang = response.ngaydathang;
        this.donHangResponse.phuongthucthanhtoan = response.phuongthucthanhtoan;
        this.donHangResponse.trangthai = response.trangthai;
        this.donHangResponse.tongtien = response.tongtien;
        debugger
        this.donHangResponse.ctdhList=response.ctdhList.map((ctdh:CTDH)=>{
          //ctdh.sanPham.thumnail = `${environment.apiBaseUrl}/sanphams/images/${ctdh}` su ly lay thumbnail
          return ctdh;
        });
        
      },
      complete: () => { debugger },
      error: (error: any) => {
        debugger
        console.error('Error fetching detail: ', error);
      }
    })
  }
}
