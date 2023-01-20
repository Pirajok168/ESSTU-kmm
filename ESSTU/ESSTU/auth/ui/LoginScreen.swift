//
//  AuthScreenPattern.swift
//  ESSTU
//
//  Created by Данила Еремин on 17.09.2022.
//

import SwiftUI
import shared

struct LoginScreen: View {
    @EnvironmentObject  var authViewModel: AuthViewModel
    @EnvironmentObject  var authNavigation: AuthNavigation
    
   
    
    var body: some View {
       
        VStack(alignment: .leading, spacing: 30){
            Text("Добро пожаловать в личный кабинет ВСГУТУ")
                .font(.largeTitle.bold())
                .frame(maxWidth: .infinity)
                .multilineTextAlignment(.center)
                
            
            Text("Пожалуйста выполните авторизацию")
                .font(.title3)
                .fontWeight(.light)
                .frame(maxWidth: .infinity)
                .multilineTextAlignment(.center)
                
        
            
            TextField("Логин", text: $authViewModel.login)
                .textFieldStyle(.roundedBorder)
                .disableAutocorrection(true)
                .textInputAutocapitalization(.never)
            
            
            TextField("Пароль", text: $authViewModel.login)
                .textFieldStyle(.roundedBorder)
                .disableAutocorrection(true)
                .textInputAutocapitalization(.never)
                
            
            
            
            Button(action: {
                authNavigation.toPasswordScreen()
            }, label: {
                Text("Продолжить")
                    .frame(maxWidth: .infinity)
                    .font(.title)
                
            })
            .buttonStyle(.borderedProminent)
            
            Spacer()
            
        }
        .navigationBarBackButtonHidden(true)
        .padding()
        .ignoresSafeArea(.keyboard)
       
            
    }
       
}

struct AuthScreenPattern_Previews: PreviewProvider {
    static var previews: some View {
        SplashScreen(sdkESSTU: ESSTUSdk())
    }
}

