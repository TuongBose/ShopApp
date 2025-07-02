import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { LoaiSanPham } from "../models/loaisanpham";
import { OrderDTO } from "../dtos/order.dto";
import { DonHang } from "../models/donhang";

@Injectable({
    providedIn:'root'
})

export class DonHangService{
    private apiCreateDonHang = `${environment.apiBaseUrl}/donhangs`;
    constructor(private http: HttpClient){}

    placeOrder(orderData: OrderDTO):Observable<any>{
        return this.http.post(this.apiCreateDonHang, orderData);
    }

    getDonHangById(maDonHang: number){
        return this.http.get(`${environment.apiBaseUrl}/donhangs/${maDonHang}`);
    }
}