package com.example.animaltrackingui.db

import com.example.animaltrackingui.R
import com.google.android.gms.maps.model.LatLng


data class CountryCode(
    var countryCode: String,
    val countryName: String = "",
    val flagResID: Int = 0
)

val countries: List<CountryCode> = listOf(
    CountryCode("ad", "Andorra"),
    CountryCode("ae", "United Arab Emirates (UAE)"),
    CountryCode("af", "Afghanistan"),
    CountryCode("ag", "Antigua and Barbuda"),
    CountryCode("ai", "Anguilla"),
    CountryCode("al", "Albania"),
    CountryCode("am", "Armenia"),
    CountryCode("ao", "Angola"),
    CountryCode("aq", "Antarctica"),
    CountryCode("ar", "Argentina"),
    CountryCode("as", "American Samoa"),
    CountryCode("at", "Austria"),
    CountryCode("au", "Australia"),
    CountryCode("aw", "Aruba"),
    CountryCode("ax", "Åland Islands"),
    CountryCode("az", "Azerbaijan"),
    CountryCode("ba", "Bosnia And Herzegovina"),
    CountryCode("bb", "Barbados"),
    CountryCode("bd", "Bangladesh"),
    CountryCode("be", "Belgium"),
    CountryCode("bf", "Burkina Faso"),
    CountryCode("bg", "Bulgaria"),
    CountryCode("bh", "Bahrain"),
    CountryCode("bi", "Burundi"),
    CountryCode("bj", "Benin"),
    CountryCode("bl", "Saint Barthélemy"),
    CountryCode("bm", "Bermuda"),
    CountryCode("bn", "Brunei Darussalam"),
    CountryCode("bo", "Bolivia, Plurinational State Of"),
    CountryCode("br", "Brazil"),
    CountryCode("bs", "Bahamas"),
    CountryCode("bt", "Bhutan"),
    CountryCode("bw", "Botswana"),
    CountryCode("by", "Belarus"),
    CountryCode("bz", "Belize"),
    CountryCode("ca", "Canada"),
    CountryCode("cc", "Cocos (keeling) Islands"),
    CountryCode("cd", "Congo, The Democratic Republic Of The"),
    CountryCode("cf", "Central African Republic"),
    CountryCode("cg", "Congo"),
    CountryCode("ch", "Switzerland"),
    CountryCode("ci", "Côte D'ivoire"),
    CountryCode("ck", "Cook Islands"),
    CountryCode("cl", "Chile"),
    CountryCode("cm", "Cameroon"),
    CountryCode("cn", "China"),
    CountryCode("co", "Colombia"),
    CountryCode("cr", "Costa Rica"),
    CountryCode("cu", "Cuba"),
    CountryCode("cv", "Cape Verde"),
    CountryCode("cw", "Curaçao"),
    CountryCode("cx", "Christmas Island"),
    CountryCode("cy", "Cyprus"),
    CountryCode("cz", "Czech Republic"),
    CountryCode("de", "Germany"),
    CountryCode("dj", "Djibouti"),
    CountryCode("dk", "Denmark"),
    CountryCode("dm", "Dominica"),
    CountryCode("do", "Dominican Republic"),
    CountryCode("dz", "Algeria"),
    CountryCode("ec", "Ecuador"),
    CountryCode("ee", "Estonia"),
    CountryCode("eg", "Egypt"),
    CountryCode("er", "Eritrea"),
    CountryCode("es", "Spain"),
    CountryCode("et", "Ethiopia"),
    CountryCode("fi", "Finland"),
    CountryCode("fj", "Fiji"),
    CountryCode("fk", "Falkland Islands (malvinas)"),
    CountryCode("fm", "Micronesia, Federated States Of"),
    CountryCode("fo", "Faroe Islands"),
    CountryCode("fr", "France"),
    CountryCode("ga", "Gabon"),
    CountryCode("gb", "United Kingdom"),
    CountryCode("gd", "Grenada"),
    CountryCode("ge", "Georgia"),
    CountryCode("gf", "French Guyana"),
    CountryCode("gh", "Ghana"),
    CountryCode("gi", "Gibraltar"),
    CountryCode("gl", "Greenland"),
    CountryCode("gm", "Gambia"),
    CountryCode("gn", "Guinea"),
    CountryCode("gp", "Guadeloupe"),
    CountryCode("gq", "Equatorial Guinea"),
    CountryCode("gr", "Greece"),
    CountryCode("gt", "Guatemala"),
    CountryCode("gu", "Guam"),
    CountryCode("gw", "Guinea-bissau"),
    CountryCode("gy", "Guyana"),
    CountryCode("hk", "Hong Kong"),
    CountryCode("hn", "Honduras"),
    CountryCode("hr", "Croatia"),
    CountryCode("ht", "Haiti"),
    CountryCode("hu", "Hungary"),
    CountryCode("id", "Indonesia"),
    CountryCode("ie", "Ireland"),
    CountryCode("im", "Isle Of Man"),
    CountryCode("is", "Iceland"),
    CountryCode("in", "India"),
    CountryCode("io", "British Indian Ocean Territory"),
    CountryCode("iq", "Iraq"),
    CountryCode("ir", "Iran, Islamic Republic Of"),
    CountryCode("it", "Italy"),
    CountryCode("je", "Jersey "),
    CountryCode("jm", "Jamaica"),
    CountryCode("jo", "Jordan"),
    CountryCode("jp", "Japan"),
    CountryCode("ke", "Kenya"),
    CountryCode("kg", "Kyrgyzstan"),
    CountryCode("kh", "Cambodia"),
    CountryCode("ki", "Kiribati"),
    CountryCode("km", "Comoros"),
    CountryCode("kn", "Saint Kitts and Nevis"),
    CountryCode("kp", "North Korea"),
    CountryCode("kr", "South Korea"),
    CountryCode("kw", "Kuwait"),
    CountryCode("ky", "Cayman Islands"),
    CountryCode("kz", "Kazakhstan"),
    CountryCode("la", "Lao People's Democratic Republic"),
    CountryCode("lb", "Lebanon"),
    CountryCode("lc", "Saint Lucia"),
    CountryCode("li", "Liechtenstein"),
    CountryCode("lk", "Sri Lanka"),
    CountryCode("lr", "Liberia"),
    CountryCode("ls", "Lesotho"),
    CountryCode("lt", "Lithuania"),
    CountryCode("lu", "Luxembourg"),
    CountryCode("lv", "Latvia"),
    CountryCode("ly", "Libya"),
    CountryCode("ma", "Morocco"),
    CountryCode("mc", "Monaco"),
    CountryCode("md", "Moldova, Republic Of"),
    CountryCode("me", "Montenegro"),
    CountryCode("mf", "Saint Martin"),
    CountryCode("mg", "Madagascar"),
    CountryCode("mh", "Marshall Islands"),
    CountryCode("mk", "Macedonia (FYROM)"),
    CountryCode("ml", "Mali"),
    CountryCode("mm", "Myanmar"),
    CountryCode("mn", "Mongolia"),
    CountryCode("mo", "Macau"),
    CountryCode("mp", "Northern Mariana Islands"),
    CountryCode("mq", "Martinique"),
    CountryCode("mr", "Mauritania"),
    CountryCode("ms", "Montserrat"),
    CountryCode("mt", "Malta"),
    CountryCode("mu", "Mauritius"),
    CountryCode("mv", "Maldives"),
    CountryCode("mw", "Malawi"),
    CountryCode("mx", "Mexico"),
    CountryCode("my", "Malaysia"),
    CountryCode("mz", "Mozambique"),
    CountryCode("na", "Namibia"),
    CountryCode("nc", "New Caledonia"),
    CountryCode("ne", "Niger"),
    CountryCode("nf", "Norfolk Islands"),
    CountryCode("ng", "Nigeria"),
    CountryCode("ni", "Nicaragua"),
    CountryCode("nl", "Netherlands"),
    CountryCode("no", "Norway"),
    CountryCode("np", "Nepal"),
    CountryCode("nr", "Nauru"),
    CountryCode("nu", "Niue"),
    CountryCode("nz", "New Zealand"),
    CountryCode("om", "Oman"),
    CountryCode("pa", "Panama"),
    CountryCode("pe", "Peru"),
    CountryCode("pf", "French Polynesia"),
    CountryCode("pg", "Papua New Guinea"),
    CountryCode("ph", "Philippines"),
    CountryCode("pk", "Pakistan"),
    CountryCode("pl", "Poland"),
    CountryCode("pm", "Saint Pierre And Miquelon"),
    CountryCode("pn", "Pitcairn Islands"),
    CountryCode("pr", "Puerto Rico"),
    CountryCode("ps", "Palestine"),
    CountryCode("pt", "Portugal"),
    CountryCode("pw", "Palau"),
    CountryCode("py", "Paraguay"),
    CountryCode("qa", "Qatar"),
    CountryCode("re", "Réunion"),
    CountryCode("ro", "Romania"),
    CountryCode("rs", "Serbia"),
    CountryCode("ru", "Russian Federation"),
    CountryCode("rw", "Rwanda"),
    CountryCode("sa", "Saudi Arabia"),
    CountryCode("sb", "Solomon Islands"),
    CountryCode("sc", "Seychelles"),
    CountryCode("sd", "Sudan"),
    CountryCode("se", "Sweden"),
    CountryCode("sg", "Singapore"),
    CountryCode("sh", "Saint Helena, Ascension And Tristan Da Cunha"),
    CountryCode("si", "Slovenia"),
    CountryCode("sk", "Slovakia"),
    CountryCode("sl", "Sierra Leone"),
    CountryCode("sm", "San Marino"),
    CountryCode("sn", "Senegal"),
    CountryCode("so", "Somalia"),
    CountryCode("sr", "Suriname"),
    CountryCode("ss", "South Sudan"),
    CountryCode("st", "Sao Tome And Principe"),
    CountryCode("sv", "El Salvador"),
    CountryCode("sx", "Sint Maarten"),
    CountryCode("sy", "Syrian Arab Republic"),
    CountryCode("sz", "Swaziland"),
    CountryCode("tc", "Turks and Caicos Islands"),
    CountryCode("td", "Chad"),
    CountryCode("tg", "Togo"),
    CountryCode("th", "Thailand"),
    CountryCode("tj", "Tajikistan"),
    CountryCode("tk", "Tokelau"),
    CountryCode("tl", "Timor-leste"),
    CountryCode("tm", "Turkmenistan"),
    CountryCode("tn", "Tunisia"),
    CountryCode("to", "Tonga"),
    CountryCode("tr", "Turkey"),
    CountryCode("tt", "Trinidad &amp; Tobago"),
    CountryCode("tv", "Tuvalu"),
    CountryCode("tw", "Taiwan"),
    CountryCode("tz", "Tanzania, United Republic Of"),
    CountryCode("ua", "Ukraine"),
    CountryCode("ug", "Uganda"),
    CountryCode("us", "United States"),
    CountryCode("uy", "Uruguay"),
    CountryCode("uz", "Uzbekistan"),
    CountryCode("va", "Holy See (vatican City State)"),
    CountryCode("vc", "Saint Vincent &amp; The Grenadines"),
    CountryCode("ve", "Venezuela, Bolivarian Republic Of"),
    CountryCode("vg", "British Virgin Islands"),
    CountryCode("vi", "US Virgin Islands"),
    CountryCode("vn", "Vietnam"),
    CountryCode("vu", "Vanuatu"),
    CountryCode("wf", "Wallis And Futuna"),
    CountryCode("ws", "Samoa"),
    CountryCode("xk", "Kosovo"),
    CountryCode("ye", "Yemen"),
    CountryCode("yt", "Mayotte"),
    CountryCode("za", "South Africa"),
    CountryCode("zm", "Zambia"),
    CountryCode("zw", "Zimbabwe")
)

