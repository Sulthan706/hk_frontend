package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.utils

import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.targetbystatus.ContentListStatusRkb

object Utils {

    val dummyClosingData = listOf(
        Closing(
            shift = "Morning",
            before = "Checked equipment status",
            progress = null,
            after = null,
            diversion = null,
            date = "2024-10-10",
            area = "Production",
            subArea = "Line 1",
            jobDesc = "Maintenance check on machinery"
        ),
        Closing(
            shift = "Afternoon",
            before = "Checked stock levels",
            progress = null,
            after = null,
            diversion = "Delayed restock due to late delivery",
            date = "2024-10-11",
            area = "Warehouse",
            subArea = "Storage B",
            jobDesc = "Inventory restocking"
        ),
        Closing(
            shift = "Night",
            before = "Cleaned work area",
            progress = "Swept and sanitized floors",
            after = null,
            diversion = "Minor spill handled",
            date = "2024-10-12",
            area = "Operations",
            subArea = "Zone A",
            jobDesc = "End of shift cleaning"
        ),
        Closing(
            shift = "Morning",
            before = "Reviewed safety protocols",
            progress = "Conducted safety drill",
            after = "All staff trained on protocols",
            diversion = "None",
            date = "2024-10-13",
            area = "Safety",
            subArea = "Main Office",
            jobDesc = "Safety protocol training"
        ),
        Closing(
            shift = "Afternoon",
            before = "Checked production reports",
            progress = "Adjusted machinery settings",
            after = "Increased production efficiency",
            diversion = null,
            date = "2024-10-14",
            area = "Production",
            subArea = "Line 2",
            jobDesc = "Machinery calibration"
        )
    )

    val dummyContentListStatusRkb = listOf(
        ContentListStatusRkb(
            approved = false,
            detailJob = "MEMBERSIHKAN LABA LABA",
            typeJob = "WEEKLY",
            diverted = false,
            done = false,
            idJob = 493139,
            locationId = 1219,
            locationName = "GF",
            subLocationId = 1031,
            subLocationName = "DINDING",
            statusPeriodic = "PLANNING",
            divertedTo = "2024-10-18",
            tanggalItem = "2024-10-18",
            beforeImage = null,
            progressImage = null,
            afterImage = null,
            shift = "-",
            baShift = "Shift Pagi",
            doneAt = null
        ),
        ContentListStatusRkb(
            approved = true,
            detailJob = "MEMBERSIHKAN DEBU",
            typeJob = "DAILY",
            diverted = true,
            done = true,
            idJob = 493140,
            locationId = 1220,
            locationName = "FF",
            subLocationId = 1032,
            subLocationName = "LANTAI",
            statusPeriodic = "COMPLETED",
            divertedTo = "2024-10-19",
            tanggalItem = "2024-10-19",
            beforeImage = "before_image_url",
            progressImage = "progress_image_url",
            afterImage = "after_image_url",
            shift = "Shift Malam",
            baShift = "Shift Sore",
            doneAt = "2024-10-19T20:00:00"
        )
    )


    val dummyDataHistoryClosing = listOf(
        HistoryClosing(
            "2024-10-13",
            "Pagi",
            "15:09"
        ),
        HistoryClosing(
            "2024-10-13",
            "Pagi",
            "15:09"
        ),
        HistoryClosing(
            "2024-10-13",
            "Pagi",
            "15:09"
        ),
        HistoryClosing(
            "2024-10-13",
            "Pagi",
            "15:09"
        ),
        HistoryClosing(
            "2024-10-13",
            "Pagi",
            "15:09"
        ),
        HistoryClosing(
            "2024-10-13",
            "Pagi",
            "15:09"
        ),
        HistoryClosing(
            "2024-10-13",
            "Pagi",
            "15:09"
        ),
        HistoryClosing(
            "2024-10-13",
            "Pagi",
            "15:09"
        ),
        HistoryClosing(
            "2024-10-13",
            "Pagi",
            "15:09"
        ),
        HistoryClosing(
            "2024-10-13",
            "Pagi",
            "15:09"
        ),
        HistoryClosing(
            "2024-10-13",
            "Pagi",
            "15:09"
        ),

    )

    val dummyClosingPending = listOf(
        ClosingPending(
            "image1.jpg",
            "John Doe",
            "2024-10-10",
            "Morning",true),

        ClosingPending(
            "image1.jpg",
            "John Doe",
            "2024-10-10",
            "Morning",false),
    )
}

data class HistoryClosing(
    val date : String,
    val shift : String,
    val time : String
)

data class ClosingPending(
    val image : String,
    val name : String,
    val date : String,
    val shift : String,
    val closing : Boolean
)

    data class Closing(
        val shift : String,
        val before:String?,
        val progress : String?,
        val after : String?,
        val diversion : String?,
        val date : String,
        val area : String,
        val subArea : String,
        val jobDesc : String
            )