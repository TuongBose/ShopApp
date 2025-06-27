import { Component, OnInit } from '@angular/core';
import { SanPham } from '../../models/sanpham';
import { SanPhamService } from '../../services/sanpham.service';
import { LoaiSanPhamService } from '../../services/loaisanpham.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-detail-product',
  standalone: false,
  templateUrl: './detail-product.component.html',
  styleUrl: './detail-product.component.css'
})
export class DetailProductComponent implements OnInit {
  sanPham?: SanPham;
  maSanPham: number = 0;
  currentImageIndex: number = 0;

  constructor(
    private sanPhamService: SanPhamService,
    // private loaiSanPhamService: LoaiSanPhamService,
    // private router: Router,
    // private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit(): void {
    // const idParam = this.activatedRoute.snapshot.paramMap.get('id');
    debugger
    const idParam=5;
    if(idParam!==null){
      this.maSanPham=+idParam;
    }
    if(!isNaN(this.maSanPham)){
      this.sanPhamService.getSanPham(this.maSanPham).subscribe({
        next:(response: any)=>{
          debugger
          this.sanPham=response
          this.showImage(0);
        },
        complete:()=>{debugger},
        error:(error:any)=>{
          debugger;
          console.error('Error fetching detail: ',error);
        }
      });
    }
    else{
      console.error('Invalid masanpham: ',idParam)
    }
  }

  showImage(index:number):void{
    debugger
    if(this.sanPham && this.sanPham.hinhanh && this.sanPham.hinhanh.length>0){
      if(index<0){
        index=0;
      }else if(index>= this.sanPham.hinhanh.length){
        index=this.sanPham.hinhanh.length-1;
      }

      //Gán index hiện tại và cập nhật ảnh hiển thị
      this.currentImageIndex=index;
    }
  }

  thumbnailClick(index:number){
    debugger
    this.currentImageIndex=index;
  }

  nextImage():void{
    debugger
    this.showImage(this.currentImageIndex+1);
  }

  previousImage():void{
    debugger
    this.showImage(this.currentImageIndex-1)
  }
}
