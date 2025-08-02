import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import { SanPham } from '../../models/sanpham';
import { SanPhamService } from '../../services/sanpham.service';
import { LoaiSanPham } from '../../models/loaisanpham';
import { LoaiSanPhamService } from '../../services/loaisanpham.service';
import { Router } from '@angular/router';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { BaseComponent } from '../base/base.component';
import { ApiResponse } from '../../responses/api.response';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  imports: [
    HeaderComponent,
    FooterComponent,
    CommonModule,
    FormsModule
  ]

})
export class HomeComponent extends BaseComponent implements OnInit {
  sanphams: SanPham[] = [];
  currentPage: number = 0;
  itemsPerPage: number = 12;
  pages: number[] = [];
  totalPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = "";
  selectedMALOAISANPHAM: number = 0;
  loaiSanPhams: LoaiSanPham[] = [];

  constructor() {
    super();
  }

  ngOnInit(): void {
    this.getAllSanPham(this.keyword, this.selectedMALOAISANPHAM, this.currentPage, this.itemsPerPage);
    this.getAllLoaiSanPham(1, 100);
    this.currentPage = Number(localStorage.getItem('currentProductPage')) || 0;
  }

  getAllLoaiSanPham(page: number, limit: number) {
    this.loaiSanPhamService.getAllLoaiSanPham(page, limit).subscribe({
      next: (response: ApiResponse) => {
        debugger
        this.loaiSanPhams = response.data;
      },
      complete: () => { debugger },
      error: (error: HttpErrorResponse) => {
        debugger;
        console.error('Error fetching loaisanpham: ', error)
      }
    })
  }

  getAllSanPham(keyword: string, selectedMALOAISANPHAM: number, page: number, limit: number) {
    debugger
    this.sanPhamService.getAllSanPham(keyword, selectedMALOAISANPHAM, page, limit).subscribe({
      next: (apiresponse: ApiResponse) => {
        debugger
        const response = apiresponse.data;
        response.sanPhamResponseList.forEach((sanPham: SanPham) => {
          sanPham.thumbnail_url = `${environment.apiBaseUrl}/sanphams/images/${sanPham.thumbnail}`;
        })
        this.sanphams = response.sanPhamResponseList;
        this.totalPages = response.tongSoTrang;
        this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
      },
      complete: () => {
        debugger;
      },
      error: (error: HttpErrorResponse) => {
        this.toastService.showToast({
          error: error,
          defaultMsg: 'Lấy dữ liệu sản phẩm không thành công',
          title: 'Lỗi'
        })
      }
    });
  }

  onPageChange(page: number) {
    debugger;
    this.currentPage = page < 0 ? 0 : page;
    this.getAllSanPham(this.keyword, this.selectedMALOAISANPHAM, this.currentPage, this.itemsPerPage);
    localStorage.setItem('currentProductPage',String(this.currentPage));
  }

  searchSanPham() {
    this.currentPage = 0;
    this.itemsPerPage = 12;
    debugger
    this.getAllSanPham(this.keyword, this.selectedMALOAISANPHAM, this.currentPage, this.itemsPerPage);
  }

  onProductClick(maSanPham: number) {
    debugger
    this.router.navigate(['/products', maSanPham]);
  }
}
