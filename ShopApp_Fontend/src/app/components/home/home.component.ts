import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import { SanPham } from '../../models/sanpham.model';
import { SanPhamService } from '../../services/sanpham.service';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  sanphams: SanPham[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 10;
  pages: number[] = [];
  totalPages: number = 0;
  visiblePages: number[] = [];

  constructor(private sanPhamService: SanPhamService) { }
  ngOnInit(): void {
    this.getAllSanPham(this.currentPage, this.itemsPerPage);
  }

  getAllSanPham(page: number, limit: number) {
    this.sanPhamService.getAllSanPham(page, limit).subscribe({
      next: (response: any) => {
        debugger
        this.sanphams = response.sanphams;
        this.totalPages = response.totalPages;
        this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.error('Error fetching products: ', error)
      }
    });
  }

  onPageChange(page: number) {
    debugger;
    this.currentPage = page;
    this.getAllSanPham(this.currentPage, this.itemsPerPage);
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
}
