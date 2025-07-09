import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-category',
  standalone: false,
  templateUrl: './catogory.component.html',
  styleUrl: './catogory.component.css'
})
export class CategoryComponent implements OnInit {
constructor(private router:Router){}

  ngOnInit(): void {
    
  }

}
