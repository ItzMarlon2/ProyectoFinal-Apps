package com.example.proyectofinal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.R
import com.example.proyectofinal.ui.theme.Primary

const val USER_TYPE = "usuario"
const val MODERATOR_TYPE = "moderador"


@Composable
fun ToggleButtonGroup(
    initialSelectedType: String = USER_TYPE,
    onButtonSelected: (String) -> Unit,
    modifier: Modifier = Modifier
){
    val (selectedButtonType, setSelectedButtonType) = rememberSaveable { mutableStateOf(initialSelectedType) }

    val activeButtonColor = Primary
    val inactiveButtonColor = Color.Transparent
    val activeTextColor = Color.White
    val inactiveTextColor = Color.DarkGray

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ){
        Button(
            onClick = {
                setSelectedButtonType(USER_TYPE)
                onButtonSelected(USER_TYPE)
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedButtonType == USER_TYPE) activeButtonColor else inactiveButtonColor,
                contentColor = if (selectedButtonType == USER_TYPE) activeTextColor else inactiveTextColor
            ),
            modifier = Modifier
                .weight(.5f)
                .height(40.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = stringResource(R.string.icon_content_description_location),
                modifier = Modifier
                    .size(25.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.login_text_user),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold

            )
        }
        Spacer(Modifier.width(16.dp))
        Button(
            onClick = {
                setSelectedButtonType(MODERATOR_TYPE)
                onButtonSelected(MODERATOR_TYPE)
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedButtonType == MODERATOR_TYPE) activeButtonColor else inactiveButtonColor,
                contentColor = if (selectedButtonType == MODERATOR_TYPE) activeTextColor else inactiveTextColor
            ),
            modifier = Modifier
                .weight(.5f)
                .height(40.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = stringResource(R.string.icon_content_description_business),
                modifier = Modifier.size(25.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.login_text_business),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}