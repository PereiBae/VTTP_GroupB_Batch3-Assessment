import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ShareNewsComponent } from './components/share-news/share-news.component';
import {ReactiveFormsModule} from "@angular/forms";
import {RouterModule, Routes} from '@angular/router';
import { SelectComponent } from './components/select/select.component';
import { ListComponent } from './components/list/list.component';
import {provideHttpClient} from '@angular/common/http';

const appRoutes: Routes = [
  {path: '', component: SelectComponent},
  {path:'post', component:ShareNewsComponent},
  {path: 'list/:tag/:minutes', component: ListComponent},
  {path: '**', redirectTo:'/', pathMatch:'full'},
]

@NgModule({
  declarations: [
    AppComponent,
    ShareNewsComponent,
    SelectComponent,
    ListComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        ReactiveFormsModule,
        RouterModule.forRoot(appRoutes, { useHash: true })
    ],
  providers: [provideHttpClient()],
  bootstrap: [AppComponent]
})
export class AppModule { }
