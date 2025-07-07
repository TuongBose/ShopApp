import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import { SanPham } from '../../models/sanpham';
import { SanPhamService } from '../../services/sanpham.service';
import { LoaiSanPham } from '../../models/loaisanpham';
import { LoaiSanPhamService } from '../../services/loaisanpham.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  sanphams: SanPham[] = [];
  currentPage: number = 0;
  itemsPerPage: number = 12;
  pages: number[] = [];
  totalPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = "";
  selectedMALOAISANPHAM: number = 0;
  loaiSanPhams: LoaiSanPham[] = [];

  constructor(
    private sanPhamService: SanPhamService,
    private loaiSanPhamService: LoaiSanPhamService,
    private router: Router) { }
    
  ngOnInit(): void {
    this.getAllSanPham(this.keyword, this.selectedMALOAISANPHAM, this.currentPage, this.itemsPerPage);
    this.getAllLoaiSanPham(1, 100);
  }

  getAllLoaiSanPham(page: number, limit: number) {
    this.loaiSanPhamService.getAllLoaiSanPham(page, limit).subscribe({
      next: (loaiSanPhams: LoaiSanPham[]) => {
        debugger
        this.loaiSanPhams = loaiSanPhams;
      },
      complete: () => { debugger },
      error: (error: any) => {
        debugger;
        console.error('Error fetching loaisanpham: ', error)
      }
    })
  }

  getAllSanPham(keyword: string, selectedMALOAISANPHAM: number, page: number, limit: number) {
    debugger
    this.sanPhamService.getAllSanPham(keyword, selectedMALOAISANPHAM, page, limit).subscribe({
      next: (response: any) => {
        debugger
        this.sanphams = response.sanPhamResponseList;
        this.totalPages = response.tongSoTrang;
        this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.error('Error fetching sanpham: ', error)
      }
    });
  }

  onPageChange(page: number) {
    debugger;
    this.currentPage = page;
    this.getAllSanPham(this.keyword, this.selectedMALOAISANPHAM, this.currentPage, this.itemsPerPage);
  }

  generateVisiblePageArray(currentPage: number, totalPages: number): number[] {
    const maxVisiblePages = 5;
    const halfVisiblePages = Math.floor(maxVisiblePages / 2);

    let startPage = Math.max(currentPage - halfVisiblePages, 1);
    let endPage = Math.min(startPage + maxVisiblePages - 1, totalPages);

    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(endPage - maxVisiblePages + 1, 1);
    }

    return new Array(endPage - startPage + 1).fill(0).map((_, index) => startPage + index);
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
