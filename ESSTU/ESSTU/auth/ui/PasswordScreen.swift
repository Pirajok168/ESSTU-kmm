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
                
                Text("Пароль")
                    .font(.title2)
                    .fontWeight(.bold)
                
                TextField("Введите пароль", text: $authModel.password)
                    .textFieldStyle(.roundedBorder)
                    .disableAutocorrection(true)
                    .textInputAutocapitalization(.never)
                    .padding(.bottom)
                
                Button(action: {
                    authModel.authorise()
                }, label: {
                    Text("Авторизироваться")
                        .frame(maxWidth: .infinity)
                        .font(.title)
                    
                })
                .alert(authModel.error?.message ?? "Неизвестная ошибка", isPresented: $authModel.isError) {
                           Button("OK", role: .cancel) { }
                       }
                .buttonStyle(.bordered)
                .navigationBarTitle("")
                .navigationBarHidden(true)
                
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

