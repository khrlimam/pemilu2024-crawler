package proj.internal

import java.io.File
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*


fun main(vararg args: String) {
    Cli().main(args)
}

fun now(): String = Date().let { SimpleDateFormat("yyyy-MM-dd HH-mm").format(it) }
val currentDatasetName: String
    get() = "${now()}.csv"

fun initDataset(location: String, name: String) {
    try {
        Files.createDirectory(Paths.get(location))
    } catch (_: FileAlreadyExistsException) {
    } finally {
        File(
            Paths.get(location, name).toUri()
        ).writeText("tps," +
                "paslon01," +
                "paslon02," +
                "paslon03," +
                "total_suara_paslon," +
                "suara_sah," +
                "suara_lebih," +
                "suara_tidak_sah," +
                "total_suara," +
                "img1," +
                "img2," +
                "img3," +
                "pengguna_dpt," +
                "pengguna_dptb," +
                "pengguna_dpk," +
                "jumlah_pengguna," +
                "pemilih_dpt_lk," +
                "pemilih_dpt_pr," +
                "jumlah_pemilih_dpt," +
                "pengguna_dpt_lk," +
                "pengguna_dpt_pr," +
                "pengguna_dptb_lk," +
                "pengguna_dptb_pr," +
                "pengguna_dpk_lk," +
                "pengguna_dpk_pr," +
                "jumlah_pengguna_lk," +
                "jumlah_pengguna_pr\n")
    }
}