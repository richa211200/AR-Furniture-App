package com.richa.armenu

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.ar.core.Config
import com.richa.armenu.ui.theme.ARMenuTheme
import com.richa.armenu.ui.theme.Translucent
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode

class ARActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ARMenuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val currentModel = remember {
                        mutableStateOf("burger")
                    }
                    BottomSheetScaffold(
                        sheetContent = {
                            BottomSheetContent(onClick = { currentModel.value = it })
                        },
                        sheetPeekHeight = 40.dp,
                        sheetElevation = 40.dp,
                        content = {
                            Box(modifier = Modifier.fillMaxSize()) {
                                ARScreen(currentModel.value)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomSheetContent(onClick: (String) -> Unit, modifier: Modifier = Modifier) {
    var currentIndex by remember {
        mutableStateOf(0)
    }

    val itemsList = listOf(
        Model3D("car",R.drawable.car),
        Model3D("burger", R.drawable.burger),
        Model3D("instant", R.drawable.instant),
        Model3D("momos", R.drawable.momos),
        Model3D("chair",R.drawable.chair),
        Model3D("pizza", R.drawable.pizza),
        Model3D("ramen", R.drawable.ramen),
    )

    fun updateIndex(offset: Int) {
        currentIndex = (currentIndex + offset + itemsList.size) % itemsList.size
        onClick(itemsList[currentIndex].name)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(2.dp)
    ) {
        Text(
            text = "Pull to select",
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .clickable { /* Handle click if needed */ }
        )

        Spacer(modifier = Modifier.height(40.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            items(itemsList) { food ->
                FoodItem(food = food, onClick = onClick)
            }
        }
    }
}

@Composable
fun FoodItem(food: Model3D, onClick: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick(food.name) }
            .padding(8.dp)
    ) {
        CircularImage(imageId = food.imageId)
        Text(text = food.name, style = MaterialTheme.typography.body2)
    }
}


@Composable
fun CircularImage(
    modifier: Modifier = Modifier,
    imageId: Int
) {
    Box(
        modifier = modifier
            .size(140.dp)
            .clip(CircleShape)
            .border(width = 3.dp, Translucent, CircleShape)
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            modifier = Modifier.size(140.dp),
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
fun ARScreen(model: String) {
    val nodes = remember {
        mutableListOf<ArNode>()
    }
    val modelNode = remember {
        mutableStateOf<ArModelNode?>(null)
    }
    val placeModelButton = remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        ARScene(
            modifier = Modifier.fillMaxSize(),
            nodes = nodes,
            planeRenderer = true,
            onCreate = { arSceneView ->
                arSceneView.lightEstimationMode = Config.LightEstimationMode.DISABLED
                arSceneView.planeRenderer.isShadowReceiver = false
                modelNode.value = ArModelNode(arSceneView.engine, PlacementMode.INSTANT).apply {
                    loadModelGlbAsync(
                        glbFileLocation = "models/${model}.glb",
                        scaleToUnits = 0.8f
                    ) {

                    }
                    onAnchorChanged = {
                        placeModelButton.value = !isAnchored
                    }
                    onHitResult = { node, hitResult ->
                        placeModelButton.value = node.isTracking
                    }

                }
                nodes.add(modelNode.value!!)
            },
            onSessionCreate = {
                planeRenderer.isVisible = false
            }
        )
        if (placeModelButton.value) {
            Button(onClick = {
                modelNode.value?.anchor()
            }, modifier = Modifier.align(Alignment.Center)) {
                Text(text = "Place It")
            }
        }
    }

    LaunchedEffect(key1 = model) {
        modelNode.value?.loadModelGlbAsync(
            glbFileLocation = "models/${model}.glb",
            scaleToUnits = 0.8f
        )
        Log.e("errorloading", "ERROR LOADING MODEL")
    }
}


data class Model3D(var name: String, var imageId: Int)




