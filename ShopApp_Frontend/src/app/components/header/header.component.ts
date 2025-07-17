import { Component, OnInit } from '@angular/core';
import { AccountService } from '../../services/account.service';
import { AccountResponse } from '../../responses/account/account.response';
import { TokenService } from '../../services/token.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
  imports:[
    CommonModule,
    NgbModule,
    RouterModule
  ]
})
export class HeaderComponent implements OnInit {
  account?: AccountResponse | null;
  isPopoverOpen?: boolean;
  activeNavItem: number = 0;

  constructor(
    private accountService: AccountService,
    private tokenService: TokenService,
    private router:Router
  ) { }

  ngOnInit(): void {
    debugger
    this.account = this.accountService.getAccountFromLocalStorage();
  }

  togglePopover(event: Event): void {
    event.preventDefault();
    this.isPopoverOpen = !this.isPopoverOpen;
  }

  handleItemClick(index: number): void {
    if(index ===0){
      debugger
      this.router.navigate(['/user-profile'])
    }else    if (index === 2) {
      this.accountService.removeAccountFromLocalStorage();
      this.tokenService.removeToken()
      this.account = this.accountService.getAccountFromLocalStorage();
    }
    this.isPopoverOpen = false;
  }

  setActiveNavItem(index: number) {
    this.activeNavItem = index;
  }
}
