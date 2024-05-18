package id.ac.istts.seton.mainPage

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Chart(
    data: Map<Float, Int>,
    max_value: Int,
    days: List<String>
) {

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start

        ){
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(50.dp),
                contentAlignment = Alignment.BottomCenter
            ){
                // scale
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = max_value.toString())
                    Spacer(modifier = Modifier.fillMaxHeight(0.73f))
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = "1")
                    Spacer(modifier = Modifier.fillMaxHeight(0.07f))
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = "2")
                    Spacer(modifier = Modifier.fillMaxHeight(0.2f))
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = "3")
                    Spacer(modifier = Modifier.fillMaxHeight(0.33f))
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = "4")
                    Spacer(modifier = Modifier.fillMaxHeight(0.47f))
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = "5")
                    Spacer(modifier = Modifier.fillMaxHeight(0.6f))
                }
            }

            Box(modifier = Modifier
                .fillMaxHeight()
                .width(2.dp)
                .background(Color.Black)
            )

            // graph
            data.forEach{
                Box(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .width(20.dp)
                        .fillMaxHeight(it.key)
                        .background(Color.Blue)
                        .clickable {
                            Toast
                                .makeText(context, it.key.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Black)
        )

        Row(
            modifier = Modifier
                .padding(start = 69.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            days.forEach{
                Text(
                    modifier = Modifier.width(36.dp),
                    text = it.toString(),
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}