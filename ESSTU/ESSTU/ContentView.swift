//
//  ContentView.swift
//  ESSTU
//
//  Created by Данила Еремин on 17.09.2022.
//

import SwiftUI
import shared


enum Tab: Int {
    case received = 1
    case sent = 2
    case sennt = 3
    var title: String {
        switch self {
            case .received:
            return "Hello"
            case .sent:
            return "Hello World"
            case .sennt:
            return "Hello, World!"
        }
    }
}

struct ContentView: View {
    @State var selection = Tab.received
    
    var body: some View {
       
        TabView(selection: $selection) {
            Screen1()
                .badge(2)
                .tabItem {
                    Label("Received", systemImage: "tray.and.arrow.down.fill")
                }
                .tag(Tab.received)
                
            Screen2()
                .tabItem {
                    Label("Sent", systemImage: "tray.and.arrow.up.fill")
                }
                .tag(Tab.sent)
            Screen3()
                .tabItem {
                    Label("Sent", systemImage: "tray.and.arrow.up.fill")
                }
                .tag(Tab.sennt)
                
        }.navigationTitle(selection.title)
        
    }
}

enum Test{
    case a
    case b
}

class Model: ObservableObject{
    @Published var test: [Test] = []
}


struct Screen1: View{
    @ObservedObject var model = Model()
    var body: some View{
        NavigationStack(path: $model.test){
            VStack{
                Text("Screen 1")
                
                
                Button{
                    model.test.append(.b)
                } label: {
                    Text("Перейти во вложенный скрин")
                }
            }
            .navigationDestination(for: Test.self){
                i in
                ChildScreen()
            }
        }
    }
}

struct ChildScreen: View {
    var body: some View {
        Text("Вложенный скрин")
    }
}

struct Screen2: View{
    var body: some View{
        Text("Screen 2")
    }
}

struct Screen3: View{
    var body: some View{
        Text("Screen 2")
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
            .previewDevice("iPhone 13")
        
        
        
    }
}
