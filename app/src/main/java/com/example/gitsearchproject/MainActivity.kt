package com.example.gitsearchproject


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.gitsearchproject.ui.theme.GitSearchProjectTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {


    val viewModel: UserViewModel by viewModels()
    //val users: List<User> by viewModel.users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GitSearchProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.DarkGray
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {

                        val text = viewModel.mQuery.observeAsState("")
                        val users = viewModel.users.observeAsState()
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = text.value,
                            onValueChange = { viewModel.searchUsers(it) },
                            label = { Text("Username") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Icon",
                                    tint = Color.Gray                                )
                            }

                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        users.value?.let { UserList(it) { viewModel.loadMore(text.value) } }
                        BackgroundGitImage()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 900)
@Composable
fun BackgroundGitImage(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.git5),
            contentDescription = "Image",
            modifier = Modifier
                .padding(16.dp)
                .size(150.dp),
            
        )
        Text(
            text = "Please search by a username",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color.LightGray,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 180.dp)
        )    }
}

@Composable
fun UserList(users: List<User> , onLoadMore: () -> Unit) {

    val listState = rememberLazyListState()
    val visibleItems = listState.layoutInfo.visibleItemsInfo
    val lastVisibleItem = visibleItems.maxByOrNull { it.index }?.index ?: 0
    val loading = 2

    LazyColumn(state = listState) {
        items(users) { user ->
            UserItem(user)
            Divider()
            if (lastVisibleItem >= users.size - loading) {
                onLoadMore()
            }
        }
    }
}

 @Composable
fun UserItem(user: User) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        AsyncImage(model =  user.avatar_url, contentDescription ="Profile Picture" ,
            modifier=Modifier.width(40.dp).height(40.dp) )

        Spacer(modifier = Modifier.width(16.dp))
        Text(text = user.login)
    }
}

//@Composable
//fun UserItem(user: User) {
//
//
//
//
//}