fun getFlags(countryName: String): Int {
    return when (countryName) {
        "ad" -> R.drawable.flag_andorra
        "ae" -> R.drawable.flag_uae
        "af" -> R.drawable.flag_afghanistan
        "ag" -> R.drawable.flag_antigua_and_barbuda
        "ai" -> R.drawable.flag_anguilla
        "al" -> R.drawable.flag_albania
        "am" -> R.drawable.flag_armenia
        "ao" -> R.drawable.flag_angola
        "aq" -> R.drawable.flag_antarctica
        "ar" -> R.drawable.flag_argentina
        "as" -> R.drawable.flag_american_samoa
        "at" -> R.drawable.flag_austria
        "au" -> R.drawable.flag_australia
        "aw" -> R.drawable.flag_aruba
        "ax" -> R.drawable.flag_aland
        "az" -> R.drawable.flag_azerbaijan
        "ba" -> R.drawable.flag_bosnia
        "bb" -> R.drawable.flag_barbados
        "bd" -> R.drawable.flag_bangladesh
        "be" -> R.drawable.flag_belgium
        "bf" -> R.drawable.flag_burkina_faso
        "bg" -> R.drawable.flag_bulgaria
        "bh" -> R.drawable.flag_bahrain
        "bi" -> R.drawable.flag_burundi
        "bj" -> R.drawable.flag_benin
        "bl" -> R.drawable.flag_saint_barthelemy // custom
        "bm" -> R.drawable.flag_bermuda
        "bn" -> R.drawable.flag_brunei
        "bo" -> R.drawable.flag_bolivia
        "br" -> R.drawable.flag_brazil
        "bs" -> R.drawable.flag_bahamas
        "bt" -> R.drawable.flag_bhutan
        "bw" -> R.drawable.flag_botswana
        "by" -> R.drawable.flag_belarus
        "bz" -> R.drawable.flag_belize
        "ca" -> R.drawable.flag_canada
        "cc" -> R.drawable.flag_cocos // custom
        "cd" -> R.drawable.flag_democratic_republic_of_the_congo
        "cf" -> R.drawable.flag_central_african_republic
        "cg" -> R.drawable.flag_republic_of_the_congo
        "ch" -> R.drawable.flag_switzerland
        "ci" -> R.drawable.flag_cote_divoire
        "ck" -> R.drawable.flag_cook_islands
        "cl" -> R.drawable.flag_chile
        "cm" -> R.drawable.flag_cameroon
        "cn" -> R.drawable.flag_china
        "co" -> R.drawable.flag_colombia
        "cr" -> R.drawable.flag_costa_rica
        "cu" -> R.drawable.flag_cuba
        "cv" -> R.drawable.flag_cape_verde
        "cw" -> R.drawable.flag_curacao
        "cx" -> R.drawable.flag_christmas_island
        "cy" -> R.drawable.flag_cyprus
        "cz" -> R.drawable.flag_czech_republic
        "de" -> R.drawable.flag_germany
        "dj" -> R.drawable.flag_djibouti
        "dk" -> R.drawable.flag_denmark
        "dm" -> R.drawable.flag_dominica
        "do" -> R.drawable.flag_dominican_republic
        "dz" -> R.drawable.flag_algeria
        "ec" -> R.drawable.flag_ecuador
        "ee" -> R.drawable.flag_estonia
        "eg" -> R.drawable.flag_egypt
        "er" -> R.drawable.flag_eritrea
        "es" -> R.drawable.flag_spain
        "et" -> R.drawable.flag_ethiopia
        "fi" -> R.drawable.flag_finland
        "fj" -> R.drawable.flag_fiji
        "fk" -> R.drawable.flag_falkland_islands
        "fm" -> R.drawable.flag_micronesia
        "fo" -> R.drawable.flag_faroe_islands
        "fr" -> R.drawable.flag_france
        "ga" -> R.drawable.flag_gabon
        "gb" -> R.drawable.flag_united_kingdom
        "gd" -> R.drawable.flag_grenada
        "ge" -> R.drawable.flag_georgia
        "gf" -> R.drawable.flag_guyane
        "gg" -> R.drawable.flag_guernsey
        "gh" -> R.drawable.flag_ghana
        "gi" -> R.drawable.flag_gibraltar
        "gl" -> R.drawable.flag_greenland
        "gm" -> R.drawable.flag_gambia
        "gn" -> R.drawable.flag_guinea
        "gp" -> R.drawable.flag_guadeloupe
        "gq" -> R.drawable.flag_equatorial_guinea
        "gr" -> R.drawable.flag_greece
        "gt" -> R.drawable.flag_guatemala
        "gu" -> R.drawable.flag_guam
        "gw" -> R.drawable.flag_guinea_bissau
        "gy" -> R.drawable.flag_guyana
        "hk" -> R.drawable.flag_hong_kong
        "hn" -> R.drawable.flag_honduras
        "hr" -> R.drawable.flag_croatia
        "ht" -> R.drawable.flag_haiti
        "hu" -> R.drawable.flag_hungary
        "id" -> R.drawable.flag_indonesia
        "ie" -> R.drawable.flag_ireland
        "im" -> R.drawable.flag_isleof_man // custom
        "is" -> R.drawable.flag_iceland
        "in" -> R.drawable.flag_india
        "io" -> R.drawable.flag_british_indian_ocean_territory
        "iq" -> R.drawable.flag_iraq_new
        "ir" -> R.drawable.flag_iran
        "it" -> R.drawable.flag_italy
        "je" -> R.drawable.flag_jersey
        "jm" -> R.drawable.flag_jamaica
        "jo" -> R.drawable.flag_jordan
        "jp" -> R.drawable.flag_japan
        "ke" -> R.drawable.flag_kenya
        "kg" -> R.drawable.flag_kyrgyzstan
        "kh" -> R.drawable.flag_cambodia
        "ki" -> R.drawable.flag_kiribati
        "km" -> R.drawable.flag_comoros
        "kn" -> R.drawable.flag_saint_kitts_and_nevis
        "kp" -> R.drawable.flag_north_korea
        "kr" -> R.drawable.flag_south_korea
        "kw" -> R.drawable.flag_kuwait
        "ky" -> R.drawable.flag_cayman_islands
        "kz" -> R.drawable.flag_kazakhstan
        "la" -> R.drawable.flag_laos
        "lb" -> R.drawable.flag_lebanon
        "lc" -> R.drawable.flag_saint_lucia
        "li" -> R.drawable.flag_liechtenstein
        "lk" -> R.drawable.flag_sri_lanka
        "lr" -> R.drawable.flag_liberia
        "ls" -> R.drawable.flag_lesotho
        "lt" -> R.drawable.flag_lithuania
        "lu" -> R.drawable.flag_luxembourg
        "lv" -> R.drawable.flag_latvia
        "ly" -> R.drawable.flag_libya
        "ma" -> R.drawable.flag_morocco
        "mc" -> R.drawable.flag_monaco
        "md" -> R.drawable.flag_moldova
        "me" -> R.drawable.flag_of_montenegro // custom
        "mf" -> R.drawable.flag_saint_martin
        "mg" -> R.drawable.flag_madagascar
        "mh" -> R.drawable.flag_marshall_islands
        "mk" -> R.drawable.flag_macedonia
        "ml" -> R.drawable.flag_mali
        "mm" -> R.drawable.flag_myanmar
        "mn" -> R.drawable.flag_mongolia
        "mo" -> R.drawable.flag_macao
        "mp" -> R.drawable.flag_northern_mariana_islands
        "mq" -> R.drawable.flag_martinique
        "mr" -> R.drawable.flag_mauritania
        "ms" -> R.drawable.flag_montserrat
        "mt" -> R.drawable.flag_malta
        "mu" -> R.drawable.flag_mauritius
        "mv" -> R.drawable.flag_maldives
        "mw" -> R.drawable.flag_malawi
        "mx" -> R.drawable.flag_mexico
        "my" -> R.drawable.flag_malaysia
        "mz" -> R.drawable.flag_mozambique
        "na" -> R.drawable.flag_namibia
        "nc" -> R.drawable.flag_new_caledonia // custom
        "ne" -> R.drawable.flag_niger
        "nf" -> R.drawable.flag_norfolk_island
        "ng" -> R.drawable.flag_nigeria
        "ni" -> R.drawable.flag_nicaragua
        "nl" -> R.drawable.flag_netherlands
        "no" -> R.drawable.flag_norway
        "np" -> R.drawable.flag_nepal
        "nr" -> R.drawable.flag_nauru
        "nu" -> R.drawable.flag_niue
        "nz" -> R.drawable.flag_new_zealand
        "om" -> R.drawable.flag_oman
        "pa" -> R.drawable.flag_panama
        "pe" -> R.drawable.flag_peru
        "pf" -> R.drawable.flag_french_polynesia
        "pg" -> R.drawable.flag_papua_new_guinea
        "ph" -> R.drawable.flag_philippines
        "pk" -> R.drawable.flag_pakistan
        "pl" -> R.drawable.flag_poland
        "pm" -> R.drawable.flag_saint_pierre
        "pn" -> R.drawable.flag_pitcairn_islands
        "pr" -> R.drawable.flag_puerto_rico
        "ps" -> R.drawable.flag_palestine
        "pt" -> R.drawable.flag_portugal
        "pw" -> R.drawable.flag_palau
        "py" -> R.drawable.flag_paraguay
        "qa" -> R.drawable.flag_qatar
        "re" -> R.drawable.flag_martinique // no exact flag found
        "ro" -> R.drawable.flag_romania
        "rs" -> R.drawable.flag_serbia // custom
        "ru" -> R.drawable.flag_russian_federation
        "rw" -> R.drawable.flag_rwanda
        "sa" -> R.drawable.flag_saudi_arabia
        "sb" -> R.drawable.flag_soloman_islands
        "sc" -> R.drawable.flag_seychelles
        "sd" -> R.drawable.flag_sudan
        "se" -> R.drawable.flag_sweden
        "sg" -> R.drawable.flag_singapore
        "sh" -> R.drawable.flag_saint_helena // custom
        "si" -> R.drawable.flag_slovenia
        "sk" -> R.drawable.flag_slovakia
        "sl" -> R.drawable.flag_sierra_leone
        "sm" -> R.drawable.flag_san_marino
        "sn" -> R.drawable.flag_senegal
        "so" -> R.drawable.flag_somalia
        "sr" -> R.drawable.flag_suriname
        "ss" -> R.drawable.flag_south_sudan
        "st" -> R.drawable.flag_sao_tome_and_principe
        "sv" -> R.drawable.flag_el_salvador
        "sx" -> R.drawable.flag_sint_maarten
        "sy" -> R.drawable.flag_syria
        "sz" -> R.drawable.flag_swaziland
        "tc" -> R.drawable.flag_turks_and_caicos_islands
        "td" -> R.drawable.flag_chad
        "tg" -> R.drawable.flag_togo
        "th" -> R.drawable.flag_thailand
        "tj" -> R.drawable.flag_tajikistan
        "tk" -> R.drawable.flag_tokelau // custom
        "tl" -> R.drawable.flag_timor_leste
        "tm" -> R.drawable.flag_turkmenistan
        "tn" -> R.drawable.flag_tunisia
        "to" -> R.drawable.flag_tonga
        "tr" -> R.drawable.flag_turkey
        "tt" -> R.drawable.flag_trinidad_and_tobago
        "tv" -> R.drawable.flag_tuvalu
        "tw" -> R.drawable.flag_taiwan
        "tz" -> R.drawable.flag_tanzania
        "ua" -> R.drawable.flag_ukraine
        "ug" -> R.drawable.flag_uganda
        "us" -> R.drawable.flag_united_states_of_america
        "uy" -> R.drawable.flag_uruguay
        "uz" -> R.drawable.flag_uzbekistan
        "va" -> R.drawable.flag_vatican_city
        "vc" -> R.drawable.flag_saint_vicent_and_the_grenadines
        "ve" -> R.drawable.flag_venezuela
        "vg" -> R.drawable.flag_british_virgin_islands
        "vi" -> R.drawable.flag_us_virgin_islands
        "vn" -> R.drawable.flag_vietnam
        "vu" -> R.drawable.flag_vanuatu
        "wf" -> R.drawable.flag_wallis_and_futuna
        "ws" -> R.drawable.flag_samoa
        "xk" -> R.drawable.flag_kosovo
        "ye" -> R.drawable.flag_yemen
        "yt" -> R.drawable.flag_martinique // no exact flag found
        "za" -> R.drawable.flag_south_africa
        "zm" -> R.drawable.flag_zambia
        "zw" -> R.drawable.flag_zimbabwe
        else -> R.drawable.flag_transparent
    }
}

