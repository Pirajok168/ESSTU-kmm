//
//  CardNews.swift
//  ESSTU
//
//  Created by Данила Еремин on 20.01.2023.
//

import SwiftUI

struct CardNews: View {
    let title: String
    let subTitle: String
    let FIO: String
    let described: String
    let countViewed: Int
    let image: String?
    
    var body: some View {
        VStack(alignment: .leading){
            HStack {
                if(image != nil){
                    Image(image!)
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                        .frame(width: 100, height: 100)
                        .clipShape(RoundedRectangle(cornerRadius: 25.0))
                }
                
                VStack(alignment: .leading) {
                    Text(title)
                        .lineLimit(2)
                        .font(.title3)
                        .fontWeight(.medium)
                        .padding(.bottom, 1)
                        
                    
                        
                    
                    Text(subTitle)
                        .lineLimit(2)
                        .font(.subheadline.weight(.thin))
                        
                        
                }
            }
            .frame( maxWidth: .infinity, maxHeight: 100, alignment: .leading)
            .padding(.bottom, 2)
            
            
            PreviewAuthor(image: image, FIO: FIO, described: described)
            
            
        }
        
           
        
    }
}

struct CardNews_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            CardNews(title: "Вебинары коnмпании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Студент 3 курса группы Б760", countViewed: 456, image: "copybook")
            
            CardNews(title: "Вебинары коnмпании \"Антиплагиат\" в январе", subTitle: "Расписание вебинаров компании в яванаре 2023 года", FIO: "Еремин Данила Александрович", described: "Студент 3 курса группы Б760", countViewed: 456, image: nil)
        }
    }
}
