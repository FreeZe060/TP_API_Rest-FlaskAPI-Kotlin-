import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.app.ApiService
import com.example.app.models.Article
import kotlinx.coroutines.launch

@Composable
fun ArticleListScreen(apiService: ApiService) {
    val scope = rememberCoroutineScope()
    var articles by remember { mutableStateOf(emptyList<Article>()) }

    LaunchedEffect(Unit) {
        scope.launch {
            articles = apiService.getArticles()
        }
    }

    Column {
        Text("Articles", style = MaterialTheme.typography.h4)
        articles.forEach { article ->
            Card(
                modifier = Modifier.padding(8.dp),
                elevation = 4.dp
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Title: ${article.title}")
                    Text("Author: ${article.author}")
                }
            }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    ArticleListScreen(apiService = ApiService())
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            ArticleListScreen(apiService = ApiService())
        }
    }
}
