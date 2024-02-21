package proj.internal.pojo

import com.google.gson.annotations.SerializedName
import java.io.File
import java.nio.file.Path

data class Hhcw(

    @SerializedName("mode") var mode: String? = null,
    @SerializedName("chart") var chart: Chart? = Chart(),
    @SerializedName("images") var images: ArrayList<String>? = arrayListOf("-", "-", "-"),
    @SerializedName("administrasi") var administrasi: Administrasi? = Administrasi(),
    @SerializedName("psu") var psu: String? = null,
    @SerializedName("ts") var ts: String? = null,
    @SerializedName("status_suara") var statusSuara: Boolean? = null,
    @SerializedName("status_adm") var statusAdm: Boolean? = null

) {

    val csvData: String
        get() = arrayOf(
            chart?.anis ?: 0,
            chart?.prabowo ?: 0,
            chart?.ganjar ?: 0,
            chart?.totalSuaraPaslon ?: 0,
            administrasi?.suaraSah ?: 0,
            chart?.totalSuaraPaslon?.minus(administrasi?.suaraSah ?: 0) ?: 0,
            administrasi?.suaraTidakSah ?: 0,
            administrasi?.suaraTotal ?: 0,
            images?.elementAtOrNull(0) ?: "-",
            images?.elementAtOrNull(1) ?: "-",
            images?.elementAtOrNull(2) ?: "-",
        ).joinToString(",").plus("\n")

    fun writeToCsv(datasetPath: Path, kode: String?) {
        File(datasetPath.toUri()).appendText("$kode,$csvData")
    }
}

data class Chart(
    @SerializedName("100025") var paslon1: Int? = 0,
    @SerializedName("100026") var paslon2: Int? = 0,
    @SerializedName("100027") var paslon3: Int? = 0
) {
    val anis: Int
        get() = paslon1 ?: 0

    val prabowo: Int
        get() = paslon2 ?: 0

    val ganjar: Int
        get() = paslon3 ?: 0

    val totalSuaraPaslon: Int
        get() = anis + prabowo + ganjar
}

data class Administrasi(

    @SerializedName("suara_sah") var suaraSah: Int? = 0,
    @SerializedName("suara_total") var suaraTotal: Int? = 0,
    @SerializedName("pemilih_dpt_j") var pemilihDptJ: Int? = null,
    @SerializedName("pemilih_dpt_l") var pemilihDptL: Int? = null,
    @SerializedName("pemilih_dpt_p") var pemilihDptP: Int? = null,
    @SerializedName("pengguna_dpt_j") var penggunaDptJ: Int? = null,
    @SerializedName("pengguna_dpt_l") var penggunaDptL: Int? = null,
    @SerializedName("pengguna_dpt_p") var penggunaDptP: Int? = null,
    @SerializedName("pengguna_dptb_j") var penggunaDptbJ: Int? = null,
    @SerializedName("pengguna_dptb_l") var penggunaDptbL: Int? = null,
    @SerializedName("pengguna_dptb_p") var penggunaDptbP: Int? = null,
    @SerializedName("suara_tidak_sah") var suaraTidakSah: Int? = 0,
    @SerializedName("pengguna_total_j") var penggunaTotalJ: Int? = null,
    @SerializedName("pengguna_total_l") var penggunaTotalL: Int? = null,
    @SerializedName("pengguna_total_p") var penggunaTotalP: Int? = null,
    @SerializedName("pengguna_non_dpt_j") var penggunaNonDptJ: Int? = null,
    @SerializedName("pengguna_non_dpt_l") var penggunaNonDptL: Int? = null,
    @SerializedName("pengguna_non_dpt_p") var penggunaNonDptP: Int? = null

)