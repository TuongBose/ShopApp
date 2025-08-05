import { EventEmitter, Injectable } from "@angular/core";
import { SanPhamService } from "./sanpham.service";
import { BehaviorSubject } from "rxjs";

@Injectable({
    providedIn: 'root'
})

export class CartService {
    private cart: Map<number, number> = new Map<number, number>();
    private cartSubject = new BehaviorSubject<Map<number, number>>(new Map());
    private cartItemCountSubject = new BehaviorSubject<number>(0);
    public cartChanged = new EventEmitter<void>();

    public cart$ = this.cartSubject.asObservable();
    public cartItemCount$ = this.cartItemCountSubject.asObservable();

    constructor() {
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
        this.updateCartItemCount();
    }

    private getCartKey(): string {
        debugger
        const accountResponseJSON = localStorage.getItem('account') || sessionStorage.getItem('account');

        if (!accountResponseJSON) {
            console.warn('Chưa đăng nhập, không thể tạo key giỏ hàng.');
            return 'cart:anonymous';
        }

        try {
            const accountResponse = JSON.parse(accountResponseJSON);
            if (!accountResponse || typeof accountResponse.userid !== 'number') {
                console.warn('Dữ liệu tài khoản không hợp lệ.');
                return 'cart:anonymous'; // hoặc throw new Error(...)
            }

            return `cart:${accountResponse.userid}`;
        } catch (err) {
            console.error('Lỗi khi parse account từ localStorage:', err);
            return 'cart:anonymous';
        }
    }

    addToCart(masanpham: number, quantity: number = 1): void {
        debugger
        if (quantity <= 0) {
            console.warn('Số lượng phải lớn hơn 0.');
            return;
        }
        if (this.cart.has(masanpham)) {
            // Nếu sản phẩm đã có trong giỏ hàng thì tăng số lượng
            this.cart.set(masanpham, this.cart.get(masanpham)! + quantity);
        } else {
            // Nếu sản phẩm chưa có trong giỏ hàng thì thêm hàng vào giỏ hàng kèm số lượng
            this.cart.set(masanpham, quantity);
        }
        // Sau khi thay đổi giỏ hàng, lưu giỏ hàng lại
        this.saveCartToLocalStorage();
        this.updateCartItemCount();
        this.cartChanged.emit();
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
        this.cartChanged.emit();
    }

    clearCart(): void {
        this.cart.clear();
        this.saveCartToLocalStorage();
        this.updateCartItemCount();
        this.cartChanged.emit();
    }

    getCartItemCount(): number {
        return Array.from(this.cart.values()).reduce((total, qty) => total + qty, 0);
    }

    removeItem(masanpham: number): void {
        this.cart.delete(masanpham);
        this.saveCartToLocalStorage();
        this.cartChanged.emit();
    }

    private updateCartItemCount(): void {
        this.cartSubject.next(new Map(this.cart));
        const count = this.getCartItemCount();
        this.cartItemCountSubject.next(count);
    }

    public forceRefreshCart(): void {
    this.refreshCart();
    this.cartChanged.emit();
  }
}