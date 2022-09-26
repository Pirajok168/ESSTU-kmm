//
//  DetailNewsScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 26.09.2022.
//

import SwiftUI

struct DetailNewsScreen: View {
    
    @EnvironmentObject var selectViewModel: SelectorViewModel
    @EnvironmentObject var newsNavigation: NewsNavigation
    
    
    var body: some View {
        
        ScrollView(.vertical){
            
            UserPreview(
                url: selectViewModel.node?.from.photo, abbreviation: selectViewModel.node?.from.initials ?? "", title: selectViewModel.node?.from.fio ?? "", subtitle: selectViewModel.node?.from.summary ?? "")
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.horizontal)
            Text(selectViewModel.node?.title ?? "")
                .font(.title)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal)
            
            Text(selectViewModel.node?.message ?? "")
                .font(.body)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.vertical, 5)
                .padding(.horizontal)
        }
        
        
        
        .navigationTitle(selectViewModel.title)
        
        .navigationBarBackButtonHidden(true)
        .toolbar{
            ToolbarItem(placement: .navigationBarLeading){
                Button {
                    newsNavigation.back()
                } label: {
                    Image(systemName: "arrow.left")
                }
                
                
                
            }
        }
        
        
    }
}

struct DetailNewsScreen_Previews: PreviewProvider {
    static var previews: some View {
        DetailNewsScreen()
    }
}
