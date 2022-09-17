//
//  ContentView.swift
//  ESSTU
//
//  Created by Данила Еремин on 17.09.2022.
//

import SwiftUI
import shared

struct ContentView: View {
    var body: some View {
        Text(Greeting().greeting())
            .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
