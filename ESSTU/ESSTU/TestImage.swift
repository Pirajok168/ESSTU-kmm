//
//  TestImage.swift
//  ESSTU
//
//  Created by Данила Еремин on 23.01.2023.
//

import SwiftUI
struct Profile: Identifiable{
    var id = UUID().uuidString
    var username: String
    var profilePicture: String
}

let profiles = [
    Profile(username: "Danila Eremin", profilePicture: "copybook"),
    Profile(username: "Danila Alex", profilePicture: "logo_esstu")
]
struct TestImage: View {
   
    @Namespace var animateion
    @State var isExpanded: Bool = false
    @State var expandedProfile: Profile?
    @State var loadExpandedContent = false
    var body: some View {
        NavigationView{
            ScrollView(.vertical, showsIndicators: false){
                VStack(spacing: 20){
                    ForEach(profiles){
                        profile in
                        RowView(profile: profile)
                    }
                }
                .padding()
            }
            .navigationTitle("Faw")
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
        .overlay{
            Rectangle()
                .fill(.black)
                .opacity(loadExpandedContent ? 1 : 0)
                .ignoresSafeArea()
        }
        .overlay{
            if let expandedProfile = expandedProfile, isExpanded{
                ExpandedProfile(profile: expandedProfile)
            }
        }
    }
    @ViewBuilder
    func ExpandedProfile(profile: Profile) -> some View{
        VStack{
            GeometryReader{
                proxy in
                let size = proxy.size
                
            
                Image(profile.profilePicture)
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    
                    .frame(width: size.width, height: size.height)
                    .cornerRadius(loadExpandedContent ? 0 : size.height)
                    .onTapGesture {
                        withAnimation(.easeInOut(duration: 0.4)){
                            loadExpandedContent = false
                        }
                        withAnimation(.easeInOut(duration: 0.4).delay(0.05)){
                            isExpanded = false
                        }
                    }
                   
            }
            .matchedGeometryEffect(id: profile.id, in: animateion)
            .frame(height: 300)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .overlay(alignment: .top){
            HStack(spacing: 10){
                Button {
                    withAnimation(.easeInOut(duration: 0.4)){
                        loadExpandedContent = false
                    }
                    withAnimation(.easeInOut(duration: 0.4).delay(0.05)){
                        isExpanded = false
                    }
                } label: {
                    Image(systemName: "arrow.left")
                }
                Spacer()
            }
            .padding()
            .opacity(loadExpandedContent ? 1 : 0)
        }
        .transition(.offset(x: 0, y: 1))
        .onAppear{
            withAnimation(.easeInOut(duration: 0.4)){
                loadExpandedContent  = true
            }
        }
        
    }
    
    @ViewBuilder
    func RowView(profile: Profile) -> some View{
        HStack(spacing: 12){
            VStack{
                if expandedProfile?.id == profile.id && isExpanded{
                    Image(profile.profilePicture)
                        .resizable()
                        .aspectRatio( contentMode: .fill)
                        .frame(width: 45, height: 45)
                        .cornerRadius(25)
                    
                        .opacity(0)
                }else{
                    Image(profile.profilePicture)
                        .resizable()
                        .aspectRatio( contentMode: .fill)
                        .frame(width: 45, height: 45)

                        .matchedGeometryEffect(id: profile.id, in: animateion)
                        .cornerRadius(25)
                }
            }
            .onTapGesture {
                withAnimation(.easeOut(duration: 0.4)){
                    isExpanded = true
                    expandedProfile = profile
                }
            }
            
            Text(profile.username)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
   
}

struct TestImage_Previews: PreviewProvider {
    static var previews: some View {
        TestImage()
    }
}
