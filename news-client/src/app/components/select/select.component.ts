import {Component, inject, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {Tag} from '../../models';
import {ShareNewsService} from '../../services/share-news.service';

@Component({
  selector: 'app-select',
  standalone: false,
  templateUrl: './select.component.html',
  styleUrl: './select.component.css'
})
export class SelectComponent implements OnInit{

  timeSelected:number = 0;
  tagList:Tag[] = []

  private newsService = inject(ShareNewsService)
  sub$!: Subscription

  ngOnInit(){
    this.timeSelected= 5;
    this.getTagsFromService(5)
  }

  dropdownSelect(select:any) {
    this.timeSelected = select.target.value
    this.getTagsFromService(this.timeSelected)
  }

  getTagsFromService(number:number){
    this.sub$ = this.newsService.getTags(number).subscribe({
      next: (res:any) => {
        this.tagList = res
        console.info(">>> TAGLIST: " + this.tagList)
      },
      error: (res:any) => {
        console.error(">>> Error: " + res)
      },
      complete: ()=> {
        this.sub$.unsubscribe()
      }
    })
  }

}
