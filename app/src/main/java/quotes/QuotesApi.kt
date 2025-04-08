package quotes

import quotesmodel
import retrofit2.Response
import retrofit2.http.GET

interface QuotesApi {
    @GET("random")
   suspend fun getRandomQuotes(): Response<List<quotesmodel>>
}