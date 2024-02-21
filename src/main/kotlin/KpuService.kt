package proj.internal

import okhttp3.Headers
import okhttp3.OkHttpClient
import proj.internal.pojo.Hhcw
import proj.internal.pojo.Wilayah
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface KpuService {

    companion object {

        val HEADERS = mapOf(
            "User-Agent" to "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:122.0) Gecko/20100101 Firefox/122.0",
            "Accept" to "application/json, text/plain, */*",
            "Accept-Language" to "en,en-US;q=0.5",
            "Origin" to "https://pemilu2024.kpu.go.id",
            "Connection" to "keep-alive",
            "Referer" to "https://pemilu2024.kpu.go.id/",
            "Sec-Fetch-Dest" to "empty",
            "Sec-Fetch-Mode" to "cors",
            "Sec-Fetch-Site" to "same-site",
            "Sec-GPC" to "1"
        )

        private val retrofit by lazy {
            Retrofit.Builder().baseUrl("https://sirekap-obj-data.kpu.go.id")
                .client(
                    OkHttpClient.Builder()
                        .readTimeout(1L, TimeUnit.MINUTES)
                        .addInterceptor {
                            val req = it.request()
                                .newBuilder()
                                .headers(Headers.of(HEADERS))
                                .build()
                            it.proceed(req)
                        }.build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val req by lazy {
            retrofit.create(KpuService::class.java)
        }
    }

    @GET("pemilu/hhcw/ppwp/{kode}.json")
    suspend fun suaraTps(@Path("kode") kode: String): Response<Hhcw>

    @GET("wilayah/pemilu/ppwp/{kode}.json")
    suspend fun wilayah(@Path("kode") kode: String): Response<List<Wilayah>>
}