fun getListOfCountries(): List<CountryCode> {
    return countries
}

fun List<CountryCode>.searchCountryList(key: String): MutableList<CountryCode> {
    val tempList = mutableListOf<CountryCode>()
    this.forEach {
        if (it.countryName.lowercase().contains(key.lowercase())) {
            tempList.add(it)
        }
    }
    return tempList
}
fun getCountryLatLng(key:String): LatLng?{
    return countryISOS[key.uppercase()]
}


val countryISOS: HashMap<String?, LatLng?> = object : HashMap<String?, LatLng?>() {
    init {
        put("AD", LatLng(42.546245, 1.601554))
        put("AE", LatLng(23.424076, 53.847818))
        put("AF", LatLng(33.93911, 67.709953))
        put("AG", LatLng(17.060816, -61.796428))
        put("AI", LatLng(18.220554, -63.068615))
        put("AL", LatLng(41.153332, 20.168331))
        put("AM", LatLng(40.069099, 45.038189))
        put("AN", LatLng(12.226079, -69.060087))
        put("AO", LatLng(-11.202692, 17.873887))
        put("AQ", LatLng(-75.250973, -0.071389))
        put("AR", LatLng(-38.416097, -63.616672))
        put("AS", LatLng(-14.270972, -170.132217))
        put("AT", LatLng(47.516231, 14.550072))
        put("AU", LatLng(-25.274398, 133.775136))
        put("AW", LatLng(12.52111, -69.968338))
        put("AZ", LatLng(40.143105, 47.576927))
        put("BA", LatLng(43.915886, 17.679076))
        put("BB", LatLng(13.193887, -59.543198))
        put("BD", LatLng(23.684994, 90.356331))
        put("BE", LatLng(50.503887, 4.469936))
        put("BF", LatLng(12.238333, -1.561593))
        put("BG", LatLng(42.733883, 25.48583))
        put("BH", LatLng(25.930414, 50.637772))
        put("BI", LatLng(-3.373056, 29.918886))
        put("BJ", LatLng(9.30769, 2.315834))
        put("BM", LatLng(32.321384, -64.75737))
        put("BN", LatLng(4.535277, 114.727669))
        put("BO", LatLng(-16.290154, -63.588653))
        put("BR", LatLng(-14.235004, -51.92528))
        put("BS", LatLng(25.03428, -77.39628))
        put("BT", LatLng(27.514162, 90.433601))
        put("BV", LatLng(-54.423199, 3.413194))
        put("BW", LatLng(-22.328474, 24.684866))
        put("BY", LatLng(53.709807, 27.953389))
        put("BZ", LatLng(17.189877, -88.49765))
        put("CA", LatLng(56.130366, -106.346771))
        put("CC", LatLng(-12.164165, 96.870956))
        put("CD", LatLng(-4.038333, 21.758664))
        put("CF", LatLng(6.611111, 20.939444))
        put("CG", LatLng(-0.228021, 15.827659))
        put("CH", LatLng(46.818188, 8.227512))
        put("CI", LatLng(7.539989, -5.54708))
        put("CK", LatLng(-21.236736, -159.777671))
        put("CL", LatLng(-35.675147, -71.542969))
        put("CM", LatLng(7.369722, 12.354722))
        put("CN", LatLng(35.86166, 104.195397))
        put("CO", LatLng(4.570868, -74.297333))
        put("CR", LatLng(9.748917, -83.753428))
        put("CU", LatLng(21.521757, -77.781167))
        put("CV", LatLng(16.002082, -24.013197))
        put("CX", LatLng(-10.447525, 105.690449))
        put("CY", LatLng(35.126413, 33.429859))
        put("CZ", LatLng(49.817492, 15.472962))
        put("DE", LatLng(51.165691, 10.451526))
        put("DJ", LatLng(11.825138, 42.590275))
        put("DK", LatLng(56.26392, 9.501785))
        put("DM", LatLng(15.414999, -61.370976))
        put("DO", LatLng(18.735693, -70.162651))
        put("DZ", LatLng(28.033886, 1.659626))
        put("EC", LatLng(-1.831239, -78.183406))
        put("EE", LatLng(58.595272, 25.013607))
        put("EG", LatLng(26.820553, 30.802498))
        put("EH", LatLng(24.215527, -12.885834))
        put("ER", LatLng(15.179384, 39.782334))
        put("ES", LatLng(40.463667, -3.74922))
        put("ET", LatLng(9.145, 40.489673))
        put("FI", LatLng(61.92411, 25.748151))
        put("FJ", LatLng(-16.578193, 179.414413))
        put("FK", LatLng(-51.796253, -59.523613))
        put("FM", LatLng(7.425554, 150.550812))
        put("FO", LatLng(61.892635, -6.911806))
        put("FR", LatLng(46.227638, 2.213749))
        put("GA", LatLng(-0.803689, 11.609444))
        put("GB", LatLng(55.378051, -3.435973))
        put("GD", LatLng(12.262776, -61.604171))
        put("GE", LatLng(42.315407, 43.356892))
        put("GF", LatLng(3.933889, -53.125782))
        put("GG", LatLng(49.465691, -2.585278))
        put("GH", LatLng(7.946527, -1.023194))
        put("GI", LatLng(36.137741, -5.345374))
        put("GL", LatLng(71.706936, -42.604303))
        put("GM", LatLng(13.443182, -15.310139))
        put("GN", LatLng(9.945587, -9.696645))
        put("GP", LatLng(16.995971, -62.067641))
        put("GQ", LatLng(1.650801, 10.267895))
        put("GR", LatLng(39.074208, 21.824312))
        put("GS", LatLng(-54.429579, -36.587909))
        put("GT", LatLng(15.783471, -90.230759))
        put("GU", LatLng(13.444304, 144.793731))
        put("GW", LatLng(11.803749, -15.180413))
        put("GY", LatLng(4.860416, -58.93018))
        put("GZ", LatLng(31.354676, 34.308825))
        put("HK", LatLng(22.396428, 114.109497))
        put("HM", LatLng(-53.08181, 73.504158))
        put("HN", LatLng(15.199999, -86.241905))
        put("HR", LatLng(45.1, 15.2))
        put("HT", LatLng(18.971187, -72.285215))
        put("HU", LatLng(47.162494, 19.503304))
        put("ID", LatLng(-0.789275, 113.921327))
        put("IE", LatLng(53.41291, -8.24389))
        put("IL", LatLng(31.046051, 34.851612))
        put("IM", LatLng(54.236107, -4.548056))
        put("IN", LatLng(20.593684, 78.96288))
        put("IO", LatLng(-6.343194, 71.876519))
        put("IQ", LatLng(33.223191, 43.679291))
        put("IR", LatLng(32.427908, 53.688046))
        put("IS", LatLng(64.963051, -19.020835))
        put("IT", LatLng(41.87194, 12.56738))
        put("JE", LatLng(49.214439, -2.13125))
        put("JM", LatLng(18.109581, -77.297508))
        put("JO", LatLng(30.585164, 36.238414))
        put("JP", LatLng(36.204824, 138.252924))
        put("KE", LatLng(-0.023559, 37.906193))
        put("KG", LatLng(41.20438, 74.766098))
        put("KH", LatLng(12.565679, 104.990963))
        put("KI", LatLng(-3.370417, -168.734039))
        put("KM", LatLng(-11.875001, 43.872219))
        put("KN", LatLng(17.357822, -62.782998))
        put("KP", LatLng(40.339852, 127.510093))
        put("KR", LatLng(35.907757, 127.766922))
        put("KW", LatLng(29.31166, 47.481766))
        put("KY", LatLng(19.513469, -80.566956))
        put("KZ", LatLng(48.019573, 66.923684))
        put("LA", LatLng(19.85627, 102.495496))
        put("LB", LatLng(33.854721, 35.862285))
        put("LC", LatLng(13.909444, -60.978893))
        put("LI", LatLng(47.166, 9.555373))
        put("LK", LatLng(7.873054, 80.771797))
        put("LR", LatLng(6.428055, -9.429499))
        put("LS", LatLng(-29.609988, 28.233608))
        put("LT", LatLng(55.169438, 23.881275))
        put("LU", LatLng(49.815273, 6.129583))
        put("LV", LatLng(56.879635, 24.603189))
        put("LY", LatLng(26.3351, 17.228331))
        put("MA", LatLng(31.791702, -7.09262))
        put("MC", LatLng(43.750298, 7.412841))
        put("MD", LatLng(47.411631, 28.369885))
        put("ME", LatLng(42.708678, 19.37439))
        put("MG", LatLng(-18.766947, 46.869107))
        put("MH", LatLng(7.131474, 171.184478))
        put("MK", LatLng(41.608635, 21.745275))
        put("ML", LatLng(17.570692, -3.996166))
        put("MM", LatLng(21.913965, 95.956223))
        put("MN", LatLng(46.862496, 103.846656))
        put("MO", LatLng(22.198745, 113.543873))
        put("MP", LatLng(17.33083, 145.38469))
        put("MQ", LatLng(14.641528, -61.024174))
        put("MR", LatLng(21.00789, -10.940835))
        put("MS", LatLng(16.742498, -62.187366))
        put("MT", LatLng(35.937496, 14.375416))
        put("MU", LatLng(-20.348404, 57.552152))
        put("MV", LatLng(3.202778, 73.22068))
        put("MW", LatLng(-13.254308, 34.301525))
        put("MX", LatLng(23.634501, -102.552784))
        put("MY", LatLng(4.210484, 101.975766))
        put("MZ", LatLng(-18.665695, 35.529562))
        put("NA", LatLng(-22.95764, 18.49041))
        put("NC", LatLng(-20.904305, 165.618042))
        put("NE", LatLng(17.607789, 8.081666))
        put("NF", LatLng(-29.040835, 167.954712))
        put("NG", LatLng(9.081999, 8.675277))
        put("NI", LatLng(12.865416, -85.207229))
        put("NL", LatLng(52.132633, 5.291266))
        put("NO", LatLng(60.472024, 8.468946))
        put("NP", LatLng(28.394857, 84.124008))
        put("NR", LatLng(-0.522778, 166.931503))
        put("NU", LatLng(-19.054445, -169.867233))
        put("NZ", LatLng(-40.900557, 174.885971))
        put("OM", LatLng(21.512583, 55.923255))
        put("PA", LatLng(8.537981, -80.782127))
        put("PE", LatLng(-9.189967, -75.015152))
        put("PF", LatLng(-17.679742, -149.406843))
        put("PG", LatLng(-6.314993, 143.95555))
        put("PH", LatLng(12.879721, 121.774017))
        put("PK", LatLng(30.375321, 69.345116))
        put("PL", LatLng(51.919438, 19.145136))
        put("PM", LatLng(46.941936, -56.27111))
        put("PN", LatLng(-24.703615, -127.439308))
        put("PR", LatLng(18.220833, -66.590149))
        put("PS", LatLng(31.952162, 35.233154))
        put("PT", LatLng(39.399872, -8.224454))
        put("PW", LatLng(7.51498, 134.58252))
        put("PY", LatLng(-23.442503, -58.443832))
        put("QA", LatLng(25.354826, 51.183884))
        put("RE", LatLng(-21.115141, 55.536384))
        put("RO", LatLng(45.943161, 24.96676))
        put("RS", LatLng(44.016521, 21.005859))
        put("RU", LatLng(61.52401, 105.318756))
        put("RW", LatLng(-1.940278, 29.873888))
        put("SA", LatLng(23.885942, 45.079162))
        put("SB", LatLng(-9.64571, 160.156194))
        put("SC", LatLng(-4.679574, 55.491977))
        put("SD", LatLng(12.862807, 30.217636))
        put("SE", LatLng(60.128161, 18.643501))
        put("SG", LatLng(1.352083, 103.819836))
        put("SH", LatLng(-24.143474, -10.030696))
        put("SI", LatLng(46.151241, 14.995463))
        put("SJ", LatLng(77.553604, 23.670272))
        put("SK", LatLng(48.669026, 19.699024))
        put("SL", LatLng(8.460555, -11.779889))
        put("SM", LatLng(43.94236, 12.457777))
        put("SN", LatLng(14.497401, -14.452362))
        put("SO", LatLng(5.152149, 46.199616))
        put("SR", LatLng(3.919305, -56.027783))
        put("ST", LatLng(0.18636, 6.613081))
        put("SV", LatLng(13.794185, -88.89653))
        put("SY", LatLng(34.802075, 38.996815))
        put("SZ", LatLng(-26.522503, 31.465866))
        put("TC", LatLng(21.694025, -71.797928))
        put("TD", LatLng(15.454166, 18.732207))
        put("TF", LatLng(-49.280366, 69.348557))
        put("TG", LatLng(8.619543, 0.824782))
        put("TH", LatLng(15.870032, 100.992541))
        put("TJ", LatLng(38.861034, 71.276093))
        put("TK", LatLng(-8.967363, -171.855881))
        put("TL", LatLng(-8.874217, 125.727539))
        put("TM", LatLng(38.969719, 59.556278))
        put("TN", LatLng(33.886917, 9.537499))
        put("TO", LatLng(-21.178986, -175.198242))
        put("TR", LatLng(38.963745, 35.243322))
        put("TT", LatLng(10.691803, -61.222503))
        put("TV", LatLng(-7.109535, 177.64933))
        put("TW", LatLng(23.69781, 120.960515))
        put("TZ", LatLng(-6.369028, 34.888822))
        put("UA", LatLng(48.379433, 31.16558))
        put("UG", LatLng(1.373333, 32.290275))
        put("US", LatLng(37.09024, -95.712891))
        put("UY", LatLng(-32.522779, -55.765835))
        put("UZ", LatLng(41.377491, 64.585262))
        put("VA", LatLng(41.902916, 12.453389))
        put("VC", LatLng(12.984305, -61.287228))
        put("VE", LatLng(6.42375, -66.58973))
        put("VG", LatLng(18.420695, -64.639968))
        put("VI", LatLng(18.335765, -64.896335))
        put("VN", LatLng(14.058324, 108.277199))
        put("VU", LatLng(-15.376706, 166.959158))
        put("WF", LatLng(-13.768752, -177.156097))
        put("WS", LatLng(-13.759029, -172.104629))
        put("XK", LatLng(42.602636, 20.902977))
        put("YE", LatLng(15.552727, 48.516388))
        put("YT", LatLng(-12.8275, 45.166244))
        put("ZA", LatLng(-30.559482, 22.937506))
        put("ZM", LatLng(-13.133897, 27.849332))
        put("ZW", LatLng(-19.015438, 29.154857))
    }
}