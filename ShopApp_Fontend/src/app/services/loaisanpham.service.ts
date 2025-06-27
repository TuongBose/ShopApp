import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { LoaiSanPham } from "../models/loaisanpham";

@Injectable({
    providedIn:'root'
})

export class LoaiSanPhamService{
    private apiGetAllLoaiSanPham = `${environment.apiBaseUrl}/loaisanphams`;
    constructor(private http: HttpClient){}

    getAllLoaiSanPham(page:number, limit:number):Observable<LoaiSanPham[]>{
        const params= new HttpParams()
        .set('page',page.toString())
        .set('limit',limit.toString());

        return this.http.get<LoaiSanPham[]>(this.apiGetAllLoaiSanPham,{params});
    }
}