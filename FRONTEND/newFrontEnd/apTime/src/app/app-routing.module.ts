import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { AuthComponent } from "./auth/auth.component";
import { SecureComponent } from "./secure/secure.component";
import { ProfileComponent } from "./profile/profile.component";
import { HomeComponent } from "./home/home.component";
import { AmplifyAngularModule, AmplifyService } from "aws-amplify-angular";
import { BrowserModule } from "@angular/platform-browser";

const routes: Routes = [
  { path: "", component: HomeComponent, pathMatch: "full" },
  { path: "login", component: AuthComponent, pathMatch: "full" },
  { path: "dashboard", component: SecureComponent, pathMatch: "full" }
];

@NgModule({
  imports: [RouterModule.forRoot(routes), AmplifyAngularModule, BrowserModule],
  exports: [RouterModule]
})
export class AppRoutingModule {}
