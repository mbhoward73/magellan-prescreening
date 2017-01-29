class PreScreeningGroovy {

    def run(int reviewDetailsPerPage, def reviewsList) {
        def groupedByFeatures = reviewsList.inject([:]) { resultMap, review ->
            resultMap[review.feature] = (resultMap[review.feature] ?: []) << review
            resultMap
        }

        groupedByFeatures.inject([]) { resultList, featureKey, featureList ->
            featureList.collate(reviewDetailsPerPage).eachWithIndex { page, pageNumber ->
                resultList << [feature:featureKey, page:pageNumber, totalRecords:featureList.size, reviews: page]
            }
            resultList
        }
    }

}
