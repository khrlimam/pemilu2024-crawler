package proj.internal.pojo

import com.google.gson.annotations.SerializedName

data class Wilayah(
    @SerializedName("nama") val nama: String = "",
    @SerializedName("id") val id: String? = null,
    @SerializedName("kode") val kode: String = "",
    @SerializedName("tingkat") val tingkat: Int
)
