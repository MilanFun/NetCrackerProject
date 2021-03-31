import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserPageComponent} from "./components/user-page/user-page.component";
import {LearnTabComponent} from "./components/learn-tab/learn-tab.component";
import {TagTreeComponent} from './components/tag-tree/tag-tree.component';
import {TeachTabComponent} from './components/teach-tab/teach-tab.component';
import {OutgoingRequestsComponent} from './components/outgoing-requests/outgoing-requests.component';
import {IncomingRequestsComponent} from './components/incoming-requests/incoming-requests.component';
import {SignInTabComponent} from './components/sign-in-tab/sign-in-tab.component';


const routes: Routes = [
  { path: 'profile', component: UserPageComponent },
  { path: 'learn', component: LearnTabComponent },
  { path: 'tags', component: TagTreeComponent },
  { path: 'teach', component: TeachTabComponent },
  { path: 'incoming-requests', component: IncomingRequestsComponent },
  { path: 'outgoing-requests', component: OutgoingRequestsComponent },
  { path: 'login', component: SignInTabComponent },
  { path: 'registration', component: SignInTabComponent },
  { path: '', redirectTo: '/learn', pathMatch: 'full' },
];

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})

export class AppRoutingModule { }
