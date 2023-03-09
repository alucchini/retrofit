import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.io.Serializable

suspend fun main() {
  val url = "https://is-vegan.netlify.com/.netlify/functions/"
  val retrofitBuilder = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(url)
    .build()
    .create(VeganAPI::class.java)
  val retrofitData = retrofitBuilder.getData(setURL())

  retrofitData.enqueue(object : Callback<VeganData> {
    override fun onResponse(
      call: Call<VeganData>,
      response: Response<VeganData>
    ) {
      val responseBody = response?.body()
      if (responseBody != null) {
        println(responseBody)
      }else println("Error responseBody == null")
    }

    override fun onFailure(call: Call<VeganData>, t: Throwable) {
      println("onFailure: ${t.message}")
    }
  })
}

fun setURL(): String{
  val chars = listOf("cherry")
  return ("api?ingredients=" + chars.joinToString(","))
}

interface VeganAPI {
  @GET
  fun getData(@Url url: String): Call<VeganData>
}

data class VeganData (
  @SerializedName("checkedIngredients")   val records: List<String>,
  @SerializedName("isVeganSafe")          val isVeganSafe: Boolean,
  @SerializedName("isVeganResult")        val veganResult: VeganResult
): Serializable

data class VeganResult (
  @SerializedName("nonvegan")       val nonVegan: List<String>,
  @SerializedName("flagged")        val flagged: List<String>,
): Serializable
