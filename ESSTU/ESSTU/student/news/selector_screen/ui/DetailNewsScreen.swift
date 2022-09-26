//
//  DetailNewsScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 26.09.2022.
//

import SwiftUI

struct DetailNewsScreen: View {
    
    @EnvironmentObject var selectViewModel: SelectorViewModel
    
    var body: some View {
        NavigationView{
            ScrollView(.vertical){
                UserPreview(
                    url: selectViewModel.node?.from.photo, abbreviation: selectViewModel.node?.from.initials ?? "", title: selectViewModel.node?.from.fio ?? "", subtitle: selectViewModel.node?.from.summary ?? "")
                Text(selectViewModel.node?.title ?? "")
                    .font(.title)
                
                Text(selectViewModel.node?.message ?? "")
                    .font(.body)
            }
            
            
            
        }
        .navigationTitle(selectViewModel.title)
        
    }
}

struct DetailNewsScreen_Previews: PreviewProvider {
    static var previews: some View {
        DetailNewsScreen()
    }
}
