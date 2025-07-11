import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoaiSanPham } from '../../models/loaisanpham';
import { LoaiSanPhamService } from '../../services/loaisanpham.service';

@Component({
  selector: 'app-category',
  standalone: false,
  templateUrl: './catogory.component.html',
  styleUrl: './catogory.component.css'
})
export class CategoryComponent implements OnInit {

  loaiSanPhams: LoaiSanPham[] = [];
  currentPage: number = 0;
  itemsPerPage: number = 12;
  pages: number[] = [];
  totalPages: number = 0;
  visiblePages: number[] = [];

  constructor(private loaiSanPhamService: LoaiSanPhamService) { }

  ngOnInit(): void {
    this.getAllLoaiSanPham(1,100);
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

  onPageChange(page: number) {
    debugger;
    this.currentPage = page;
    this.getAllLoaiSanPham(this.currentPage, this.itemsPerPage);
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
