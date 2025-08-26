package com.example.mediline.User.ui.Home



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mediline.User.data.model.Department



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(

    onDepartmentClick: (Department) -> Unit,
    viewModel: DepartmentViewModel = hiltViewModel()


) {
    val departments by viewModel.departments.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Departments") }
            )
        }
    ) { padding ->
        if (departments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(departments) { index, department ->
                    DepartmentCard(
                        department = department,
                        index = index,
                        onClick = { onDepartmentClick(department) }
                    )
                }
            }
        }
    }
}




@Composable
fun DepartmentCard(
    department: Department,
    index: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    // Nice gradient list (8 colors for 8 departments)
    val gradients = listOf(
        listOf(Color(0xFF6A11CB), Color(0xFF2575FC)), // Purple → Blue
        listOf(Color(0xFFFF512F), Color(0xFFDD2476)), // Red → Pink
        listOf(Color(0xFF11998E), Color(0xFF38EF7D)), // Teal → Green
        listOf(Color(0xFFFC5C7D), Color(0xFF6A82FB)), // Pink → Indigo
        listOf(Color(0xFFFFA17F), Color(0xFF00223E)), // Orange → Dark Blue
        listOf(Color(0xFF56CCF2), Color(0xFF2F80ED)), // Light Blue → Blue
        listOf(Color(0xFFF7971E), Color(0xFFFFD200)), // Orange → Yellow
        listOf(Color(0xFFB24592), Color(0xFFF15F79))  // Purple → Red
    )

    val backgroundGradient = gradients[index % gradients.size]

    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp), // slightly taller
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        backgroundGradient.map { it.copy(alpha = 0.25f) }
                    )
                )
                .padding(14.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                // Title + Description
                Column {
                    Text(
                        text = department.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = department.description.ifBlank { "No description" },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Doctor + Today Counter Row
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Doctor",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = department.doctor.ifBlank { "Not assigned" },
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Today counter",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "${department.todayCounter}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }
    }
}


