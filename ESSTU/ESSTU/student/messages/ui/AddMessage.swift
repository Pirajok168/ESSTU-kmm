//
//  AddMessage.swift
//  ESSTU
//
//  Created by Данила Еремин on 25.01.2023.
//

import SwiftUI

enum MessageButtons: CaseIterable, Identifiable {
    var id: Self { self }
    case dialog, support, appeal
    
    var title:  String{
        switch self{
        case.dialog:
            return "Новый диалог"
        case.support:
            return "Обратиться в тех. поддержку"
        case.appeal:
            return "Обратиться к подразделению"
        }
    }
    
    var label: String{
        switch self{
        case.dialog:
            return "bubble.left.circle"
        case.support:
            return "exclamationmark.circle"
        case.appeal:
            return "questionmark.circle"
        }
    }
    
}
struct People: Identifiable, Hashable{
    let id: String = UUID().uuidString
    let name: String = "Danila"
    let surname: String = "Eremin"
    let photo: String = "copybook"
}

struct AddMessage: View {
    let peoples = [People(), People(), People(), People(), People(), People(),People()]
    @Binding var isExpandAddMessafe: Bool
    var body: some View {
        NavigationView{
            ScrollView(.vertical, showsIndicators: false){
                ForEach(MessageButtons.allCases){
                    button in
                    Button {
                        
                    } label: {
                        HStack{
                            Image(systemName: button.label)
                                .font(.system(size: 35))
                                .foregroundColor(Color("AccentColor"))
                                .fontWeight(.thin)
                           
                            Text(button.title)
                                .font(.title3)
                                .fontWeight(.regular)
                        }

                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding(.horizontal)
                        .padding(.top)
                        
                    }
                    .buttonStyle(.plain)
                }
                Divider()
                    .padding()
                
                LazyVStack(spacing: 20, content: {
                    ForEach(peoples){
                        people in
                        
                        HStack{
                            Image(people.photo)
                                .resizable()
                                .aspectRatio(contentMode: .fill)
                                .frame(width: 60, height: 60)
                                .clipShape(Circle())
                            
                            VStack(alignment: .leading){
                                Text("\(people.name) \(people.surname)")
                                    .font(.title3)
                                    .fontWeight(.semibold)
                                    .lineLimit(1)
                                
                                
                            }
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding(.leading)
                    }
                })
            }
            .navigationTitle("Новое сообщение")
            .toolbar{
                ToolbarItem(placement: .navigationBarLeading, content: {
                    Button {
                        isExpandAddMessafe = false
                    } label: {
                        Image(systemName: "arrow.backward")
                    }

                   
                })
            }
        }
    }
}


