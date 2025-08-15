package com.example.mediline.User.ui.Home



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
//import androidx.compose.material.icons.filled.Female
//import androidx.compose.material.icons.filled.LocalHospital
//import androidx.compose.material.icons.filled.MedicalInformation
//import androidx.compose.material.icons.filled.MonitorHeart
//import androidx.compose.material.icons.filled.Psychology
//import androidx.compose.material.icons.filled.Spa
//import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// ---------- Data ----------
data class Specialty(
    val id: String,
    val name: String,
    val subtitle: String,
    val icon: ImageVector,
    val startColor: Color,
    val endColor: Color
)

private val allSpecialties = listOf(
    Specialty(
        id = "cardio",
        name = "Cardiologist",
        subtitle = "Heart & blood vessels",
        icon = Icons.Filled.Face,
        startColor = Color(0xFF5B86E5),
        endColor = Color(0xFF36D1DC)
    ),
    Specialty(
        id = "neuro",
        name = "Neurologist",
        subtitle = "Brain & nerves",
        icon =  Icons.Filled.Face,
        startColor = Color(0xFF834D9B),
        endColor = Color(0xFFD04ED6)
    ),
    Specialty(
        id = "uro",
        name = "Urologist",
        subtitle = "Urinary tract",
        icon =  Icons.Filled.Face,
        startColor = Color(0xFF00B4DB),
        endColor = Color(0xFF0083B0)
    ),
    Specialty(
        id = "ortho",
        name = "Orthopedic",
        subtitle = "Bones & joints",
        icon =  Icons.Filled.Face,
        startColor = Color(0xFF11998E),
        endColor = Color(0xFF38EF7D)
    ),
    Specialty(
        id = "derma",
        name = "Dermatologist",
        subtitle = "Skin & hair",
        icon =  Icons.Filled.Face,
        startColor = Color(0xFFFF5F6D),
        endColor = Color(0xFFFFC371)
    ),
    Specialty(
        id = "pedia",
        name = "Pediatrician",
        subtitle = "Children's health",
        icon =  Icons.Filled.Face,
        startColor = Color(0xFF36D1DC),
        endColor = Color(0xFF5B86E5)
    ),
    Specialty(
        id = "gyno",
        name = "Gynecologist",
        subtitle = "Women’s health",
        icon = Icons.Filled.Face,
        startColor = Color(0xFFF7971E),
        endColor = Color(0xFFFFD200)
    ),
    Specialty(
        id = "general",
        name = "General Physician",
        subtitle = "Primary care",
        icon =  Icons.Filled.Face,
        startColor = Color(0xFF00C9FF),
        endColor = Color(0xFF92FE9D)
    )
)

// ---------- UI ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onSpecialtyClick: (Specialty) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf<String?>(null) }

    val filtered = remember(query, selectedTag) {
        allSpecialties.filter { s ->
            val inSearch = s.name.contains(query, ignoreCase = true) ||
                    s.subtitle.contains(query, ignoreCase = true)
            val inTag = selectedTag?.let { s.name.contains(it, ignoreCase = true) } ?: true
            inSearch && inTag
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Find a Doctor", fontWeight = FontWeight.SemiBold) }
            )
        }
    ) { inner ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Search
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search cardiologist, skin…") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector =  Icons.Filled.Face,
                        contentDescription = null
                    )
                },
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(Modifier.height(12.dp))

            // Quick filter chips (optional)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val tags = listOf("Cardio", "Neuro", "Uro", "Skin")
                tags.forEach { tag ->
                    FilterChip(
                        selected = selectedTag == tag,
                        onClick = {
                            selectedTag = if (selectedTag == tag) null else tag
                        },
                        label = { Text(tag) }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Grid of specialties
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filtered, key = { it.id }) { spec ->
                    SpecialtyCard(spec) { onSpecialtyClick(spec) }
                }
            }
        }
    }
}

@Composable
private fun SpecialtyCard(
    specialty: Specialty,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        // Gradient header
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            specialty.startColor.copy(alpha = 0.20f),
                            specialty.endColor.copy(alpha = 0.06f)
                        )
                    )
                )
                .padding(14.dp)
        ) {
            // Icon bubble
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(specialty.startColor, specialty.endColor)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = specialty.icon,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = specialty.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = specialty.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Decorative gradient bar
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .height(6.dp)
                    .width(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(specialty.startColor, specialty.endColor)
                        )
                    )
                    .alpha(0.9f)
            )
        }
    }
}

// ---------- Preview ----------
@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}
