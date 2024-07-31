package com.example.firestorage.ui.compose.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.compose.AsyncImage
import com.example.firestorage.R
import com.example.firestorage.databinding.ActivityListComposeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListComposeActivity : AppCompatActivity() {

    companion object { // companion object to create intent
        fun create(context: Context) =
            Intent(context, ListComposeActivity::class.java) // function to create intent
    }

    private lateinit var binding: ActivityListComposeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListComposeBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.composeView.setContent {
            ListScreen()


        }


    }


    @Composable
    fun ListScreen() {

        val listComposeViewModel: ListComposeViewModel by viewModels() // view model instance

        val uiState by listComposeViewModel.uiState.collectAsState() // collect the state of the view model

        listComposeViewModel.getAllImages() // call the function to get all images

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                modifier = Modifier.padding(16.dp)
            ) {

                items(uiState.images) {
                    Card(modifier = Modifier.fillMaxSize(), shape = RoundedCornerShape(12)) {

                        AsyncImage(
                            model = it,
                            contentDescription = "",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                    }


                }


            }

            if (uiState.isLoading) {
                // show loading
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = colorResource(id = R.color.green)
                )
            }

        }

    }
}




