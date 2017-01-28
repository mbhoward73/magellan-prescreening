class PreScreeningGroovy {

    def run(int reviewDetailsPerPage, def reviewsList) {
        def groupedByFeatures = reviewsList.inject([:]) { resultMap, review ->
            def existingList = resultMap[review.feature] ?: []
            resultMap[review.feature] = existingList << review
            resultMap
        }

        def groupedAndPaged = groupedByFeatures.inject([]) { resultList, featureKey, featureList ->
            def collatedList = featureList.collate(reviewDetailsPerPage)
            collatedList.eachWithIndex { page, pageNumber ->
                resultList << [feature:featureKey, page:pageNumber, totalRecords:featureList.size, reviews: page]
            }
            resultList
        }

        groupedAndPaged
    }

}
