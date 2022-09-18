//
//  PasswordScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 18.09.2022.
//

import SwiftUI


struct PasswordScreen: View {
   
    @ObservedObject private(set) var authModel: AuthViewModel
    
    @State var data: String = ""
    
    init(authModel: AuthViewModel){
        self.authModel = authModel
    }
    var body: some View {
        NavigationView{
            VStack(alignment: .leading){
                Text("Добро пожаловать в личный кабинет ВСГУТУ")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .padding(.bottom, 10)
                
            

                
                Text("Выполните авторизацию")
                    .font(.title)
                    .fontWeight(.light)
                    .padding(.bottom, 40)
                
                Text("Логин")
                    .font(.title2)
                    .fontWeight(.bold)
                
                TextField("Введите логин", text: $authModel.password)
                    .textFieldStyle(.roundedBorder)
                    .disableAutocorrection(true)
                    .padding(.bottom)
                
                Button(action: {
                    authModel.authorise()
                }, label: {
                    Text("Продолжить")
                        .frame(maxWidth: .infinity)
                        .font(.title)
                    
                })
                .buttonStyle(.bordered)
                .navigationBarTitleDisplayMode(.inline)
                
                
                Spacer()
                
            }
            .padding()
        }
    }
}

struct PasswordScreen_Previews: PreviewProvider {
    static var previews: some View {
        PasswordScreen(authModel: AuthViewModel())
    }
}

