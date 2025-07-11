import { Component, OnInit } from '@angular/core';
import { SanPham } from '../../models/sanpham';
import { SanPhamService } from '../../services/sanpham.service';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-product',
  standalone: false,
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})
export class ProductComponent implements OnInit {
  sanPhams: SanPham[] = [];
  currentPage: number = 0;
  itemsPerPage: number = 12;
  pages: number[] = [];
  totalPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = "";
  selectedMALOAISANPHAM: number = 0;

  constructor(private sanPhamService: SanPhamService) { }

  ngOnInit(): void {
    this.getAllSanPham(this.keyword, this.selectedMALOAISANPHAM, this.currentPage, this.itemsPerPage);
  }

  getAllSanPham(keyword: string, selectedMALOAISANPHAM: number, page: number, limit: number) {
    debugger
    this.sanPhamService.getAllSanPham(keyword, selectedMALOAISANPHAM, page, limit).subscribe({
      next: (response: any) => {
        debugger
        response.sanPhamResponseList.forEach((sanPham: SanPham) => {
          sanPham.thumbnail_url = `${environment.apiBaseUrl}/sanphams/images/${sanPham.thumbnail}`;
        })
        this.sanPhams = response.sanPhamResponseList;
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
}
