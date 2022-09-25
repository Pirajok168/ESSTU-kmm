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
    
    @ObservedObject var studentNavigation: StudentNavigation = StudentNavigation()
   
    
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
                    Button{
                        authNavigation.toLoginScreen()
                    } label:{
                        Text("To Login")
                    }
                }
                
            }
        }else if authViewModel.token?.owner is TokenOwners.Student{
        
            NavigationStack(path: $studentNavigation.path){
                BottomNavigationStudent()
                    .environmentObject(studentNavigation)
                    .navigationDestination(for: StudentDestination.self){
                        destation in
                        
                    }
                    
            }
        }
    }
}

/// Description
struct SplashScreen_Previews: PreviewProvider {
    static var previews: some View {
        SplashScreen()
    }
}
