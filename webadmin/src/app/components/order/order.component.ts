import { Component, OnInit } from '@angular/core';
import { DonHangService } from '../../services/donhang.service';
import { DonHangResponse } from '../../responses/donhang.response';

@Component({
  selector: 'app-order',
  standalone: false,
  templateUrl: './order.component.html',
  styleUrl: './order.component.scss'
})
export class OrderComponent implements OnInit {
  donHang:DonHangResponse[]=[];
  currentPage:number=0;
  itemsPerPage:number=12;
  pages:number[]=[];
  totalPages:number=0;
  keyword:string="";
  visiblePages: number[] = [];
  status:string="";

constructor(private donHangService:DonHangService){}

  ngOnInit(): void {
        this.getAllDonHang(this.keyword, this.currentPage, this.itemsPerPage);
  }

  getAllDonHang(keyword: string, page: number, limit: number) {
    debugger
    this.donHangService.getAllDonHang(keyword, page, limit).subscribe({
      next: (response: any) => {
        debugger
        this.donHang = response.donHangResponseList;
        this.totalPages = response.totalPages;
        this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.error('Error fetching don hang: ', error)
      }
    });
  }

  onPageChange(page: number) {
    debugger;
    this.currentPage = page;
    this.getAllDonHang(this.keyword, this.currentPage, this.itemsPerPage);
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

  viewDetails(maDonHang: number) {
    this.donHangService.viewDetails(maDonHang).subscribe({
      next: (response: DonHangResponse) => {
        debugger;
        console.log('Order Details:', response);
        alert(`Chi tiết đơn hàng ${maDonHang}: ${JSON.stringify(response)}`);
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.error('Error fetching don hang: ', error);
      }
    });
  }

  updateStatus(maDonHang: number, newStatus: string) {
    this.donHangService.updateStatus(maDonHang, newStatus).subscribe({
      next: (response: DonHangResponse) => {
        debugger;
        console.log('Status updated:', response);
        this.getAllDonHang(this.keyword, this.currentPage, this.itemsPerPage); // Làm mới danh sách
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.error('Error updating status: ', error);
        alert('Cập nhật trạng thái thất bại: ' + (error.error?.message || error.message));
      }
    });
  }

  onStatusChange(maDonHang: number, event: Event) {
    const target = event.target as HTMLSelectElement;
    if (target && target.value) {
      this.updateStatus(maDonHang, target.value);
    } else {
      console.error('Event target or value is null/undefined');
      alert('Vui lòng chọn trạng thái hợp lệ');
    }
  }
}
