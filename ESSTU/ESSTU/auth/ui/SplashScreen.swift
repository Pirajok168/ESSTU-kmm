//
//  SplashScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 18.09.2022.
//

import SwiftUI
import shared



struct SplashScreen: View {
    @ObservedObject  var authViewModel: AuthViewModel = AuthViewModel()
    @ObservedObject  var authNavigation: AuthNavigation = AuthNavigation()
    
   
   
    
    var body: some View {
        
        if authViewModel.token == nil{
            NavigationStack(path: $authNavigation.path){
                VStack{
                    Image("logo_splash_screen")
                        .navigationDestination(for: AuthDestination.self){
                            destation in
                            
                            if destation == AuthDestination.LoginScreen{
                                LoginScreen()
                                    .environmentObject(authNavigation)
                                    .environmentObject(authViewModel)
                            }else if destation == AuthDestination.PasswordScreen{
                                PasswordScreen()
                                    .environmentObject(authNavigation)
                                    .environmentObject(authViewModel)
                            }
                        }
                    Button(action: {authNavigation.toLoginScreen()}, label: {Text("авторизироваться")})
                }
                .onAppear{
                    authViewModel.onRestoreSession()
                }
                
                
            }
        }else if authViewModel.token?.owner is TokenOwners.Student{
    
            BottomNavigationStudent()
            
        }
    }
}

/// Description
struct SplashScreen_Previews: PreviewProvider {
    static var previews: some View {
        SplashScreen()
    }
}
