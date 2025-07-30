import { Component, OnInit } from "@angular/core";
import { AccountResponse } from "../../responses/account/account.response";
import { ActivatedRoute, Router } from "@angular/router";
import { CommonModule } from "@angular/common";
import { subscribe } from "diagnostics_channel";

@Component({
  selector: 'app-base',
  standalone: true,
  templateUrl: './base.component.html',
  styleUrl: './base.component.css',
  imports:[
    CommonModule
  ]
})

export class BaseComponent {
    
}