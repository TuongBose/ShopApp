import { Injectable } from "@angular/core";
import { SanPhamService } from "./sanpham.service";

@Injectable({
    providedIn: 'root'
})

export class CartService {
    private cart: Map<number, number> = new Map<number, number>();

    constructor(private sanPhamService: SanPhamService) {
        // Lấy dữ liệu giỏ hàng từ localStorage khi khởi tạo service
        this.refreshCart();
    }

    public refreshCart() {
        const storedCart = localStorage.getItem(this.getCartKey());
        if (storedCart) {
            this.cart = new Map(JSON.parse(storedCart));
        }
        else {
            this.cart = new Map<number, number>();
        }
    }

    private getCartKey(): string {
        const accountResponseJSON = localStorage.getItem('account');
        const accountResponse = JSON.parse(accountResponseJSON!);
        debugger;
        return `cart:${accountResponse.userid}`
    }

    addToCart(masanpham: number, quantity: number = 1): void {
        debugger
        if (this.cart.has(masanpham)) {
            // Nếu sản phẩm đã có trong giỏ hàng thì tăng số lượng
            this.cart.set(masanpham, this.cart.get(masanpham)! + quantity);
        } else {
            // Nếu sản phẩm chưa có trong giỏ hàng thì thêm hàng vào giỏ hàng kèm số lượng
            this.cart.set(masanpham, quantity);
        }
        // Sau khi thay đổi giỏ hàng, lưu giỏ hàng lại
        this.saveCartToLocalStorage();
    }

    getCart(): Map<number, number> {
        return this.cart;
    }

    private saveCartToLocalStorage(): void {
        debugger
        localStorage.setItem(this.getCartKey(), JSON.stringify(Array.from(this.cart.entries())));
    }

    setCart(cart: Map<number, number>) {
        this.cart = cart ?? new Map<number, number>();
        this.saveCartToLocalStorage();
    }

    clearCart(): void {
        this.cart.clear();
        this.saveCartToLocalStorage();
    }


}