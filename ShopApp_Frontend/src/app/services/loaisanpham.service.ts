import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { LoaiSanPham } from "../models/loaisanpham";
import { ApiResponse } from "../responses/api.response";

@Injectable({
    providedIn:'root'
})

export class LoaiSanPhamService{
    private apiGetAllLoaiSanPham = `${environment.apiBaseUrl}/loaisanphams`;
    constructor(private http: HttpClient){}

    getAllLoaiSanPham(page:number, limit:number):Observable<ApiResponse>{
        const params= new HttpParams()
        .set('page',page.toString())
        .set('limit',limit.toString());

        return this.http.get<ApiResponse>(this.apiGetAllLoaiSanPham,{params});
    }
}