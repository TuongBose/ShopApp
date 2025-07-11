import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { SanPham } from "../models/sanpham";

@Injectable({
    providedIn: 'root'
})

export class SanPhamService {
    private apiGetAllSanPham = `${environment.apiBaseUrl}/sanphams`;
    constructor(private http: HttpClient) { }

    getAllSanPham(keyword: string, selectedMALOAISANPHAM: number, page: number, limit: number): Observable<SanPham[]> {
        const params = new HttpParams()
            .set('keyword', keyword.toString())
            .set('MALOAISANPHAM', selectedMALOAISANPHAM.toString())
            .set('page', page.toString())
            .set('limit', limit.toString());

        return this.http.get<SanPham[]>(this.apiGetAllSanPham, { params });
    }

    getSanPham(masanpham: number) {
        return this.http.get(`${environment.apiBaseUrl}/sanphams/${masanpham}`);
    }
}