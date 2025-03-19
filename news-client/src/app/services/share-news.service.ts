import {ElementRef, inject, Injectable} from '@angular/core';
import {firstValueFrom} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {FormGroup} from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class ShareNewsService {

  private http = inject(HttpClient)

  save(imageFile: ElementRef, form: FormGroup){

    const values = form.value

    console.info("FORM VALUE>>>>>>", values)
    console.info("SEE IMAGE ELEMENT REF>>>>>>", imageFile.nativeElement.files[0])

    const dataForm = new FormData() // // when angular sends formData to server, client will know to send a multiplatform (header)
    dataForm.set('title', values.title)
    dataForm.set('description', values.description)
    dataForm.set('tags', values.tags)
    dataForm.set('imageFile', imageFile.nativeElement.files[0])

    return firstValueFrom(this.http.post<any>('/api/process', dataForm))

  }

  saveObservable(imageFile: ElementRef, form: FormGroup){

    const values = form.value

    console.info("FORM VALUE>>>>>>", values)
    console.info("SEE IMAGE ELEMENT REF>>>>>>", imageFile.nativeElement.files[0])

    const dataForm = new FormData() // // when angular sends formData to server, client will know to send a multiplatform (header)
    dataForm.set('title', values.title)
    dataForm.set('description', values.description)
    dataForm.set('tags', values.tags)
    dataForm.set('imageFile', imageFile.nativeElement.files[0])

    return this.http.post<any>('/api/process', dataForm)

  }

  getTags(minutes:number){
    return this.http.get<any>(`/api/get-tags/${minutes}`)
  }

  getFilteredPosts(tag:string, minutes:number){
    return this.http.get<any>(`/api/posts/${tag}/${minutes}`)
  }

}
