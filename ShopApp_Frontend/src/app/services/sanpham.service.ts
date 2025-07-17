import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { SanPham } from "../models/sanpham";
import { insertSanPhamDTO } from "../dtos/insertsanpham.dto";

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

    getSanPhamByMASANPHAMList(MASANPHAMList: number[]): Observable<SanPham[]> {
        // Chuyển danh sách MASANPHAM thành một chuỗi và truyền vào params
        debugger
        const params = new HttpParams().set('ids', MASANPHAMList.join(','));
        return this.http.get<SanPham[]>(`${this.apiGetAllSanPham}/by-ids`,{params});
    }

    deleteSanPham(masanpham: number): Observable<string>{
        debugger
        return this.http.delete<string>( `${this.apiGetAllSanPham}/${masanpham}`)
    }

    insertSanPham(insertSanPhamDTO: insertSanPhamDTO):Observable<any>{
        return this.http.post(`${this.apiGetAllSanPham}`,insertSanPhamDTO);
    }

    // uploadImages(masanpham: number, files:File[]):Observable<any>{
    //     const formData=new FormData();
    //     for(let i = 0;i<files.length;i++    )
    // }
}