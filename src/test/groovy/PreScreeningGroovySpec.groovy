import spock.lang.Specification


class PreScreeningGroovySpec extends Specification {

    PreScreeningGroovy preScreeningGroovy

    def setup() {
        preScreeningGroovy = new PreScreeningGroovy()
    }

    def "provided test case - one review per page"() {
        given:
        def feature1 = 'can use this product'
        def feature2 = 'can not use this product'
        def reviewOne = [reviewId:'2874274',feature:feature1]
        def reviewTwo = [reviewId:'3535533',feature:feature1]
        def reviewThree = [reviewId:'5839589',feature:feature2]
        def reviewsListInput = [reviewOne,reviewTwo,reviewThree]
        def groupedAndPagedExpectedOutput = [
                [feature:feature1, page:0, totalRecords:2, reviews:[reviewOne]],
                [feature:feature1, page:1, totalRecords:2, reviews:[reviewTwo]],
                [feature:feature2, page:0, totalRecords:1, reviews:[reviewThree]]
        ]

        when:
        def groupedAndPagedList = preScreeningGroovy.run(1, reviewsListInput)

        then:
        groupedAndPagedList == groupedAndPagedExpectedOutput
    }

    def "two reviews per page with equal size pages"() {
        given:
        def feature1 = 'can use this product'
        def feature2 = 'can not use this product'
        def reviewOne = [reviewId:'2874274',feature:feature1]
        def reviewTwo = [reviewId:'3535533',feature:feature1]
        def reviewThree = [reviewId:'5839589',feature:feature2]
        def reviewsListInput = [reviewOne,reviewTwo,reviewThree]
        def groupedAndPagedExpectedOutput = [
                [feature:feature1, page:0, totalRecords:2, reviews:[reviewOne, reviewTwo]],
                [feature:feature2, page:0, totalRecords:1, reviews:[reviewThree]]
        ]

        when:
        def groupedAndPagedList = preScreeningGroovy.run(2, reviewsListInput)

        then:
        groupedAndPagedList == groupedAndPagedExpectedOutput
    }

    def "two reviews per page with unequal size pages"() {
        given:
        def feature1 = 'can use this product'
        def feature2 = 'can not use this product'
        def reviewOne = [reviewId:'2874274',feature:feature1]
        def reviewTwo = [reviewId:'3535533',feature:feature1]
        def reviewThree = [reviewId:'5839589',feature:feature2]
        def reviewFour = [reviewId:'44444',feature:feature1]
        def reviewsListInput = [reviewOne,reviewTwo,reviewThree, reviewFour]
        def groupedAndPagedExpectedOutput = [
                [feature:feature1, page:0, totalRecords:3, reviews:[reviewOne, reviewTwo]],
                [feature:feature1, page:1, totalRecords:3, reviews:[reviewFour]],
                [feature:feature2, page:0, totalRecords:1, reviews:[reviewThree]]
        ]

        when:
        def groupedAndPagedList = preScreeningGroovy.run(2, reviewsListInput)

        then:
        groupedAndPagedList == groupedAndPagedExpectedOutput
    }

    def "multiple reviews per page for multiple features"() {
        given:
        def feature1 = 'can use this product'
        def feature2 = 'can not use this product'
        def reviewOne = [reviewId:'2874274',feature:feature1]
        def reviewTwo = [reviewId:'3535533',feature:feature1]
        def reviewThree = [reviewId:'5839589',feature:feature2]
        def reviewFour = [reviewId:'44444',feature:feature1]
        def reviewFive = [reviewId:'33333',feature:feature2]
        def reviewsListInput = [reviewOne,reviewTwo,reviewThree, reviewFour, reviewFive]
        def groupedAndPagedExpectedOutput = [
                [feature:feature1, page:0, totalRecords:3, reviews:[reviewOne, reviewTwo]],
                [feature:feature1, page:1, totalRecords:3, reviews:[reviewFour]],
                [feature:feature2, page:0, totalRecords:2, reviews:[reviewThree, reviewFive]]
        ]

        when:
        def groupedAndPagedList = preScreeningGroovy.run(2, reviewsListInput)

        then:
        groupedAndPagedList == groupedAndPagedExpectedOutput
    }

    def "empty reviewList"() {
        when:
        def groupedAndPagedList = preScreeningGroovy.run(1, [])

        then:
        groupedAndPagedList == []
    }

    //seems strange but this is consistent with what java program does
    def "verify that review without feature key is included"() {
        given:
        def feature1 = 'can use this product'
        def feature2 = 'can not use this product'
        def reviewOne = [reviewId:'2874274',feature:feature1]
        def reviewTwo = [reviewId:'3535533']
        def reviewThree = [reviewId:'5839589',feature:feature2]
        def reviewsListInput = [reviewOne,reviewTwo,reviewThree]
        def groupedAndPagedExpectedOutput = [
                [feature:feature1, page:0, totalRecords:1, reviews:[reviewOne]],
                [feature:null, page:0, totalRecords:1, reviews:[reviewTwo]],
                [feature:feature2, page:0, totalRecords:1, reviews:[reviewThree]]
        ]

        when:
        def groupedAndPagedList = preScreeningGroovy.run(1, reviewsListInput)

        then:
        groupedAndPagedList == groupedAndPagedExpectedOutput
    }



}