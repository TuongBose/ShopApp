import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { DonHangResponse } from "../responses/donhang.response";

@Injectable({
    providedIn: 'root'
})

export class DonHangService {
    private apiGetAllDonHang = `${environment.apiBaseUrl}/donhangs/get-alldonhang-by-keyword`;
    constructor(private http: HttpClient) { }

    getAllDonHang(keyword: string, page: number, limit: number): Observable<DonHangResponse[]> {
        const params = new HttpParams()
            .set('keyword', keyword)
            .set('page', page.toString())
            .set('limit', limit.toString());
        return this.http.get<any>(this.apiGetAllDonHang, { params });
    }

    viewDetails(madonhang: number): Observable<DonHangResponse> {
        return this.http.get<DonHangResponse>(`${environment.apiBaseUrl}/donhangs/${madonhang}`);
    }

    updateStatus(madonhang: number, status: string): Observable<DonHangResponse> {
        return this.http.put<DonHangResponse>(`${environment.apiBaseUrl}/donhangs/status/${madonhang}`, null, {
            params: new HttpParams().set('status', status)
        });
    }
}