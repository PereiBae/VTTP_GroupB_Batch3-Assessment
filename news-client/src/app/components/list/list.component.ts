import {Component, inject, OnInit} from '@angular/core';
import {News} from '../../models';
import {ShareNewsService} from '../../services/share-news.service';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-list',
  standalone: false,
  templateUrl: './list.component.html',
  styleUrl: './list.component.css'
})
export class ListComponent implements OnInit{

  tag!:string;
  minutes!:number
  news:News[]=[]

  private newsService= inject(ShareNewsService)
  private activatedRoute = inject(ActivatedRoute)
  sub$!: Subscription

  ngOnInit(){
    this.tag = this.activatedRoute.snapshot.params['tag'];
    this.minutes = this.activatedRoute.snapshot.params['minutes'];

    console.log("viewing posts from this tag", this.tag)
    console.log("viewing posts from this tag from these minues ago ", this.minutes)

    this.sub$ = this.newsService.getFilteredPosts(this.tag,this.minutes).subscribe({
      next: results => {
        this.news = results;
        console.log(this.news);
      },
      error: err => {
        console.log(err);
      },
      complete: () => {
        this.sub$.unsubscribe();
      }
    })

  }

}
