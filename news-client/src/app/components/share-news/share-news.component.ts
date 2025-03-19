import {Component, ElementRef, inject, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {ShareNewsService} from '../../services/share-news.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-share-news',
  standalone: false,
  templateUrl: './share-news.component.html',
  styleUrl: './share-news.component.css'
})
export class ShareNewsComponent implements OnInit{

  private fb = inject(FormBuilder)
  private router = inject(Router)
  private newsService = inject(ShareNewsService)

  private sub!: Subscription

  protected form!:FormGroup

  displayTags:string[]=[]

  @ViewChild('imageFile')
  imageFile!:ElementRef

  ngOnInit(){
    this.createForm()
  }

  createForm(){
    this.form = this.fb.group({
      title: this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
      photo: this.fb.control('', [Validators.required]),
      description: this.fb.control<string>('', [Validators.required,Validators.minLength(5)]),
      tags: this.fb.control<string>('')
    })
  }

  addTags(enterTags:string){
    console.info("Adding new tags:", enterTags)
    this.displayTags = enterTags.split(',');
  }

  removeTag(toRemove:string){
    const index = this.displayTags.indexOf(toRemove);
    if (index !== -1) {
      this.displayTags.splice(index, 1);
    }
  }

  processForm(){
    this.newsService.save(this.imageFile, this.form)
      .then(resp => {
        console.log('resp:', resp)
        // this.router.navigate(['/'])
        alert('ALERT! your news id: '+ resp.newsId)
        this.router.navigate(['/'])
      })
      .catch(resp => {
        alert(`ADD ERROR: ${resp.error.message}`)
      })

    // observable method
    this.sub = this.newsService.saveObservable(this.imageFile, this.form).subscribe(
      resp=>{
        console.info('>>> Observable method resp:', resp)
      }
    )

  }



}
