package com.example.mediline.User.ui.Home



import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.ReceiptLong
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mediline.Admin.ui.AdminCreateTicket.CurvedTopBar
import com.example.mediline.data.room.DepartmentEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onDepartmentClick: (String) -> Unit,
    onViewTicketsClick: () -> Unit,
    viewModel: DepartmentViewModel = hiltViewModel()
) {
    val departments by viewModel.departments.collectAsState()

    Scaffold(
        topBar = {
            HomeCurvedTopBar("Departments",onViewTicketsClick)

//
        }
    ) { padding ->
        if (departments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
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
                items(departments) { department ->
                    DepartmentCard(
                        department = department,
                        modifier = Modifier,
                        onClick = { onDepartmentClick(department.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun DepartmentCard(
    department: DepartmentEntity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.LocalHospital,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = department.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = department.description.ifBlank { "No description" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
@Composable
fun HomeCurvedTopBar(
    title: String,
    navigateViewTicket: () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp) // taller for spacing
    ) {
        // Draw concave curve
        Canvas(modifier = Modifier.matchParentSize()) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(0f, height) // bottom-left
                quadraticBezierTo(
                    width / 2, height * 0.35f, // control point up → concave dip
                    width, height // bottom-right
                )
                lineTo(width, 0f)
                lineTo(0f, 0f)
                close()
            }

            drawPath(
                path = path,
                color = primary
            )
        }

        // Tickets icon on top-right
        IconButton(
            onClick = { navigateViewTicket() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 56.dp, end = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ReceiptLong,
                contentDescription = "View Tickets",
                tint = onPrimary
            )
        }

        // Title moved upwards
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = onPrimary,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp)
        )

        // Circular icon inside concave dip (center of curve)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-20).dp) // lift it into the dip
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(3.dp, primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MedicalServices, // replace with stethoscope
                contentDescription = "Stethoscope",
                tint = primary,
                modifier = Modifier.size(36.dp)
            )


        }
    }
}



@Composable
fun HomeCurvedTopBar1(
    title: String,
    navigateViewTicket: () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),

        ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(0f, height * 0.6f)
                quadraticBezierTo(
                    width / 2, height, // control point
                    width, height * 0.6f // end point
                )
                lineTo(width, 0f)
                lineTo(0f, 0f)
                close()
            }

            drawPath(
                path = path,
                color = primary
            )
        }

            IconButton(
                onClick = { navigateViewTicket()},
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ReceiptLong,
                    contentDescription = "View Tickets",
                    tint = onPrimary
                )
            }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = onPrimary,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen({}, {})
}

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreen(
//
//    onDepartmentClick: (String) -> Unit,
//    viewModel: DepartmentViewModel = hiltViewModel()
//
//
//) {
//    val departments by viewModel.departments.collectAsState()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Departments") }
//            )
//        }
//    ) { padding ->
//        if (departments.isEmpty()) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(padding),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        } else {
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(2),
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(padding),
//                contentPadding = PaddingValues(12.dp),
//                verticalArrangement = Arrangement.spacedBy(12.dp),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                items(departments) { department ->
//                    DepartmentCard(
//                        department = department,
//                        modifier = Modifier,
//                        onClick = { onDepartmentClick(department.id) }
//                    )
//                }
//            }
//        }
//    }
//}
//
//
//
//
//@Composable
//fun DepartmentCard(
//    department: DepartmentEntity,
//    modifier: Modifier = Modifier,
//    onClick: () -> Unit
//) {
//    ElevatedCard(
//        onClick = onClick,
//        shape = RoundedCornerShape(18.dp),
//        modifier = modifier
//            .fillMaxWidth()
//            .height(140.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    Brush.verticalGradient(
//                        listOf(
//                            Color(0xFF6A11CB).copy(alpha = 0.15f),
//                            Color(0xFF2575FC).copy(alpha = 0.05f)
//                        )
//                    )
//                )
//                .padding(14.dp)
//        ) {
//            Column(
//                modifier = Modifier.align(Alignment.CenterStart)
//            ) {
//                Text(
//                    text = department.name,
//                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//                Spacer(Modifier.height(6.dp))
//                Text(
//                    text = department.description.ifBlank { "No description" },
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis
//                )
//            }
//        }
//    }
//}
//
//
