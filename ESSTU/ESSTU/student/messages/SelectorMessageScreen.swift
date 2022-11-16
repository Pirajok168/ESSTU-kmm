//
//  MessagesScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 25.09.2022.
//

import SwiftUI



enum TypeMessages: String, CaseIterable, Identifiable{
    case dialogs, discussions, support, appeals
    var title: String{
        switch self{
        case.dialogs:
            return "Диалоги"
        case.discussions:
            return "Обсуждения"
        case.support:
            return "Тех. поддержка"
        case.appeals:
            return "Обращения"
        }
    }
    var id: Self { self }
}

extension Color {
    static let ui = Color.UI()
    
    struct UI {
         let yellow = Color("AccentColor")
    }
}

struct Contact : Identifiable, Hashable {
    var id = UUID()
    var firstName: String
    var lastName: String
    var telephone: String
}

class ContactsModel : ObservableObject {
    //MARK:- Variables
    var contactData : [Contact]
    @Published var sectionDictionary : Dictionary<String , [Contact]>
    
    @Published  var offsetChange: CGFloat = 0
    func test(offsetChange: CGFloat){
        self.offsetChange = offsetChange
    }
    
    //MARK:- initializer
    init() {
        contactData = [Contact(firstName: "Jacob", lastName: "Smith", telephone: ""),
                       Contact(firstName: "alexandra", lastName: "Johnson", telephone: ""),
                       Contact(firstName: "daniel", lastName: "Williams", telephone: ""),
                       Contact(firstName: "Angel", lastName: "Brown", telephone: ""),
                       Contact(firstName: "David", lastName: "Jones", telephone: ""),
                       Contact(firstName: "Michael", lastName: "Miller", telephone: ""),
                       Contact(firstName: "Jose", lastName: "Garcia", telephone: ""),
                       Contact(firstName: "Joseph", lastName: "Davis", telephone: ""),
                       Contact(firstName: "christopher", lastName: "Martin", telephone: ""),
                       Contact(firstName: "Gabriel", lastName: "Wilson", telephone: ""),
                       Contact(firstName: "Anthony", lastName: "Brown", telephone: "")]
        sectionDictionary = [:]
        sectionDictionary = getSectionedDictionary()
    }
    func getSectionedDictionary() -> Dictionary <String , [Contact]> {
            let sectionDictionary: Dictionary<String, [Contact]> = {
                return Dictionary(grouping: contactData, by: {
                    let name = $0.firstName
                    let normalizedName = name.folding(options: [.diacriticInsensitive, .caseInsensitive], locale: .current)
                    let firstChar = String(normalizedName.first!).uppercased()
                    return firstChar
                })
            }()
            return sectionDictionary
        }
}

struct SelectorMessageScreen: View {
    @State private var selectedTab: TypeMessages = .dialogs
    @State private var isPresented: Bool = false
    @State private var searchValue: String = ""
    
    @State private var isScrollDown = false
    
    @Namespace var namespace
    

    @StateObject var contactsModel = ContactsModel()
    
   
    
    var body: some View {
        NavigationStack{
            ScrollView{
                VStack{
                    switch(selectedTab) {
                    case .dialogs: MessagesScreen()
                        
                    case .discussions: MessagesScreen()
                    case .support: MessagesScreen()
                    default:
                        Text("2")
                    }
                }
                .background(GeometryReader {
                                return Color.clear.preference(key: ViewOffsetKey.self,
                                                              value: -$0.frame(in: .named("scroll")).origin.y)
                            })
                .onPreferenceChange(ViewOffsetKey.self) { offset in
                    
                    contactsModel.offsetChange = offset
                }
               
            }
            .coordinateSpace(name: "scroll")
           
            .safeAreaInset(edge: .top, content: {
                Color.clear.frame(height: 80)
            })
            .overlay{
                NavBar(conent: {
                    VStack{
                        ZStack{
                            Text("Мессенджер")
                                .frame(maxWidth: .infinity, alignment: .center)
                                .font(.title2.bold())
                            
                            Button(action: {
                                isPresented = true
                        
                            }, label: {
                                Image(systemName: "plus.message")
                                    
                            })
                            .frame(maxWidth: .infinity, alignment: .trailing)
                            .padding(.trailing)
                            .foregroundColor(Color.ui.yellow)
                            
                            
                        }
                        
                        
                        Text("\(contactsModel.offsetChange)")
                        
                        
                        
                       

                        ScrollView(.horizontal, showsIndicators: false){
        
                            HStack(){
                                ForEach(TypeMessages.allCases, id: \TypeMessages.self){
                                    index in
        
                                    VStack(){
                                        Button(action: {
                                            withAnimation{
                                                selectedTab = index.id
                                            }
                                        }){
                                            Text(index.title)
                                        }
                                        .frame(maxHeight: 50, alignment: .top)
                                        
                                        
                                        
                                        
                                        if selectedTab == index{
                                            Rectangle()
                                                .frame(height: 2)
                                                .matchedGeometryEffect(id: "title", in: namespace, isSource: true)
                                                .foregroundColor(Color.ui.yellow)
                                        }
                                      
                                    }
                                    .frame(maxHeight: 50, alignment: .bottom)
                                    
                                    .padding(.horizontal)
                                }
                                .buttonStyle(.plain)
                            }
                        }
                        
                    }
                })
                
                
                
                
                
                
            }
            .sheet(isPresented: $isPresented, content: {
                NavigationStack{
                    
                    
                    List(contactsModel.sectionDictionary.keys.sorted(), id: \.self){
                        key in
                        if let contacts = contactsModel.sectionDictionary[key]!.filter({ (contact) -> Bool in
                            self.searchValue.isEmpty ? true : contact.firstName.lowercased().contains( self.searchValue.lowercased())
                        }), !contacts.isEmpty
                        {
                            Section(header: Text("\(key)")) {
                               ForEach(contacts){ value in
                                   Text("\(value.firstName) \(value.lastName)")
                               }
                           }
                            
                        }
                    }
                        .navigationTitle("Написать сообщение")
                       
                        .navigationBarTitleDisplayMode(.inline)
                    
                        .searchable(text: $searchValue)
                        .toolbar{
                            ToolbarItem(placement: .navigationBarLeading, content: {
                                Button(action: {
                                    
                                }, label: {
                                    Text("Отмена")
                                })
                            })
                        }
                }
            })
            
            
    
           
        }
        
        
        
        
    }
    
   
    
}

struct SelectorMessageScreen_Previews: PreviewProvider {
    static var previews: some View {
        SelectorMessageScreen()
            
            
    }
}
