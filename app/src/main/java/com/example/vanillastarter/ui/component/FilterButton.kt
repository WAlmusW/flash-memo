package com.example.uas_mobcomp_test.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uas_mobcomp_test.ui.viewmodel.SortType
import com.example.uas_mobcomp_test.R

@Composable
fun FilterDropdownButton(
    selectedSortType: SortType,
    onSortTypeSelected: (SortType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5FA9A9)),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.height(48.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Filter",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Filter Icon",
                    tint = Color(0xFFA7ECEE)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            SortType.entries.forEach { sortType ->
                DropdownMenuItem(
                    text = { Text(sortTypeToText(sortType)) },
                    onClick = {
                        onSortTypeSelected(sortType)
                        expanded = false
                    }
                )
            }
        }
    }
}

// Helper function to convert SortType to readable text
fun sortTypeToText(sortType: SortType): String {
    return when (sortType) {
        SortType.NAME_ASC -> "Name (Ascending)"
        SortType.NAME_DESC -> "Name (Descending)"
        SortType.FREQUENCY_ASC -> "Frequency (Ascending)"
        SortType.FREQUENCY_DESC -> "Frequency (Descending)"
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewFilterDropdownButton() {
    FilterDropdownButton(
        selectedSortType = SortType.NAME_ASC,
        onSortTypeSelected = { /* Handle sort type selection here */ }
    )
}