package edu.temple.convoy.main_convoy.location_data

data class ConvoyData(
    val id: String,
    val number: Int
)

object ConvoyMockData {
    val convoyData = listOf<ConvoyData>(
        ConvoyData("sfd6hy", 12),
        ConvoyData("tgf7h9", 15),
        ConvoyData("47d6b5", 6),
        ConvoyData("s896g2", 25),
        ConvoyData("nhd60c", 10),
        ConvoyData("as46hp", 18),
    )
}